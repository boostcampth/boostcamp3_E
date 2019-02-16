package com.teame.boostcamp.myapplication.util.workmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.ui.MainActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AlarmWork extends Worker {

    private String CHANNEL_NAME="test";
    private static String KEY_SELECTED="KEY_SELECTED";
    private String CHANNEL_ID="test";
    private ArrayList<String> itemNames=new ArrayList<>();

    public AlarmWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationCompat.Builder notiBuilder=new NotificationCompat.Builder(getApplicationContext(),null);
        NotificationChannel channel;
        NotificationManager manager=(NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String[] strArr =getInputData().getStringArray(KEY_SELECTED);
        String body="";
        for(String str:strArr)
            body+=str+", ";
        body=body.substring(0,body.length()-2);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            channel=new NotificationChannel("TEST","NAME", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Test Alarm Channel");
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
            notiBuilder.setChannelId("TEST");
        }
        PendingIntent intent=PendingIntent.getActivity(getApplicationContext(),0,new Intent(getApplicationContext(), MainActivity.class),PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.BigTextStyle style=new NotificationCompat.BigTextStyle()
                .bigText(body)
                .setBigContentTitle("이거 사셨나요?");

        notiBuilder.setAutoCancel(true)
                .setContentText(body)
                .setContentTitle("이거 사셨나요?")
                .setContentIntent(intent)
                .setStyle(style)
                .setSmallIcon(R.mipmap.ic_launcher);

        manager.notify(11,notiBuilder.build());

        return Result.success();
    }
}
