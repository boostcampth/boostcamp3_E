package com.teame.boostcamp.myapplication.ui.addpost;

import android.app.Activity;

public class AddPostPresenter implements AddPostContract.Presenter {
    private AddPostContract.View view;

    public AddPostPresenter(AddPostContract.View view) {
        this.view = view;
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        this.view=null;
    }

    @Override
    public void addPost() {

    }

    @Override
    public void pickGalleryImages() {

    }

    @Override
    public void takePicture() {

    }
}

