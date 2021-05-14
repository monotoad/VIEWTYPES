package com.example.viewtypes;

import android.app.Application;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.Pair;
import android.view.SurfaceControl;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class EventViewModel extends AndroidViewModel {

    private EventRepository eventRepository;
    private final LiveData<List<Frog>> allFrogs;

    private final LiveData<List<Elephant>> allElephants;
    private final LiveData<List<ElephantWithSteaks>> allElephantsWithSteaks;
    private final LiveData<List<Steak>> allSteaks;

    private LiveData<List<Event>> allEvents;
    private LiveData<List<KairosWithEvents>> allKairosesWithEvents;
    private LiveData<List<EventsWithKairos>> allEventsWithKairoses;
    private LiveData<List<KairosEventCrossRef>> allCrossRed;
    private LiveData<List<Kairos>> allKairos;

    private LiveData<List<Event>> showingEvents;

    private LiveData<List<DateWithSteaks>> allDateWithSteaks;

    private LiveData<List<SteakView>> allStakeViews;

    private LiveData<List<SteakView>> showingStakeView;
    private LiveData<List<Steak>> showingSteaks;


    private MutableLiveData<Long> showingDateEvent = new MutableLiveData<>();
    private MutableLiveData<Long> dateEnd = new MutableLiveData<>();
    private CustomLiveData trigger;

    //private MutableLiveData<android.util.Pair<Long, Long>> showingDateEventPeriod = new MutableLiveData<>();

    private LiveData<List<HardDateTimeEvent>> allHardDateTimeEvents;

    private LiveData<List<HardDateTimeEvent>> showingDateHardEvents;
    private LiveData<List<HardDateTimeEvent>> transDateHardEvents;
    private CombinedLiveData<List<HardDateTimeEvent>,List<HardDateTimeEvent>, List<HardDateTimeEvent>> statHardEvents;

    private LiveData<List<SoftDateTimeEvent>> allSoftDateTimeEvents;

    private LiveData<List<SoftDateTimeEvent>> showingDateSoftEvents;
    private LiveData<List<SoftDateTimeEvent>> transDateSoftEvents;
    private CombinedLiveData<List<SoftDateTimeEvent>,List<SoftDateTimeEvent>, List<SoftDateTimeEvent>> statSoftEvents;

    private LiveData<List<BudgetDateTimeEvent>> allBudgetDateTimeEvents;
    private LiveData<List<BudgetDateTimeEvent>> showingDateBudgetEvents;
    private LiveData<List<BudgetDateTimeEvent>> transDateBudgetEvents;
    private CombinedLiveData<List<BudgetDateTimeEvent>,List<BudgetDateTimeEvent>, List<BudgetDateTimeEvent>> statBudgetEvents;

    private LiveData<List<DTEventEWithSubDTEvents>> allDTEventsWithSubs;

    private LiveData<List<Frog>> showingFrogs;

    private LiveData<List<Frog>> nonCompletedFrogs;

    private LiveData<List<Frog>> FreeFrogs;

    private LiveData<List<SoftDateTimeEvent>> SDTEofUnknownWeek;
    private LiveData<List<SoftDateTimeEvent>> SDTEofWeek;
    private LiveData<List<SoftDateTimeEvent>> SDTEofUnknownMonth;
    private LiveData<List<SoftDateTimeEvent>> SDTEofMonth;

    private CombinedLiveData<List<SoftDateTimeEvent>, List<SoftDateTimeEvent>, List<SoftDateTimeEvent>> allSDTEWeek;
    private CombinedLiveData<List<SoftDateTimeEvent>, List<SoftDateTimeEvent>, List<SoftDateTimeEvent>> allSDTEMonth;

    private LiveData<List<BudgetDateTimeEvent>> BDTEofUnknownWeek;
    private LiveData<List<BudgetDateTimeEvent>> BDTEofWeek;
    private LiveData<List<BudgetDateTimeEvent>> BDTEofUnknownMonth;
    private LiveData<List<BudgetDateTimeEvent>> BDTEofMonth;

    private CombinedLiveData<List<BudgetDateTimeEvent>, List<BudgetDateTimeEvent>, List<BudgetDateTimeEvent>> allBDTEWeek;
    private CombinedLiveData<List<BudgetDateTimeEvent>, List<BudgetDateTimeEvent>, List<BudgetDateTimeEvent>> allBDTEMonth;

    private LiveData<List<HardDateTimeEvent>> HDTEofUnknownWeek;
    private LiveData<List<HardDateTimeEvent>> HDTEofWeek;
    private LiveData<List<HardDateTimeEvent>> HDTEofUnknownMonth;
    private LiveData<List<HardDateTimeEvent>> HDTEofMonth;

    private CombinedLiveData<List<HardDateTimeEvent>, List<HardDateTimeEvent>, List<HardDateTimeEvent>> allHDTEWeek;
    private CombinedLiveData<List<HardDateTimeEvent>, List<HardDateTimeEvent>, List<HardDateTimeEvent>> allHDTEMonth;


    private LiveData<List<Goal>> allGoals;



    @RequiresApi(api = Build.VERSION_CODES.N)
    public EventViewModel(@NonNull Application application) {
        super(application);

        LocalDate start = new LocalDate().withDayOfWeek(DateTimeConstants.MONDAY);
        LocalDate end = new LocalDate().withDayOfWeek(DateTimeConstants.SUNDAY);

        LocalDate startMonth = new LocalDate().dayOfMonth().withMinimumValue();
        LocalDate endMonth = new LocalDate().dayOfMonth().withMaximumValue();

        eventRepository = new EventRepository(application);
        allFrogs = eventRepository.getAllFrogs();

        nonCompletedFrogs = eventRepository.getNonCompletedFrogs();

        FreeFrogs = eventRepository.getFreeFrogs();

        allElephants = eventRepository.getAllElephants();
        allSteaks = eventRepository.getAllSteaks();
        allElephantsWithSteaks = eventRepository.getAllElephantsWithSteaks();

        allEvents = eventRepository.getAllEvents();
        allKairosesWithEvents = eventRepository.getAllKairosesWithEvents();
        allEventsWithKairoses = eventRepository.getAllEventsWithKairoses();
        allCrossRed = eventRepository.getAllCrossRed();
        allKairos = eventRepository.getKairoses();

        allDateWithSteaks = eventRepository.getAllDateWithSteaks();

        allStakeViews = eventRepository.getSteaksWithCompleteness();

        /*showingStakeView = Transformations.switchMap(
                showingDateEvent,
                date -> eventRepository.getSteaksWithCompletenessByDate(date)
        );*/

        /*showingSteaks = Transformations.switchMap(
                showingDateEvent,
                date -> eventRepository.getSteaksByDate(date)
        );*/

        /*showingFrogs = Transformations.switchMap(
                showingDateEvent,
                date -> eventRepository.getFrogsByDate(date)
        );*/

        trigger = new CustomLiveData(showingDateEvent, dateEnd);

        showingFrogs = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second !=null)
                return eventRepository.getFrogsByDate(value.first, value.second);
            return null;
        });

        showingStakeView = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second != null)
                return eventRepository.getSteaksWithCompletenessByDatePeriod(value.first, value.second);
            return null;
        });

        showingSteaks = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second != null)
                return eventRepository.getSteaksByDate(value.first, value.second);
            return null;
        });

        showingEvents = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second !=null)
                return eventRepository.getEventsByDate(value.first, value.second);
            return null;
        });


        ////////////////////////////////////////////////////////////////////

        allHardDateTimeEvents = eventRepository.getAllHardDateTimeEvents();

        showingDateHardEvents = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second != null)
                return eventRepository.getHardDateTimeEventsByDatePeriod(value.first, value.second);
            return null;
        });


        /*showingDateHardEvents = Transformations.switchMap(
                showingDateEvent,
                date -> eventRepository.getHardDateTimeEventsByDate(date)
        );*/

        HDTEofUnknownWeek = eventRepository.getHDTEofUnknownWeek();
        HDTEofWeek = eventRepository.getHDTEbyPeriod(start.toDate().getTime(), end.toDate().getTime());

        allHDTEWeek = new CombinedLiveData<>(HDTEofWeek, HDTEofUnknownWeek, (hardDateTimeEvents, hardDateTimeEvents2) -> {
            List<HardDateTimeEvent> all = new ArrayList<>();
            if (hardDateTimeEvents != null)
                all.addAll(hardDateTimeEvents);
            if (hardDateTimeEvents2 != null)
                all.addAll(hardDateTimeEvents2);
            return all;
        });


        HDTEofUnknownMonth = eventRepository.getHDTEofUnknownMonth();
        HDTEofMonth = eventRepository.getHDTEbyPeriod(startMonth.toDate().getTime(), endMonth.toDate().getTime());

        allHDTEMonth = new CombinedLiveData<>(HDTEofMonth, HDTEofUnknownMonth, (hardDateTimeEvents, hardDateTimeEvents2) -> {
            List<HardDateTimeEvent> all = new ArrayList<>();
            if (hardDateTimeEvents != null)
                all.addAll(hardDateTimeEvents);
            if (hardDateTimeEvents2 != null)
                all.addAll(hardDateTimeEvents2);
            return all;
        });



        allSoftDateTimeEvents = eventRepository.getAllSoftDateTimeEvents();

        showingDateSoftEvents = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second != null)
                return eventRepository.getSDTEbyPeriod(value.first, value.second);
            return null;
        });

        /*showingDateSoftEvents = Transformations.switchMap(
                showingDateEvent,
                date -> eventRepository.getSoftDateTimeEventsByDate(date)
        );*/

        SDTEofUnknownWeek = eventRepository.getSDTEofUnknownWeek();
        SDTEofWeek = eventRepository.getSDTEbyPeriod(start.toDate().getTime(), end.toDate().getTime());

        allSDTEWeek = new CombinedLiveData<>(SDTEofWeek, SDTEofUnknownWeek, (softDateTimeEvents, softDateTimeEvents2) -> {
            List<SoftDateTimeEvent> all = new ArrayList<>();
            if (softDateTimeEvents != null)
                all.addAll(softDateTimeEvents);
            if (softDateTimeEvents2 != null)
                all.addAll(softDateTimeEvents2);
            return all;
        });


        SDTEofUnknownMonth = eventRepository.getSDTEofUnknownMonth();
        SDTEofMonth = eventRepository.getSDTEbyPeriod(startMonth.toDate().getTime(), endMonth.toDate().getTime());

        allSDTEMonth = new CombinedLiveData<>(SDTEofMonth, SDTEofUnknownMonth, (softDateTimeEvents, softDateTimeEvents2) -> {
            List<SoftDateTimeEvent> all = new ArrayList<>();
            if (softDateTimeEvents != null)
                all.addAll(softDateTimeEvents);
            if (softDateTimeEvents2 != null)
                all.addAll(softDateTimeEvents2);
            return all;
        });




        allBudgetDateTimeEvents = eventRepository.getAllBudgetDateTimeEvents();

        showingDateBudgetEvents = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second != null)
                return eventRepository.getBDTEbyPeriod(value.first, value.second);
            return null;
        });

        /*showingDateBudgetEvents = Transformations.switchMap(
                showingDateEvent,
                date -> eventRepository.getBudgetDateTimeEventsByDate(date)
        );*/

        allDTEventsWithSubs = eventRepository.getAllDTEventsWithSubs();


        BDTEofUnknownWeek = eventRepository.getBDTEofUnknownWeek();

        BDTEofWeek = eventRepository.getBDTEbyPeriod(start.toDate().getTime(), end.toDate().getTime());

        allBDTEWeek = new CombinedLiveData<>(BDTEofWeek, BDTEofUnknownWeek, (budgetDateTimeEvents, budgetDateTimeEvents2) -> {
            List<BudgetDateTimeEvent> all = new ArrayList<>();
            if (budgetDateTimeEvents != null)
                all.addAll(budgetDateTimeEvents);
            if (budgetDateTimeEvents2 != null)
                all.addAll(budgetDateTimeEvents2);
            return all;
        });

        BDTEofUnknownMonth = eventRepository.getBDTEofUnknownMonth();
        BDTEofMonth = eventRepository.getBDTEbyPeriod(startMonth.toDate().getTime(), endMonth.toDate().getTime());

        allBDTEMonth = new CombinedLiveData<>(BDTEofMonth, BDTEofUnknownMonth, (budgetDateTimeEvents, budgetDateTimeEvents2) -> {
            List<BudgetDateTimeEvent> all = new ArrayList<>();
            if (budgetDateTimeEvents != null)
                all.addAll(budgetDateTimeEvents);
            if (budgetDateTimeEvents2 != null)
                all.addAll(budgetDateTimeEvents2);
            return all;
        });


        //transDateHardEvents = Transformations.switchMap(
        //        showingDateEvent,
        //        date -> eventRepository.getHardDateTimeTransEventsByDate(date)
        //);

        transDateHardEvents = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second != null)
                return eventRepository.getHardDateTimeTransEventsByDate(value.first, value.second);
            return null;
        });

        statHardEvents = new CombinedLiveData<>(showingDateHardEvents, transDateHardEvents, (hardDateTimeEvents2, hardDateTimeEvents) -> {
            List<HardDateTimeEvent> all = new ArrayList<>();
            if (hardDateTimeEvents != null)
                all.addAll(hardDateTimeEvents);
            if (hardDateTimeEvents2 != null)
                all.addAll(hardDateTimeEvents2);
            return all;
        });

        transDateSoftEvents = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second != null)
                return eventRepository.getSoftDateTimeTransEventsByDate(value.first, value.second);
            return null;
        });

        statSoftEvents = new CombinedLiveData<>(showingDateSoftEvents, transDateSoftEvents, (softDateTimeEvents, softDateTimeEvents2) -> {
            List<SoftDateTimeEvent> all = new ArrayList<>();
            if (softDateTimeEvents != null)
                all.addAll(softDateTimeEvents);
            if (softDateTimeEvents2 != null)
                all.addAll(softDateTimeEvents2);
            return all;
        });

        transDateBudgetEvents = Transformations.switchMap(trigger, value -> {
            if (value.first != null && value.second != null)
                return eventRepository.getBudgetDateTimeTransEventsByDate(value.first, value.second);
            return null;
        });

        statBudgetEvents = new CombinedLiveData<>(showingDateBudgetEvents, transDateBudgetEvents, (budgetDateTimeEvents, budgetDateTimeEvents2) -> {
            List<BudgetDateTimeEvent> all = new ArrayList<>();
            if (budgetDateTimeEvents != null)
                all.addAll(budgetDateTimeEvents);
            if (budgetDateTimeEvents2 != null)
                all.addAll(budgetDateTimeEvents2);
            return all;
        });


        ///////////

        allGoals = eventRepository.getAllGoals();



        //allSDTEWeek.addSource(SDTEofWeek, value -> allSDTEWeek.setValue(value));
        //allSDTEWeek.addSource(SDTEofUnknownWeek, value -> allSDTEWeek.setValue(value));


    }

    public LiveData<List<Frog>> getAllFrogs(){
        return allFrogs;
    }

    public void insertFrog(Frog event) {eventRepository.insert(event);}

    public void deleteAllFrogs() {
        eventRepository.deleteAllFrogs();
    }

    public void delete(Frog frog){
        eventRepository.delete(frog);
    }

    public void updateFrog(Frog event){eventRepository.updateFrog(event);}

    public LiveData<List<Frog>> getShowingFrogs(){
        return showingFrogs;
    }


    public LiveData<List<Frog>> getNonCompletedFrogs()
    {
        return nonCompletedFrogs;
    }

    public LiveData<List<Frog>> getFreeFrogs(){
        return FreeFrogs;
    }
    ////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<Elephant>> getAllElephants(){
        return allElephants;
    }

    public LiveData<List<ElephantWithSteaks>> getAllElephantsWithSteaks(){
        return allElephantsWithSteaks;
    }

    public void insertElephant(Elephant event) {eventRepository.insert(event);}

    public void deleteAllElephants() {
        eventRepository.deleteAllElephants();
    }

    public void updateElephant(Elephant event){eventRepository.updateElephant(event);}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertElephantWithSteaks(ElephantWithSteaks elephantWithSteaks) {
        eventRepository.insert(elephantWithSteaks);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteElephant(int elephantId){
        eventRepository.deleteElephant(elephantId);
    }

    void delete(Elephant event){
        eventRepository.delete(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(Date date, Elephant elephant){
        eventRepository.insert(date, elephant);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delete(Date date, Elephant elephant){
        eventRepository.delete(date, elephant);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteDSCRabove(long date, int id){
        eventRepository.deleteDSCRabove(date, id);
    }

    public void delete(DateSteakCrossRef dateSteakCrossRef){
        eventRepository.delete(dateSteakCrossRef);
    }

    ///////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<Steak>> getAllSteaks(){
        return allSteaks;
    }

    public void deleteAllElephantsWithSteaks() {
        eventRepository.deleteAllElephantsWithSteaks();
    }

    public void insertSteak(Steak steak){
        eventRepository.insert(steak);
    }

    public LiveData<List<Steak>> getShowingSteaks(){
        return showingSteaks;
    }

    /*public void insertSteaksForElephant(Elephant elephant, List<Steak> steaks){
        eventRepository.insertSteaksForElephant(elephant, steaks);
    }*/

    public void deleteSteaksOfElephant(int id){
        eventRepository.deleteSteaksOfElephant(id);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteElephantWithSteaks(ElephantWithSteaks elephantWithSteaks){
        eventRepository.deleteElephantWithSteaks(elephantWithSteaks);
    }
    /////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<Event>> getAllEvents(){
        return allEvents;
    }

    public LiveData<List<Kairos>> getAllKairoses(){
        return allKairos;
    }

    public LiveData<List<KairosWithEvents>> getAllKairosesWithEvents(){
        return allKairosesWithEvents;
    }

    public LiveData<List<EventsWithKairos>> getAllEventsWithKairoses(){
        return allEventsWithKairoses;
    }

    public LiveData<List<KairosEventCrossRef>> getAllCrossRed(){
        return allCrossRed;
    }

    public LiveData<List<Kairos>> getAllKairos(){
        return allKairos;
    }

    public void insert(KairosEventCrossRef crossRef){
        eventRepository.insert(crossRef);
    }

    public void insert(Kairos kairos){
        eventRepository.insert(kairos);
    }

    public void insert(Event event){
        eventRepository.insert(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertKairosWithEvents(Kairos kairos, List<Event> events) {
        eventRepository.insert(kairos, events);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertEventWithKairoses(Event event, List<Kairos> kairoses) {
        eventRepository.insertE(event, kairoses);
    }

    public LiveData<List<Event>> getShowingEvents(){
        return showingEvents;
    }


    public void insert(int kairosId, Event event){
        eventRepository.insert(kairosId, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteEventById(int eventId){
        eventRepository.deleteEventById(eventId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteKairosById(int kairodId) {eventRepository.deleteKairosById(kairodId);}

    public void deleteAllKairosesAndEvents(){
        eventRepository.deleteAllKairosesAndEvents();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<DateWithSteaks>> getAllDateWithSteaks(){
        return allDateWithSteaks;
    }

    public void insertDateSteakCrossRef(Date date, Steak steak, Boolean isComplete){
        eventRepository.insertDateSteakCrossRef(date, steak, isComplete);
    }

    public void insert(DateSteakCrossRef dateSteakCrossRef){
        eventRepository.insert(dateSteakCrossRef);
    }

    public LiveData<List<SteakView>> getSteaksWithCompleteness(){
        return allStakeViews;
    }

    public LiveData<List<SteakView>> getSteaksWithCompletenessByDate(){
        return showingStakeView;
    }

    //public void setDate(Long date) {
    //    showingDateStake.setValue(date);
    //}



    //@RequiresApi(api = Build.VERSION_CODES.N)
    //public void initSteaksForDate(Date date){
    //    eventRepository.initSteaksForDate(date);
    //}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initSteaksForDatePeriod(Long dateStart, Long dateEnd){
        eventRepository.initSteaksForDate(dateStart, dateEnd);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteDateSteakCrossRefsBySteak(int id){
        eventRepository.deleteDateSteakCrossRefsBySteak(id);
    }

    ////////////////////////////////////////////////////////////////////////////

    public void setShowingDateEvents(Long date){
        showingDateEvent.setValue(date);
    }


    public void setShowingDateEventsEnd(Long date){
        dateEnd.setValue(date);
    }

    /*public void setShowingDateEventsPeriod(Long startDate, Long endDate){
        android.util.Pair<Long, Long> pair = new Pair<>(startDate, endDate);
        showingDateEventPeriod.setValue(pair);
        //showingDateEvent.setValue(date);

    }*/




    public LiveData<List<HardDateTimeEvent>> getAllHardDateTimeEvents(){
        return allHardDateTimeEvents;
    }

    public LiveData<List<HardDateTimeEvent>> getShowingDateHardEvents(){
        return showingDateHardEvents;
    }

    public void insert(HardDateTimeEvent hardDateTimeEvent){
        eventRepository.insert(hardDateTimeEvent);
    }

    public void insertHardDateTimeEvent(HardDateTimeEvent hardDateTimeEvent){
        eventRepository.insertHardDateTimeEvent(hardDateTimeEvent);
    }

    public void deleteAllHardDateTimeEvents() {
        eventRepository.deleteAllHardDateTimeEvents();
    }


    public MediatorLiveData<List<HardDateTimeEvent>> getAllHDTEWeek(){
        return allHDTEWeek;
    }

    public MediatorLiveData<List<HardDateTimeEvent>> getAllHDTEMonth(){
        return allHDTEMonth;
    }

    public void delete(HardDateTimeEvent hardDateTimeEvent){
        eventRepository.delete(hardDateTimeEvent);
    }



    public LiveData<List<SoftDateTimeEvent>> getAllSoftDateTimeEvents(){
        return allSoftDateTimeEvents;
    }

    public LiveData<List<SoftDateTimeEvent>> getShowingDateSoftEvents(){
        return showingDateSoftEvents;
    }

    public void insert(SoftDateTimeEvent softDateTimeEvent){
        eventRepository.insert(softDateTimeEvent);
    }

    public void insertSoftDateTimeEvent(SoftDateTimeEvent softDateTimeEvent){
        eventRepository.insertSoftDateTimeEvent(softDateTimeEvent);
    }

    public void deleteAllSoftDateTimeEvents() {
        eventRepository.deleteAllSoftDateTimeEvents();
    }

    public LiveData<List<SoftDateTimeEvent>> getSDTEofUnknownWeek(){
        return SDTEofUnknownWeek;
    }

    public LiveData<List<SoftDateTimeEvent>> getSDTEofWeek(){
        return SDTEofWeek;
    }

    public MediatorLiveData<List<SoftDateTimeEvent>> getAllSDTEWeek(){
        return allSDTEWeek;
    }

    public MediatorLiveData<List<SoftDateTimeEvent>> getAllSDTEMonth(){
        return allSDTEMonth;
    }

    public void delete(SoftDateTimeEvent softDateTimeEvent){
        eventRepository.delete(softDateTimeEvent);
    }


    public LiveData<List<BudgetDateTimeEvent>> getAllBudgetDateTimeEvents(){
        return allBudgetDateTimeEvents;
    }

    public LiveData<List<BudgetDateTimeEvent>> getShowingDateBudgetEvents(){
        return showingDateBudgetEvents;
    }

    public void insert(BudgetDateTimeEvent budgetDateTimeEvent){
        eventRepository.insert(budgetDateTimeEvent);
    }

    public void insertBudgetDateTimeEvent(BudgetDateTimeEvent budgetDateTimeEvent){
        eventRepository.insertBudgetDateTimeEvent(budgetDateTimeEvent);
    }

    public void deleteAllBudgetDateTimeEvents() {
        eventRepository.deleteAllBudgetDateTimeEvents();
    }

    public MediatorLiveData<List<BudgetDateTimeEvent>> getAllBDTEWeek(){
        return allBDTEWeek;
    }

    public MediatorLiveData<List<BudgetDateTimeEvent>> getAllBDTEMonth(){
        return allBDTEMonth;
    }

    public void delete(BudgetDateTimeEvent budgetDateTimeEvent){
        eventRepository.delete(budgetDateTimeEvent);
    }

    ////////////////////////////////////////////////////////////////////////////////////

    public LiveData<List<DTEventEWithSubDTEvents>> getAllDTEventsWithSubs(){
        return allDTEventsWithSubs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(DTEventEWithSubDTEvents dtEventEWithSubDTEvents){
        eventRepository.insert(dtEventEWithSubDTEvents);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeSubEvent(SubDateTimeEvent subDateTimeEvent){
        eventRepository.changeSubEvent(subDateTimeEvent);
    }

    ////////////////////////////////////////////////////////////////////////////////////

    public void insert(LogsHDTE logsHDTE){
        eventRepository.insert(logsHDTE);
    }

    public MediatorLiveData<List<HardDateTimeEvent>> getAllStatHDTE(){
        return statHardEvents;
    }

    public MediatorLiveData<List<SoftDateTimeEvent>> getAllStatSDTE(){
        return statSoftEvents;
    }

    public MediatorLiveData<List<BudgetDateTimeEvent>> getAllStatBDTE(){
        return statBudgetEvents;
    }

    ////////////////////////////////////////////////////////////////////////////

    public LiveData<List<Goal>> getAllGoals(){
        return allGoals;
    }

    public void insert(Goal goal) {eventRepository.insert(goal);}

    public void delete(Goal goal){
        eventRepository.delete(goal);
    }

    public void update(Goal goal){eventRepository.updateGoal(goal);}

}
