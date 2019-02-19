package com.teame.boostcamp.myapplication.model.repository.remote;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teame.boostcamp.myapplication.model.MinPriceAPI;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.MinPriceResponse;
import com.teame.boostcamp.myapplication.model.repository.GoodsListDataSource;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class GoodsListRemoteDataSource implements GoodsListDataSource {

    private static final String QUERY_COUNTRY = "country";
    private static final String QUERY_COUNTRY_BASE_LIST = "baselist";

    private static GoodsListRemoteDataSource INSTANCE;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private GoodsListRemoteDataSource() {
    }

    public static GoodsListRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoodsListRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<Goods> getItemListToObservable(String nation, String city) {
        DLogUtil.d(":: 진입");
        CollectionReference baseRef = db.collection(QUERY_COUNTRY)
                .document(nation)
                .collection(QUERY_COUNTRY_BASE_LIST);

        CollectionReference countryRef = db.collection(QUERY_COUNTRY)
                .document(nation)
                .collection(city);

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

        return subject.zipWith(Observable.interval(200, TimeUnit.MILLISECONDS), (item, i) -> item).subscribeOn(Schedulers.io()).flatMap(targetItem ->
                Observable.just(targetItem)
                        .observeOn(Schedulers.io())
                        .zipWith(MinPriceAPI.getInstance()
                                        .api
                                        .getMinPrice(targetItem.getName())
                                        .subscribeOn(Schedulers.io()),
                                (item, response) -> {
                                    MinPriceResponse minPriceResponse = response.body();
                                    item.setMinPriceResponse(minPriceResponse);

                                    return item;
                                }));
    }

    /**
     * 유저가 선택할 리스트를 가져옴
     */
    @Override
    public Single<List<Goods>> getItemList(String nation, String city) {
        DLogUtil.d(":: 진입");
        CollectionReference baseRef = db.collection(QUERY_COUNTRY)
                .document(nation)
                .collection(QUERY_COUNTRY_BASE_LIST);

        CollectionReference countryRef = db.collection(QUERY_COUNTRY)
                .document(nation)
                .collection(city);

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

        return subject.zipWith(Observable.interval(200, TimeUnit.MILLISECONDS), (item, i) -> item).subscribeOn(Schedulers.io()).flatMap(targetItem ->
                Observable.just(targetItem)
                        .observeOn(Schedulers.io())
                        .zipWith(MinPriceAPI.getInstance()
                                        .api
                                        .getMinPrice(targetItem.getName())
                                        .subscribeOn(Schedulers.io()),
                                (item, response) -> {
                                    MinPriceResponse minPriceResponse = response.body();
                                    item.setMinPriceResponse(minPriceResponse);

                                    return item;
                                }))
                .toList();
    }
}
