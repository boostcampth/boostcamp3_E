package com.teame.boostcamp.myapplication.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class InputKeyboardUtil {
    public static void hideKeyboard(Activity activity){
        InputMethodManager manager=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0);
    }
}
