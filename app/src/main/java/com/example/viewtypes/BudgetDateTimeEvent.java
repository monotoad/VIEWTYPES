package com.example.viewtypes;

import androidx.room.Entity;

import org.joda.time.Period;

@Entity
public class BudgetDateTimeEvent extends SoftDateTimeEvent{

    public long duration;

    public BudgetDateTimeEvent() {
        title = "BDTEvent";
        isCompleted = false;
        priority = Constants.EVENT_NONE;
        duration = new Period(2,3, 0, 0).toStandardDuration().getMillis();
    }

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_BUDGET_DTE;
    }
}
