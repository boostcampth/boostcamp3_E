package com.teame.boostcamp.myapplication.util;

import android.Manifest;
import android.content.Context;

import com.gun0912.tedpermission.TedPermissionResult;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import io.reactivex.Single;

public class TedPermissionUtil {
    public static final String CAMERA= Manifest.permission.CAMERA;
    public static final String LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String STORAGE=Manifest.permission.READ_EXTERNAL_STORAGE;

    public static Single<TedPermissionResult> requestPermission(Context context, String title, String message, String...permission){
        return TedRx2Permission.with(context)
                .setRationaleTitle(title)
                .setRationaleMessage(message)
                .setPermissions(permission)
                .request();
    }
}
