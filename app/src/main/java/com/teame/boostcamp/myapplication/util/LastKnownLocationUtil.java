package com.teame.boostcamp.myapplication.util;

import android.content.Context;
import android.content.pm.PackageManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import androidx.core.app.ActivityCompat;
import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;

public class LastKnownLocationUtil {
    public static Single<LatLng> getLastPosition(Context context){
        SingleSubject<LatLng> subject=SingleSubject.create();
        FusedLocationProviderClient fusedLocationClient= LocationServices.getFusedLocationProviderClient(context);
        if(ActivityCompat.checkSelfPermission(context, TedPermissionUtil.LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                LatLng latlng;
                if(task.getResult()==null)
                    latlng = new LatLng(37.566581, 126.978641);
                else
                    latlng=new LatLng(task.getResult().getLatitude(),task.getResult().getLongitude());
                subject.onSuccess(latlng);
            });
        }
        else{
            subject.onError(new Throwable("Deny Permission"));
        }
        return subject;
    }
}
