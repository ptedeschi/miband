package br.com.tedeschi.miband.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class MiBandNotificationListenerService extends android.service.notification.NotificationListenerService {
    private static final String TAG = MiBandNotificationListenerService.class.getName();

    public MiBandNotificationListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        try
        {
            String packageName = sbn.getPackageName();

            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString("android.title");
            String text = extras.getCharSequence("android.text").toString();

            Log.d(TAG, "Received " + packageName + " notification with data " + title + " " + text);
        }
        catch(Exception e)
        {
            Log.e(TAG, "Error handling notification", e);
        }
    }
}
