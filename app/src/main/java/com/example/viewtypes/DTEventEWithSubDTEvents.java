package com.example.viewtypes;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DTEventEWithSubDTEvents implements ItemTypeInterfaсe{
    @Embedded
    public DateTimeEvent dateTimeEvent;
    @Relation(
            parentColumn = "id",
            entityColumn = "parentId"
    )
    public List<SubDateTimeEvent> SubEvents;


    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_DTE;
    }
}
