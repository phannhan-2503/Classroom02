package com.example.quanlybantingiaovien.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.quanlybantingiaovien.model.nhanxetModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Database(entities = nhanxetModel.class, version = 1)
@TypeConverters(DataBaseNhanXet.DateConverter.class)
public abstract class DataBaseNhanXet extends RoomDatabase {
    private static final String DATABASE_NAME = "nhanxet_DataBase";
    private static DataBaseNhanXet instance;

    public static class DateConverter {
        private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        @TypeConverter
        public static Date fromTimestamp(String value) {
            try {
                return value == null ? null : formatter.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

        @TypeConverter
        public static String dateToTimestamp(Date date) {
            return date == null ? null : formatter.format(date);
        }
    }

    public static synchronized DataBaseNhanXet getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DataBaseNhanXet.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract DAONhanXet DAOnhanxet();
}
