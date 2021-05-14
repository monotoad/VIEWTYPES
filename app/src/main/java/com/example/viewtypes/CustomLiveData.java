package com.example.viewtypes;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

class CustomLiveData extends MediatorLiveData<Pair<Long, Long>> {
    public CustomLiveData(LiveData<Long> code, LiveData<Long> nbDays) {
        addSource(code, new Observer<Long>() {
            public void onChanged(@Nullable Long first) {
                setValue(Pair.create(first, nbDays.getValue()));
            }
        });
        addSource(nbDays, new Observer<Long>() {
            public void onChanged(@Nullable Long second) {
                setValue(Pair.create(code.getValue(), second));
            }
        });
    }
}