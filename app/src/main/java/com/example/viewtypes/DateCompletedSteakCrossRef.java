package com.example.viewtypes;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Objects;

@Entity(primaryKeys = {"date", "id"},
        foreignKeys = {
                @ForeignKey(entity = Date.class,
                        parentColumns = "date",
                        childColumns = "date"
                ),
                @ForeignKey(entity = Steak.class,
                        parentColumns = "id",
                        childColumns = "id"
                )
        })
public class DateCompletedSteakCrossRef {
    public long date;
    public int id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateCompletedSteakCrossRef that = (DateCompletedSteakCrossRef) o;
        return date == that.date &&
                id == that.id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(date, id);
    }
}