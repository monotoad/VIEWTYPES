package com.example.viewtypes;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.joda.time.LocalDate;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Date {
    @PrimaryKey
    public long date;


    public Date() {
        date = new LocalDate().toDate().getTime();
    }


}
