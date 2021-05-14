package com.example.viewtypes;

import androidx.room.Entity;

@Entity
public class DateTimeEvent extends Frog implements ItemTypeInterfaсe {

    //public long date; // Дата выполнения или конечная дата

    public int subId;

    public DateTimeEvent() {
        date = -1;
        title = "ДатеТимеЭвент";
        isCompleted = false;
    }

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_DTE;
    }
}
