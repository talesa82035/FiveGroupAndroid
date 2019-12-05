package com.example.fivegroup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.VibrationEffect;
import android.util.Log;
import android.widget.Toast;
import android.os.Vibrator;

import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    Context vContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        vContext = context;
        //Toast.makeText(vContext, "你設定的鬧鈴時間到了", Toast.LENGTH_LONG).show();

        //設置震動
        setVibrate(1000);

        //設置鈴聲
        PlaySound(context);
    }

    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator)vContext.getSystemService(Service.VIBRATOR_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            Toast.makeText(vContext, "你設定的鬧鈴時間到了29", Toast.LENGTH_LONG).show();
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    //.setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            myVibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK),audioAttributes);
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Toast.makeText(vContext, "你設定的鬧鈴時間到了21", Toast.LENGTH_LONG).show();
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    //.setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            myVibrator.vibrate(time,audioAttributes);
        }else{
            Toast.makeText(vContext, "你設定的鬧鈴時間到了15", Toast.LENGTH_LONG).show();
            myVibrator.vibrate(time);
        }
    }

    public static int PlaySound(final Context context) {
        NotificationManager mgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification nt = new Notification();
        nt.defaults = Notification.DEFAULT_SOUND;
        int soundId = new Random(System.currentTimeMillis())
                .nextInt(Integer.MAX_VALUE);
        mgr.notify(soundId, nt);
        return soundId;
    }

}
