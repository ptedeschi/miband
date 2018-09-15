package br.com.tedeschi.miband.parser;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import br.com.tedeschi.miband.device.IconID;
import br.com.tedeschi.miband.model.Message;
import br.com.tedeschi.miband.util.App;
import br.com.tedeschi.miband.util.StatusBarNotificationUtil;
import br.com.tedeschi.miband.util.StringUtils;

public class GmailParser extends Parser {
    @Override
    public Message parse(Context context, StatusBarNotification notification) {
        Bundle extras = notification.getNotification().extras;

        if (StatusBarNotificationUtil.getStringExtra(extras, android.app.Notification.EXTRA_SUMMARY_TEXT) != null) {
            return null;
        }

        if (notification.getId() == 0) {
            return null;
        }

        String packageName = notification.getPackageName();
        String title = StatusBarNotificationUtil.getStringExtra(extras, android.app.Notification.EXTRA_TITLE);
        String text = StatusBarNotificationUtil.getStringExtra(extras, android.app.Notification.EXTRA_TEXT);

        if (title != null && title.equals("Syncing mail…")) {
            return null;
        }

        String bigText = StatusBarNotificationUtil.getStringExtra(extras, Notification.EXTRA_BIG_TEXT);

        if (bigText != null) {
            text = bigText.replace("\n", " ");
        }

        title = StringUtils.unaccent(title);
        text = StringUtils.unaccent(text);

        Message message = new Message();
        message.setId(packageName + "|" + notification.getNotification().when);
        message.setPackageName(packageName);
        message.setAppName(App.getApplicationName(context, packageName));
        message.setTitle(title);
        message.setBody(text);
        message.setIconId(IconID.NOTIFICATION_MAIL);

        return message;
    }
}
