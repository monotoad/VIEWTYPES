package com.example.viewtypes;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.joda.time.DateTimeConstants;

import java.util.List;

public class Constants {
    public static final int EVENT_UNKNOWN = 0;

    public static final int EVENT_FROG = 1;
    public static final int EVENT_ELEPHANT = 2;
    public static final int EVENT_STEAK = 3;

    public static final int EVENT_SOFT = 4;
    public static final int EVENT_URGENT = 5; // Безотлагательное
    public static final int EVENT_CLARIFY = 6; // Уточняющее
    public static final int EVENT_NONE = 9; // Обычное

    public static final int EVENT_HARD = 7;
    public static final int EVENT_BUDGET = 8;

    public static final int KAIROS = 10;
    public static final int EVENT_KAIROS = 11;

    public static final int EVENT_MONTH = 12;
    public static final int EVENT_YEAR = 13;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void Debug(String TAG, List<KairosWithEvents> values){
        //for (int i = 0; i < values.size(); i++){
        //    Log.d(TAG, values. + " = " + values.get(i).getTitle() + " - " + values.get(i).getCompleted().toString());
        //}
        for (KairosWithEvents kairosWithEvents: values){
            Log.d(TAG, String.valueOf(kairosWithEvents.kairos.kairosId));
            kairosWithEvents.events.forEach(i -> {
                Log.d(TAG, "           " + i.id);
            });

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void DebugSteakView(String TAG, List<SteakView> values){
        //for (int i = 0; i < values.size(); i++){
        //    Log.d(TAG, values. + " = " + values.get(i).getTitle() + " - " + values.get(i).getCompleted().toString());
        //}
        values.forEach(i -> {
            Log.d(TAG, String.valueOf(i.steak.id) + " - " + i.isDailyCompleted);
        });
    }


    public static String getDayOfWeek(int dow)
    {
        if (DateTimeConstants.MONDAY == dow) return "ПН";
        else if (DateTimeConstants.TUESDAY == dow) return "ВТ";
        else if (DateTimeConstants.WEDNESDAY == dow) return "СР";
        else if (DateTimeConstants.THURSDAY == dow) return "ЧТ";
        else if (DateTimeConstants.FRIDAY == dow) return "ПТ";
        else if (DateTimeConstants.SATURDAY == dow) return "СБ";
        else if (DateTimeConstants.SUNDAY == dow) return "ВС";
        return "null";
    }

}