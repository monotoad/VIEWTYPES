package com.example.viewtypes;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class XAxisValueFormatter extends ValueFormatter {

    private DateTimeFormatter dateFormat1;

    public XAxisValueFormatter() {
        dateFormat1 = DateTimeFormat.forPattern("dd.MM");
    }

    @Override
    public String getFormattedValue(float value) {
        return dateFormat1.print((long) value);
    }
}
