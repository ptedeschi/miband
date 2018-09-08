package br.com.tedeschi.miband.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class App {
    public static String getApplicationName(Context context, String packageName) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        ApplicationInfo applicationInfo;

        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }

        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
    }
}
