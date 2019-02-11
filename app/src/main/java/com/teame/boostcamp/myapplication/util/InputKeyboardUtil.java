package com.teame.boostcamp.myapplication.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class InputKeyboardUtil {

    private InputKeyboardUtil(){

    }

    public static void hideKeyboard(Activity activity){
        InputMethodManager manager=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        }
    }
}
