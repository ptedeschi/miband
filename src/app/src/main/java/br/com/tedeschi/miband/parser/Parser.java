package br.com.tedeschi.miband.parser;

import android.content.Context;
import android.service.notification.StatusBarNotification;

import br.com.tedeschi.miband.model.Message;

public abstract class Parser {
    public abstract Message parse(Context context, StatusBarNotification Notification);
}
