package br.com.tedeschi.miband.activity;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import br.com.tedeschi.miband.R;
import br.com.tedeschi.miband.persistence.MessageDatabase;
import br.com.tedeschi.miband.util.NotificationPermission;
import br.com.tedeschi.miband.util.StringUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final String DATABASE_NAME = "miband_db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        initDatabase();

        Log.d(TAG, getString(R.string.str_gmail_syncing_mail));

        String title = "Campinas / Mogi";
        String text = "Fátima Tedeschi: Bom dia Tici, infelizmente a Irene não vai poder ir sábado.";

        Log.d(TAG, "title -> " + title);
        Log.d(TAG, "text -> " + text);

        if (title != null && title.equals(getString(R.string.str_whatsapp_web))) {
            Log.d(TAG, "-parse -> Title is WhatsApp Web");

            return;
        }

        if (text != null && text.equals(getString(R.string.str_whatsapp_checking_for_new_messages))) {
            Log.d(TAG, "-parse -> Text is Checking for new messages");

            return;
        }

        // Photo
        if (text != null && text.contains(getString(R.string.str_whatsapp_photo))) {
            Log.d(TAG, "-parse -> Photo");

            return;
        }

        // Video
        if (text != null && text.contains(getString(R.string.str_whatsapp_video))) {
            Log.d(TAG, "-parse -> Video");

            return;
        }

        // Audio
        if (text != null && text.contains(getString(R.string.str_whatsapp_audio))) {
            Log.d(TAG, "-parse -> Audio");

            return;
        }

        // Location
        if (text != null && text.contains(getString(R.string.str_whatsapp_location))) {
            Log.d(TAG, "-parse -> Location");

            return;
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


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.deskclock.ALARM_CHANGED");

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("WPT014", "received");
            }
        };

        registerReceiver(receiver, intentFilter);
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
