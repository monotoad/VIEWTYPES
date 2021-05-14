package com.example.viewtypes;

import androidx.room.Entity;

@Entity
public class SoftDateTimeEvent extends HardDateTimeEvent {

    public int priority;

    public SoftDateTimeEvent() {
        title = "SDTEvent";
        isCompleted = false;
        priority = Constants.EVENT_NONE;
        timeE = -1;
        timeS = -1;
    }

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_SOFT_DTE;
    }


}
