package com.teame.boostcamp.myapplication.ui.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.FragmentSearchBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchContract.Presenter> implements OnMapReadyCallback, SearchContract.View {

    private GoogleMap googleMap=null;
    private static final float ZOOM=16;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_search;
    }

    @Override
    protected SearchContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected String getClassName() {
        return "SearchFragment";
    }

    @Deprecated
    public SearchFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=super.onCreateView(inflater,container,savedInstanceState);
        setPresenter(new SearchPresenter(this, new ResourceProvider(getContext())));
        setUp();
        return view;
    }

    private void setUp(){
        binding.mvGooglemap.getMapAsync(this);
        binding.svPlace.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearchSubmit(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void onSearchSubmit(String place){
        presenter.onSearchSubmit(place);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mvGooglemap.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mvGooglemap.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mvGooglemap.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mvGooglemap.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mvGooglemap.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.mvGooglemap.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mvGooglemap.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    @Override
    public void showPositionInMap(LatLng latlon) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon,ZOOM));
    }

    @Override
    public void showExSearchList(List<String> exsearch) {

    }

    @Override
    public void showSearchResult() {

    }

    @Override
    public void showUserPin() {

    }

    @Override
    public void userPinClicked() {

    }

    @Override
    public void showPeriodSetting() {

    }
}
