package br.com.tedeschi.miband.mechanism;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import br.com.tedeschi.miband.device.MiBand;
import br.com.tedeschi.miband.model.Message;
import br.com.tedeschi.miband.parser.GenericParser;
import br.com.tedeschi.miband.parser.GmailParser;
import br.com.tedeschi.miband.parser.Life360Parser;
import br.com.tedeschi.miband.parser.Parser;
import br.com.tedeschi.miband.parser.WhatsAppParser;
import br.com.tedeschi.miband.persistence.MessageDatabase;
import br.com.tedeschi.miband.util.StatusBarNotificationUtil;
import br.com.tedeschi.miband.util.StringUtils;

public class ProcessNotification implements Runnable {
    private static final String TAG = ProcessNotification.class.getName();
    private static final String DATABASE_NAME = "miband_db";

    private Context context;
    private StatusBarNotification notification;

    public ProcessNotification(Context context, StatusBarNotification notification) {
        this.context = context;
        this.notification = notification;
    }

    public void run() {
        Log.d(TAG, "+ProcessNotification");

        StatusBarNotificationUtil.debug(notification);

        final MessageDatabase notificationDatabase = Room.databaseBuilder(context.getApplicationContext(),
                MessageDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();

        String notificationId = notification.getPackageName() + "|" + notification.getNotification().when;

        Log.d(TAG, "Checking for notification " + notificationId);

        if (notificationDatabase.daoAccess().fetchNotificationById(notificationId) == null) {
            String packageName = notification.getPackageName();

            Parser parser = null;

            if (packageName.equals("com.whatsapp")) {
                parser = new WhatsAppParser();
            } else if (packageName.equals("com.google.android.gm")) {
                parser = new GmailParser();
            } else if (packageName.equals("com.android.deskclock")) {
                parser = new GenericParser();
            } else if (packageName.equals("com.life360.android.safetymapd")) {
                parser = new Life360Parser();
            }

            if (parser != null) {
                final Message message = parser.parse(context, notification);

                if (message != null) {
                    MiBand miband = new MiBand();
                    miband.connect(context, "E1:33:93:3C:CB:AF");

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String sender = message.getTitle();
                    String data = StringUtils.truncate(sender, 32) + "\0";

                    if (message.getSubject() != null) {
                        data += StringUtils.truncate(message.getSubject(), 128) + "\n\n";
                    }

                    if (message.getBody() != null) {
                        data += StringUtils.truncate(message.getBody(), 128);
                    }

                    miband.sendNotification(message.getAppName(), data, message.getIconId());

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

        Log.d(TAG, "-ProcessNotification");
    }
}
