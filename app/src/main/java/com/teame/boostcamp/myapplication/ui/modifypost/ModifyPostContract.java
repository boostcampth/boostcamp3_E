package com.teame.boostcamp.myapplication.ui.modifypost;

import android.app.ProgressDialog;
import android.net.Uri;

import com.teame.boostcamp.myapplication.adapter.PreviewImageAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface ModifyPostContract {

    interface View extends BaseView {
        void pickGalleryImages();

        void takePicture();

        void succeedModifyPost();

        void occurServerError();

        ProgressDialog showLoading();

    }

    interface Presenter extends BasePresenter {

        void loadModifyImage(List<String> imagePath, PreviewImageAdapter adapter);

        void modifyPost(Post oldPost, String content);


    }
}
