package com.example.viewtypes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Kairos implements Parcelable, ItemTypeInterfaсe{

    @PrimaryKey(autoGenerate = true)
    public int kairosId;
    public String title;

    public Kairos() {
        title = "Тест";
    }

    protected Kairos(Parcel in) {
        kairosId = in.readInt();
        title = in.readString();
    }

    public static final Creator<Kairos> CREATOR = new Creator<Kairos>() {
        @Override
        public Kairos createFromParcel(Parcel in) {
            return new Kairos(in);
        }

        @Override
        public Kairos[] newArray(int size) {
            return new Kairos[size];
        }
    };

    @Override
    public int getType() {
        return ItemTypeInterfaсe.KAIROS;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(kairosId);
        parcel.writeString(title);
    }
}
