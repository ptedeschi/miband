package br.com.tedeschi.miband.service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.concurrent.ExecutorService;

import br.com.tedeschi.miband.mechanism.ProcessNotification;
import br.com.tedeschi.miband.mechanism.ThreadPool;

public class AlarmListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("WPT014", "AlarmListener");

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d("WPT014", String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }
        }

        // Create a fake StatusBarNotification notification
        Notification notification = new Notification();
        notification.tickerText = "Alarm";

        StatusBarNotification sbn = new StatusBarNotification("com.android.deskclock", "com.android.deskclock", 1, "tag", 1,
                1, 1, notification, null, 1);

        ExecutorService executor = ThreadPool.getInstance().getExecutorService();
        executor.execute(new ProcessNotification(context, sbn));
    }
}



