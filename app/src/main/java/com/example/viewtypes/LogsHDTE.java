package com.example.viewtypes;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"dateOld", "dateNew", "id"})
public class LogsHDTE {
    public long dateOld;
    public long dateNew;
    public int id;
    public int type;
    public boolean isDailyCompleted;

    public LogsHDTE(long dateOld, long dateNew, int id, int type, boolean isDailyCompleted) {
        this.dateOld = dateOld;
        this.dateNew = dateNew;
        this.id = id;
        this.type = type;
        this.isDailyCompleted = isDailyCompleted;
    }
}
