package com.teame.boostcamp.myapplication.ui.searchmap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityMapSearchBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.search.SearchContract;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.TedPermissionUtil;
import com.teame.boostcamp.myapplication.util.VectorConverterUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import io.reactivex.disposables.CompositeDisposable;

public class SearchMapActivity extends BaseMVPActivity<ActivityMapSearchBinding,SearchMapContract.Presenter> implements OnMapReadyCallback,SearchMapContract.View {
    private GoogleMap map;
    private CompositeDisposable disposable=new CompositeDisposable();
    private static final int ZOOM=15;
    private static final String EXTRA_PLACE="EXTRA_PLACE";


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
        binding.mvGooglemap.getMapAsync(this);
        binding.mvGooglemap.onCreate(savedInstanceState);
    }

    public void hideBottom(){
        binding.pbSearchloading.setVisibility(View.VISIBLE);
        binding.tvCurrentPlace.setVisibility(View.GONE);
        binding.fabSetStart.hide();
        binding.tvSetStart.setVisibility(View.GONE);
    }

    public void showBottom(){
        binding.pbSearchloading.setVisibility(View.GONE);
        binding.tvCurrentPlace.setVisibility(View.VISIBLE);
        binding.fabSetStart.show();
        binding.tvSetStart.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSearchResult(String place) {
        showBottom();
        binding.tvCurrentPlace.setText(place);
    }

    @Override
    public void moveCamera(LatLng latlng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,ZOOM));
    }

    @Deprecated
    public SearchMapActivity() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
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
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        googleMap.setOnCameraMoveListener(() -> {
            hideBottom();
        });
        googleMap.setOnCameraIdleListener(() -> {
            showBottom();
            presenter.searchMapFromLocation(googleMap.getCameraPosition().target);
        });

        FusedLocationProviderClient fusedLocationClient= LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), TedPermissionUtil.LOCATION)== PackageManager.PERMISSION_GRANTED){
            Intent intent=getIntent();
            String place=intent.getStringExtra(EXTRA_PLACE);
            if(place!=null){
                presenter.searchMapFromName(place);
                return;
            }
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
            disposable.add(TedPermissionUtil.requestPermission(getApplicationContext(),getString(R.string.permission_location_title),getString(R.string.permission_location_message),TedPermissionUtil.LOCATION)
                    .subscribe(tedPermissionResult -> {
                        if(tedPermissionResult.isGranted()) {
                            Intent intent=getIntent();
                            String place=intent.getStringExtra(EXTRA_PLACE);
                            if(place!=null){
                                presenter.searchMapFromName(place);
                                return;
                            }
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

    }
}
