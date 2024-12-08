package com.example.inshorts.data.room;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {MovieEntity.class}, version = 2)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE movies ADD COLUMN isBookmarked INTEGER NOT NULL DEFAULT 0");
        }
    };
}
