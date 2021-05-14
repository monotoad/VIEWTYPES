package com.example.viewtypes;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class DateWithSteaks {
    @Embedded Date date;

    @Relation(
            parentColumn = "date",
            entityColumn = "id",
            associateBy = @Junction(DateSteakCrossRef.class)
    )

    public List<Steak> steaks;
}
