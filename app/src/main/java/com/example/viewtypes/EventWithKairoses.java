package com.example.viewtypes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class EventWithKairoses implements Parcelable, ItemTypeInterfaсe{

    @Embedded
    public Event event;
    @Relation(
            parentColumn = "id",
            entityColumn = "kairosId",
            associateBy = @Junction(KairosEventCrossRef.class)
    )
    public List<Kairos> kairoses;

    public EventWithKairoses(Event event, List<Kairos> kairoses) {
        this.event = event;
        this.kairoses = kairoses;
    }

    protected EventWithKairoses(Parcel in) {
        event = in.readParcelable(Event.class.getClassLoader());
        kairoses = in.createTypedArrayList(Kairos.CREATOR);
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(event, flags);
        dest.writeTypedList(kairoses);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventWithKairoses> CREATOR = new Creator<EventWithKairoses>() {
        @Override
        public EventWithKairoses createFromParcel(Parcel in) {
            return new EventWithKairoses(in);
        }

        @Override
        public EventWithKairoses[] newArray(int size) {
            return new EventWithKairoses[size];
        }
    };

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_KAIROS;
    }



}
