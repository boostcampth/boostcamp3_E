package com.teame.boostcamp.myapplication.ui.userpininfo;

import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.UserPinRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import io.reactivex.disposables.CompositeDisposable;

public class UserPinInfoPresenter implements UserPinInfoContract.Presenter {

    private UserPinInfoContract.View view;
    private ResourceProvider provider;
    private CompositeDisposable disposable=new CompositeDisposable();
    private UserPinRepository remote;
    private GoodsListHeader header;

    public UserPinInfoPresenter(UserPinInfoContract.View view, ResourceProvider provider, GoodsListHeader header){
        this.view=view;
        this.provider=provider;
        remote=UserPinRepository.getInstance();
        this.header=header;
    }

    @Override
    public GoodsListHeader getGoodsListHeader() {
        return header;
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        if(disposable!=null&&!disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    public void getGoodsList() {
        disposable.add(remote.getUserPinGoodsList(header.getKey())
                .subscribe(goodslist->{
                    view.showUserShoppinglistActivitiy(goodslist);
                },e->{
                    DLogUtil.e(e.toString());
                }));
    }
}
