package com.example.viewtypes;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Frog.class, Elephant.class, Steak.class, Kairos.class, Event.class, KairosEventCrossRef.class, Date.class,
        DateSteakCrossRef.class, HardDateTimeEvent.class, SoftDateTimeEvent.class, BudgetDateTimeEvent.class,
        DateTimeEvent.class, SubDateTimeEvent.class, DateCompletedElephantCrossRef.class, DateCompletedSteakCrossRef.class,
        LogsHDTE.class,
        Goal.class},
        version = 91,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    private static final int NUMBER_OF_THREADS = 5;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract EventDao eventDao();

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "events").fallbackToDestructiveMigration().addCallback(appDatabaseCallback).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback appDatabaseCallback = new RoomDatabase.Callback(){
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() ->{
                EventDao dao = instance.eventDao();



                /*dao.deleteAll();

                SimpleEvent event = new SimpleEvent();
                event.title = "Жаба" + event.id;
                dao.insert(event);

                event = new SimpleEvent();
                event.title = "Жаба" + event.id;
                dao.insert(event);*/

                /*Event event = new Event();
                dao.insert(event);
                event = new Event();
                dao.insert(event);*/
            });
        }
    };


}