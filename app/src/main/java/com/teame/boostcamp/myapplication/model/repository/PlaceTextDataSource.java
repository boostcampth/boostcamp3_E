package com.teame.boostcamp.myapplication.model.repository;

import io.reactivex.Observable;

public interface PlaceTextDataSource {
    void setText(String text);
    Observable<String> getText();
}
