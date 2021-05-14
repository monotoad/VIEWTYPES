package com.example.viewtypes;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Steak extends Frog implements Parcelable, ItemTypeInterfaсe {

    public int elephantId;


    @TypeConverters(Converters.class)
    public List<Integer> days;


    public Steak() {
        title = "Стаке";
        isCompleted = false;
        days = new ArrayList<>();
    }

    protected Steak(Parcel in) {
        super(in);
        elephantId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(elephantId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Steak> CREATOR = new Creator<Steak>() {
        @Override
        public Steak createFromParcel(Parcel in) {
            return new Steak(in);
        }

        @Override
        public Steak[] newArray(int size) {
            return new Steak[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Steak steak = (Steak) o;
        return id == steak.id &&
                title.equals(steak.title) &&
                isCompleted.equals(steak.isCompleted);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, title, isCompleted);
    }

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_STEAK;
    }
}
