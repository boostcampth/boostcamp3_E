package com.teame.boostcamp.myapplication.util;

import android.content.Context;

import java.lang.ref.WeakReference;


/**
 * Presenter에서 엑티비티에서 필요한 자원들을 접근할 수 있게 해주는 클레스(사용법은 ExampleActivity 참조)
 */
public class ResourceProvider {
    private WeakReference<Context> context;

    public ResourceProvider(Context context) {
        this.context = new WeakReference<>(context);
    }

    public Context getApplicationContext() {
        return context.get().getApplicationContext();
    }

    public String getString(int resource) {
        return context.get().getString(resource);
    }

}
