package com.teame.boostcamp.myapplication.ui.base;

public interface BasePresenter {

    /**
     * presenter가 붙었을때 해줘야할 일들 작성 */
    void onAttach();


    /**
     * presenter가 떨어졌을떄 해줘야할 일들 작성
     * ex) dispose() , clear() etc.. */
    void onDetach();
}
