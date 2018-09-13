package br.com.tedeschi.miband.activity;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.tedeschi.miband.R;
import br.com.tedeschi.miband.persistence.MessageDatabase;
import br.com.tedeschi.miband.util.NotificationPermission;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final String DATABASE_NAME = "miband_db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        initDatabase();
    }

    private void checkPermissions() {
        if (!NotificationPermission.isNotificationServiceEnabled(this)) {
            AlertDialog enableNotificationListenerAlertDialog = NotificationPermission.buildNotificationServiceAlertDialog(this);
            enableNotificationListenerAlertDialog.show();
        }
    }

    private void initDatabase() {

        MessageDatabase messageDatabase;
        messageDatabase = Room.databaseBuilder(getApplicationContext(),
                MessageDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }
}
