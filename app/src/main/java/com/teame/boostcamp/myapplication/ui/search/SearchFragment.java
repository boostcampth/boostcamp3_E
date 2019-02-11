package com.teame.boostcamp.myapplication.ui.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import com.teame.boostcamp.myapplication.model.entitiy.UserPinPreview;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.InputKeyboardUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.RxUserShoppingListActivityResult;
import com.teame.boostcamp.myapplication.util.TedPermissionUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
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
    private static final int REQUEST_RESULT_CODE=111;
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
        //adapter setting
        ExListAdapter adapter=new ExListAdapter();
        adapter.setOnItemClickListener(text -> {
            presenter.onSearchSubmit(text);
        });

        //RecyclerView setting
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        DividerItemDecoration divider=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        binding.rvExList.addItemDecoration(divider);
        binding.rvExList.setLayoutManager(layoutManager);
        binding.rvExList.setAdapter(adapter);
        binding.rvExList.setOnTouchListener((__, event) -> {
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                InputKeyboardUtil.hideKeyboard(getActivity());
                return true;
            }
            return false;
        });

        //view setting
        binding.ivUserpin.setOnClickListener(__ -> {
            if(isShowUserPin){
                isShowUserPin=false;
                hideUserPin();
                binding.ivUserpin.setImageResource(R.drawable.btn_create_userpinview);
            }
            else{
                isShowUserPin=true;
                presenter.showUserPin();
                binding.ivUserpin.setImageResource(R.drawable.btn_uncreate_userpinview);
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

        //presenter setting
        presenter.setAdpaterView(adapter);
        presenter.setAdpaterModel(adapter);

        //googlemap setting
        binding.mvGooglemap.getMapAsync(this);

        //SearchBar setting
        binding.toolbarSearch.setOnClickListener(__ -> {
            binding.svPlace.setIconified(false);
            showExSearchView();
        });
        binding.svPlace.setMaxWidth(binding.toolbarSearch.getWidth());
        binding.svPlace.setOnQueryTextFocusChangeListener((__, hasFocus) -> {
            if(hasFocus) {
                if(binding.rvExList.getVisibility()==View.GONE)
                    showExSearchView();
            }
            else{
                hideExSearchView();
            }
        });
        binding.svPlace.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onSearchSubmit(query);
                InputKeyboardUtil.hideKeyboard(getActivity());
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //presenter.onTextChange(newText);
                return true;
            }
        });
    }

    @Override
    public void showUserGoodsListActivity(List<Goods> list) {
        disposable.add(RxUserShoppingListActivityResult.getInstance()
                .getEvent()
                .subscribe(object->{
                    if(object instanceof Intent){
                        isSelectedGoods=true;
                        selectedGoodsList=((Intent)object).getParcelableArrayListExtra(EXTRA_GOODSLIST);
                    }
                },error->{
                    DLogUtil.e(error.getMessage());
                }));
        UserShoppinglistActivity.startActivity(getContext(),(ArrayList<Goods>)list);
    }

    @Override
    public void showPositionInMap(LatLng latlon, String currentNation, String currentCity) {
        binding.svPlace.clearFocus();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlon,ZOOM));
        this.currentNation=currentNation;
        this.currentCity=currentCity;
    }

    @Override
    public void showUserPin(LatLng location) {
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(vectorDescriptor(getContext(),R.drawable.btn_uncllick_marker)));
    }

    public void hideUserPin(){
        googleMap.clear();
    }

    public void showExSearchView() {
        binding.rvExList.setVisibility(View.VISIBLE);
        binding.viewBackground.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        presenter.initView();
    }

    @Override
    public void hideExSearchView(){
        binding.svPlace.clearFocus();
        binding.svPlace.setIconified(true);
        binding.rvExList.setVisibility(View.GONE);
        binding.viewBackground.setBackgroundColor(getResources().getColor(R.color.colorClear));
    }

    @Override
    public void showFragmentToast(String text) {
        binding.svPlace.clearFocus();
        super.showToast(text);
    }

    @Override
    public void showSearchResult(int count) {
        binding.ivUserpin.setVisibility(View.VISIBLE);
        binding.includeVisited.cvVisited.setVisibility(View.VISIBLE);
        binding.includeVisited.tvVisitedPlace.setText(currentCity);
        binding.includeVisited.tvVisitedCount.setText(count+"명이 이곳을 방문하였습니다.");
        binding.rvExList.setVisibility(View.GONE);
        binding.includePeriodSetting.cvPeriodSetting.setVisibility(View.GONE);
        binding.includeUserShoppingPreview.cvUserShoppingPreview.setVisibility(View.GONE);
    }

    @Override
    public void showUserPinPreview(UserPinPreview preview) {
        binding.includeUserShoppingPreview.cvUserShoppingPreview.setVisibility(View.VISIBLE);
        binding.includeVisited.cvVisited.setVisibility(View.GONE);
        binding.includeUserShoppingPreview.setPreview(preview);
    }

    @Override
    public void showPeriodSetting() {
        binding.includePeriodSetting.cvPeriodSetting.setVisibility(View.VISIBLE);
        binding.includeVisited.cvVisited.setVisibility(View.GONE);
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
        if(disposable!=null)
            disposable.dispose();
    }

    @Override
    public void onDetach() {
        presenter.onDetach();
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
                LatLng latlnt=new LatLng(task.getResult().getLatitude(),task.getResult().getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlnt,ZOOM));
            })
            .addOnFailureListener(exception->{
                DLogUtil.e(exception.toString());
                LatLng latlnt = new LatLng(0,0);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlnt, ZOOM));
            });
        }
        else{
            disposable.add(TedPermissionUtil.requestPermission(getContext(),getString(R.string.permission_location_title),getString(R.string.permission_location_message),TedPermissionUtil.LOCATION)
                    .subscribe(tedPermissionResult -> {
                        if(tedPermissionResult.isGranted()) {
                            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                                LatLng latlnt = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlnt, ZOOM));
                            })
                            .addOnFailureListener(exception->{
                                DLogUtil.e(exception.toString());
                                LatLng latlnt = new LatLng(0,0);
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlnt, ZOOM));
                            });
                        }
                    }));
        }
        googleMap.setOnMarkerClickListener(marker -> {
            marker.setIcon(vectorDescriptor(getContext(),R.drawable.btn_click_marker));
            presenter.getUserPinPreview(marker);
            currentMarker=marker;
            return true;
        });
        googleMap.setOnMapLongClickListener(latLng -> {
            if(userMarker!=null)
                userMarker.remove();
            userMarker=googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(vectorDescriptor(getContext(),R.drawable.btn_location_marker))
                    .title("이 지역 선택"));
            userMarker.showInfoWindow();
        });
        googleMap.setOnInfoWindowClickListener(marker -> {
            showPeriodSetting();
        });
    }

    private BitmapDescriptor vectorDescriptor(Context context, int vectorId){
        Drawable background= ContextCompat.getDrawable(context,vectorId);
        background.setBounds(0,0,background.getIntrinsicWidth(),background.getIntrinsicHeight());
        Bitmap vectorImage=Bitmap.createBitmap(background.getIntrinsicWidth(),background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(vectorImage);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(vectorImage);
    }
}
