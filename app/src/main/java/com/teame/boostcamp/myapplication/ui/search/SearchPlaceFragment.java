package com.teame.boostcamp.myapplication.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentSearchBinding;
import com.teame.boostcamp.myapplication.databinding.FragmentSearchPlaceBinding;
import com.teame.boostcamp.myapplication.ui.FragmentCallback;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.SharedPreferenceUtil;
import com.teame.boostcamp.myapplication.util.VectorConverterUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SearchPlaceFragment extends BaseFragment<FragmentSearchPlaceBinding, SearchPlaceContract.Presenter> implements SearchPlaceContract.View {


    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_search_place;
    }

    @Override
    protected SearchPlaceContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                getFragmentManager().beginTransaction().remove(this).commit();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static SearchPlaceFragment newInstance(){
        return new SearchPlaceFragment();
    }

    @Override
    public void exitFragment() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new SearchPlacePresenter(this, new ResourceProvider(getContext())));
        ExListAdapter adapter=new ExListAdapter();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.rvExList.setLayoutManager(layoutManager);
        binding.rvExList.setAdapter(adapter);
        binding.toolbarSearch.setNavigationIcon(R.drawable.btn_back);
        binding.etSearchPlace.setOnEditorActionListener((v, actionId, __) -> {
            if(actionId== EditorInfo.IME_ACTION_SEARCH){
                presenter.search(v.getText().toString());
                return true;
            }
            return false;
        });
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
        presenter.createList();
    }

    @Override
    public void onDestroyView() {
        presenter.saveToJson();
        super.onDestroyView();
    }
}
