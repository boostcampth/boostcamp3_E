package com.teame.boostcamp.myapplication.ui.search;

import android.location.Address;
import android.location.Geocoder;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.PlaceTextDataRepository;
import com.teame.boostcamp.myapplication.model.repository.UserPinRepository;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View view;
    private Geocoder geocoder=null;
    private ResourceProvider provider;
    private UserPinRepository remote;
    private CompositeDisposable disposable=new CompositeDisposable();
    private HashMap<LatLng,String> userPinMap=new HashMap<>();
    private PlaceTextDataRepository placeRepository;
    private boolean imageViewClick=false;
    private ArrayList<Goods> selectedlist;
    private String currentNation;
    private String currentCity;
    private LatLng currentPosition;
    private Marker currentMarker;
    private Marker userMarker;


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
    public void showUserPinClicked() {
        if(imageViewClick) {
            view.hideUserPin();
            imageViewClick=false;
        }
        else {
            for(LatLng latlng:userPinMap.keySet())
                view.showUserPin(latlng);
            imageViewClick=true;
        }
    }

    @Override
    public void mapLongClicked(Marker user) {
        if(userMarker!=null)
            userMarker.remove();
        userMarker=user;
    }

    @Override
    public void infoWindowClicked() {
        if(currentNation==null||currentCity==null)
            view.showFragmentToast("장소를 선택해 주세요");
        else
            view.showPeriodSetting();
    }

    @Override
    public void setSelectedList(List<Goods> goodslist) {
        selectedlist=(ArrayList<Goods>)goodslist;
    }

    @Override
    public void userPreviewClicked() {
        if(currentPosition==null)
            return;
        disposable.add(remote.getUserPinGoodsList(userPinMap.get(currentPosition))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(goodslist->{
                    view.showUserGoodsListActivity(goodslist);
                }));

    }

    @Override
    public void floatingButtonClicked(Date start, Date end) {
        LatLng latlng=userMarker.getPosition();
        GoodsListHeader header=new GoodsListHeader(currentNation,currentCity,start,end,latlng.latitude,latlng.longitude);
        if(selectedlist==null)
            CreateListActivity.startActivity(provider.getApplicationContext(),header);
        else
            CreateListActivity.startActivity(provider.getApplicationContext(),header,selectedlist);
        selectedlist=null;
    }

    @Override
    public boolean markerClicked(Marker marker) {
        view.redPinShow(currentMarker);
        currentMarker=marker;
        boolean lat=userMarker.getPosition().latitude==marker.getPosition().latitude;
        boolean lon=userMarker.getPosition().longitude==marker.getPosition().longitude;
        if(lat||lon) {
            view.showmarkerInfoWindow(marker);
            return false;
        }
        userPinMap.get(marker);
        String mapKey=userPinMap.get(marker.getPosition());
        DLogUtil.e(mapKey);
        disposable.add(remote.getUserPinPreview(mapKey)
                        .subscribe(userPinPreview -> {
                            view.showUserPinPreview(userPinPreview);
                        },e->{
                            DLogUtil.e(e.getMessage());
                        }));
        return true;
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
            currentPosition=latlon;
            DLogUtil.e(geoResult.toString());
            currentNation=geoResult.get(0).getCountryCode();
            currentCity=geoResult.get(0).getFeatureName().replace(" ","");
            view.showPositionInMap(latlon);
            view.hideUserPin();
            disposable.add(remote.getUserVisitedLocation(latlon)
                                .subscribe(pairlist -> {
                                    userPinMap.clear();
                                    view.showSearchResult(pairlist.size(),currentCity);
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
