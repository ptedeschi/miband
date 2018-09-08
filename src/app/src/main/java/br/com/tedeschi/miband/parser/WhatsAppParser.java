package br.com.tedeschi.miband.parser;

import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import br.com.tedeschi.miband.device.IconID;
import br.com.tedeschi.miband.model.Message;
import br.com.tedeschi.miband.util.App;

public class WhatsAppParser extends Parser {
    @Override
    public Message parse(Context context, StatusBarNotification notification) {
        debug(notification);

        Bundle extras = notification.getNotification().extras;

        if (getStringExtra(extras, android.app.Notification.EXTRA_SUMMARY_TEXT) != null) {
            return null;
        }

        String packageName = notification.getPackageName();
        String title = getStringExtra(extras, android.app.Notification.EXTRA_TITLE);
        String text = getStringExtra(extras, android.app.Notification.EXTRA_TEXT);

        if (title != null && title.equals("WhatsApp Web")) {
            return null;
        }

        if (text != null && text.equals("Checking for new messages")) {
            return null;
        }

        // Video
        if (text != null && text.contains("\uD83C\uDFA5")) {
            return null;
        }

        if (title != null) {
            title = title.replace("\u200B", "");
            title = title.trim();
        }

        String split[] = title.split(":");

        if (split != null) {
            if (split.length == 1) {
                // Handling individual message
                String contact = split[0].trim();

                // Getting contact first name
                contact = contact.split(" ")[0];

                title = contact;
            } else if (split.length == 2) {
                // Handling group message
                String group = split[0].trim();
                String contact = split[1].trim();

                // Removing spaces
                group = group.replace(" ", "");

                // Group names can contain details about number of messages.
                // Removing this information
                if (group.contains("(")) {
                    int index = group.indexOf('(');

                    group = group.substring(0, index);
                }

                // Getting contact first name
                contact = contact.split(" ")[0];

                title = contact + "@" + group;
            }
        }

        Message message = new Message();
        message.id = notification.getKey();
        message.packageName = packageName;
        message.appName = App.getApplicationName(context, packageName);
        message.title = title;
        message.body = text;
        message.iconId = IconID.NOTIFICATION_MESSAGE;

        return message;
    }
}
