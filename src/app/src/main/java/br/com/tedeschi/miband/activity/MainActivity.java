package br.com.tedeschi.miband.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.tedeschi.miband.R;
import br.com.tedeschi.miband.util.NotificationPermission;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
    }

    private void checkPermissions() {
        if (!NotificationPermission.isNotificationServiceEnabled(this)) {
            AlertDialog enableNotificationListenerAlertDialog = NotificationPermission.buildNotificationServiceAlertDialog(this);
            enableNotificationListenerAlertDialog.show();
        }
    }
}
