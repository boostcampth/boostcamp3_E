package com.teame.boostcamp.myapplication.model.repository.remote;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.teame.boostcamp.myapplication.model.MinPriceAPI;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.ShoppingListDataSource;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ShoppingListRemoteDataSource implements ShoppingListDataSource {

    private static final String QUERY_COUNTRY = "country";
    private static final String QUERY_COUNTRY_TARGET = "japan";
    private static final String QUERY_COUNTRY_TARGET2 = "osaka";
    private static final String QUERY_COUNTRY_BASE_LIST = "baselist";

    private static ShoppingListRemoteDataSource INSTANCE;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference baseRef = db.collection(QUERY_COUNTRY)
            .document(QUERY_COUNTRY_TARGET)
            .collection(QUERY_COUNTRY_BASE_LIST);

    private CollectionReference countryRef = db.collection(QUERY_COUNTRY)
            .document(QUERY_COUNTRY_TARGET)
            .collection("osaka");

    private ShoppingListRemoteDataSource() {
    }

    public static ShoppingListRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShoppingListRemoteDataSource();
        }
        return INSTANCE;
    }


    /**
     * 유저가 선택할 리스트를 가져옴
     */
    @Override
    public Single<List<Goods>> getItemList() {
        DLogUtil.d(":: 진입");

        PublishSubject<Goods> subject = PublishSubject.create();

        // 국가의 Base Shopping List를 가져옴
            Task base = baseRef.get()
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

        // 국가의도시의 Shopping List를 가져옴
        Task country = countryRef.get()
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
        Tasks.whenAll(base, country).addOnCompleteListener(task -> {
            subject.onComplete();
        })
                .addOnFailureListener(e -> DLogUtil.e("error : " + e.toString()));

        // 가져온 리스트를 MinPriceAPI를 통해 최저가를 붙여준 후 모든아이템을 List로 반환해줌
        /*return subject.flatMapSingle(targetItem ->
                MinPriceAPI.getInstance()
                        .api
                        .getMinPrice(targetItem.getName())
                        .map(minPriceResponse -> {
                            targetItem.setMinPriceResponse(minPriceResponse);
                            return targetItem;
                        }))
                .toList();*/

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

    /**
     * 유저가 선택한 리스트를 저장함 */
    @Override
    public void saveMyChoiceList(List<Goods> list) {
        FirebaseUser user = auth.getCurrentUser();
        String userId;

        if (user != null) {
            userId = user.getUid();
            DLogUtil.d("userId : " + user.getUid());
        } else {
            return;
        }

        CollectionReference myListRef = db.collection("users")
                .document(userId)
                .collection("mylist");

        // 마이리스트 도큐먼트 ID
        String myListId = getMyListID();
        // 생성되는 Mylist의 ID 할당
        DocumentReference myThisListRef = myListRef.document(myListId);
        CollectionReference myThisListItemRef = myThisListRef.collection("items");
        WriteBatch batch = db.batch();
        // 선택된 각 아이템을 ID, Item 할당
        for (Goods item : list) {
            DocumentReference itemRef = myThisListItemRef.document(item.getKey());
            batch.set(itemRef, item);
        }

        batch.commit()
                .addOnSuccessListener(aVoid -> DLogUtil.d("성공"))
                .addOnFailureListener(e -> DLogUtil.e("fail : " + e.getMessage()));

    }

    /**
     * 저장될 MyList의 고유ID를 만듦
     */
    private String getMyListID() {
        // 리스트의 UID 생성 = 국가 + 지역 + 출발일자
        return "일본오사카20190128";
    }
}
