package com.teame.boostcamp.myapplication.ui;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.adapter.LocationBaseGoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.MainOtherListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.repository.GoodsListRepository;
import com.teame.boostcamp.myapplication.model.repository.UserPinRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class NewMainPresenter implements NewMainContract.Presenter {
    private NewMainContract.View view;
    private CompositeDisposable disposable;
    private GoodsListRepository shoppingListRepository;
    private UserPinRepository userPinRepository;
    private LocationBaseGoodsListRecyclerAdapter goodsAdapter;

    public NewMainPresenter(NewMainContract.View view) {
        this.view = view;
        this.shoppingListRepository = GoodsListRepository.getInstance();
        this.userPinRepository = UserPinRepository.getInstance();
        disposable = new CompositeDisposable();
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
        disposable.add(shoppingListRepository.getItemList(nation, city)
                .subscribe(
                        list -> {
                            goodsAdapter.initItems(list);
                            view.finishLoad();
                            DLogUtil.d(list.toString());
                        },
                        e -> {
                            DLogUtil.d(e.getMessage());
                            //view.finishLoad(Constant.FAIL_LOAD);
                        }
                )
        );
    }

    @Override
    public void loadHeaderKeys(LatLng center, MainOtherListRecyclerAdapter listAdapter) {
        disposable.add(userPinRepository.getUserVisitedLocation(center).subscribe(
                list -> {
                    List<String> keyList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        keyList.add(list.get(i).second);
                    }
                    loadHeaders(keyList, listAdapter);
                },
                e -> DLogUtil.d(e.getMessage())
        ));
    }


    @Override
    public void loadHeaders(List<String> keyList, MainOtherListRecyclerAdapter listAdapter) {
        disposable.add(userPinRepository.getUserHeaderList(keyList)
                .subscribe(
                        list -> {
                            DLogUtil.e(list.toString());
                            listAdapter.initItems(list);
                        },
                        e -> DLogUtil.d(e.getMessage())
                ));
    }

}
