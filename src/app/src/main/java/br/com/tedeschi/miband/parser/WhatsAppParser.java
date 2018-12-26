package br.com.tedeschi.miband.parser;

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

public class WhatsAppParser extends Parser {
    private static final String TAG = WhatsAppParser.class.getName();

    @Override
    public Message parse(Context context, StatusBarNotification notification) {
        Log.d(TAG, "+parse");

        Bundle extras = notification.getNotification().extras;

        if (StatusBarNotificationUtil.getStringExtra(extras, android.app.Notification.EXTRA_SUMMARY_TEXT) != null) {
            Log.d(TAG, "-parse -> EXTRA_SUMMARY_TEXT != null");

            return null;
        }

        String packageName = notification.getPackageName();
        String title = StatusBarNotificationUtil.getStringExtra(extras, android.app.Notification.EXTRA_TITLE);
        String text = StatusBarNotificationUtil.getStringExtra(extras, android.app.Notification.EXTRA_TEXT);

        Log.d(TAG, "title -> " + title);
        Log.d(TAG, "text -> " + text);

        if (title != null && title.equals(context.getString(R.string.str_whatsapp_web))) {
            Log.d(TAG, "-parse -> Title is WhatsApp Web");

            return null;
        }

        if (text != null && text.equals(context.getString(R.string.str_whatsapp_checking_for_new_messages))) {
            Log.d(TAG, "-parse -> Text is Checking for new messages");

            return null;
        }

        // Photo
        if (text != null && text.contains(context.getString(R.string.str_whatsapp_photo))) {
            Log.d(TAG, "-parse -> Photo");

            return null;
        }

        // Video
        if (text != null && text.contains(context.getString(R.string.str_whatsapp_video))) {
            Log.d(TAG, "-parse -> Video");

            return null;
        }

        // Audio
        if (text != null && text.contains(context.getString(R.string.str_whatsapp_audio))) {
            Log.d(TAG, "-parse -> Audio");

            return null;
        }

        // Location
        if (text != null && text.contains(context.getString(R.string.str_whatsapp_location))) {
            Log.d(TAG, "-parse -> Location");

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

        title = StringUtils.unaccent(title);
        text = StringUtils.unaccent(text);

        Log.d(TAG, "Final title: " + title);
        Log.d(TAG, "Final text: " + text);

        if (title.isEmpty() || text.isEmpty()) {
            Log.d(TAG, "-parse -> Empty data");

            return null;
        }

        Message message = new Message();
        message.setId(packageName + "|" + notification.getNotification().when);
        message.setPackageName(packageName);
        message.setAppName(App.getApplicationName(context, packageName));
        message.setTitle(title);
        message.setBody(text);
        message.setIconId(IconID.NOTIFICATION_MESSAGE);

        Log.d(TAG, "-parse -> Completed");

        return message;
    }
}
