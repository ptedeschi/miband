package br.com.tedeschi.miband.parser;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.util.Log;

import java.text.SimpleDateFormat;

import br.com.tedeschi.miband.model.Message;

public abstract class Parser {
    private static final String TAG = Parser.class.getName();

    public abstract Message parse(Context context, StatusBarNotification Notification);

    protected void debug(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        String pack = sbn.getPackageName();
        String temp = pack;

        if (notification.category != null) {
            temp += "Category: " + notification.category.toString() + "\n";
        } else {
            temp += "Category = null \n";
        }

        if (notification.tickerText != null) {
            temp += "TickerText: " + notification.tickerText.toString() + "\n";
        } else {
            temp += "TickerText = null \n";
        }

        temp += "Key: " + sbn.getKey() + "\n" + "ID: " + sbn.getId() + "\n";

        SimpleDateFormat format = new SimpleDateFormat("DD-kk:mm:ss:SSS");
        Long ptime = sbn.getPostTime();
        temp += "Post time: " + format.format(ptime) + "\n";
        Long nottime = notification.when;
        temp += "When: " + format.format(nottime) + "\n";

        temp += "Extras..." + "\n";
        Bundle bun = notification.extras;

        temp += "BigText: " + getStringExtra(bun, Notification.EXTRA_BIG_TEXT) + "\n";
        temp += "SummaryText: " + getStringExtra(bun, Notification.EXTRA_SUMMARY_TEXT) + "\n";
        temp += "InfoText: " + getStringExtra(bun, Notification.EXTRA_INFO_TEXT) + "\n";
        temp += "Text: " + getStringExtra(bun, Notification.EXTRA_TEXT) + "\n";
        temp += "SubText: " + getStringExtra(bun, Notification.EXTRA_SUB_TEXT) + "\n";
        temp += "Title: " + getStringExtra(bun, Notification.EXTRA_TITLE) + "\n";
        temp += "Big Title: " + getStringExtra(bun, Notification.EXTRA_TITLE_BIG) + "\n";

        temp += "\nFields... \n";

        CharSequence[] lines = bun.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

        if (lines != null) {
            for (CharSequence line : lines) {
                temp += "line: " + line.toString() + "  \n";
            }
        } else {
            temp += " no lines... \n";
        }

        Log.d(TAG, temp);
    }

    protected String getStringExtra(Bundle extras, String key) {
        if (extras != null && key != null) {
            Object obj = extras.get(key);

            try {
                return (String) obj;
            } catch (ClassCastException e) {
                // Do nothing
            }

            try {
                SpannableString spannableString = (SpannableString) obj;
                return spannableString.toString();
            } catch (ClassCastException e) {
                // Do nothing
            }
        }

        return null;
    }
}
