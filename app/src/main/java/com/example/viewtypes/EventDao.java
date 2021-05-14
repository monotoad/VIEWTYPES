package com.example.viewtypes;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.function.LongFunction;

@Dao
public abstract class EventDao {
    @Query("SELECT * FROM Frog")
    abstract List<Frog> getAllFrogs();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(Frog event);

    @Update
    abstract void update(Frog event);

    @Delete
    abstract void delete(Frog event);

    @Query("DELETE FROM Frog")
    abstract void deleteAllFrogs();

    @Query("SELECT * FROM Frog")
    abstract LiveData<List<Frog>> getAllLiveDataFrogs();

    @Query("SELECT * FROM Frog WHERE id IN (:Ids)")
    abstract List<Frog> loadFrogsByIds(int[] Ids);

    @Query("SELECT * FROM Frog WHERE id=:id")
    abstract Frog getById(int id);

    @Transaction
    @Query("SELECT * FROM Frog WHERE date=:date")
    abstract LiveData<List<Frog>> getFrogsByDate(long date);

    //@Query("SELECT * FROM Frog WHERE date BETWEEN :dateStart AND :dateEnd")
    @Transaction
    @Query("SELECT * FROM Frog WHERE date BETWEEN :dateStart AND :dateEnd")
    abstract LiveData<List<Frog>> getFrogsByDate(long dateStart, long dateEnd);

    @Transaction
    @Query("SELECT * FROM Frog WHERE date = 0")
    abstract LiveData<List<Frog>> getFreeFrogs();


    ////////////////////////////////adadasdddddddddddddddddddddddddddddd

    @Query("SELECT * FROM Frog WHERE isCompleted = " + 0)
    abstract LiveData<List<Frog>> getNonCompletedFrogs();

    /////////////////////////////////////////////////

    @Query("SELECT * FROM Elephant")
    abstract List<Elephant> getAllElephants();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(Elephant event);

    @Update
    abstract void update(Elephant event);

    @Delete
    abstract void delete(Elephant event);


    @Query("SELECT * FROM DateSteakCrossRef WHERE id=:id")
    abstract List<DateSteakCrossRef> getDateSteakCrossRefs(int id);

    @Delete
    abstract void delete(DateSteakCrossRef dateSteakCrossRef);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void deleteElephant(int elephantId){

        getSteaksByElephantId(elephantId).forEach(i -> {
            getDateSteakCrossRefs(i.id).forEach(this::delete);
            delete(i);
        });



        if (getDCECRbyID(elephantId) != null){
            delete(getDCECRbyID(elephantId));
        }

        Elephant elephant = getElephant(elephantId);

        delete(elephant);




    }

    @Query("DELETE FROM Elephant")
    abstract void deleteAllElephants();

    @Query("SELECT * FROM Elephant")
    abstract LiveData<List<Elephant>> getAllLiveDataElephants();

    @Query("SELECT * FROM Elephant WHERE id IN (:Ids)")
    abstract List<Elephant> loadElephantsByIds(int[] Ids);

    @Transaction
    @Query("SELECT * FROM Elephant")
    abstract LiveData<List<ElephantWithSteaks>> getElephantsWithSteaks();


    @Transaction
    @Query("SELECT * FROM Elephant WHERE id=:id")
    abstract public List<ElephantWithSteaks> getElephantWithSteaks(int id);


    @Transaction
    @Query("SELECT * FROM Elephant WHERE id=:id")
    abstract Elephant getElephant(int id);



    ///////////////////////////////////////

