package com.teame.boostcamp.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import androidx.core.content.ContextCompat;

public class VectorConverterUtil {

    public static BitmapDescriptor convert(Context context, int vectorId){
        Drawable background= ContextCompat.getDrawable(context,vectorId);
        background.setBounds(0,0,background.getIntrinsicWidth(),background.getIntrinsicHeight());
        Bitmap vectorImage=Bitmap.createBitmap(background.getIntrinsicWidth(),background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(vectorImage);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(vectorImage);
    }
}
