package br.com.tedeschi.miband.parser;

import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import br.com.tedeschi.miband.activity.MainActivity;
import br.com.tedeschi.miband.device.IconID;
import br.com.tedeschi.miband.model.Message;
import br.com.tedeschi.miband.util.App;

public class WhatsAppParser extends Parser {
    private static final String TAG = WhatsAppParser.class.getName();

    @Override
    public Message parse(Context context, StatusBarNotification notification) {
        debug(notification);

        Bundle extras = notification.getNotification().extras;

        if (getStringExtra(extras, android.app.Notification.EXTRA_SUMMARY_TEXT) != null) {
            Log.d(TAG, "Exiting: EXTRA_SUMMARY_TEXT != null");

            return null;
        }

        String packageName = notification.getPackageName();
        String title = getStringExtra(extras, android.app.Notification.EXTRA_TITLE);
        String text = getStringExtra(extras, android.app.Notification.EXTRA_TEXT);

        if (title != null && title.equals("WhatsApp Web")) {
            Log.d(TAG, "Exiting: Title is WhatsApp Web");

            return null;
        }

        if (text != null && text.equals("Checking for new messages")) {
            Log.d(TAG, "Exiting: Text is Checking for new messages");

            return null;
        }

        // Video
        if (text != null && text.contains("\uD83C\uDFA5")) {
            Log.d(TAG, "Exiting: Video");

            return null;
        }
		
		// Audio
        if (text != null && text.contains("\uD83C\uDFA4")) {
            Log.d(TAG, "Exiting: Audio");

            return null;
        }

        Log.d(TAG, "Removing \u200B");

        Log.d(TAG, "Before " + title);

        if (title != null) {
            title = title.replace("\u200B", "");
            title = title.trim();
        }

        Log.d(TAG, "After " + title);

        String split[] = title.split(":");

        if (split != null) {
            Log.d(TAG, "Total split length: " + split.length);

            if (split.length == 1) {
                Log.d(TAG, "Handling individual message");

                // Handling individual message
                String contact = split[0].trim();

                // Getting contact first name
                contact = contact.split(" ")[0];

                title = contact;
            } else if (split.length == 2) {
                Log.d(TAG, "Handling group message");

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

        Log.d(TAG, "Final title: " + title);
        Log.d(TAG, "Final text: " + text);

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
