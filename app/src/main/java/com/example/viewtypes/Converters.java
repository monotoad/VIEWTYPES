package com.example.viewtypes;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTimeConstants;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<Integer> stringToSomeObjectList(String value){
        Type listType = new TypeToken<List<Integer>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Integer> someObjects){
        return new Gson().toJson(someObjects);
    }
}
