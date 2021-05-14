package com.example.viewtypes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class KairosWithEvents implements Parcelable, ItemTypeInterfaсe{
    @Embedded public Kairos kairos;
    @Relation(
            parentColumn = "kairosId",
            entityColumn = "id",
            associateBy = @Junction(KairosEventCrossRef.class)
    )
    public List<Event> events;

    public KairosWithEvents(Kairos kairos, List<Event> events) {
        this.kairos = kairos;
        this.events = events;
    }

    protected KairosWithEvents(Parcel in) {
        kairos = in.readParcelable(Kairos.class.getClassLoader());
        events = in.createTypedArrayList(Event.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(kairos, flags);
        dest.writeTypedList(events);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KairosWithEvents> CREATOR = new Creator<KairosWithEvents>() {
        @Override
        public KairosWithEvents createFromParcel(Parcel in) {
            return new KairosWithEvents(in);
        }

        @Override
        public KairosWithEvents[] newArray(int size) {
            return new KairosWithEvents[size];
        }
    };

    @Override
    public int getType() {
        return ItemTypeInterfaсe.KAIROS;
    }
}
