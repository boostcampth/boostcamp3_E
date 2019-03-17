package com.teame.boostcamp.myapplication.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.ExListAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentSearchPlaceBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.ui.searchmap.SearchMapActivity;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
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
    public void onDetach() {
        presenter.onDetach();
        super.onDetach();
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
        binding.toolbarSearch.setNavigationIcon(R.drawable.btn_back);
        binding.toolbarSearch.setNavigationOnClickListener(__ -> {
            getFragmentManager().beginTransaction().remove(this).commit();
        });
        binding.etSearchPlace.setOnEditorActionListener((v, actionId, __) -> {
            if(actionId== EditorInfo.IME_ACTION_SEARCH){
                showMapActivity(v.getText().toString());
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
            presenter.goMapButtonClick();
        });
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
    }

    @Override
    public void showMapActivity(String city) {
        SearchMapActivity.startActivity(getContext(),city);
    }

    @Override
    public void onPause() {
        presenter.saveToJson();
        super.onPause();
    }
}
