package com.teame.boostcamp.myapplication.ui.addpost;

import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface AddPostContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void addPost();
        void pickGalleryImages();
        void takePicture();
    }
}
