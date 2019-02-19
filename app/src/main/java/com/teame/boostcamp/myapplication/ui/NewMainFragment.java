package com.teame.boostcamp.myapplication.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.teame.boostcamp.myapplication.MainApplication;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.FamousPlaceAdapter;
import com.teame.boostcamp.myapplication.adapter.LocationBaseGoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.MainOtherListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentMainBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Banner;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.util.TedPermissionUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

public class NewMainFragment extends BaseFragment<FragmentMainBinding, NewMainContract.Presenter> implements NewMainContract.View {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_main;
    }

    @Override
    protected NewMainContract.Presenter getPresenter() {
        return new NewMainPresenter(this);
    }

    @Override
    public void setPresenter(NewMainContract.Presenter presenter) {
        super.setPresenter(presenter);
    }

    @Deprecated
    public NewMainFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setPresenter(new NewMainPresenter(this));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static NewMainFragment newInstance() {
        NewMainFragment fragment = new NewMainFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        // TODO - 초기 위치정보를 받아와야 합니다. 위치정보가 없을 시 default 값으로 설정해 주어야 합니다.
        // 초기의 상단 배너 리소스와 텍스트 초기화 - 하드코딩
        List<Integer> drawableId = new ArrayList<>();
        drawableId.add(R.drawable.view_japan);
        drawableId.add(R.drawable.view_thailand);
        List<Banner> banner = new ArrayList<>();
        banner.add(new Banner("오사카", "쇼핑의 천국 일본! 환율이 떨어진 만큼 부담없는 쇼핑!", "JP"));
        banner.add(new Banner("태국", "저렴한 물가, 맛있는 음식! 태국에서 꼭 사야할 꿀템들!", "TH"));
        binding.setBanner(banner.get(0));
        binding.tvBannerCreate.setOnClickListener(__ -> bannerCreateList(banner.get(0).getCountryCode()));

        binding.ivSearchPlace.setOnClickListener(__->{
            onSearchButtonClick();
        });
        binding.ablMain.addOnOffsetChangedListener((appBarLayout, i) -> {
            if(Math.abs(i)>=appBarLayout.getTotalScrollRange()){
                binding.ivSearchPlace.setImageResource(R.drawable.btn_search);
            }
            else if(i==0){
                binding.ivSearchPlace.setImageResource(R.drawable.btn_search_white);
            }
        });
        binding.vpFamousplace.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.setBanner(banner.get(position));
                binding.tvBannerCreate.setOnClickListener(__ -> bannerCreateList(banner.get(position).getCountryCode()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 리사이클러뷰 초기화
        LocationBaseGoodsListRecyclerAdapter goodsAdapter = new LocationBaseGoodsListRecyclerAdapter();
        LinearLayoutManager bannerLayoutManager = new LinearLayoutManager(getContext());
        bannerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        binding.ivSetLocation.setOnClickListener(__ -> onSetLocationClick());

        binding.rvLocationBaseItems.setLayoutManager(bannerLayoutManager);
        binding.rvLocationBaseItems.setAdapter(goodsAdapter);

        MainOtherListRecyclerAdapter listAdapter = new MainOtherListRecyclerAdapter();
        LinearLayoutManager listLayoutManager = new LinearLayoutManager(getContext());
        listLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // 하단 리사이클러뷰 구경하기 버튼 온클릭 리스너! -- GoodsHeaderList 가지고 import하는 레이아웃으로 이동하시면 됩니다!
        listAdapter.setOnShowDetailClickListener((v, position) -> showToast(position+""));
        binding.rvOtherList.setLayoutManager(listLayoutManager);
        binding.rvOtherList.setAdapter(listAdapter);

        binding.vpFamousplace.setAdapter(new FamousPlaceAdapter(getContext(), drawableId));
        //binding.tlCountryIndicator.setupWithViewPager(binding.vpFamousplace, true);



        // TODO- 아래의 두 부분을 조건에 따라 실행시키고 위치정보가 바뀌었을떄 다시 실행시켜주어야 합니다
        presenter.loadListData(goodsAdapter, "JP", "osaka");
        presenter.loadHeaderKeys(new LatLng(34.683036, 135.487775), listAdapter);

    }

    private void onSearchButtonClick(){
        // TODO- 상단툴바의 검색버튼(서치뷰)가 클릭되었을때의 이벤트 작성 --> 지도 검색 기능 -> 체크리스트 생성
        ((FragmentCallback)getActivity()).startNewFragment();
    }

    private void onSetLocationClick(){
        // TODO - 위치 버튼 클릭 -> 지도 검색 후 위치 선택 -> 메인으로 돌아와서 위치 set 및 recycler view 갱신
    }

    private void bannerCreateList(String countryCode){
        //Country Code 는 국가 코드 ex) JP, TH, 국가의 꿀템 리스트를 보여주는 액티비티로 전환 되어야 함.
        switch(countryCode){
            case "JP" :
                CreateListActivity.startActivity(getContext(), new GoodsListHeader(countryCode, "oskaka", Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 34.683036, 135.487775));
                break;
            case "TH" :
                CreateListActivity.startActivity(getContext(), new GoodsListHeader(countryCode, countryCode, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 13.7522, 100.5267));
                break;
        }
    }

}



