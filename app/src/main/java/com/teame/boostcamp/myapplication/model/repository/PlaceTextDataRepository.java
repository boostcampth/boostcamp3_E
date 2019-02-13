package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.repository.local.PlaceTextLocalDataSource;

import io.reactivex.Observable;

public class PlaceTextDataRepository implements PlaceTextDataSource{
    private PlaceTextLocalDataSource placeTextLocalDataSource;
    private static PlaceTextDataRepository INSTANCE;

    public PlaceTextDataRepository(){
        placeTextLocalDataSource=PlaceTextLocalDataSource.getInstance();
    }

    public static PlaceTextDataRepository getInstance(){
        if(INSTANCE==null){
            INSTANCE=new PlaceTextDataRepository();
        }
        return INSTANCE;
    }

    @Override
    public void setText(String text) {
        placeTextLocalDataSource.setText(text);
    }

    @Override
    public Observable<String> getText() {
        return placeTextLocalDataSource.getText();
    }
}
