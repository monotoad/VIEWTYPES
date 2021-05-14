package com.example.viewtypes;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import java.util.Objects;


public class ElephantWithSteaks implements Parcelable, ItemTypeInterfaсe {
    @Embedded public Elephant elephant;
    @Relation(
            parentColumn = "id",
            entityColumn = "elephantId"
    )
    public List<Steak> steaks;




    public ElephantWithSteaks(Elephant elephant, List<Steak> steaks) {
        this.elephant = elephant;
        this.steaks = steaks;
    }


    protected ElephantWithSteaks(Parcel in) {
        elephant = in.readParcelable(Elephant.class.getClassLoader());
        steaks = in.createTypedArrayList(Steak.CREATOR);
    }

    public Elephant getElephant() {
        return elephant;
    }

    public void setElephant(Elephant elephant) {
        this.elephant = elephant;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(elephant, flags);
        dest.writeTypedList(steaks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ElephantWithSteaks> CREATOR = new Creator<ElephantWithSteaks>() {
        @Override
        public ElephantWithSteaks createFromParcel(Parcel in) {
            return new ElephantWithSteaks(in);
        }

        @Override
        public ElephantWithSteaks[] newArray(int size) {
            return new ElephantWithSteaks[size];
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElephantWithSteaks that = (ElephantWithSteaks) o;
        return elephant.equals(that.elephant) &&
                Objects.equals(steaks, that.steaks);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(elephant, steaks);
    }

    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_ELEPHANT;
    }
}
