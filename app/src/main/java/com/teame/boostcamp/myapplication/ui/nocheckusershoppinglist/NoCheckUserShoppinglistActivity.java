package com.teame.boostcamp.myapplication.ui.nocheckusershoppinglist;

import com.teame.boostcamp.myapplication.databinding.ActivityNoCheckUsershoppinglistBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;

public class NoCheckUserShoppinglistActivity extends BaseMVPActivity<ActivityNoCheckUsershoppinglistBinding,NoCheckUserShoppinglistContract.Presenter>
        implements NoCheckUserShoppinglistContract.View {


    @Override
    protected NoCheckUserShoppinglistContract.Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutResourceId() {
        return 0;
    }
}
