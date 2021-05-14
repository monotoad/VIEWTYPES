package com.example.viewtypes;

import androidx.room.Entity;

@Entity(primaryKeys = {"dateOld", "dateNew", "id"})
public class LogsBDTE {
    long dateOld;
    long dateNew;
    int id;

    public LogsBDTE(long dateOld, long dateNew, int id) {
        this.dateOld = dateOld;
        this.dateNew = dateNew;
        this.id = id;
    }
}
