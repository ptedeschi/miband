package br.com.tedeschi.miband.activity;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
