package br.com.tedeschi.miband.mechanism;

import android.content.Context;
import android.service.notification.StatusBarNotification;

import br.com.tedeschi.miband.device.MiBand;
import br.com.tedeschi.miband.model.Message;
import br.com.tedeschi.miband.parser.GmailParser;
import br.com.tedeschi.miband.parser.Parser;
import br.com.tedeschi.miband.parser.WhatsAppParser;

public class ProcessNotification {
    public void process(Context context, StatusBarNotification notification) {
        MiBand miband = new MiBand();
        miband.connect(context, "<ENTER-YOUR-MIBAND-ADDRESS");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String packageName = notification.getPackageName();

        Parser parser = null;

        if (packageName.equals("com.whatsapp")) {
            parser = new WhatsAppParser();
        } else if (packageName.equals("com.google.android.gm")) {
            parser = new GmailParser();
        }

        if (parser != null) {
            Message message = parser.parse(context, notification);

            if (message != null) {
                miband.sendNotification(message);
            }
        }
    }
}
