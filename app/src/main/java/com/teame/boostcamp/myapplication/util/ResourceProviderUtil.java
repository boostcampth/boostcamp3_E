package com.teame.boostcamp.myapplication.util;

import android.content.Context;


/**
 * Presenter에서 엑티비티에서 필요한 자원들을 접근할 수 있게 해주는 클레스(사용법은 ExampleActivity 참조) */
public class ResourceProviderUtil {
    public Context context;

    public ResourceProviderUtil(Context context){
        this.context = context;
    }

    public Context getApplicationContext(){
        return context.getApplicationContext();
    }

    public String getString(int resource){
        return context.getString(resource);
    }

}
