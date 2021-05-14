package com.example.viewtypes;

import androidx.room.Embedded;

public class SteakView implements ItemTypeInterfaсe {
    @Embedded
    public Steak steak;

    public boolean isDailyCompleted;

    public long getDate(){
        return steak.date;
    }

    public int getId(){
        return steak.id;
    }

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_STEAKDATE;
    }
}
