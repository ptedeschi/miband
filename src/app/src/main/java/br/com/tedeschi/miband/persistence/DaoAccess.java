package br.com.tedeschi.miband.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.com.tedeschi.miband.model.Message;

@Dao
public interface DaoAccess {
    //private static final String name = "shared_preferences_name";
    //private static final String key = "myStrings";

    @Insert
    void insertNotification(Message message);
//    {
//        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        Set<String> data = settings.getStringSet(key, new HashSet<String>());
//
//        // Add the new value.
//        data.add(String.valueOf(id));
//
//        // Save the list.
//        editor.putStringSet(key, data);
//        editor.commit();
//    }

    @Query("SELECT * FROM Message WHERE id = :id")
    Message fetchNotificationById(String id);
//    {
//        // Get the current list.
//        SharedPreferences settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        Set<String> data = settings.getStringSet(key, new HashSet<String>());
//
//        return data.contains(String.valueOf(id));
//        return true;
//    }
}
