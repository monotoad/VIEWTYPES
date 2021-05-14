package com.example.viewtypes;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Item implements Parcelable {
    int type;
    public Object object;
    int id;

    public Item(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    protected Item(Parcel in) {
        type = in.readInt();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    /*public boolean isCompleted(){
        if (object instanceof Frog){
            return ((Frog) object).isCompleted;
        }
        else if (object instanceof Elephant || object instanceof ElephantWithSteaks){
            return ((Elephant) object).isCompleted;
        }
        else if (object instanceof Steak){
            return ((Steak) object).isCompleted;
        }
        return false;
    }*/

    public int getId(){
        if (object instanceof Frog){
            return ((Frog) object).id;
        }
        else if (object instanceof Elephant){
            return ((Elephant) object).id;
        }
        else if (object instanceof ElephantWithSteaks){
            return ((ElephantWithSteaks) object).elephant.id;
        }
        else if (object instanceof Steak){
            return ((Steak) object).id;
        }
        else if (object instanceof KairosWithEvents){
            return ((KairosWithEvents) object).kairos.kairosId;
        }

        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return type == item.type &&
                object.equals(item.object);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(type, object);
    }
}
