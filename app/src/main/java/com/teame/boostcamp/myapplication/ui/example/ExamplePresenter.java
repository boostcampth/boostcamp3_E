package com.teame.boostcamp.myapplication.ui.example;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

public class ExamplePresenter implements ExampleContract.Presenter {

    private ExampleContract.View view;
    private ResourceProvider resourceProvider;

    /**
     * view를 받고 자기자신(presenter)를 view.setPresenter(this)로 반환시켜준다. */
    protected ExamplePresenter(ExampleContract.View view, ResourceProvider resourceProvider){
        this.view = view;
        this.resourceProvider = resourceProvider;
        view.setPresenter(this);
    }

    @Override
    public void showTost() {
//        view.showTost(resourceProvider.getString(R.string.app_name));
    }

    /**
     * presenter가 붙었을때 해줘야할 일들 작성 */
    @Override
    public void onAttach() {

    }


    /**
     * presenter가 떨어졌을떄 해줘야할 일들 작성
     * ex) dispose() , clear() etc.. */
    @Override
    public void onDetach() {

    }
}
