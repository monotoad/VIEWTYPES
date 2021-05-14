package com.example.viewtypes.ui;

import android.view.View;

import com.example.viewtypes.BudgetDateTimeEvent;
import com.example.viewtypes.DTEventEWithSubDTEvents;
import com.example.viewtypes.DateTimeEvent;
import com.example.viewtypes.Elephant;
import com.example.viewtypes.ElephantWithSteaks;
import com.example.viewtypes.Event;
import com.example.viewtypes.Frog;
import com.example.viewtypes.Goal;
import com.example.viewtypes.HardDateTimeEvent;
import com.example.viewtypes.Item;
import com.example.viewtypes.ItemTypeInterfaсe;
import com.example.viewtypes.KairosWithEvents;
import com.example.viewtypes.SoftDateTimeEvent;
import com.example.viewtypes.Steak;
import com.example.viewtypes.SteakView;
import com.example.viewtypes.SubDateTimeEvent;

public interface RecyclerViewClickInterface<T extends ItemTypeInterfaсe> {
    void onItemFrogClick(Frog frogItem, int TAG);
    void onItemElephantClick(ElephantWithSteaks elephantItem, int TAG);
    void onItemSteakClick(Steak steakItem, int TAG);

    void onItemFrogLongClick(Frog frogItem, int TAG);
    void onItemElephantLongClick(ElephantWithSteaks elephantItem, int TAG);
    void onItemSteakLongClick(Steak steakItem, int TAG);

    void onItemFrogCompleteClick(Frog frogItem, int TAG);
    void onItemElephantCompleteClick(ElephantWithSteaks elephantItem, int TAG);
    void onItemSteakCompleteClick(Steak steakItem, int TAG);

    void onItemFrogContextClick(Frog frogItem, View view, int TAG);

    void onItemKairosWithEventsClick(KairosWithEvents kairosWithEvents, int TAG);
    void onItemEventClick(Event event, int TAG);
    void onItemKairosWithEventsLongClick(KairosWithEvents kairosWithEvents, int TAG);
    void onItemEventLongClick(Event event, int TAG);

    void onItemEventCompleteClick(Event event, int TAG);

    void onItemDateSteakClick(SteakView steakView, int TAG);
    void onItemDateSteakCompleteClick(SteakView steakView, int TAG);

    void onItemHardDateTimeClick(HardDateTimeEvent hardDateTimeEvent);
    void onItemSoftDateTimeClick(SoftDateTimeEvent softDateTimeEvent);
    void onItemBudgetDateTimeClick(BudgetDateTimeEvent budgetDateTimeEvent);

    void onItemHardDateTimeLongClick(HardDateTimeEvent hardDateTimeEvent, int TAG);
    void onItemBudgetDateTimeLongClick(BudgetDateTimeEvent budgetDateTimeEvent, int TAG);
    void onItemSoftDateTimeLongClick(SoftDateTimeEvent softDateTimeEvent, int TAG);

    void onItemHardDateTimeCompleteClick(HardDateTimeEvent hardDateTimeEvent, int TAG);
    void onItemSoftDateTimeCompleteClick(SoftDateTimeEvent softDateTimeEvent, int TAG);
    void onItemBudgetDateTimeCompleteClick(BudgetDateTimeEvent budgetDateTimeEvent, int TAG);

    void onItemHardDateTimeContextClick(HardDateTimeEvent hardDateTimeEvent, View view, int TAG);
    void onItemSoftDateTimeContextClick(SoftDateTimeEvent softDateTimeEvent, View view, int TAG);
    void onItemBudgetDateTimeContextClick(BudgetDateTimeEvent budgetDateTimeEvent, View view, int TAG);

    void onItemDateTimeEvent(DTEventEWithSubDTEvents dateTimeEvent);
    void onItemSubDateTimeEvent(SubDateTimeEvent subDateTimeEvent);

    void onItemGoalClick(Goal goal, int TAG);
    void onItemGoalExpandClick(Goal goal, int TAG);

}
