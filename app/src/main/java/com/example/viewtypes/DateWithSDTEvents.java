package com.example.viewtypes;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DateWithSDTEvents {
    @Embedded
    public Date date;
    @Relation(
            parentColumn = "date",
            entityColumn = "date"
    )
    public List<SoftDateTimeEvent> SDTEvents;
}
