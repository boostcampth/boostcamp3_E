package com.teame.boostcamp.myapplication.model.repository.local;

import com.teame.boostcamp.myapplication.model.repository.PlaceTextDataSource;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class PlaceTextLocalDataSource implements PlaceTextDataSource {
    private static PlaceTextLocalDataSource INSTANCE;
    private PublishSubject<String> subject;

    private PlaceTextLocalDataSource(){
        subject=PublishSubject.create();
    }

    public static PlaceTextLocalDataSource getInstance(){
        if(INSTANCE==null){
            INSTANCE=new PlaceTextLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void setText(String text) {
        subject.onNext(text);
    }

    @Override
    public Observable<String> getText() {
        return subject;
    }
}
