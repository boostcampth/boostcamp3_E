package com.teame.boostcamp.myapplication.util.databinding;

import androidx.databinding.BindingConversion;

public class TextViewUtil {

    @BindingConversion
    public static String convertDoubleToDisplayedString(Double num) {
        return num.toString();
    }

    @BindingConversion
    public static String convertintToDisplayedString(int number) {
        return String.valueOf(number);
    }

}
