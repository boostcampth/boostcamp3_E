package com.teame.boostcamp.myapplication.ui.addpost;

import android.net.Uri;

import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface AddPostContract {

    interface View extends BaseView {
        void pickGalleryImages();
        void succeedAddPost();
    }

    interface Presenter extends BasePresenter {
        void addPost(String title, String content, List<Uri> uriList);
        void takePicture();
    }
}
