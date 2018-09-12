package br.com.tedeschi.miband.parser;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import br.com.tedeschi.miband.device.IconID;
import br.com.tedeschi.miband.model.Message;
import br.com.tedeschi.miband.util.App;

public class GmailParser extends Parser {
    @Override
    public Message parse(Context context, StatusBarNotification notification) {
        debug(notification);

        Bundle extras = notification.getNotification().extras;

        if (getStringExtra(extras, android.app.Notification.EXTRA_SUMMARY_TEXT) != null) {
            return null;
        }

        if (notification.getId() == 0) {
            return null;
        }

        String packageName = notification.getPackageName();
        String title = getStringExtra(extras, android.app.Notification.EXTRA_TITLE);
        String text = getStringExtra(extras, android.app.Notification.EXTRA_TEXT);

        if (title != null && title.equals("Syncing mail…")) {
            return null;
        }

        String bigText = getStringExtra(extras, Notification.EXTRA_BIG_TEXT);

        if (bigText != null) {
            text = bigText.replace("\n", " ");
        }

        Message message = new Message();
        message.id = notification.getKey();
        message.packageName = packageName;
        message.appName = App.getApplicationName(context, packageName);
        message.title = title;
        message.body = text;
        message.iconId = IconID.NOTIFICATION_MAIL;

        return message;
    }
}
