package com.teame.boostcamp.myapplication.ui.userpininfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.FragmentUserpininfoBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.ui.search.UserShoppinglistActivity;
import com.teame.boostcamp.myapplication.ui.searchmap.UserPinFragmentCallback;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserPinInfoFragment extends BaseFragment<FragmentUserpininfoBinding,UserPinInfoContract.Presenter> implements UserPinInfoContract.View{

    private static final String ARGUMENT_HEADER="ARGUMENT_HEADER";
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_userpininfo;
    }

    @Override
    protected UserPinInfoContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void showUserShoppinglistActivitiy(List<Goods> list) {
        UserShoppinglistActivity.startActivity(getContext(),presenter.getGoodsListHeader(),(ArrayList<Goods>)list);
    }

    public static UserPinInfoFragment newInstance(GoodsListHeader header){
        UserPinInfoFragment userPinInfoFragment=new UserPinInfoFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(ARGUMENT_HEADER,header);
        userPinInfoFragment.setArguments(bundle);
        return userPinInfoFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle=getArguments();
        GoodsListHeader header = bundle.getParcelable(ARGUMENT_HEADER);
        DLogUtil.e(header.toString());
        setPresenter(new UserPinInfoPresenter(this,new ResourceProvider(getContext()),header));
        binding.setHeader(header);
        binding.cvUserpininfo.setOnClickListener(__ -> {
            presenter.getGoodsList();
        });
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if(keyCode==KeyEvent.KEYCODE_BACK){
                getFragmentManager().beginTransaction().remove(this).commit();
                ((UserPinFragmentCallback)getActivity()).fragmentFinish();
                return true;
            }
            return false;
        });
    }
}
