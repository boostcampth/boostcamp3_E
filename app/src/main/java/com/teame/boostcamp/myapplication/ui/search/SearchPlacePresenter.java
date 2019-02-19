package com.teame.boostcamp.myapplication.ui.search;

import android.location.Address;
import android.location.Geocoder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapterContract;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.PlaceTextDataRepository;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.ui.searchmap.SearchMapActivity;
import com.teame.boostcamp.myapplication.util.LastKnownLocationUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

public class SearchPlacePresenter implements SearchPlaceContract.Presenter {

    private static final String PREF_EX_SEARCH="PREF_EX_SEARCH";
    private SearchPlaceContract.View view;
    private ResourceProvider provider;
    private ExListAdapterContract.View adapterView;
    private ExListAdapterContract.Model adapterModel;
    private ArrayList<String> exList;
    private int STRING_CAPACITY=20;
    private boolean isChange=false;
    private CompositeDisposable disposable=new CompositeDisposable();

    @Override
    public void setAdapterView(ExListAdapterContract.View view) {
        adapterView=view;
        adapterView.setOnItemClickListener(text -> {
            SearchMapActivity.startActivity(provider.getApplicationContext(),text);
        });
    }

    @Override
    public void onStop() {
        if(isChange)
            adapterModel.setList(exList);
    }

    @Override
    public void currentButtonClick() {
        disposable=new CompositeDisposable();
        disposable.add(LastKnownLocationUtil.getLastPosition(provider.getApplicationContext())
                .subscribe(latLng -> {
                    Geocoder geocoder=new Geocoder(provider.getApplicationContext(), Locale.KOREA);
                    List<Address> result=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    String currentNation=result.get(0).getCountryCode();
                    String currentCity=result.get(0).getLocality().replace(" ","");
                    GoodsListHeader header=new GoodsListHeader(currentNation,currentCity, latLng.latitude,latLng.longitude);
                    view.showCreateList(header);
                }));
    }

    @Override
    public void search(String text) {
        if(adapterModel.searchList(text)){
            if(adapterModel.getList().size()>=STRING_CAPACITY){
                exList.remove(0);
            }
            exList.add(text);
            isChange=true;
        }
        else
            isChange=false;
    }

    @Override
    public void setAdapterModel(ExListAdapterContract.Model model) {
        adapterModel=model;
    }

    @Override
    public void createList() {
        String exJson= SharedPreferenceUtil.getString(provider.getApplicationContext(),PREF_EX_SEARCH);
        Gson gson=new Gson();
        if(exJson==null){
            exList=new ArrayList<>();
        }else{
            exList=gson.fromJson(exJson,new TypeToken<ArrayList<String>>(){}.getType());
        }
        adapterModel.setList(exList);
    }

    public SearchPlacePresenter(SearchPlaceContract.View view, ResourceProvider provider){
        this.view=view;
        this.provider=provider;
    }

    @Override
    public void saveToJson() {
        Gson gson=new Gson();
        String toJson=gson.toJson(adapterModel.getList());
        SharedPreferenceUtil.putString(provider.getApplicationContext(),PREF_EX_SEARCH,toJson);
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        if(disposable!=null&&disposable.isDisposed())
            disposable.dispose();
    }
}
