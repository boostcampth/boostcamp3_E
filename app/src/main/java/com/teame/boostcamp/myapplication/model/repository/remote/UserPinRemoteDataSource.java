package com.teame.boostcamp.myapplication.model.repository.remote;

import android.util.Pair;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.teame.boostcamp.myapplication.model.MinPriceAPI;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.entitiy.UserPinPreview;
import com.teame.boostcamp.myapplication.model.repository.UserPinDataSource;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.SingleSubject;

public class UserPinRemoteDataSource implements UserPinDataSource {
    private static String QUERY_COLLECTION="testcountry";
    private static String QUERY_NATION="JP";
    private static String QUERY_CITY="오사카 시";
    private static String QUERY_SHOPPINGLIST="shoppinglist";
    private static String QUERY_LOCATION="location";
    private static String QUERY_ITEMS="items";
    private static String FIREBASE_URL="https://boostcamp-1548575868471.firebaseio.com/_geofire";
    private static final int QUERY_RADIUS=50;
    private DatabaseReference firebase= FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_URL);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private static UserPinRemoteDataSource INSTANCE;

    private UserPinRemoteDataSource(){
    }

    public static UserPinRemoteDataSource getInstance(){
        if(INSTANCE==null){
            INSTANCE=new UserPinRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Single<List<Goods>> getUserPinGoodsList(String Key) {
        CollectionReference collection=firestore.collection(QUERY_LOCATION).document(Key)
                .collection(QUERY_ITEMS);
        PublishSubject<Goods> subject=PublishSubject.create();
        Task task=collection.get().addOnCompleteListener(result->{
                        if(result.isSuccessful()){
                            List<DocumentSnapshot> documentList=result.getResult().getDocuments();
                            for(DocumentSnapshot document:documentList){
                                Goods goods=document.toObject(Goods.class);
                                String itemKey=document.getReference().getId();
                                goods.setKey(itemKey);
                                DLogUtil.e(goods.toString());
                                subject.onNext(goods);
                            }
                        }
                        else{
                            DLogUtil.e("GetUserPinGoodsList Error");
                        }
                    })
                    .addOnFailureListener(exception->{
                        DLogUtil.e(exception.toString());
                    });
        Tasks.whenAll(task).addOnCompleteListener(complete->{
            if(complete.isSuccessful())
                subject.onComplete();
            else
                DLogUtil.e("Tasks Fail");
        });

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

    @Override
    public Single<UserPinPreview> getUserPinPreview(String Key) {
        DocumentReference doc=firestore.collection(QUERY_LOCATION).document(Key);
        SingleSubject<UserPinPreview> subject=SingleSubject.create();
        doc.get().addOnCompleteListener(result -> {
                    if(result.isSuccessful()){
                        DocumentSnapshot snapshot=result.getResult();
                        GoodsListHeader header =snapshot.toObject(GoodsListHeader.class);
                        DLogUtil.e(header.toString());
                        UserPinPreview preview=new UserPinPreview();
                        preview.setCity(header.getCity());
                        preview.setStart(header.getStartDate());
                        preview.setEnd(header.getEndDate());
                        subject.onSuccess(preview);
                    }
                    else{
                        DLogUtil.e(result.getException().toString());
                    }
                });
        return subject;
    }

    @Override
    public void setLocation(String Key, LatLng location) {
        //반드시 쇼핑리스트 생성한 후 호출
        GeoFire fire=new GeoFire(firebase);
        //String KEY=""+location.latitude+","+location.longitude;
        fire.setLocation(Key, new GeoLocation(location.latitude, location.longitude), (__, ___) -> {  });
    }

    @Override
    public Single<List<Pair<LatLng,String>>> getUserVisitedLocation(LatLng center) {
        PublishSubject<Pair<LatLng,String>> subject=PublishSubject.create();
        GeoFire geoFire=new GeoFire(firebase);
        GeoQuery query=geoFire.queryAtLocation(new GeoLocation(center.latitude,center.longitude),QUERY_RADIUS);
        query.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                LatLng latlng=new LatLng(location.latitude,location.longitude);
                subject.onNext(new Pair<LatLng,String>(latlng,key));
            }

            @Override
            public void onKeyExited(String key) {
                DLogUtil.d("Exit");
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                DLogUtil.d("Move");
            }

            @Override
            public void onGeoQueryReady() {
                subject.onComplete();
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                DLogUtil.d(error.toString());
            }
        });
        return subject.toList();
    }
}
