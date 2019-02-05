package com.teame.boostcamp.myapplication.util.databinding;

import android.view.View;

import androidx.databinding.BindingConversion;

public class ViewUtil {

    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

}
