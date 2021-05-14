package com.example.viewtypes;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Frog implements Parcelable, ItemTypeInterfaсe {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public long date;

    public String title;

    public Boolean isCompleted;

    public String stimulus;


    public Frog() {
        title = "Жаб";
        isCompleted = false;
        stimulus = null;
    }

    protected Frog(Parcel in) {
        id = in.readInt();
        title = in.readString();
        byte tmpIsCompleted = in.readByte();
        isCompleted = tmpIsCompleted == 0 ? null : tmpIsCompleted == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeByte((byte) (isCompleted == null ? 0 : isCompleted ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Frog> CREATOR = new Creator<Frog>() {
        @Override
        public Frog createFromParcel(Parcel in) {
            return new Frog(in);
        }

        @Override
        public Frog[] newArray(int size) {
            return new Frog[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frog frog = (Frog) o;
        return id == frog.id &&
                title.equals(frog.title) &&
                isCompleted.equals(frog.isCompleted);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, title, isCompleted);
    }


    @Override
    public int getType() {
        return ItemTypeInterfaсe.EVENT_FROG;
    }
}
