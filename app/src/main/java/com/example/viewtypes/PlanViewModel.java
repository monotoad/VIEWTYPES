package com.example.viewtypes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public class PlanViewModel extends ViewModel {

    private MutableLiveData<Long> date;
    private MutableLiveData<Long> timeMin, timeMax;

    public PlanViewModel() {
        date = new MutableLiveData<>();
        date.setValue(new LocalDate().toDate().getTime());

        timeMin = new MutableLiveData<>();
        LocalTime lt = new LocalTime("8:00");
        timeMin.setValue(lt.toDateTimeToday().getMillis());

        timeMax = new MutableLiveData<>();
        LocalTime l = new LocalTime("22:00");
        timeMax.setValue(l.toDateTimeToday().getMillis());
    }

    public void setDate(long date){
        this.date.postValue(date);
    }

    public LiveData<Long> getDate(){
        return date;
    }




    public void setTimeMin(long timeMin){
        this.timeMin.postValue(timeMin);
    }

    public LiveData<Long> getTimeMin(){
        return timeMin;
    }




    public void setTimeMax(long timeMax){
        this.timeMax.postValue(timeMax);
    }

    public LiveData<Long> getTimeMax(){
        return timeMax;
    }
}
