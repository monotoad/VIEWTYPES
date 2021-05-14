package com.example.viewtypes;

public class Placeholder extends HardDateTimeEvent implements ItemTypeInterfaсe{


    public Placeholder() {
    }

    public Placeholder(long startTime, long endTime) {
        timeS = startTime;
        timeE = endTime;
    }


    @Override
    public int getType() {
        return PLACEHOLDER;
    }
}
