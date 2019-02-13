package com.teame.boostcamp.myapplication.ui.search;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapterContract;
import com.teame.boostcamp.myapplication.model.repository.PlaceTextDataRepository;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.SharedPreferenceUtil;

import java.util.ArrayList;

public class SearchPlacePresenter implements SearchPlaceContract.Presenter {

    private static final String PREF_EX_SEARCH="PREF_EX_SEARCH";
    private SearchPlaceContract.View view;
    private ResourceProvider provider;
    private ExListAdapterContract.View adapterView;
    private ExListAdapterContract.Model adapterModel;
    private PlaceTextDataRepository repository;
    private ArrayList<String> exList;
    private int STRING_CAPACITY=20;

    @Override
    public void setAdapterView(ExListAdapterContract.View view) {
        adapterView=view;
        adapterView.setOnItemClickListener(text -> {
            repository.setText(text);
            this.view.exitFragment();
        });
    }

    @Override
    public void search(String text) {
        repository.setText(text);
        if(adapterModel.searchList(text)){
            if(adapterModel.getList().size()>=STRING_CAPACITY){
                exList.remove(0);
                adapterModel.remove(0);
            }
            exList.add(text);
            adapterModel.add(text);
        }
        view.exitFragment();
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
        repository=PlaceTextDataRepository.getInstance();
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
    }
}
