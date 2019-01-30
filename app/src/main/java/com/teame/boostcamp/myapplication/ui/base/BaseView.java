package com.teame.boostcamp.myapplication.ui.base;

public interface BaseView<T> {

    /**
     * presenter 인스턴스를 받기위한 BaseMethod */
    void setPresenter(T presenter);

}
