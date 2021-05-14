package com.example.viewtypes;

import androidx.room.Entity;

@Entity(primaryKeys = {"id", "kairosId"})
public class KairosEventCrossRef {
    public int id;
    public int kairosId;

    public KairosEventCrossRef(int id, int kairosId) {
        this.id = id;
        this.kairosId = kairosId;
    }
}
