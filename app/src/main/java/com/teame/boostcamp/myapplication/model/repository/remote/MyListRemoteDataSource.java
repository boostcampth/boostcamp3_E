package com.teame.boostcamp.myapplication.model.repository.remote;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.teame.boostcamp.myapplication.model.MinPriceAPI;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.entitiy.MinPriceResponse;
import com.teame.boostcamp.myapplication.model.repository.MyListDataSoruce;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MyListRemoteDataSource implements MyListDataSoruce {
    private static final String QUERY_USER = "users";
    private static final String QUERY_MY_LIST = "mylist";
    private static final String QUERY_MY_GOODS = "items";
    private static final String QUERT_LOCATION = "location";
    private static MyListRemoteDataSource INSTANCE;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection(QUERY_USER);

    private MyListRemoteDataSource() {
    }

    public static MyListRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyListRemoteDataSource();
        }
        return INSTANCE;
    }


    @Override
    public Single<List<GoodsListHeader>> getMyList() {
        String uid = auth.getUid();
        PublishSubject<GoodsListHeader> subject = PublishSubject.create();

        Task myList = userRef.document(uid).collection(QUERY_MY_LIST).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();

                        for (DocumentSnapshot document : documents) {
                            String key = document.getId();
                            GoodsListHeader listHeader = document.toObject(GoodsListHeader.class);
                            listHeader.setKey(key);
                            subject.onNext(listHeader);
                        }
                    } else {
                        DLogUtil.d("No such document");
                    }
                });

        Tasks.whenAll(myList).addOnCompleteListener(task -> subject.onComplete());
        return subject.toList();
    }

    @Override
    public Single<List<Goods>> getMyListItems(String headerUid) {
        DLogUtil.d(":: 진입");
        String uid = auth.getUid();
        PublishSubject<Goods> subject = PublishSubject.create();
        Task myList = userRef.document(uid).collection(QUERY_MY_LIST)
                .document(headerUid)
                .collection(QUERY_MY_GOODS).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();

                        for (DocumentSnapshot document : documents) {
                            Goods item = document.toObject(Goods.class);
                            String key = document.getReference().getId();
                            item.setKey(key);
                            subject.onNext(item);
                        }
                    } else {
                        DLogUtil.d("No such document");
                    }
                })
                .addOnFailureListener(Throwable::getMessage);

        // 두 리스트가 가져오는데 성공하면 아이템을 반환해줌
        Tasks.whenAll(myList).addOnCompleteListener(task -> {
            subject.onComplete();
        })
                .addOnFailureListener(e -> DLogUtil.e("error : " + e.toString()));

        // 가져온 리스트를 MinPriceAPI를 통해 최저가를 붙여준 후 모든아이템을 List로 반환해줌
        return subject.zipWith(Observable.interval(200, TimeUnit.MILLISECONDS), (item, i) -> item).subscribeOn(Schedulers.io()).flatMap(targetItem ->
                Observable.just(targetItem)
                        .observeOn(Schedulers.io())
                        .zipWith(MinPriceAPI.getInstance()
                                        .api
                                        .getMinPrice(targetItem.getName())
                                        .subscribeOn(Schedulers.io()),
                                (item, response) -> {
                                    MinPriceResponse minPriceResponse = response.body();
                                    DLogUtil.d(item + "/" + minPriceResponse.toString());
                                    item.setMinPriceResponse(minPriceResponse);

                                    return item;
                                }))
                .toList();
    }

    @Override
    public Single<Boolean> saveMyList(List<Goods> goodsList, List<String> hashTag, GoodsListHeader header) {
        DLogUtil.d(":: 진입");
        PublishSubject<Boolean> subject = PublishSubject.create();

        String uid = auth.getUid();
        header.setKey(uid);
        CollectionReference myListRef = userRef.document(uid).collection(QUERY_MY_LIST);
        String myListUid = myListRef.document().getId();

        //마이리스트용
        DocumentReference myListDocRef = myListRef.document(myListUid);
        CollectionReference myListItemRef = myListDocRef.collection(QUERY_MY_GOODS);

        //위치검색용
        DocumentReference locationRef = db.collection(QUERT_LOCATION).document(myListUid);
        CollectionReference locationItemRef = db.collection(QUERT_LOCATION).document(myListUid)
                .collection(QUERY_MY_GOODS);

        // 선택된 각 아이템을 ID, Item 할당
        WriteBatch batch = db.batch();
        batch.set(myListDocRef, header);
        batch.set(locationRef, header);
        for (Goods item : goodsList) {
            DocumentReference itemRef = myListItemRef.document(item.getKey());
            batch.set(itemRef, item);
            batch.set(locationItemRef.document(item.getKey()), item);
        }


        batch.commit()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        subject.onNext(true);
                    } else {
                        subject.onError(task.getException());
                    }
                    subject.onComplete();
                });

        return subject.subscribeOn(Schedulers.io()).flatMapSingle(Single::just)
                .single(true);
    }

    @Override
    public Single<Boolean> deleteMyList(String headerUid) {
        DLogUtil.d(":: 진입");
        PublishSubject<Boolean> subject = PublishSubject.create();

        String uid = auth.getUid();
        DocumentReference myListRef = userRef.document(uid).collection(QUERY_MY_LIST).document(headerUid);
        CollectionReference myListItemsRef = userRef.document(uid)
                .collection(QUERY_MY_LIST)
                .document(headerUid)
                .collection(QUERY_MY_GOODS);

        WriteBatch batch = db.batch();
        batch.delete(myListRef);
        myListRef.delete();
        Task delete = myListItemsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                for (DocumentSnapshot document : documents) {
                    batch.delete(document.getReference());
                }
            } else {
                DLogUtil.d("No such document");
            }
        });

        Tasks.whenAll(delete).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subject.onNext(true);
            } else {
                subject.onError(task.getException());
            }
            batch.commit();
            subject.onComplete();
        });

        return subject.flatMapSingle(Single::just).single(true);
    }
}
