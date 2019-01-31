package com.teame.boostcamp.myapplication.util;

import android.Manifest;
import android.content.Context;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

public class TedPermissionUtil {
    public static final String CAMERA= Manifest.permission.CAMERA;
    public static final String LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String STORAGE=Manifest.permission.READ_EXTERNAL_STORAGE;

    public static void requestPermission(Context context, String title, String message, String...permission){
        TedRx2Permission.with(context)
                .setRationaleTitle(title)
                .setRationaleMessage(message)
                .setPermissions(permission)
                .request()
                .subscribe(tedPermissionResult -> {
                    if(tedPermissionResult.isGranted())
                        Toast.makeText(context,"Permission is Granted",Toast.LENGTH_SHORT).show();
                    else{
                        Toast.makeText(context,
                                "Permission Denied\n" + tedPermissionResult.getDeniedPermissions().toString(), Toast.LENGTH_SHORT)
                                .show();
                    }
                },throwable -> {
                });
    }
}
