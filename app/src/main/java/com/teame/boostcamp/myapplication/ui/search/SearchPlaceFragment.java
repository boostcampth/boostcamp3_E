package com.teame.boostcamp.myapplication.ui.search;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentSearchPlaceBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.ui.searchmap.SearchMapActivity;
import com.teame.boostcamp.myapplication.util.LastKnownLocationUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class SearchPlaceFragment extends BaseFragment<FragmentSearchPlaceBinding, SearchPlaceContract.Presenter> implements SearchPlaceContract.View {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_search_place;
    }

    @Override
    protected SearchPlaceContract.Presenter getPresenter() {
        return presenter;
    }

    public static SearchPlaceFragment newInstance(){
        return new SearchPlaceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onStart() {
        presenter.createList();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void showCreateList(GoodsListHeader header) {
        CreateListActivity.startActivity(getContext(),header);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new SearchPlacePresenter(this, new ResourceProvider(getContext())));
        ExListAdapter adapter=new ExListAdapter();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.rvExList.setLayoutManager(layoutManager);
        binding.rvExList.setAdapter(adapter);
        binding.rvExList.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        binding.rvExList.addItemDecoration(new MarginDecorator(32));
        binding.toolbarSearch.setNavigationIcon(R.drawable.btn_back);
        binding.toolbarSearch.setNavigationOnClickListener(__ -> {
            getFragmentManager().beginTransaction().remove(this).commit();
        });
        binding.etSearchPlace.setOnEditorActionListener((v, actionId, __) -> {
            if(actionId== EditorInfo.IME_ACTION_SEARCH){
                SearchMapActivity.startActivity(getContext(),v.getText().toString());
                presenter.search(v.getText().toString());
                return true;
            }
            return false;
        });
        binding.buttonCurrentLocation.setOnClickListener(__->{
            //TODO: 아이템 리스트 생성
            presenter.currentButtonClick();
        });
        binding.buttonGoMap.setOnClickListener(__->{
            //TODO: SearchMapActivity 생성
            SearchMapActivity.startActivity(getContext());
        });
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
    }

    @Override
    public void onPause() {
        presenter.saveToJson();
        super.onPause();
    }
}
