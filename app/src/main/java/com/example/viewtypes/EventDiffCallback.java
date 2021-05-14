package com.example.viewtypes;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class EventDiffCallback extends DiffUtil.Callback {

    private final List<Item> oldEventList;
    private final List<Item> newEventList;

    public EventDiffCallback(List<Item> oldEventList, List<Item> newEventList) {
        this.oldEventList = oldEventList;
        this.newEventList = newEventList;
    }

    @Override
    public int getOldListSize() {
        return oldEventList.size();
    }

    @Override
    public int getNewListSize() {
        return newEventList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        /*if ((oldEventList.get(oldItemPosition).object instanceof Frog && newEventList.get(newItemPosition).object instanceof Frog)){
            return (((Frog) oldEventList.get(oldItemPosition).object)).id == (((Frog) oldEventList.get(oldItemPosition).object)).id;
        }

        if ((oldEventList.get(oldItemPosition).object instanceof Elephant && newEventList.get(newItemPosition).object instanceof Elephant)){
            return ((Elephant)(oldEventList.get(oldItemPosition).object)).id == (((Elephant) oldEventList.get(oldItemPosition).object)).id;
        }

        if (oldEventList.get(oldItemPosition).object.getClass() != newEventList.get(newItemPosition).object.getClass()){
            r
        }*/

        //return oldEventList.get(oldItemPosition).equals(newEventList.get(newItemPosition));
        return oldEventList.get(oldItemPosition).getId() == newEventList.get(newItemPosition).getId();   // ?????


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldEventList.get(oldItemPosition) == (newEventList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}

