package com.teame.boostcamp.myapplication.ui;

import android.location.Address;
import android.location.Geocoder;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.adapter.FamousPlaceAdapter;
import com.teame.boostcamp.myapplication.adapter.LocationBaseGoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.MainOtherListViewPagerAdapter;
import com.teame.boostcamp.myapplication.adapter.OnItemClickListener;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.GoodsListRepository;
import com.teame.boostcamp.myapplication.model.repository.UserPinRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.LastKnownLocationUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewMainPresenter implements NewMainContract.Presenter, OnItemClickListener {
    private NewMainContract.View view;
    private CompositeDisposable disposable;
    private GoodsListRepository shoppingListRepository;
    private UserPinRepository userPinRepository;
    private ResourceProvider provider;
    private static final int MAX=5;
    private static final int PERIOD=2;
    private LocationBaseGoodsListRecyclerAdapter goodsAdapter;
    private FamousPlaceAdapter famousViewPagerAdapter;
    private MainOtherListViewPagerAdapter userViewPagerAdapter;
    private LatLng lastKnownPosition;
    private String lastKnownCode;
    private String lastKnownCity;

    public NewMainPresenter(NewMainContract.View view, ResourceProvider provider) {
        this.view = view;
        this.provider=provider;
        this.shoppingListRepository = GoodsListRepository.getInstance();
        this.userPinRepository = UserPinRepository.getInstance();
        disposable = new CompositeDisposable();
    }

    @Override
    public void onItemClick(View v, int position) {
        view.bannerClick(famousViewPagerAdapter.getItem(position).getCountryCode());
    }

    @Override
    public void locationMoreClick() {
        view.showCreateListActivity(new GoodsListHeader(lastKnownCode,lastKnownCity,lastKnownPosition.latitude,lastKnownPosition.longitude));
    }

    @Override
    public void visitedMoreClick() {
        view.showSearchMapActivity(lastKnownCity);
    }

    @Override
    public void getCurrentLocation() {
        view.setVisitedMoreView(false);
        view.setGoodsMoreView(false);
        view.showVisitedEmptyView(false);
        view.showGoodsEmptyView(false);
        disposable.add(LastKnownLocationUtil.getLastPosition(provider.getApplicationContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(latlng->{
                    lastKnownPosition=latlng;
                    Geocoder geocoder=new Geocoder(provider.getApplicationContext(), Locale.KOREA);
                    List<Address> result=geocoder.getFromLocation(latlng.latitude,latlng.longitude,1);
                    lastKnownCode=result.get(0).getCountryCode();
                    lastKnownCity=result.get(0).getLocality();
                    view.setCurrentLocation(result.get(0).getCountryName(),lastKnownCity);
                    loadListData(goodsAdapter,lastKnownCode,lastKnownCity);
                    loadHeaderKeys(latlng);
                },e->{
                    DLogUtil.e(e.toString());
                }));
    }

    @Override
    public void setLocationAdapter(LocationBaseGoodsListRecyclerAdapter adapter) {
        goodsAdapter=adapter;
        goodsAdapter.setOnClickListener((v, position) -> {
            Goods goods=goodsAdapter.getItem(position);
            view.showGoodsDetail(goods);
        });
    }

    @Override
    public void setUserViewPagerAdapter(MainOtherListViewPagerAdapter adapter) {
        userViewPagerAdapter=adapter;
        userViewPagerAdapter.setOnClickListener((v, position) -> {
            GoodsListHeader header= userViewPagerAdapter.getItem(position);
            getGoodsListFromHeader(header);
        });
    }

    private void getGoodsListFromHeader(GoodsListHeader header){
        disposable.add(userPinRepository.getUserPinGoodsList(header.getKey())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list->{
                    view.showUserShoppingActivity(list,header);
                },e->{
                    DLogUtil.e(e.toString());
                }));
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        this.view = null;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void loadListData(LocationBaseGoodsListRecyclerAdapter goodsAdapter, String nation, String city) {
        this.goodsAdapter = goodsAdapter;
        List<Goods> list=new ArrayList<>();
        disposable.add(shoppingListRepository.getItemListToObservable(nation, city)
                .take(MAX)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        goods -> {
                            list.add(goods);
                        },
                        e -> {
                            DLogUtil.d(e.getMessage());
                        },
                        ()->{
                            if(list.size()== Constant.LOADING_NONE_ITEM) {
                                view.showGoodsEmptyView(true);
                                view.setGoodsMoreView(false);
                                return;
                            }
                            else if(list.size()<MAX) {
                                view.setGoodsMoreView(false);
                            }
                            else{
                                view.setGoodsMoreView(true);
                            }
                            view.showGoodsEmptyView(false);
                            goodsAdapter.initItems(list);
                        }
                )
        );
    }

    @Override
    public void setViewPagerAdapter(FamousPlaceAdapter adapter) {
        this.famousViewPagerAdapter=adapter;
        famousViewPagerAdapter.setOnClickListener(this);
        disposable.add(Observable.interval(PERIOD, TimeUnit.SECONDS)
                .map(Long::intValue)
                .map(value->value%famousViewPagerAdapter.getCount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item->{
                    view.showViewPage(item);
                },e->{
                    DLogUtil.e(e.toString());
                }));
    }

    @Override
    public void loadHeaderKeys(LatLng center) {
        List<String> keyList=new ArrayList<>();
        disposable.add(userPinRepository.getUserVisitedLocationToSubject(center)
                .take(MAX)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    keyList.add(pair.second);
                },
                e -> DLogUtil.d(e.getMessage())
                ,()->{
                    if(keyList.size()== Constant.LOADING_NONE_ITEM) {
                        view.showVisitedEmptyView(true);
                        view.setVisitedMoreView(false);
                        return;
                    }
                    else if(keyList.size()<MAX) {
                        view.setVisitedMoreView(false);
                    }
                    else{
                        view.setVisitedMoreView(true);
                    }
                    view.showVisitedEmptyView(false);
                    loadHeaders(keyList);
                }));
    }

    private void loadHeaders(List<String> keyList) {
        disposable.add(userPinRepository.getUserHeaderList(keyList)
                .subscribe(
                        list -> {
                            userViewPagerAdapter.setHeaderlist(list);
                        },
                        e -> DLogUtil.d(e.getMessage())
                ));
    }

}
