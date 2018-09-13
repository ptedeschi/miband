package br.com.tedeschi.miband.mechanism;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import br.com.tedeschi.miband.device.MiBand;
import br.com.tedeschi.miband.model.Message;
import br.com.tedeschi.miband.parser.GmailParser;
import br.com.tedeschi.miband.parser.Parser;
import br.com.tedeschi.miband.parser.WhatsAppParser;
import br.com.tedeschi.miband.persistence.MessageDatabase;
import br.com.tedeschi.miband.util.StatusBarNotificationUtil;

public class ProcessNotification {
    private static final String TAG = ProcessNotification.class.getName();
    private static final String DATABASE_NAME = "miband_db";

    public void process(final Context context, final StatusBarNotification notification) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StatusBarNotificationUtil.debug(notification);

                final MessageDatabase notificationDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        MessageDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();

                String notificationId = notification.getPackageName() + "|" + notification.getNotification().when;

                Log.d(TAG, "Checking for notification " + notificationId);

                if (notificationDatabase.daoAccess().fetchNotificationById(notificationId) == null) {
                    MiBand miband = new MiBand();
                    miband.connect(context, "<ENTER-YOUR-MIBAND-ADDRESS");

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String packageName = notification.getPackageName();

                    Parser parser = null;

                    if (packageName.equals("com.whatsapp")) {
                        parser = new WhatsAppParser();
                    } else if (packageName.equals("com.google.android.gm")) {
                        parser = new GmailParser();
                    }

                    if (parser != null) {
                        final Message message = parser.parse(context, notification);

                        if (message != null) {
                            miband.sendNotification(message);

                            Log.d(TAG, "Preparing to add notification " + notificationId);

                            notificationDatabase.daoAccess().insertNotification(message);

                            Log.d(TAG, notificationId + " | Notification added into database");
                        } else {
                            Log.d(TAG, "No message to handle");
                        }
                    } else {
                        Log.d(TAG, "No parser to handle it");
                    }
                } else {
                    Log.d(TAG, notificationId + " | Notification already exists. Exiting to avoid duplicity");
                }

                notificationDatabase.close();
            }
        }).start();
    }
}
