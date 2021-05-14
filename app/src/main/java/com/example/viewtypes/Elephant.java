package com.example.viewtypes;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;

import java.util.Objects;

@Entity
public class Elephant extends Frog implements Parcelable {


    public Elephant() {
        title = "Слон";
        isCompleted = false;
    }

    protected Elephant(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Elephant> CREATOR = new Creator<Elephant>() {
        @Override
        public Elephant createFromParcel(Parcel in) {
            return new Elephant(in);
        }

        @Override
        public Elephant[] newArray(int size) {
            return new Elephant[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Elephant elephant = (Elephant) o;
        return id == elephant.id &&
                title.equals(elephant.title) &&
                isCompleted.equals(elephant.isCompleted);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, title, isCompleted);
    }


    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_ELEPHANT;
    }
}
