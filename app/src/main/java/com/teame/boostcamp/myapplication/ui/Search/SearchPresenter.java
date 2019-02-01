package com.teame.boostcamp.myapplication.ui.Search;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import java.util.List;
import java.util.Locale;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View view;
    private Geocoder geocoder=null;
    private ResourceProvider provider;
    public SearchPresenter(SearchContract.View view, ResourceProvider provider){
        this.view=view;
        this.provider=provider;
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
            view.showPositionInMap(latlon);
            DLogUtil.d("위도: "+geoResult.get(0).getLatitude()+", 경도: "+geoResult.get(0).getLongitude());
            DLogUtil.d("지역: "+geoResult.get(0).getCountryCode());
        }catch(Exception e){
            DLogUtil.e(e.getMessage());
        }
    }

    @Override
    public void onDetach() {
        geocoder=null;
        view=null;
    }
}
