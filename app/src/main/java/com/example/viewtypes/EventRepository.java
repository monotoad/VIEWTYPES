package com.example.viewtypes;

import android.app.Application;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private EventDao eventDao;
    private LiveData<List<Frog>> allFrogs;
    private LiveData<List<Elephant>> allElephants;
    private LiveData<List<Steak>> allSteaks;
    private LiveData<List<ElephantWithSteaks>> allElephantsWithSteaks;

    private LiveData<List<Event>> allEvents;
    private LiveData<List<KairosWithEvents>> allKairosesWithEvents;
    private LiveData<List<EventsWithKairos>> allEventsWithKairoses;
    private LiveData<List<KairosEventCrossRef>> allCrossRed;
    private LiveData<List<Kairos>> allKairos;

    private LiveData<List<DateWithSteaks>> allDateWithSteaks;

    private LiveData<List<SteakView>> allStakeViews;


    private LiveData<List<HardDateTimeEvent>> allHardDateTimeEvents;
    private LiveData<List<SoftDateTimeEvent>> allSoftDateTimeEvents;
    private LiveData<List<BudgetDateTimeEvent>> allBudgetDateTimeEvents;

    private LiveData<List<DTEventEWithSubDTEvents>> allDTEventsWithSubs;
    private LiveData<List<Frog>> nonCompletedFrogs;
    private LiveData<List<Frog>> FreeFrogs;

    private LiveData<List<Goal>> allGoals;


    /*private LiveData<List<SoftDateTimeEvent>> SDTEofUnknownWeek;
    private LiveData<List<SoftDateTimeEvent>> SDTEofWeek;*/


    public EventRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        eventDao = database.eventDao();

        allFrogs = eventDao.getAllLiveDataFrogs();
        nonCompletedFrogs = eventDao.getNonCompletedFrogs();
        FreeFrogs = eventDao.getFreeFrogs();

        allElephants = eventDao.getAllLiveDataElephants();
        allSteaks = eventDao.getAllLiveDataSteaks();
        allElephantsWithSteaks = eventDao.getElephantsWithSteaks();

        allEvents = eventDao.getAllEvents();
        allKairosesWithEvents = eventDao.getKairosesWithEvents();
        allEventsWithKairoses = eventDao.getEventsWithKairoses();
        allCrossRed = eventDao.getCrossRef();
        allKairos = eventDao.getKairoses();

        allDateWithSteaks = eventDao.getDateWithSteaks();

        allStakeViews = eventDao.getSteaksWithCompleteness();

        allHardDateTimeEvents = eventDao.getAllHardDateTimeEvents();
        allSoftDateTimeEvents = eventDao.getAllSoftDateTimeEvents();
        allBudgetDateTimeEvents = eventDao.getAllBudgetDateTimeEvents();

        allDTEventsWithSubs = eventDao.getAllEventsWithSubEvents();

        /*SDTEofUnknownWeek = eventDao.getSDTEwithUnknownWeek();
        LocalDate start = new LocalDate().withDayOfWeek(DateTimeConstants.MONDAY);
        LocalDate end = new LocalDate().withDayOfWeek(DateTimeConstants.SUNDAY);
        SDTEofWeek = eventDao.getSDTEbyWeek(start.toDate().getTime(), end.toDate().getTime());*/

        allGoals = eventDao.getAllGoals();
    }


    // Жабы

    LiveData<List<Frog>> getAllFrogs(){
        return allFrogs;
    }

    void insert(Frog frog){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(frog);
        });
    }

    void delete(Frog frog){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.delete(frog);
        });
    }

    void deleteAllFrogs(){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteAllFrogs();
        });
    }

    void updateFrog(Frog frog){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.update(frog);
        });
    }

    public LiveData<List<Frog>> getFrogsByDate(long date){
        return eventDao.getFrogsByDate(date);
    }

    public LiveData<List<Frog>> getFrogsByDate(long startDate, long endDate){
        return eventDao.getFrogsByDate(startDate, endDate);
    }

    LiveData<List<Frog>> getNonCompletedFrogs()
    {
        return nonCompletedFrogs;
    }

    LiveData<List<Frog>> getFreeFrogs(){return FreeFrogs;}

    ///////////////////////////////////////////////////////////////////

    // Слоны

    LiveData<List<Elephant>> getAllElephants(){
        return allElephants;
    }

    void insert(Elephant elephant){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(elephant);
        });
    }

    void deleteAllElephants(){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteAllElephants();
        });
    }

    void updateElephant(Elephant elephant){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.update(elephant);
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    void deleteElephant(int elephantId){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteElephant(elephantId);
        });

    }

    void delete(Elephant event){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.delete(event);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void deleteElephantWithSteaks(ElephantWithSteaks elephantWithSteaks){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteElephantWithSteaks(elephantWithSteaks);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void insert(Date date, Elephant elephant){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertDateElephantCrossRef(date, elephant);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void delete(Date date, Elephant elephant){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteDateElephantCrossRef(date, elephant);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteDSCRabove(long date, int id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteDSCRabove(date, id);
        });
    }

    public void delete(DateSteakCrossRef dateSteakCrossRef){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.delete(dateSteakCrossRef);
        });
    }

    ////////////////////////////////////////////

    // Стейки и слоны

    LiveData<List<Steak>> getAllSteaks(){
        return allSteaks;
    }

    LiveData<List<ElephantWithSteaks>> getAllElephantsWithSteaks() {
        return allElephantsWithSteaks;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(ElephantWithSteaks elephantWithSteaks) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(elephantWithSteaks);
        });
    }



    public void insert(Steak steak){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertSteak(steak);
        });
    }


    public void deleteAllElephantsWithSteaks() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteAllElephantsWithSteaks();
        });
    }

    public void deleteSteaksOfElephant(int id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteSteaksOfElephant(id);
        });
    }

    public LiveData<List<Steak>> getSteaksByDate(long date){
        return eventDao.getSteaksByDate(date);
    }

    public LiveData<List<Steak>> getSteaksByDate(long dateStart, long dateEnd){
        return eventDao.getSteaksByDate(dateStart, dateEnd);
    }

    /*public void insertSteaksForElephant(Elephant elephant, List<Steak> steaks){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertSteaksForElephant(elephant, steaks);
        });
    }*/

    ///////////////////////////////////////////////////////////////////////
    // Эвенты и кайросы

    LiveData<List<KairosWithEvents>> getAllKairosesWithEvents(){
        return allKairosesWithEvents;
    }

    LiveData<List<EventsWithKairos>> getAllEventsWithKairoses(){
        return allEventsWithKairoses;
    }

    LiveData<List<Event>> getAllEvents(){
        return allEvents;
    }

    LiveData<List<KairosEventCrossRef>> getAllCrossRed(){
        return allCrossRed;
    }

    LiveData<List<Kairos>> getKairoses(){
        return allKairos;
    }

    public LiveData<List<Event>> getEventsByDate(long startDate, long endDate){
        return eventDao.getEventsByDate(startDate, endDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(Kairos kairos, List<Event> events) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(kairos, events);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertE(Event event, List<Kairos> kairoses) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertE(event, kairoses);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(KairosWithEvents kairosWithEvents) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(kairosWithEvents);
        });
    }


    public void insert(KairosEventCrossRef crossRef){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(crossRef);
        });
    }

    public void insert(Kairos kairos){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(kairos);
        });
    }

    public void insert(Event event){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(event);
        });
    }

    public void insert(int kairosId, Event event){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(kairosId, event);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteEventById(int eventId){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteEventById(eventId);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteKairosById(int kairosId){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteKairosbyId(kairosId);
        });
    }

    public void deleteAllKairosesAndEvents() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteAllKairosesAndEvents();
        });
    }

    ///////////////////////////////////////////////

    LiveData<List<DateWithSteaks>> getAllDateWithSteaks(){
        return allDateWithSteaks;
    }



    public void insertDateSteakCrossRef(Date date, Steak steak, Boolean isComplete){
        AppDatabase.databaseWriteExecutor.execute(() ->{
            eventDao.insertDateSteakCrossRef(date, steak, isComplete);
        });
    }

    public void insert(DateSteakCrossRef dateSteakCrossRef){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(dateSteakCrossRef);
        });
    }

    public LiveData<List<SteakView>> getSteaksWithCompleteness(){
        return allStakeViews;
   }

    public LiveData<List<SteakView>> getSteaksWithCompletenessByDate(long date){
        return eventDao.getSteaksWithCompletenessByDate(date);
    }

    public LiveData<List<SteakView>> getSteaksWithCompletenessByDatePeriod(long dateStart, long dateEnd){
        return eventDao.getSteaksWithCompletenessByDatePeriod(dateStart, dateEnd);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initSteaksForDate(Date date){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.initSteaksForDate(date);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initSteaksForDate(Long dateStart, Long dateEnd){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.initSteaksForDate(dateStart, dateEnd);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteDateSteakCrossRefsBySteak(int id){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteDateSteakCrossRefsBySteak(id);
        });
    }

///////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<HardDateTimeEvent>> getHardDateTimeEventsByDate(long date){
        return eventDao.getHardDateTimeEventsByDate(date);
    }

    public LiveData<List<HardDateTimeEvent>> getHardDateTimeEventsByDatePeriod(long dateStart, long dateEnd){
        return eventDao.getHardDateTimeEventsByDatePeriod(dateStart, dateEnd);
    }

    public LiveData<List<HardDateTimeEvent>> getAllHardDateTimeEvents(){
        return allHardDateTimeEvents;
    }

    public void insert(HardDateTimeEvent hardDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(hardDateTimeEvent);
        });
    }

    public void delete(HardDateTimeEvent hardDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteH(hardDateTimeEvent);
        });
    }

    public void insertHardDateTimeEvent(HardDateTimeEvent hardDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertHardDateTimeEvent(hardDateTimeEvent);
        });
    }

    public void deleteAllHardDateTimeEvents() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteAllHardDateTimeEvents();
        });
    }

    public LiveData<List<HardDateTimeEvent>> getHDTEbyPeriod(long startDate, long endDate){
        return eventDao.getHDTEbyPeriod(startDate, endDate);
    }

    public LiveData<List<HardDateTimeEvent>> getHDTEofUnknownWeek(){
        return eventDao.getHDTEwithUnknownWeek();
    }


    public LiveData<List<HardDateTimeEvent>> getHDTEofUnknownMonth(){
        return eventDao.getHDTEwithUnknownMonth();
    }




    public LiveData<List<SoftDateTimeEvent>> getSoftDateTimeEventsByDate(long date){
        return eventDao.getSoftDateTimeEventsByDate(date);
    }


    public LiveData<List<SoftDateTimeEvent>> getAllSoftDateTimeEvents(){
        return allSoftDateTimeEvents;
    }

    public LiveData<List<SoftDateTimeEvent>> getSDTEbyPeriod(long startDate, long endDate){
        return eventDao.getSDTEbyPeriod(startDate, endDate);
    }

    public LiveData<List<SoftDateTimeEvent>> getSDTEofUnknownWeek(){
        return eventDao.getSDTEwithUnknownWeek();
    }


    public LiveData<List<SoftDateTimeEvent>> getSDTEofUnknownMonth(){
        return eventDao.getSDTEwithUnknownMonth();
    }

    public void insert(SoftDateTimeEvent softDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(softDateTimeEvent);
        });
    }

    public void insertSoftDateTimeEvent(SoftDateTimeEvent softDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertSoftDateTimeEvent(softDateTimeEvent);
        });
    }

    public void deleteAllSoftDateTimeEvents() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteAllHardDateTimeEvents();
        });
    }

    public void delete(SoftDateTimeEvent softDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteS(softDateTimeEvent);
        });
    }



    public LiveData<List<BudgetDateTimeEvent>> getBudgetDateTimeEventsByDate(long date){
        return eventDao.getBudgetDateTimeEventsByDate(date);
    }

    public LiveData<List<BudgetDateTimeEvent>> getAllBudgetDateTimeEvents(){
        return allBudgetDateTimeEvents;
    }

    public void insert(BudgetDateTimeEvent budgetDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(budgetDateTimeEvent);
        });
    }

    public void insertBudgetDateTimeEvent(BudgetDateTimeEvent budgetDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertBudgetDateTimeEvent(budgetDateTimeEvent);
        });
    }

    public void deleteAllBudgetDateTimeEvents() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteAllBudgetDateTimeEvents();
        });
    }

    public LiveData<List<BudgetDateTimeEvent>> getBDTEbyPeriod(long startDate, long endDate){
        return eventDao.getBDTEbyPeriod(startDate, endDate);
    }

    public LiveData<List<BudgetDateTimeEvent>> getBDTEofUnknownWeek(){
        return eventDao.getBDTEwithUnknownWeek();
    }


    public LiveData<List<BudgetDateTimeEvent>> getBDTEofUnknownMonth(){
        return eventDao.getBDTEwithUnknownMonth();
    }

    public void delete(BudgetDateTimeEvent budgetDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.deleteB(budgetDateTimeEvent);
        });
    }

    /////////////////////////////////////////////////////////////

    public LiveData<List<DTEventEWithSubDTEvents>> getAllDTEventsWithSubs(){
        return allDTEventsWithSubs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(DTEventEWithSubDTEvents dtEventEWithSubDTEvents){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertDTEventWithSubs(dtEventEWithSubDTEvents);
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeSubEvent(SubDateTimeEvent subDateTimeEvent){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.changeSubEvent(subDateTimeEvent);
        });
    }


    ////////////////////////////////////////////////////////////

    public void insert(LogsHDTE logsHDTE){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insertLogsHDTE(logsHDTE);
        });
    }

    public LiveData<List<HardDateTimeEvent>> getHardDateTimeTransEventsByDate(long dateStart, long dateEnd){
        return eventDao.getHardDateTimeTransEventsByDate(dateStart, dateEnd);
    }

    public LiveData<List<SoftDateTimeEvent>> getSoftDateTimeTransEventsByDate(long dateStart, long dateEnd){
        return eventDao.getSoftDateTimeTransEventsByDate(dateStart, dateEnd);
    }

    public LiveData<List<BudgetDateTimeEvent>> getBudgetDateTimeTransEventsByDate(long dateStart, long dateEnd){
        return eventDao.getBudgetDateTimeTransEventsByDate(dateStart, dateEnd);
    }


    //////////////////////////////////////////////////////////////////

    LiveData<List<Goal>> getAllGoals(){
        return allGoals;
    }

    void insert(Goal goal){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(goal);
        });
    }

    void delete(Goal goal){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.delete(goal);
        });
    }


    void updateGoal(Goal goal){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.update(goal);
        });
    }

}