    @Delete
    abstract void delete(Steak steak);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertSteaks(List<Steak> steaks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insertSteak(Steak steak);

    @Query("SELECT * FROM Steak")
    abstract LiveData<List<Steak>> getAllLiveDataSteaks();

    @Query("SELECT * FROM Steak")
    abstract List<Steak> getAllSteaks();

    @Query("SELECT * FROM Steak WHERE isCompleted = " + 0)
    abstract List<Steak> getAllNonComplSteaks();

    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(ElephantWithSteaks elephantWithSteaks) {
        int id = (int) insert(elephantWithSteaks.elephant);

        List<Steak> steaksOld = getSteaksByElephantId(id);
        steaksOld.removeAll(elephantWithSteaks.steaks);
        steaksOld.forEach(i -> {
            delete(i);
        });

        elephantWithSteaks.steaks.forEach(i -> i.elephantId = id);
        insertSteaks(elephantWithSteaks.steaks);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void deleteElephantWithSteaks(ElephantWithSteaks elephantWithSteaks){
        delete(elephantWithSteaks.elephant);

        /*elephantWithSteaks.steaks.forEach( i -> {
            i.elephantId = -1;
            delete(i);
        });*/


        //deleteSteaksOfElephant(elephantWithSteaks.elephant.id);
    }


    @Transaction
    @Query("SELECT * FROM Steak WHERE elephantId=:id")
    abstract public List<Steak> getSteaksByElephantId(int id);

    @Query("SELECT * FROM Steak WHERE id=:id")
    abstract Steak getSteak(int id);


    @Transaction
    @Query("DELETE FROM Steak WHERE elephantId=:id")
    abstract void  deleteSteaksOfElephant(int id);

    @Query("DELETE FROM Steak")
    abstract void deleteAllSteaks();

    @Transaction
    @Query("SELECT * FROM Steak WHERE date=:date")
    abstract public LiveData<List<Steak>> getSteaksByDate(long date);

    @Transaction
    @Query("SELECT * FROM Steak WHERE date BETWEEN :startDate AND :endDate")
    abstract public LiveData<List<Steak>> getSteaksByDate(long startDate, long endDate);


    @Transaction
    public void deleteAllElephantsWithSteaks(){
        deleteAllElephants();
        deleteAllSteaks();

    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(DateCompletedElephantCrossRef dateCompletedElephantCrossRef);

    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertDateElephantCrossRef(Date date, Elephant elephant) {
        DateCompletedElephantCrossRef dateCompletedElephantCrossRef = new DateCompletedElephantCrossRef();
        dateCompletedElephantCrossRef.date = date.date;
        dateCompletedElephantCrossRef.id = elephant.id;

        insert(date);
        insert(dateCompletedElephantCrossRef);
    }

    @Query("SELECT * FROM DateCompletedElephantCrossRef WHERE id = :id")
    abstract public DateCompletedElephantCrossRef getDCECRbyID(int id);


    /*@Delete
    abstract void delete(DateCompletedSteakCrossRef dateCompletedElephantCrossRef);

    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteDateSteakCrossRef(Date date, Steak elephant) {
        DateCompletedElephantCrossRef dateCompletedElephantCrossRef = new DateCompletedElephantCrossRef();
        dateCompletedElephantCrossRef.date = date.date;
        dateCompletedElephantCrossRef.id = elephant.id;
        delete(dateCompletedElephantCrossRef);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(DateCompletedElephantCrossRef dateCompletedElephantCrossRef);

    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertDateElephantCrossRef(Date date, Elephant elephant) {
        DateCompletedElephantCrossRef dateCompletedElephantCrossRef = new DateCompletedElephantCrossRef();
        dateCompletedElephantCrossRef.date = date.date;
        dateCompletedElephantCrossRef.id = elephant.id;

        insert(date);
        insert(dateCompletedElephantCrossRef);
    }*/


    @Delete
    abstract void delete(DateCompletedElephantCrossRef dateCompletedElephantCrossRef);

    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteDateElephantCrossRef(Date date, Elephant elephant) {
        DateCompletedElephantCrossRef dateCompletedElephantCrossRef = new DateCompletedElephantCrossRef();
        dateCompletedElephantCrossRef.date = date.date;
        dateCompletedElephantCrossRef.id = elephant.id;
        delete(dateCompletedElephantCrossRef);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // Кайросы и Эвенты

    @Transaction
    @Query("SELECT * FROM Kairos")
    abstract public LiveData<List<KairosWithEvents>> getKairosesWithEvents();


    @Query("SELECT * FROM Kairos")
    abstract public LiveData<List<Kairos>> getKairoses();

    @Transaction
    @Query("SELECT * FROM Event WHERE date BETWEEN :dateStart AND :dateEnd")
    abstract LiveData<List<Event>> getEventsByDate(long dateStart, long dateEnd);

    @Transaction
    @Query("SELECT * FROM Event")
    abstract public LiveData<List<EventsWithKairos>> getEventsWithKairoses();

    @Transaction
    @Query("SELECT * FROM Event")
    abstract public LiveData<List<Event>> getAllEvents();


    @Transaction
    @Query("SELECT * FROM KairosEventCrossRef")
    abstract public LiveData<List<KairosEventCrossRef>> getCrossRef();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(Kairos kairos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(Event event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(KairosEventCrossRef event);

    @Query("DELETE FROM Kairos")
    abstract void deleteAllKairos();

    @Query("DELETE FROM Event")
    abstract void deleteAllEvents();

    @Query("DELETE FROM KairosEventCrossRef")
    abstract void deleteAllKairosEvent();

    @Transaction
    public void deleteAllKairosesAndEvents(){
        /*List<ElephantWithSteaks> elephantsWithSteaks = getElephantsWithSteaks().getValue();
        if (elephantsWithSteaks != null){
            for (ElephantWithSteaks elephantWithSteaks: elephantsWithSteaks){
                delete(elephantWithSteaks.elephant);
                deleteSteaksOfElephant(elephantWithSteaks.elephant.id);
            }
        }*/

        deleteAllKairos();
        deleteAllEvents();
        deleteAllKairosEvent();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void insert(EventsWithKairos eventsWithKairos){
        Event event = eventsWithKairos.event;

        List<Kairos> kairoses = eventsWithKairos.kairoses;

        int id = (int) insert(event);
        kairoses.forEach(i -> {
            int idd = (int) insert(i);
            insert(new KairosEventCrossRef(id, idd));
        });
    }

    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(KairosWithEvents kairosWithEvents) {

        // Проверить, если передать сюда не KairosWithEvents а Kairos и List<Events>, то будет ли запись в таблице KairosWithEvents
        Kairos kairos = kairosWithEvents.kairos;
        List<Event> events = kairosWithEvents.events;

        int id = (int) insert(kairos);
        events.forEach(i -> {
            int idd = (int) insert(i);
            insert(new KairosEventCrossRef(idd, id));

        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void insert(Event event, List<Kairos> kairoses){
        int id = (int) insert(event);
        kairoses.forEach(i -> {
            int idd = (int) insert(i);
            insert(new KairosEventCrossRef(id, idd));
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void deleteKairosbyId(int kairosId){

        List<KairosEventCrossRef> kairoses = getKairosEventCrossRefByKairosID(kairosId);

        kairoses.forEach(i -> {
            if (getKairosEventCrossRefByEventID(i.id).size() == 1){
                deleteEventById(i.id);
            }
        });

        Kairos kairos = getKairos(kairosId);

        delete(kairos);

        kairoses.forEach(i ->{
            delete(i);
        });
    }

    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(Kairos kairos, List<Event> events) {
        int id = (int) insert(kairos);

        List<Event> eventsOld = getEventsByKairosId(id);

        eventsOld.removeAll(events);
        eventsOld.forEach(i -> {
            deleteKairosEventCrossRef(id, i.id);
        });

        eventsOld.forEach(i -> {
            //delete(i);
            if (getKairosEventCrossRefByEventID(i.id).size() == 0){
                //delete(getKairosEventCrossRefByIDID(getKairosEventCrossRefByEventID(i.id).get(0).kairosId, getKairosEventCrossRefByEventID(i.id).get(0).id));
                deleteEventById(i.id);
            }
        });

        events.forEach(i -> {
            long is = insert(i);
            insert(new KairosEventCrossRef((int) is, id));
        });
    }

    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertE(Event event, List<Kairos> kairoses) {
        int id = (int) insert(event);

        List<Kairos> kairosesOld = getKairosesByEventId(id);

        kairosesOld.removeAll(kairoses);
        kairosesOld.forEach(i -> {
            deleteKairosEventCrossRef(i.kairosId, id);
        });

        kairosesOld.forEach(i -> {
            //delete(i);
            if (getKairosEventCrossRefByKairosID(i.kairosId).size() == 0){
                //delete(getKairosEventCrossRefByIDID(getKairosEventCrossRefByEventID(i.id).get(0).kairosId, getKairosEventCrossRefByEventID(i.id).get(0).id));
                deleteKairosById(i.kairosId);
            }
        });

        kairoses.forEach(i -> {
            long is = insert(i);
            insert(new KairosEventCrossRef(id, (int) is));
        });
    }

    @Query("SELECT * FROM Kairos WHERE kairosId=:id")
    abstract Kairos getKairos(int id);

    @Query("SELECT * FROM Event WHERE id=:id")
    abstract Event getEvent(int id);


    @Query("SELECT * FROM KairosEventCrossRef WHERE id=:id")
    abstract List<KairosEventCrossRef> getKairosEventCrossRefByEventID(int id);


    @Query("SELECT * FROM KairosEventCrossRef WHERE kairosId=:id")
    abstract List<KairosEventCrossRef> getKairosEventCrossRefByKairosID(int id);

    @Query("SELECT * FROM KairosEventCrossRef WHERE kairosId=:kairosId AND id=:id")
    abstract KairosEventCrossRef getKairosEventCrossRefByIDID(int kairosId, int id);


    @Query("SELECT * FROM Event INNER JOIN KairosEventCrossRef ON Event.id = KairosEventCrossRef.id WHERE KairosEventCrossRef.kairosId = :id")
    abstract List<Event> getEventsByKairosId(int id);

    @Query("SELECT * FROM Kairos INNER JOIN KairosEventCrossRef ON Kairos.kairosId = KairosEventCrossRef.id WHERE KairosEventCrossRef.id = :id")
    abstract List<Kairos> getKairosesByEventId(int id);

    @Transaction
    public void insert(int kairosId, Event event){
        int id = (int) insert(event);

        insert(new KairosEventCrossRef(id, kairosId));
    }

    @Delete
    abstract void delete(Event event);

    @Delete
    abstract void delete(Kairos kairos);

    @Delete
    abstract void delete(KairosEventCrossRef kairosEventCrossRef);

    @Transaction
    public void deleteKairosEventCrossRef(int kairosId, int eventId){
        delete(getKairosEventCrossRefByIDID(kairosId, eventId));
    }


    // Удаление Эвента из Event и всех строчек с ним из KairosEventCrossRef
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void deleteEventById(int eventId){
        Event event = getEvent(eventId);

        delete(event);

        List<KairosEventCrossRef> events = getKairosEventCrossRefByEventID(eventId);

        events.forEach(i -> {
            delete(i);
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteKairosById(int kairosId){
        Kairos kairos = getKairos(kairosId);

        delete(kairos);

        List<KairosEventCrossRef> kairoses = getKairosEventCrossRefByKairosID(kairosId);

        kairoses.forEach(i -> {
            delete(i);
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Transaction
    @Query("SELECT * FROM Frog")
    public abstract List<DateWithFrogs> getFrogsAndDates();


    //@Transaction
    //@Query("SELECT * FROM D")
    //public abstract DateWithFrogs getDatewithFrogsByDate(Date date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(Date date);


    @Transaction
    @Query("SELECT * FROM Date WHERE date=:date")
    abstract Date getDatebyDate(long date);

    // получить даты по промежутку дата1..дата2


///////////////////////////////////////////////////////

    @Transaction
    @Query("SELECT * FROM Date")
    abstract public LiveData<List<DateWithSteaks>> getDateWithSteaks();


    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateDayStake(long date, int id, boolean Complete) {

        Steak steak = getSteak(id);

    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(DateSteakCrossRef dateSteakCrossRef);


    @Query("SELECT * FROM DateSteakCrossRef WHERE date=:date AND id=:id")
    abstract public DateSteakCrossRef getDateSteakCrossRef(long date, int id);

    @Query("SELECT * FROM DateSteakCrossRef WHERE id = :id")
    abstract public List<DateSteakCrossRef> getDateSteakCrossRefById(int id);

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteDateSteakCrossRefsBySteak(int id){
        getDateSteakCrossRefs(id).forEach(i -> {
            delete(i);
        });
    }

    @Query("SELECT * FROM DateSteakCrossRef WHERE id=:id AND isDailyCompleted =" + 1)
    abstract public List<DateSteakCrossRef> getDSCR_Completed(int id);

    @Transaction
    public void insertDateSteakCrossRef(Date date, Steak steak, Boolean isComplete){
        long dateId = insert(date);

        int steakId = (int) insertSteak(steak);

        DateSteakCrossRef dateSteakCrossRef = new DateSteakCrossRef();
        dateSteakCrossRef.id = steakId;
        dateSteakCrossRef.date = dateId;
        dateSteakCrossRef.isDailyCompleted = isComplete;
        insert(dateSteakCrossRef);

        if (getDSCR_Completed(steakId).isEmpty()){
            steak.isCompleted=false;
            steak.date = 0;
            insertSteak(steak);
        }

        /*if (getDateSteakCrossRef(dateId, steakId) == null){
            DateSteakCrossRef steakCrossRef = new DateSteakCrossRef();
            steakCrossRef.date = dateId;
            steakCrossRef.id = steakId;


            if (steak.days.isEmpty() || !steak.days.contains(new LocalDate(date.date).getDayOfWeek()))
                steakCrossRef.isDailyCompleted = steak.isCompleted;
            //}else{
            //    steakCrossRef.isDailyCompleted = false;
            //}


            insert(steakCrossRef);
        }
        else {
            DateSteakCrossRef dateSteakCrossRef = new DateSteakCrossRef();
            dateSteakCrossRef.date = dateId;
            dateSteakCrossRef.id = steakId;
            dateSteakCrossRef.isDailyCompleted = isComplete;
            insert(dateSteakCrossRef);
        }*/

    }


    @Transaction
    @Query("SELECT * FROM Steak " + "INNER JOIN DateSteakCrossRef ON Steak.id = DateSteakCrossRef.id " + "WHERE DateSteakCrossRef.date LIKE :date")
    abstract public List<Steak> getCrossSteaksByDate(long date);

    @Transaction
    @Query("SELECT *, isDailyCompleted " + "FROM Steak, DateSteakCrossRef " + "WHERE Steak.id == DateSteakCrossRef.id")
    abstract public LiveData<List<SteakView>> getSteaksWithCompleteness();

    @Transaction
    @Query("SELECT * FROM DateSteakCrossRef WHERE id=:id AND date=:date")
    abstract public SteakView getSteakCrossRefById(long date, int id);

    @Transaction
    @Query("SELECT *, isDailyCompleted " + "FROM Steak, DateSteakCrossRef " + "WHERE Steak.id == DateSteakCrossRef.id AND DateSteakCrossRef.date == :date")
    abstract public LiveData<List<SteakView>> getSteaksWithCompletenessByDate(long date);

    @Transaction
    @Query("SELECT *, isDailyCompleted FROM Steak, DateSteakCrossRef WHERE Steak.id == DateSteakCrossRef.id AND DateSteakCrossRef.date BETWEEN :dateStart AND :dateEnd")
    abstract public LiveData<List<SteakView>> getSteaksWithCompletenessByDatePeriod(long dateStart, long dateEnd);


    @Transaction
    @Query("SELECT * FROM DateSteakCrossRef WHERE date > :date AND id = :id")
    abstract public List<DateSteakCrossRef> getSteaksAboveDate(long date, int id);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void deleteDSCRabove(long date, int id){
        getSteaksAboveDate(date, id).forEach(this::delete);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void initSteaksForDate(Date date){
        int dow = new LocalDate(date.date).getDayOfWeek();

        List<Steak> all = getAllNonComplSteaks();

        all.forEach(i -> {
            if (i.days.contains(dow)) {

                if (getSteakCrossRefById(date.date, i.id) != null){
                    insertDateSteakCrossRef(date, i, getSteakCrossRefById(date.date, i.id).isDailyCompleted);
                }
                else insertDateSteakCrossRef(date, i, Boolean.FALSE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void initSteaksForDate(Long dateStart, Long dateEnd){
        LocalDate dS = new LocalDate(dateStart);
        LocalDate dE = new LocalDate(dateEnd);

        while (dS.toDate().getTime() <= dE.toDate().getTime()){
            Date date = new Date();
            date.date = dS.toDate().getTime();
            initSteaksForDate(date);

            dS = dS.plusDays(1);
        }



        /*while (dS.toDate().getTime() <= dE.toDate().getTime()){
            int dow = dS.getDayOfWeek();

            List<Steak> all = getAllNonComplSteaks();

            all.forEach(i -> {
                if (i.days.contains(dow)){
                    if (getSteakCrossRefById(dS.toDate().getTime(), i.id) != null){
                        Date da = new Date();
                        da.date = dS.toDate().getTime();
                        insertDateSteakCrossRef(da, i, getSteakCrossRefById(da.date, i.id).isDailyCompleted);
                    }
                    else{
                        Date da = new Date();
                        da.date = dS.toDate().getTime();
                        insertDateSteakCrossRef(da, i, Boolean.FALSE);
                    }
                }
            });

            dS.plusDays(1);
        }*/

        /*Long temp = dateStart;
        while (temp <= dateEnd){
            int dow = new LocalDate(temp).getDayOfWeek();

            List<Steak> all = getAllNonComplSteaks();

            Long finalTemp = temp;
            all.forEach(i -> {
                if (i.days.contains(dow)){
                    if (getSteakCrossRefById(finalTemp, i.id) != null){
                        Date da = new Date();
                        da.date = finalTemp;
                        insertDateSteakCrossRef(da, i, getSteakCrossRefById(da.date, i.id).isDailyCompleted);
                    }
                    else{
                        Date da = new Date();
                        da.date = finalTemp;
                        insertDateSteakCrossRef(da, i, Boolean.FALSE);
                    }
                }
            });

            temp = new LocalDate(temp).plusDays(1).toDate().getTime();

        }*/
    }

    //////////////////////////////////////////////////////////////////

    @Transaction
    @Query("SELECT * FROM Date")
    abstract public LiveData<List<DateWithHDTEvents>> getAllDatesWithHDTEvents();

    @Transaction
    @Query("SELECT * FROM HardDateTimeEvent")
    abstract public LiveData<List<HardDateTimeEvent>> getAllHardDateTimeEvents();

    @Transaction
    @Query("SELECT * FROM HardDateTimeEvent " + "WHERE date=:date")
    abstract public LiveData<List<HardDateTimeEvent>> getHardDateTimeEventsByDate(long date);

    @Transaction
    @Query("SELECT * FROM HardDateTimeEvent " + "WHERE date BETWEEN :startDate AND :endDate")
    abstract public LiveData<List<HardDateTimeEvent>> getHardDateTimeEventsByDatePeriod(long startDate, long endDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(HardDateTimeEvent hardDateTimeEvent);

    @Transaction
    public void insertHardDateTimeEvent(HardDateTimeEvent hardDateTimeEvent){
        Date date = new Date();
        date.date = hardDateTimeEvent.date;
        insert(date);
    }

    @Query("DELETE FROM HardDateTimeEvent")
    abstract void deleteAllHardDateTimeEvents();

    @Delete
    abstract void delete(HardDateTimeEvent hardDateTimeEvent);

    public void deleteH(HardDateTimeEvent hardDateTimeEvent){
        deleteLogsHDTEbyID(hardDateTimeEvent.id, ItemTypeInterfaсe.EVENT_HARD_DTE);
        delete(hardDateTimeEvent);
    }


    @Query("SELECT * FROM HardDateTimeEvent WHERE date=-1")
    abstract LiveData<List<HardDateTimeEvent>> getHDTEwithUnknownWeek();

    @Query("SELECT * FROM HardDateTimeEvent WHERE date BETWEEN :startDate AND :endDate")
    abstract LiveData<List<HardDateTimeEvent>> getHDTEbyPeriod(long startDate, long endDate);

    @Query("SELECT * FROM HardDateTimeEvent WHERE date=-2")
    abstract LiveData<List<HardDateTimeEvent>> getHDTEwithUnknownMonth();

    @Transaction
    @Query("SELECT * FROM Date")
    abstract public LiveData<List<DateWithSDTEvents>> getAllDatesWithSDTEvents();

    @Transaction
    @Query("SELECT * FROM SoftDateTimeEvent")
    abstract public LiveData<List<SoftDateTimeEvent>> getAllSoftDateTimeEvents();

    @Transaction
    @Query("SELECT * FROM SoftDateTimeEvent " + "WHERE date=:date")
    abstract public LiveData<List<SoftDateTimeEvent>> getSoftDateTimeEventsByDate(long date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(SoftDateTimeEvent softDateTimeEvent);

    @Transaction
    public void insertSoftDateTimeEvent(SoftDateTimeEvent softDateTimeEvent){
        Date date = new Date();
        date.date = softDateTimeEvent.date;

        insert(date);
        insert(softDateTimeEvent);
    }

    @Query("DELETE FROM SoftDateTimeEvent")
    abstract void deleteAllSoftDateTimeEvents();

    @Delete
    abstract void delete(SoftDateTimeEvent softDateTimeEvent);

    public void deleteS(SoftDateTimeEvent softDateTimeEvent){
        deleteLogsHDTEbyID(softDateTimeEvent.id, ItemTypeInterfaсe.EVENT_SOFT_DTE);
        delete(softDateTimeEvent);
    }

    @Query("SELECT * FROM SoftDateTimeEvent WHERE date=-1")
    abstract LiveData<List<SoftDateTimeEvent>> getSDTEwithUnknownWeek();

    @Query("SELECT * FROM SoftDateTimeEvent WHERE date BETWEEN :startDate AND :endDate")
    abstract LiveData<List<SoftDateTimeEvent>> getSDTEbyPeriod(long startDate, long endDate);

    @Query("SELECT * FROM SoftDateTimeEvent WHERE date=-2")
    abstract LiveData<List<SoftDateTimeEvent>> getSDTEwithUnknownMonth();

    @Transaction
    @Query("SELECT * FROM Date")
    abstract public LiveData<List<DateWithBDTEvents>> getAllDatesWithBDTEvents();

    @Transaction
    @Query("SELECT * FROM BudgetDateTimeEvent")
    abstract public LiveData<List<BudgetDateTimeEvent>> getAllBudgetDateTimeEvents();

    @Transaction
    @Query("SELECT * FROM BudgetDateTimeEvent " + "WHERE date=:date")
    abstract public LiveData<List<BudgetDateTimeEvent>> getBudgetDateTimeEventsByDate(long date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(BudgetDateTimeEvent budgetDateTimeEvent);

    @Transaction
    public void insertBudgetDateTimeEvent(BudgetDateTimeEvent budgetDateTimeEvent){
        Date date = new Date();
        date.date = budgetDateTimeEvent.date;

        insert(date);
        insert(budgetDateTimeEvent);
    }

    @Query("DELETE FROM BudgetDateTimeEvent")
    abstract void deleteAllBudgetDateTimeEvents();

    @Query("SELECT * FROM BudgetDateTimeEvent WHERE date=-1")
    abstract LiveData<List<BudgetDateTimeEvent>> getBDTEwithUnknownWeek();

    @Query("SELECT * FROM BudgetDateTimeEvent WHERE date BETWEEN :startDate AND :endDate")
    abstract LiveData<List<BudgetDateTimeEvent>> getBDTEbyPeriod(long startDate, long endDate);

    @Query("SELECT * FROM BudgetDateTimeEvent WHERE date=-2")
    abstract LiveData<List<BudgetDateTimeEvent>> getBDTEwithUnknownMonth();

    @Delete
    abstract void delete(BudgetDateTimeEvent budgetDateTimeEvent);

    public void deleteB(BudgetDateTimeEvent budgetDateTimeEvent){
        deleteLogsHDTEbyID(budgetDateTimeEvent.id, ItemTypeInterfaсe.EVENT_BUDGET_DTE);
        delete(budgetDateTimeEvent);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Transaction
    @Query("SELECT * FROM DateTimeEvent")
    abstract public LiveData<List<DTEventEWithSubDTEvents>> getAllEventsWithSubEvents();

    @Transaction
    @Query("SELECT * FROM DateTimeEvent where id=:id")
    abstract public LiveData<List<DTEventEWithSubDTEvents>> getEventWithSubEvents(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(DateTimeEvent dateTimeEvent);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(SubDateTimeEvent dateTimeEvent);

    @Transaction
    public void insertDateTimeEvent(DateTimeEvent dateTimeEvent){
        Date date = new Date();
        date.date = dateTimeEvent.date;

        insert(date);
        insert(dateTimeEvent);
    }



    @Transaction
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertDTEventWithSubs(DTEventEWithSubDTEvents dtEventEWithSubDTEvents) {
        int id = (int) insert(dtEventEWithSubDTEvents.dateTimeEvent);

        dtEventEWithSubDTEvents.SubEvents.forEach(i -> {
            i.parentId = id;
            long i1 = insert(i);

            insertSoftEventsBySub(i1, i);
        });
    }

    @Transaction
    public void insertSoftEventsBySub(long id, SubDateTimeEvent subDateTimeEvent){
        long dateS = subDateTimeEvent.dateS;
        long dateE = subDateTimeEvent.date;

        LocalDate ldS = new LocalDate(dateS);
        LocalDate ldE = new LocalDate(dateE);

        while (ldS.isBefore(ldE) || ldS.equals(ldE)){
            SoftDateTimeEvent softDateTimeEvent = new SoftDateTimeEvent();
            softDateTimeEvent.date = ldS.toDate().getTime();
            softDateTimeEvent.title = subDateTimeEvent.title;
            softDateTimeEvent.priority = Constants.EVENT_NONE;
            softDateTimeEvent.isCompleted = false;
            softDateTimeEvent.subId = (int) id;
            insertSoftDateTimeEvent(softDateTimeEvent);
            ldS = ldS.plusDays(1);
        }
    }

    @Query("SELECT * FROM SubDateTimeEvent WHERE id=:id")
    abstract public SubDateTimeEvent getSubById(int id);


    @Query("SELECT * FROM SoftDateTimeEvent WHERE subId = :id")
    abstract public List<SoftDateTimeEvent> getSoftBySubId(int id);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public void changeSubEvent(SubDateTimeEvent subDateTimeEvent){
        SubDateTimeEvent old = getSubById(subDateTimeEvent.id);
        if (old != null){
            Interval oldfag = new Interval(old.dateS, old.date);
            Interval newFag = new Interval(subDateTimeEvent.dateS, subDateTimeEvent.date);

            Interval cross = oldfag.overlap(newFag);
            List<SoftDateTimeEvent> softs = getSoftBySubId(subDateTimeEvent.id);
            // То что не попало в новый промежуток - удаляем
            softs.forEach(i -> {
                if (!(i.date >= cross.getStartMillis() && i.date <= cross.getEndMillis())){
                    delete(i);
                }
            });

            long oldStart = old.dateS;
            long newStart = subDateTimeEvent.dateS;
            LocalDate oldS = new LocalDate(oldStart);
            LocalDate newS = new LocalDate(newStart);

            long oldEnd = old.date;
            long newEnd = subDateTimeEvent.date;
            LocalDate oldE = new LocalDate(oldEnd).plusDays(1);
            LocalDate newE = new LocalDate(newEnd);


            // Если дата старта стала раньше, чем была
            if (newStart < oldStart){
                while (newS.isBefore(oldS)){
                    SoftDateTimeEvent softDateTimeEvent = new SoftDateTimeEvent();
                    softDateTimeEvent.date = newS.toDate().getTime();
                    softDateTimeEvent.title = subDateTimeEvent.title;
                    softDateTimeEvent.priority = Constants.EVENT_NONE;
                    softDateTimeEvent.isCompleted = false;
                    softDateTimeEvent.subId = subDateTimeEvent.id;
                    insertSoftDateTimeEvent(softDateTimeEvent);
                    newS = newS.plusDays(1);
                }
            }

            // Если дата окончания стала позже чем была
            if (newEnd >= oldEnd){
                while (oldE.isBefore(newE) || oldE.isEqual(newE)){
                    SoftDateTimeEvent softDateTimeEvent = new SoftDateTimeEvent();
                    softDateTimeEvent.date = oldE.toDate().getTime();
                    softDateTimeEvent.title = subDateTimeEvent.title;
                    softDateTimeEvent.priority = Constants.EVENT_NONE;
                    softDateTimeEvent.isCompleted = false;
                    softDateTimeEvent.subId = subDateTimeEvent.id;
                    insertSoftDateTimeEvent(softDateTimeEvent);
                    oldE = oldE.plusDays(1);
                }
            }

            insert(subDateTimeEvent);
        }
        else{
            insertSoftEventsBySub(subDateTimeEvent.id, subDateTimeEvent);
        }
    }

    ///////////////////////////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(LogsHDTE logsHDTE);

    @Transaction
    public void insert(long dateOld, long dateNew, int id, int type, boolean isDailyCompleted){
        LogsHDTE logsHDTE = new LogsHDTE(dateOld, dateNew, id, type, isDailyCompleted);
        insert(logsHDTE);
    }

    @Transaction
    public void insertLogsHDTE(LogsHDTE logsHDTE){

        long temp = logsHDTE.dateNew;

        if (getLogsHDTEbyIDandDateOld(logsHDTE.id, logsHDTE.type, temp) != null){
            while (getLogsHDTEbyIDandDateOld(logsHDTE.id, logsHDTE.type, temp) != null){
                temp = getLogsHDTEbyIDandDateOld(logsHDTE.id, logsHDTE.type, temp).dateNew;
                delete(getLogsHDTEbyIDandDateNew(logsHDTE.id, logsHDTE.type, temp));
            }
        }
        else{
            insert(logsHDTE);
        }

    }

    @Delete
    abstract void delete(LogsHDTE logsHDTE);

    @Delete
    abstract void delete(List<LogsHDTE> logsHDTES);

    @Query("SELECT * FROM LogsHDTE WHERE id=:id AND type=:type")
    abstract public List<LogsHDTE> getLogsHDTEbyID(int id, int type);

    @Query("SELECT * FROM LogsHDTE WHERE id=:id AND dateOld=:dateOld AND type=:type")
    abstract public LogsHDTE getLogsHDTEbyIDandDateOld(int id, int type, long dateOld);

    @Query("SELECT * FROM LogsHDTE WHERE id=:id AND dateNew=:dateNew AND type=:type")
    abstract public LogsHDTE getLogsHDTEbyIDandDateNew(int id, int type,  long dateNew);

    @Transaction
    public void deleteLogsHDTEbyID(int id, int type){
        if (getLogsHDTEbyID(id, type) != null){
            delete(getLogsHDTEbyID(id, type));
        }
    }



    //@Query("SELECT LogsHDTE.dateOld, HardDateTimeEvent.id, HardDateTimeEvent.title, HardDateTimeEvent.subId, HardDateTimeEvent.timeS, HardDateTimeEvent.timeE, HardDateTimeEvent.stimulus, " + "LogsHDTE.isDailyCompleted FROM HardDateTimeEvent, LogsHDTE INNER JOIN LogsHDTE ON HardDateTimeEvent.id = LogsHDTE.id " + "WHERE dateOld BETWEEN :dateStart AND :dateEnd AND LogsHDTE.type="+ItemTypeInterfaсe.EVENT_HARD_DTE)
    @Transaction
    @Query("SELECT HardDateTimeEvent.id, stimulus, timeS, timeE, title, LogsHDTE.isDailyCompleted as isCompleted, subId, LogsHDTE.dateOld as date FROM HardDateTimeEvent, LogsHDTE WHERE HardDateTimeEvent.id = LogsHDTE.id AND dateOld BETWEEN :dateStart AND :dateEnd AND type LIKE " + ItemTypeInterfaсe.EVENT_HARD_DTE)
    abstract public LiveData<List<HardDateTimeEvent>> getHardDateTimeTransEventsByDate(long dateStart, long dateEnd);

    //@Query("SELECT * FROM Event INNER JOIN KairosEventCrossRef ON Event.id = KairosEventCrossRef.id WHERE KairosEventCrossRef.kairosId = :id")



    @Transaction
    @Query("SELECT SoftDateTimeEvent.id, stimulus, timeS, timeE, priority, title, LogsHDTE.isDailyCompleted as isCompleted, subId, LogsHDTE.dateOld as date FROM SoftDateTimeEvent, LogsHDTE WHERE SoftDateTimeEvent.id = LogsHDTE.id AND dateOld BETWEEN :dateStart AND :dateEnd AND type LIKE " + ItemTypeInterfaсe.EVENT_SOFT_DTE)
    abstract public LiveData<List<SoftDateTimeEvent>> getSoftDateTimeTransEventsByDate(long dateStart, long dateEnd);

    @Transaction
    @Query("SELECT BudgetDateTimeEvent.id, stimulus, timeS, timeE, priority, duration, title, LogsHDTE.isDailyCompleted as isCompleted, subId, LogsHDTE.dateOld as date FROM BudgetDateTimeEvent, LogsHDTE WHERE BudgetDateTimeEvent.id = LogsHDTE.id AND dateOld BETWEEN :dateStart AND :dateEnd AND type LIKE " + ItemTypeInterfaсe.EVENT_BUDGET_DTE)
    abstract public LiveData<List<BudgetDateTimeEvent>> getBudgetDateTimeTransEventsByDate(long dateStart, long dateEnd);


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insert(Goal goal);

    @Update
    abstract void update(Goal goal);

    @Delete
    abstract void delete(Goal goal);

    @Query("SELECT * FROM Goal")
    abstract LiveData<List<Goal>> getAllGoals();

}
