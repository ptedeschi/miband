package br.com.tedeschi.miband.parser;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import br.com.tedeschi.miband.device.IconID;
import br.com.tedeschi.miband.model.Message;
import br.com.tedeschi.miband.util.App;

public class GenericParser extends Parser {
    @Override
    public Message parse(Context context, StatusBarNotification notification) {
        debug(notification);

        Bundle extras = notification.getNotification().extras;

        if (getStringExtra(extras, Notification.EXTRA_SUMMARY_TEXT) != null) {
            return null;
        }

        String packageName = notification.getPackageName();
        String title = getStringExtra(extras, Notification.EXTRA_TITLE);
        String text = getStringExtra(extras, Notification.EXTRA_TEXT);

        Message message = new Message();
        message.id = notification.getKey();
        message.packageName = packageName;
        message.appName = App.getApplicationName(context, packageName);
        message.title = title;
        message.body = text;
        message.iconId = IconID.NOTIFICATION_GENERIC;

        return message;
    }
}
