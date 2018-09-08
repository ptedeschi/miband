package br.com.tedeschi.miband.service;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import br.com.tedeschi.miband.mechanism.ProcessNotification;

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = NotificationListener.class.getName();

    public NotificationListener() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        ProcessNotification processNotification = new ProcessNotification();
        processNotification.process(this, sbn);
    }
}
