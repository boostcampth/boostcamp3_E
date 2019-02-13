package com.teame.boostcamp.myapplication.ui.search;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapterContract;
import com.teame.boostcamp.myapplication.model.repository.PlaceTextDataRepository;
import com.teame.boostcamp.myapplication.model.repository.UserPinRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View view;
    private Geocoder geocoder=null;
    private ResourceProvider provider;
    private UserPinRepository remote;
    private CompositeDisposable disposable=new CompositeDisposable();
    private HashMap<LatLng,String> userPinMap=new HashMap<>();
    private PlaceTextDataRepository placeRepository;


    public SearchPresenter(SearchContract.View view, ResourceProvider provider){
        this.view=view;
        this.provider=provider;
        remote= UserPinRepository.getInstance();
        placeRepository=PlaceTextDataRepository.getInstance();
        disposable.add(placeRepository.getText()
                            .subscribe(s -> {
                                onSearchSubmit(s);
                            },e->{
                                DLogUtil.e("Submit Error");
                            }));
    }

    @Override
    public void getUserPinGoodsList(Marker marker) {
        disposable.add(remote.getUserPinGoodsList(userPinMap.get(marker.getPosition()))
                            .subscribe(goodslist->{
                                view.showUserGoodsListActivity(goodslist);
                            }));
    }

    @Override
    public void getUserPinPreview(Marker marker) {
        userPinMap.get(marker);
        String mapKey=userPinMap.get(marker.getPosition());
        DLogUtil.e(mapKey);
        disposable.add(remote.getUserPinPreview(mapKey)
                        .subscribe(userPinPreview -> {
                            view.showUserPinPreview(userPinPreview);
                        },e->{
                            DLogUtil.e(e.getMessage());
                        }));
    }

    @Override
    public void showUserPin() {
        int onlyone=0;
        for(LatLng latlng:userPinMap.keySet()){
            if(onlyone==0) {
                view.showPositionInMap(latlng);
                onlyone++;
            }
            view.showUserPin(latlng);
        }
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onSearchSubmit(String place) {
        if(geocoder==null){
            geocoder=new Geocoder(provider.getApplicationContext(), Locale.KOREA);
        }
        try{
            List<Address> geoResult=geocoder.getFromLocationName(place,1);
            LatLng latlon=new LatLng(geoResult.get(0).getLatitude(),geoResult.get(0).getLongitude());
            DLogUtil.e(geoResult.toString());
            String nationCode=geoResult.get(0).getCountryCode();
            String cityName=geoResult.get(0).getFeatureName().replace(" ","");
            view.showPositionInMap(latlon);
            view.hideUserPin();
            disposable.add(remote.getUserVisitedLocation(latlon)
                                .subscribe(pairlist -> {
                                    userPinMap.clear();
                                    view.showSearchResult(pairlist.size(),nationCode,cityName);
                                    for(Pair<LatLng,String> pair:pairlist){
                                        userPinMap.put(pair.first,pair.second);
                                    }
                                },e->{
                                    DLogUtil.e(e.getMessage());
                                }));
        }catch(Exception e){
            view.showFragmentToast("검색 결과가 없습니다.");
            return;
        }
    }

    @Override
    public void onDetach() {
        geocoder=null;
        view=null;
        disposable.dispose();
    }
}
