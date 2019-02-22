package com.teame.boostcamp.myapplication.ui.searchmap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityMapSearchBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.MainActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.ui.userpininfo.UserPinInfoFragment;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.LastKnownLocationUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.TedPermissionUtil;
import com.teame.boostcamp.myapplication.util.VectorConverterUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SearchMapActivity extends BaseMVPActivity<ActivityMapSearchBinding,SearchMapContract.Presenter> implements OnMapReadyCallback
        ,SearchMapContract.View
        ,UserPinFragmentCallback{
    private GoogleMap map;
    private CompositeDisposable disposable=new CompositeDisposable();
    private static final int ZOOM=15;
    private static final String EXTRA_PLACE="EXTRA_PLACE";
    private static final String EXTRA_GOODSLIST="EXTRA_GOODSLIST";


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_map_search;
    }

    @Override
    protected SearchMapContract.Presenter getPresenter() {
        return presenter;
    }

    public static void startActivity(Context context){
        Intent intent=new Intent(context,SearchMapActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String place){
        Intent intent=new Intent(context,SearchMapActivity.class);
        intent.putExtra(EXTRA_PLACE,place);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_search_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.place_confirm:
                GoodsListHeader header=presenter.getGoodsListHeader();
                header.setLat(map.getCameraPosition().target.latitude);
                header.setLng(map.getCameraPosition().target.longitude);
                if(header.getNation()==null||header.getCity()==null){
                    showToast("장소 값이 없습니다. 다시 시도해 주세요");
                    MainActivity.startActivity(this);
                }
                if(presenter.getSelectedList()==null)
                    CreateListActivity.startActivity(getApplicationContext(),header);
                else
                    CreateListActivity.startActivity(getApplicationContext(),header,presenter.getSelectedList());
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new SearchMapPresenter(this,new ResourceProvider(getApplicationContext())));
        setSupportActionBar(binding.toolbarText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        binding.toolbarText.setNavigationIcon(R.drawable.btn_back);
        binding.toolbarText.setNavigationOnClickListener(v -> {
            finish();
        });
        binding.fabCurrent.setOnClickListener(__ -> {
            setCurrentLocation();
        });
        binding.mvGooglemap.getMapAsync(this);
        binding.mvGooglemap.onCreate(savedInstanceState);
    }

    public void hideBottom(){
        binding.pbSearchloading.setVisibility(View.VISIBLE);
        binding.tvCurrentPlace.setVisibility(View.GONE);
        binding.fabCurrent.hide();
    }

    public void showBottom(){
        binding.pbSearchloading.setVisibility(View.GONE);
        binding.tvCurrentPlace.setVisibility(View.VISIBLE);
        binding.fabCurrent.show();
    }

    private void setCurrentLocation(){
        disposable.add(LastKnownLocationUtil.getLastPosition(this)
                .subscribe(latLng -> {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,ZOOM));
                }));
    }

    @Override
    public void showSearchResult(String place) {
        showBottom();
        binding.tvCurrentPlace.setText(place);
    }

    @Override
    public void fragmentFinish() {
        presenter.userPinMarkerFinish();
    }

    @Override
    public void moveCamera(LatLng latlng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,ZOOM));
    }

    @Override
    public void setUserPinMarker(LatLng latlng) {
        map.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(VectorConverterUtil.convert(getApplicationContext(),R.drawable.btn_unclick_marker)));
    }

    @Override
    public void setUserPinMarkerClick(Marker marker, boolean click) {
        if(click){
            marker.setIcon(VectorConverterUtil.convert(getApplicationContext(),R.drawable.btn_click_marker));
        }
        else{
            marker.setIcon(VectorConverterUtil.convert(getApplicationContext(),R.drawable.btn_unclick_marker));
        }
    }

    @Deprecated
    public SearchMapActivity() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==111){
            List<Goods> list=data.getParcelableArrayListExtra(EXTRA_GOODSLIST);
            DLogUtil.e(list.toString());
            if(list.size()==0){
                DLogUtil.e("List is Empty");
            }
            else
                presenter.addSelectedList(list);
        }
    }

    @Override
    public void userPinMarkerFinish(Marker marker) {
        marker.setIcon(VectorConverterUtil.convert(getApplicationContext(),R.drawable.btn_unclick_marker));
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.mvGooglemap.onStop();
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
        presenter.onDetach();
        if(disposable!=null&&!disposable.isDisposed())
            disposable.dispose();
        binding.mvGooglemap.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mvGooglemap.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mvGooglemap.onSaveInstanceState(outState);
    }

    @Override
    public void failSearch() {
        showToast("찾으시는 장소가 없습니다.");
        disposable.add(LastKnownLocationUtil.getLastPosition(getApplicationContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(latLng -> {
                    moveCamera(latLng);
                }));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        googleMap.setOnCameraMoveListener(() -> {
            hideBottom();
        });

        googleMap.setOnCameraIdleListener(() -> {
            showBottom();
            presenter.searchMapFromLocation(googleMap.getCameraPosition().target);
        });

        googleMap.setOnMarkerClickListener(marker -> {
            presenter.userMarkerClicked(marker);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),ZOOM));
            presenter.getGoodsListHeaderFromMarker(marker)
                    .subscribe(header -> {
                        FragmentManager manager=getSupportFragmentManager();
                        manager.popBackStackImmediate();
                        FragmentTransaction transaction=manager.beginTransaction();
                        transaction.replace(R.id.view_userpininfo, UserPinInfoFragment.newInstance(header));
                        transaction.addToBackStack(null);
                        transaction.commit();
                    });
            return true;
        });
        Intent intent=getIntent();
        String place=intent.getStringExtra(EXTRA_PLACE);
        if(place!=null){
            presenter.searchMapFromName(place);
            return;
        }
        disposable.add(LastKnownLocationUtil.getLastPosition(getApplicationContext())
                .subscribe(latLng -> {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,ZOOM));
                },e->{
                    DLogUtil.e(e.toString());
                }));
    }
}
