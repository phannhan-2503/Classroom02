package com.example.quanlybantingiaovien.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.quanlybantingiaovien.model.nhanxetModel;
import java.util.Date;

@Database(entities = nhanxetModel.class,version = 1)
@TypeConverters(DataBaseNhanXet.DateConverter.class)
public abstract class DataBaseNhanXet extends RoomDatabase {
    private static final String DATABASE_NAME="nhanxet_DataBase";
    private static DataBaseNhanXet instance;
    public static class DateConverter {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }
    public static synchronized DataBaseNhanXet getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(), DataBaseNhanXet.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract DAONhanXet DAOnhanxet();
}
