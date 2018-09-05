package br.com.tedeschi.miband.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.tedeschi.miband.R;
import br.com.tedeschi.miband.util.NotificationUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
    }

    private void checkPermissions() {
        if(!NotificationUtil.isNotificationServiceEnabled(this)){
            AlertDialog enableNotificationListenerAlertDialog = NotificationUtil.buildNotificationServiceAlertDialog(this);
            enableNotificationListenerAlertDialog.show();
        }
    }
}
