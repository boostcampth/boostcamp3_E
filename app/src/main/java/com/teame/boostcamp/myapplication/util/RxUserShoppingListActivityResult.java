package com.teame.boostcamp.myapplication.util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxUserShoppingListActivityResult {
    private static RxUserShoppingListActivityResult INSTANCE;
    private PublishSubject<Object> result=PublishSubject.create();

    public static RxUserShoppingListActivityResult getInstance(){
        if(INSTANCE==null){
            INSTANCE=new RxUserShoppingListActivityResult();
        }
        return INSTANCE;
    }

    public void register(Object object){
        result.onNext(object);
    }

    public Observable<Object> getEvent(){
        return result;
    }
}
