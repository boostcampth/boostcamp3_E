package com.teame.boostcamp.myapplication.ui.searchmap;

import android.location.Address;
import android.location.Geocoder;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.UserPinRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.databinding.ObservableBoolean;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.SingleSubject;

public class SearchMapPresenter implements SearchMapContract.Presenter {

    private SearchMapContract.View view;
    private ResourceProvider provider;
    private Geocoder geocoder;
    private CompositeDisposable disposable=new CompositeDisposable();
    private Marker currentMarker;
    private String currentNation;
    private String currentCity;
    private UserPinRepository remote;
    private HashMap<LatLng,String> userPinMap=new HashMap<>();

    public SearchMapPresenter(SearchMapContract.View view, ResourceProvider provider){
        this.view=view;
        this.provider=provider;
        remote=UserPinRepository.getInstance();
    }

    @Override
    public void searchMapFromLocation(LatLng latlng) {
        if(geocoder==null){
            geocoder=new Geocoder(provider.getApplicationContext(), Locale.KOREA);
        }
        try{
            List<Address> geoResult=geocoder.getFromLocation(latlng.latitude,latlng.longitude,1);
            LatLng latlon=new LatLng(geoResult.get(0).getLatitude(),geoResult.get(0).getLongitude());
            DLogUtil.e(geoResult.toString());
            currentNation=geoResult.get(0).getCountryCode();
            currentCity=geoResult.get(0).getFeatureName().replace(" ","");
            view.showSearchResult(geoResult.get(0).getAddressLine(0));
        }catch(Exception e){
            DLogUtil.e(e.toString());
            return;
        }
    }

    @Override
    public Single<GoodsListHeader> getGoodsListHeader(Marker marker) {
        String key=userPinMap.get(marker.getPosition());
        SingleSubject<GoodsListHeader> subject=SingleSubject.create();
        disposable.add(remote.getUserPinPreview(key)
                .subscribe(header -> {
                    subject.onSuccess(header);
                },e->{
                    subject.onError(e);
                    DLogUtil.e(e.toString());
                }));
        return subject;
    }

    @Override
    public void userPinMarkerFinish() {
        view.userPinMarkerFinish(currentMarker);
    }

    @Override
    public void searchMapFromName(String place) {
        if(geocoder==null){
            geocoder=new Geocoder(provider.getApplicationContext(), Locale.KOREA);
        }
        try{
            List<Address> geoResult=geocoder.getFromLocationName(place,1);
            LatLng latlon=new LatLng(geoResult.get(0).getLatitude(),geoResult.get(0).getLongitude());
            DLogUtil.e(geoResult.toString());
            currentNation=geoResult.get(0).getCountryCode();
            currentCity=geoResult.get(0).getFeatureName().replace(" ","");
            view.showSearchResult(geoResult.get(0).getAddressLine(0));
            view.moveCamera(latlon);
            disposable.add(remote.getUserVisitedLocation(latlon)
                    .subscribe(pairlist -> {
                        userPinMap.clear();
                        for(Pair<LatLng,String> pair:pairlist){
                            userPinMap.put(pair.first,pair.second);
                            view.setUserPinMarker(pair.first);
                        }
                    },e->{
                        DLogUtil.e(e.getMessage());
                    }));
        }catch(Exception e){
            DLogUtil.e(e.toString());
            return;
        }
    }

    @Override
    public void userMarkerClicked(Marker marker) {
        if(currentMarker==null){
            currentMarker=marker;
            view.setUserPinMarkerClick(marker,true);
            return;
        }
        view.setUserPinMarkerClick(currentMarker,false);
        view.setUserPinMarkerClick(marker,true);
        currentMarker=marker;
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
