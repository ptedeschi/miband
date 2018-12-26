package br.com.tedeschi.miband.parser;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import br.com.tedeschi.miband.R;
import br.com.tedeschi.miband.device.IconID;
import br.com.tedeschi.miband.model.Message;
import br.com.tedeschi.miband.util.App;
import br.com.tedeschi.miband.util.StatusBarNotificationUtil;
import br.com.tedeschi.miband.util.StringUtils;

public class Life360Parser extends Parser {
    private static final String TAG = Life360Parser.class.getName();

    @Override
    public Message parse(Context context, StatusBarNotification notification) {
        Bundle extras = notification.getNotification().extras;

        if (StatusBarNotificationUtil.getStringExtra(extras, Notification.EXTRA_SUMMARY_TEXT) != null) {
            return null;
        }

        String packageName = notification.getPackageName();
        String title = StatusBarNotificationUtil.getStringExtra(extras, Notification.EXTRA_TITLE);
        String text = StatusBarNotificationUtil.getStringExtra(extras, Notification.EXTRA_TEXT);

        if (title != null && title.equals("Updating location…")) {
            Log.d(TAG, "-parse -> Title is Updating location");

            return null;
        }

        if (title != null && title.equals("Driving Activated")) {
            Log.d(TAG, "-parse -> Title is Driving Activated");

            return null;
        }

        if (title != null && title.equals("Life360 is running")) {
            Log.d(TAG, "-parse -> Title is Life360 is running");

            return null;
        }

        if (title != null && title.equals("Checking for activity")) {
            Log.d(TAG, "-parse -> Title is Checking for activity");

            return null;
        }

        title = StringUtils.unaccent(title);
        text = StringUtils.unaccent(text);

        Message message = new Message();
        message.setId(packageName + "|" + notification.getNotification().when);
        message.setPackageName(packageName);
        message.setAppName(App.getApplicationName(context, packageName));
        message.setTitle(title);
        message.setBody(text);
        message.setIconId(IconID.NOTIFICATION_GENERIC);

        return message;
    }
}
