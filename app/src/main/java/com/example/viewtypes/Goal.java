package com.example.viewtypes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.joda.time.Days;
import org.joda.time.LocalDate;

@Entity
public class Goal implements Parcelable, ItemTypeInterfaсe {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String title;

    private String unitTitle;

    private float unitCount;               // Необходимый объём

    private boolean isCompleted;

    private float budgetWeek;              // Бюджет часов в неделю

    private float completeUnitCount;       // Выполненный объём

    private float hoursSpent;            // Затрачено часов

    private float unitsPerHour;         // Знаков в час

    private float hoursLeft;             // Осталось часов

    private float weeksBeforeDeadline;   // Недель до дедлайна

    private float weeksWork;             // Недель работы

    private long deadline;              // Дедлайн

    private boolean isExpanded;



    public Goal() {
        title = "Диплом";
        unitTitle = "страница";
        isCompleted = false;
        budgetWeek = 8;
        unitCount = 90;
        completeUnitCount = 0;
        hoursSpent = 0;
        unitsPerHour = 0;
        hoursLeft = 0;
        weeksBeforeDeadline = 0;
        weeksWork = 0;
        deadline = new LocalDate().plusDays(5).toDate().getTime();
        isExpanded = true;
    }

    public Goal(float unitCount, float completeUnitCount, float budgetWeek,  float hoursSpent, long deadline) {
        setUnitCount(unitCount);
        setCompleteUnitCount(completeUnitCount);
        setBudgetWeek(budgetWeek);
        setHoursSpent(hoursSpent);
        setDeadline(deadline);
        isExpanded = true;
    }



    ///////////////////////////////////////////////////////


    protected Goal(Parcel in) {
        id = in.readInt();
        title = in.readString();
        unitTitle = in.readString();
        unitCount = in.readFloat();
        isCompleted = in.readByte() != 0;
        budgetWeek = in.readFloat();
        completeUnitCount = in.readFloat();
        hoursSpent = in.readFloat();
        unitsPerHour = in.readFloat();
        hoursLeft = in.readFloat();
        weeksBeforeDeadline = in.readFloat();
        weeksWork = in.readFloat();
        deadline = in.readLong();
        isExpanded = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(unitTitle);
        dest.writeFloat(unitCount);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
        dest.writeFloat(budgetWeek);
        dest.writeFloat(completeUnitCount);
        dest.writeFloat(hoursSpent);
        dest.writeFloat(unitsPerHour);
        dest.writeFloat(hoursLeft);
        dest.writeFloat(weeksBeforeDeadline);
        dest.writeFloat(weeksWork);
        dest.writeLong(deadline);
        dest.writeByte((byte) (isExpanded ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    ////////////////////////////////////////////////////////////


    public void setUnitsPerHour(float unitsPerHour) {
        this.unitsPerHour = unitsPerHour;
    }

    public void setHoursLeft(float hoursLeft) {
        this.hoursLeft = hoursLeft;
    }

    public void setWeeksBeforeDeadline(float weeksBeforeDeadline) {
        this.weeksBeforeDeadline = weeksBeforeDeadline;
    }

    public void setWeeksWork(float weeksWork) {
        this.weeksWork = weeksWork;
    }



    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
        setWeeksBeforeDeadline();
    }

    private void setUnitsPerHour(){
        unitsPerHour = hoursSpent != 0 ? completeUnitCount / hoursSpent : 0;
        setWeeksWork();
    }

    private void setHoursLeft(){
        hoursLeft = budgetWeek * weeksBeforeDeadline;
    }

    private void setWeeksWork(){
        weeksWork = unitsPerHour != 0 && budgetWeek != 0 ? (unitCount - completeUnitCount) / unitsPerHour / budgetWeek : 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public float getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(float unitCount) {
        this.unitCount = unitCount;
        setWeeksWork();
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public float getBudgetWeek() {
        return budgetWeek;
    }

    public void setBudgetWeek(float budgetWeek) {
        this.budgetWeek = budgetWeek;
        setWeeksWork();
        setHoursLeft();
    }

    public float getCompleteUnitCount() {
        return completeUnitCount;
    }

    public void setCompleteUnitCount(float completeUnitCount) {
        this.completeUnitCount = completeUnitCount;
        setUnitsPerHour();
    }

    public float getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(float hoursSpent) {
        this.hoursSpent = hoursSpent;
        setUnitsPerHour();
    }

    public float getUnitsPerHour() {
        return unitsPerHour;
    }


    public float getHoursLeft() {
        return hoursLeft;
    }

    public float getWeeksBeforeDeadline() {
        return weeksBeforeDeadline;
    }

    private void setWeeksBeforeDeadline() {
        weeksBeforeDeadline = (float)(Days.daysBetween(new LocalDate(), new LocalDate(deadline)).getDays())/7;
        setHoursLeft();
    }

    public float getWeeksWork() {
        return weeksWork;
    }

    @Override
    public int getType() {
        return ItemTypeInterfaсe.GOAL;
    }
}
