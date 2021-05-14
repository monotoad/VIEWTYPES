package com.example.viewtypes;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;

import java.util.Objects;

@Entity
public class SubDateTimeEvent extends DateTimeEvent implements ItemTypeInterfaсe{

    public long dateS;

    public long parentId;

    public SubDateTimeEvent() {
        title = "SUB_EVENT";
        isCompleted = false;
    }

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_SUB_DTE;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubDateTimeEvent that = (SubDateTimeEvent) o;
        return dateS == that.dateS &&
                parentId == that.parentId;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateS, parentId);
    }
}

