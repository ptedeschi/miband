package br.com.tedeschi.miband.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

public class NotificationPermission {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";


    /**
     * Is Message Service Enabled.
     * Verifies if the notification listener service is enabled.
     *
     * @return True if eanbled, false otherwise.
     */
    public static boolean isNotificationServiceEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Build Message Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Message Listener Service on yet.
     *
     * @return An alert dialog which leads to the notification enabling screen
     */
    public static AlertDialog buildNotificationServiceAlertDialog(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("R.string.notification_listener_service");
        alertDialogBuilder.setMessage("R.string.notification_listener_service_explanation");
        alertDialogBuilder.setPositiveButton("R.string.yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton("R.string.no",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return (alertDialogBuilder.create());
    }
}
