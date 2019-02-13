package com.teame.boostcamp.myapplication.ui.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentSearchBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.FragmentCallback;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.InputKeyboardUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.TedPermissionUtil;
import com.teame.boostcamp.myapplication.util.VectorConverterUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.disposables.CompositeDisposable;

public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchContract.Presenter> implements OnMapReadyCallback, SearchContract.View {

    private static final String EXTRA_GOODSLIST="EXTRA_GOODSLIST";
    private GoogleMap googleMap=null;
    private static final float ZOOM=16;
    private CompositeDisposable disposable=new CompositeDisposable();
    private Marker userMarker=null;
    private boolean isShowUserPin=false;
    private String currentNation="";
    private String currentCity="";
    private Marker currentMarker;
    private boolean isSelectedGoods=false;
    private ArrayList<Goods> selectedGoodsList;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_search;
    }

    @Override
    protected SearchContract.Presenter getPresenter() {
        return presenter;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new SearchPresenter(this, new ResourceProvider(getContext())));
        setUp();
    }

    private void setUp(){
        //view setting
        binding.ivUserpin.setOnClickListener(__ -> {
            if(isShowUserPin){
                hideUserPin();
            }
            else{
                showUserPin();
            }
        });
        binding.includeUserShoppingPreview.cvUserShoppingPreview.setOnClickListener(__ -> {
            presenter.getUserPinGoodsList(currentMarker);
        });
        binding.fabCreateChecklist.hide();
        binding.fabCreateChecklist.setOnClickListener(v -> {
            List<CalendarDay> selectedDates=binding.includePeriodSetting.mcvPeriod.getSelectedDates();
            Date startDate= java.sql.Date.valueOf(selectedDates.get(0).getDate().toString());
            Date endDate= java.sql.Date.valueOf(selectedDates.get(selectedDates.size()-1).getDate().toString());
            DLogUtil.e(endDate.toString());
            GoodsListHeader header=new GoodsListHeader(currentNation,currentCity,startDate,endDate);
            if(isSelectedGoods)
                CreateListActivity.startActivity(getContext(),header,selectedGoodsList);
            else
                CreateListActivity.startActivity(getContext(),header);
        });

        //googlemap setting
        binding.mvGooglemap.getMapAsync(this);

        //SearchBar setting
        binding.toolbarSearch.setOnClickListener(__ -> {
            ((FragmentCallback)getActivity()).startNewFragment();
        });
    }


    private void showUserPin(){
        isShowUserPin=true;
        presenter.showUserPin();
        binding.ivUserpin.setImageResource(R.drawable.btn_uncreate_userpinview);
    }

    @Override
    public void showUserGoodsListActivity(List<Goods> list) {
        UserShoppinglistActivity.startActivity(this,(ArrayList<Goods>)list);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==111){
            selectedGoodsList=data.getParcelableArrayListExtra(EXTRA_GOODSLIST);
            if(selectedGoodsList==null)
                isSelectedGoods=false;
            else
                isSelectedGoods=true;
        }
    }

    @Override
    public void showPositionInMap(LatLng latlon) {
        binding.svPlace.clearFocus();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon,ZOOM));
    }

    @Override
    public void showUserPin(LatLng location) {
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(VectorConverterUtil.convert(getContext(),R.drawable.btn_uncllick_marker)));
    }

    @Override
    public void hideUserPin(){
        googleMap.clear();
        isShowUserPin=false;
        binding.ivUserpin.setImageResource(R.drawable.btn_create_userpinview);
    }

    @Override
    public void showFragmentToast(String text) {
        binding.svPlace.clearFocus();
        super.showToast(text);
    }

    @Override
    public void showSearchResult(int count, String nation, String city) {
        currentNation=nation;
        currentCity=city;
        binding.ivUserpin.setVisibility(View.VISIBLE);
        binding.includeVisited.cvVisited.setVisibility(View.VISIBLE);
        binding.includeVisited.tvVisitedPlace.setText(currentCity);
        binding.includeVisited.tvVisitedCount.setText(count+"명이 이곳을 방문하였습니다.");
        binding.rvExList.setVisibility(View.GONE);
        binding.includePeriodSetting.cvPeriodSetting.setVisibility(View.GONE);
        binding.includeUserShoppingPreview.cvUserShoppingPreview.setVisibility(View.GONE);
    }

    @Override
    public void showUserPinPreview(GoodsListHeader header) {
        binding.includeUserShoppingPreview.cvUserShoppingPreview.setVisibility(View.VISIBLE);
        binding.includeVisited.cvVisited.setVisibility(View.GONE);
        binding.includeUserShoppingPreview.setHeader(header);
    }

    @Override
    public void showPeriodSetting() {
        binding.includePeriodSetting.cvPeriodSetting.setVisibility(View.VISIBLE);
        binding.includeVisited.cvVisited.setVisibility(View.GONE);
        binding.includeUserShoppingPreview.cvUserShoppingPreview.setVisibility(View.GONE);
        binding.fabCreateChecklist.show();
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
    public void onDetach() {
        presenter.onDetach();
        if(disposable!=null&&!disposable.isDisposed())
            disposable.dispose();
        super.onDetach();
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
        FusedLocationProviderClient fusedLocationClient= LocationServices.getFusedLocationProviderClient(getContext());
        if(ActivityCompat.checkSelfPermission(getContext(), TedPermissionUtil.LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                LatLng latlnt;
                if(task.getResult()==null)
                    latlnt = new LatLng(0,0);
                else
                    latlnt=new LatLng(task.getResult().getLatitude(),task.getResult().getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlnt,ZOOM));
            });
        }
        else{
            disposable.add(TedPermissionUtil.requestPermission(getContext(),getString(R.string.permission_location_title),getString(R.string.permission_location_message),TedPermissionUtil.LOCATION)
                    .subscribe(tedPermissionResult -> {
                        if(tedPermissionResult.isGranted()) {
                            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                                LatLng latlnt;
                                if(task.getResult()==null)
                                    latlnt = new LatLng(0,0);
                                else
                                    latlnt=new LatLng(task.getResult().getLatitude(),task.getResult().getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlnt,ZOOM));
                            });
                        }
                    }));
        }
        googleMap.setOnMarkerClickListener(marker -> {
            marker.setIcon(VectorConverterUtil.convert(getContext(),R.drawable.btn_click_marker));
            presenter.getUserPinPreview(marker);
            currentMarker=marker;
            return true;
        });
        googleMap.setOnMapLongClickListener(latLng -> {
            if(userMarker!=null)
                userMarker.remove();
            userMarker=googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(VectorConverterUtil.convert(getContext(),R.drawable.btn_location_marker))
                    .title("이 지역 선택"));
            userMarker.showInfoWindow();
        });
        googleMap.setOnInfoWindowClickListener(marker -> {
            if(currentNation.isEmpty()||currentCity.isEmpty())
                showFragmentToast("지역을 선택해 주세요");
            else
                showPeriodSetting();
        });
    }

}
