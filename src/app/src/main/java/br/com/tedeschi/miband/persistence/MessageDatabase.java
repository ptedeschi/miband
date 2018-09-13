package br.com.tedeschi.miband.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.tedeschi.miband.model.Message;

@Database(entities = {Message.class}, version = 1, exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}