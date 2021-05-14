package com.example.viewtypes;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.function.BiFunction;

public class CombinedLiveData<T, K, S> extends MediatorLiveData<S> {

    private T data1;
    private K data2;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CombinedLiveData(LiveData<T> source1, LiveData<K> source2, BiFunction<T, K, S> combine) {
        super.addSource(source1, it -> {
            data1 = it;
            setValue(combine.apply(data1, data2));
        });

        super.addSource(source2, it -> {
            data2 = it;
            setValue(combine.apply(data1, data2));
        });
    }

    @Override
    public <S1> void addSource(@NonNull LiveData<S1> source, @NonNull Observer<? super S1> onChanged) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S1> void removeSource(@NonNull LiveData<S1> toRemote) {
        throw new UnsupportedOperationException();
    }
}
