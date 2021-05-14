package com.example.viewtypes;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DateWithFrogs {
    @Embedded public Date date;
    @Relation(
            parentColumn = "date",
            entityColumn = "date"
    )

    public List<Frog> frogs;
}
