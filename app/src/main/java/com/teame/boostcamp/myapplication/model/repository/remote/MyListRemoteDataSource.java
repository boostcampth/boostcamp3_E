package com.teame.boostcamp.myapplication.model.repository.remote;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teame.boostcamp.myapplication.model.MinPriceAPI;
import com.teame.boostcamp.myapplication.model.entitiy.Item;
import com.teame.boostcamp.myapplication.model.entitiy.ItemListHeader;
import com.teame.boostcamp.myapplication.model.repository.MyListDataSoruce;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MyListRemoteDataSource implements MyListDataSoruce {
    private static final String QUERY_USER = "users";
    private static final String QUERY_MY_LIST = "mylist";
    private static final String QUERY_MY_ITEMS = "items";
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
    public Single<List<ItemListHeader>> getMyList() {
        String uid = auth.getUid();
        // TODO : uid 테스트 코드.
        uid = "4qr7xncum1Na7WYz6vD1NPaxFAp1";
        PublishSubject<ItemListHeader> subject = PublishSubject.create();

        Task myList = userRef.document(uid).collection(QUERY_MY_LIST).get()
                     .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            for (DocumentSnapshot document : documents) {
                                String key = document.getId();
                                ItemListHeader listHeader = document.toObject(ItemListHeader.class);
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
    public Single<List<Item>> getMyListItems(String headerUid) {
        DLogUtil.d(":: 진입");
        String uid = auth.getUid();
        // TODO : uid 테스트 코드.
        uid = "4qr7xncum1Na7WYz6vD1NPaxFAp1";
        PublishSubject<Item> subject = PublishSubject.create();
        Task myList = userRef.document(uid).collection(QUERY_MY_LIST)
                .document(headerUid)
                .collection(QUERY_MY_ITEMS).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();

                        for (DocumentSnapshot document : documents) {
                            Item item = document.toObject(Item.class);
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
        return subject.flatMap(targetItem ->
                Observable.just(targetItem)
                        .observeOn(Schedulers.io())
                        .zipWith(MinPriceAPI.getInstance()
                                        .api
                                        .getMinPrice(targetItem.getName())
                                        .subscribeOn(Schedulers.io()),
                                (item, minPriceResponse) -> {
                                    item.setMinPriceResponse(minPriceResponse);
                                    return item;
                                }))
                .toList();
    }
}
