package com.example.viewtypes;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public interface ItemTypeInterfaсe {
    int EVENT_FROG = 1;
    int EVENT_ELEPHANT = 2;
    int EVENT_STEAK = 3;

    int EVENT_STEAKDATE = 4;
    int EVENT_DTE = 5;
    int EVENT_HARD_DTE = 6;
    int EVENT_SOFT_DTE = 7;
    int EVENT_BUDGET_DTE = 8;
    int EVENT_SUB_DTE = 9;

    int KAIROS = 10;
    int EVENT_KAIROS = 11;

    int GOAL = 12;

    int PLACEHOLDER = 13;
    int HEADER = 14;

    int getType();
}
