package com.example.viewtypes;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class EventsWithKairos implements ItemTypeInterfaсe{
    @Embedded public Event event;
    @Relation(
            parentColumn = "id",
            entityColumn = "kairosId",
            associateBy = @Junction(KairosEventCrossRef.class)
    )
    public List<Kairos> kairoses;

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_KAIROS;
    }
}
