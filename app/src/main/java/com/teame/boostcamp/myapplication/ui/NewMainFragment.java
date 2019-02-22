package com.teame.boostcamp.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.FamousPlaceAdapter;
import com.teame.boostcamp.myapplication.adapter.LocationBaseGoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.MainOtherListViewPagerAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentMainBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Banner;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.ui.goodsdetail.GoodsDetailActivity;
import com.teame.boostcamp.myapplication.ui.nocheckusershoppinglist.NoCheckUserShoppinglistActivity;
import com.teame.boostcamp.myapplication.ui.searchmap.SearchMapActivity;
import com.teame.boostcamp.myapplication.ui.usershoppinglist.UserShoppinglistActivity;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NewMainFragment extends BaseFragment<FragmentMainBinding, NewMainContract.Presenter> implements NewMainContract.View {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_main;
    }

    @Override
    protected NewMainContract.Presenter getPresenter() {
        return presenter;
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
        setPresenter(new NewMainPresenter(this,new ResourceProvider(getContext())));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static NewMainFragment newInstance() {
        NewMainFragment fragment = new NewMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void showGoodsDetail(Goods goods) {
        GoodsDetailActivity.startActivity(getContext(),goods);
    }

    @Override
    public void setGoodsMoreView(boolean state) {
        if(state)
            binding.tvItemMore.setVisibility(View.VISIBLE);
        else
            binding.tvItemMore.setVisibility(View.GONE);
    }

    @Override
    public void setVisitedMoreView(boolean state) {
        if(state)
            binding.tvVisitedMore.setVisibility(View.VISIBLE);
        else
            binding.tvVisitedMore.setVisibility(View.GONE);
    }

    @Override
    public void showGoodsEmptyView(boolean state) {
        if(state){
            binding.includeLocationEmptyView.clLocationContainer.setVisibility(View.VISIBLE);
            binding.rvLocationBaseItems.setVisibility(View.GONE);
        }
        else{
            binding.includeLocationEmptyView.clLocationContainer.setVisibility(View.GONE);
            binding.rvLocationBaseItems.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showVisitedEmptyView(boolean state) {
        if(state){
            binding.includeVisitedEmptyView.clVisitedContainer.setVisibility(View.VISIBLE);
            binding.vpVisitedList.setVisibility(View.GONE);
        }
        else{
            binding.includeVisitedEmptyView.clVisitedContainer.setVisibility(View.GONE);
            binding.vpVisitedList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setCurrentLocation(String nation, String city) {
        binding.tvCurrentLoaction.setText(nation+" "+city);
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
        FamousPlaceAdapter famousAdapter=new FamousPlaceAdapter(getContext(),drawableId,banner);
        presenter.setViewPagerAdapter(famousAdapter);
        binding.vpFamousplace.setAdapter(famousAdapter);

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
        // 리사이클러뷰 초기화
        LocationBaseGoodsListRecyclerAdapter goodsAdapter = new LocationBaseGoodsListRecyclerAdapter();
        LinearLayoutManager bannerLayoutManager = new LinearLayoutManager(getContext());
        bannerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        presenter.setLocationAdapter(goodsAdapter);
        binding.ivSetLocation.setOnClickListener(__ -> onSetLocationClick());
        binding.tvItemMore.setOnClickListener(__->{
            presenter.locationMoreClick();
        });
        binding.tvVisitedMore.setOnClickListener(__->{
            presenter.visitedMoreClick();
        });

        binding.rvLocationBaseItems.setLayoutManager(bannerLayoutManager);
        binding.rvLocationBaseItems.setAdapter(goodsAdapter);
        MainOtherListViewPagerAdapter listAdapter = new MainOtherListViewPagerAdapter();
        LinearLayoutManager listLayoutManager = new LinearLayoutManager(getContext());
        listLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        presenter.setUserViewPagerAdapter(listAdapter);
        binding.vpVisitedList.setAdapter(listAdapter);
        binding.vpVisitedList.setClipToPadding(false);
        binding.vpVisitedList.setPageMargin(50);

        binding.rvLocationBaseItems.setNestedScrollingEnabled(false);

        presenter.getCurrentLocation();

        binding.includeLocationEmptyView.clLocationContainer.setOnClickListener(__->{
            onSearchButtonClick();
        });

        binding.includeVisitedEmptyView.clVisitedContainer.setOnClickListener(__->{
            presenter.visitedMoreClick();
        });
    }

    @Override
    public void showCreateListActivity(GoodsListHeader header) {
        CreateListActivity.startActivity(getContext(),header);
    }

    @Override
    public void showSearchMapActivity(String place) {
        SearchMapActivity.startActivity(getContext(),place);
    }

    @Override
    public void showUserShoppingActivity(List<Goods> list, GoodsListHeader header) {
        NoCheckUserShoppinglistActivity.startActivity(getContext(),header,(ArrayList<Goods>)list);
    }

    @Override
    public void showViewPage(int position) {
        binding.vpFamousplace.setCurrentItem(position);
    }

    private void onSearchButtonClick(){
        // TODO- 상단툴바의 검색버튼(서치뷰)가 클릭되었을때의 이벤트 작성 --> 지도 검색 기능 -> 체크리스트 생성
        ((FragmentCallback)getActivity()).startNewFragment();
    }

    private void onSetLocationClick(){
        // TODO - 위치 버튼 클릭 -> 지도 검색 후 위치 선택 -> 메인으로 돌아와서 위치 set 및 recycler view 갱신
        presenter.getCurrentLocation();
    }

    @Override
    public void bannerClick(String country) {
        //Country Code 는 국가 코드 ex) JP, TH, 국가의 꿀템 리스트를 보여주는 액티비티로 전환 되어야 함.
        switch(country){
            case "JP" :
                CreateListActivity.startActivity(getContext(), new GoodsListHeader(country, "oskaka", Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 34.683036, 135.487775));
                break;
            case "TH" :
                CreateListActivity.startActivity(getContext(), new GoodsListHeader(country, "bangkok", Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), 13.7522, 100.5267));
                break;
        }
    }

    @Override
    public void onDetach() {
        presenter.onDetach();
        super.onDetach();
    }
}



