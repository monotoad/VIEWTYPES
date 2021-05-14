package com.example.viewtypes;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;

import org.joda.time.LocalTime;

import java.util.Objects;

@Entity
public class HardDateTimeEvent extends DateTimeEvent{
    public long timeS;
    public long timeE;

    public HardDateTimeEvent() {
        title = "HDTEvent";
        isCompleted = false;
        timeS = new LocalTime(LocalTime.now()).toDateTimeToday().getMillis();
        timeE = new LocalTime(LocalTime.now()).toDateTimeToday().plusMinutes(10).getMillis();
    }

    public long getDate(){
        return date;
    }

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_HARD_DTE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HardDateTimeEvent that = (HardDateTimeEvent) o;
        return timeS == that.timeS &&
                timeE == that.timeE;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timeS, timeE);
    }
}
