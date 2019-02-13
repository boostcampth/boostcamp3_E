package com.teame.boostcamp.myapplication.ui.addpost;

import android.graphics.Bitmap;

import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;
import com.teame.boostcamp.myapplication.ui.goodsdetail.GoodsDetailActivity;

import java.util.List;

public interface AddPostContract {

    interface View extends BaseView {
        void pickGalleryImages();
        void takePicture();
        void succeedAddPost();
        void showListSelection(List<GoodsListHeader> goodsListHeaders);
        void failAddPost();
    }

    interface Presenter extends BasePresenter {
        void addPost(String content, List<Bitmap> bitmapList);
        void loadMyList();
        void setListSelection(GoodsListHeader selected);

    }
}
