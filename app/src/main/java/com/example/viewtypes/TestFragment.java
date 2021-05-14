package com.example.viewtypes;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viewtypes.ui.RecyclerViewClickInterface;
import com.example.viewtypes.ui.home.AnimalFragmentDirections;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment implements RecyclerViewClickInterface {

    EventViewModel eventViewModel;

    List<Frog> nonCompletenessFrogs;

    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMMM");
    DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm");
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    CharSequence show[] = {"Дата", "Месяц", "Год"};

    private MaterialToolbar topBar;

    public ActionMode action_Mode = null;

    EventAdapter_Test adapterPlan;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private long showingDate;
    private Date showing;

    private long startTime;
    private long endTime;

    public static final int RV_FROG = 3;
    public static final int RV_STEAKS = 4;
    public static final int RV_PLAN = 5;

    ExRecyclerView rvPlan;

    public TestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        showingDate = new LocalDate().toDate().getTime();
        showing = new Date();
        showing.date = showingDate;
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test, container, false);

        TextView tv = view.findViewById(R.id.textView);

        PlanViewModel planViewModel = new ViewModelProvider(getActivity()).get(PlanViewModel.class);

        planViewModel.getDate().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                showingDate = aLong;

                //eventViewModel.setShowingDateEvents(showingDate);
                eventViewModel.setShowingDateEvents(showingDate);
                eventViewModel.setShowingDateEventsEnd(showingDate);

                Date date = new Date();
                date.date = showingDate;

                showing.date = showingDate;


                eventViewModel.initSteaksForDatePeriod(date.date,date.date);

            }
        });


        /*empty_frog_view.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(final View view) {

                List<String> titles = new ArrayList<>();
                nonCompletenessFrogs.forEach(i -> {
                    titles.add(i.title);
                });


                final String[] fr = titles.toArray(new String[0]);

                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Выберите лягушку дня");
                builder.setSingleChoiceItems(fr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Frog buffer = nonCompletenessFrogs.get(i);
                        if (buffer != null){
                            buffer.date = showingDate;
                            eventViewModel.updateFrog(buffer);
                            //Log.d("PEPEL", viewModel.getDayFrog().getValue().getTitle());
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Добавить новое", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //PlanFragmentDirections.ActionNavigationPlanToNavigationEdit action = PlanFragmentDirections.actionNavigationPlanToNavigationEdit();
                        Navigation.findNavController(requireParentFragment().requireParentFragment().getView()).navigate(R.id.navigation_edit);
                    }
                });
                builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });*/


        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.setShowingDateEvents(showingDate);
        eventViewModel.setShowingDateEventsEnd(showingDate);

        Date date = new Date();
        date.date = showingDate;




        eventViewModel.initSteaksForDatePeriod(date.date,date.date);



        eventViewModel.getNonCompletedFrogs().observe(getViewLifecycleOwner(), new Observer<List<Frog>>() {
            @Override
            public void onChanged(List<Frog> frogs) {
                nonCompletenessFrogs = frogs;

            }
        });


        rvPlan = view.findViewById(R.id.rvPlan);

        adapterPlan = new EventAdapter_Test(getContext(), RV_PLAN);
        rvPlan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlan.setAdapter(adapterPlan);
        adapterPlan.setOnClickListeners(this);


        startTime = new LocalTime("8:00").toDateTimeToday().getMillis();
        endTime = new LocalTime("22:00").toDateTimeToday().getMillis();

        //adapterPlan.setHourMin(startTime);
        //adapterPlan.setHourMax(endTime);
        adapterPlan.initPlaceHolders();


        planViewModel.getTimeMin().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                adapterPlan.setHourMin(aLong);
                startTime = aLong;
                adapterPlan.updateHours();
                adapterPlan.initPlaceHolders();
            }
        });

        planViewModel.getTimeMax().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                adapterPlan.setHourMax(aLong);
                endTime = aLong;

                adapterPlan.updateHours();
                adapterPlan.initPlaceHolders();
            }
        });

        eventViewModel.getShowingDateHardEvents().observe(getViewLifecycleOwner(), new Observer<List<HardDateTimeEvent>>() {
            @Override
            public void onChanged(List<HardDateTimeEvent> hardDateTimeEvents) {
                adapterPlan.setValues(adapterPlan.setItems(hardDateTimeEvents, ItemTypeInterfaсe.EVENT_HARD_DTE));
                adapterPlan.updateHours();
                adapterPlan.initPlaceHolders();
            }
        });

        eventViewModel.getShowingDateSoftEvents().observe(getViewLifecycleOwner(), new Observer<List<SoftDateTimeEvent>>() {
            @Override
            public void onChanged(List<SoftDateTimeEvent> softDateTimeEvents) {
                adapterPlan.setValues(adapterPlan.setItems(softDateTimeEvents, ItemTypeInterfaсe.EVENT_SOFT_DTE));
                adapterPlan.updateHours();
                adapterPlan.initPlaceHolders();
            }
        });

        eventViewModel.getShowingDateBudgetEvents().observe(getViewLifecycleOwner(), new Observer<List<BudgetDateTimeEvent>>() {
            @Override
            public void onChanged(List<BudgetDateTimeEvent> budgetDateTimeEvents) {
                adapterPlan.setValues(adapterPlan.setItems(budgetDateTimeEvents, ItemTypeInterfaсe.EVENT_BUDGET_DTE));
                adapterPlan.updateHours();
                adapterPlan.initPlaceHolders();
            }
        });

        initDragnDrop();

        /*Button btnChangeTime = view.findViewById(R.id.btnStartTime);
        btnChangeTime.setText(timeFormat.print(startTime));
        btnChangeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker.Builder ma = new MaterialTimePicker.Builder();
                ma.setTimeFormat(TimeFormat.CLOCK_24H);

                MaterialTimePicker materialTimePicker = ma.build();
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startTime = new LocalTime(materialTimePicker.getHour(), materialTimePicker.getMinute()).toDateTimeToday().getMillis();
                        adapterPlan.setHourMin(startTime);
                        btnChangeTime.setText(timeFormat.print(startTime));
                    }
                });
                materialTimePicker.show(getActivity().getSupportFragmentManager(), "TIME_PICKER");
            }
        });*/

        return view;
    }

    private void initDragnDrop(){

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, 0) {

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
                if (target.getAdapterPosition() != 0 && target.getAdapterPosition() != adapterPlan.getItemCount()-1){
                    return true;
                }

                return false;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Log.d("onMove", String.valueOf(recyclerView.getX()));

                adapterPlan.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags =  ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags =  ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                adapterPlan.notifyItemChanged(viewHolder.getAdapterPosition());


                ItemTypeInterfaсe T = adapterPlan.get(viewHolder.getAdapterPosition());
                if (T instanceof BudgetDateTimeEvent){

                    /*if (!(adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1) instanceof Placeholder)){
                        if (adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1) instanceof HardDateTimeEvent){
                            Period period = new Period(adapterPlan.getTimeAbove(viewHolder.getAdapterPosition()), adapterPlan.getTimeBelow(viewHolder.getAdapterPosition()));
                            Period eventPeriod = new Period(((BudgetDateTimeEvent) T).duration);

                            ((BudgetDateTimeEvent)T).timeE = adapterPlan.getTimeBelow(viewHolder.getAdapterPosition());
                            ((BudgetDateTimeEvent)T).timeS = new LocalTime(((SoftDateTimeEvent) T).timeE).minusMinutes(5).toDateTimeToday().getMillis();
                        }
                    }
                    else{
                        ((BudgetDateTimeEvent)T).timeS = adapterPlan.getTimeAbove(viewHolder.getAdapterPosition());
                        ((BudgetDateTimeEvent)T).timeE = new LocalTime(((SoftDateTimeEvent) T).timeS).plusMinutes(5).toDateTimeToday().getMillis();
                    }*/

                    eventViewModel.insert((BudgetDateTimeEvent) T);
                    Log.d("ВУХАВУХА", timeFormat.print(((BudgetDateTimeEvent) T).timeS));
                    Log.d("ВУХАВУХА", timeFormat.print(((BudgetDateTimeEvent) T).timeE));
                }
                else if (T instanceof SoftDateTimeEvent){

                    if (!(adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1) instanceof Placeholder)){
                        if (adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1) instanceof HardDateTimeEvent){
                            ((SoftDateTimeEvent)T).timeE = adapterPlan.getTimeBelow(viewHolder.getAdapterPosition());
                            ((SoftDateTimeEvent)T).timeS = new LocalTime(((SoftDateTimeEvent) T).timeE).minusMinutes(5).toDateTimeToday().getMillis();
                        }
                    }
                    else{
                        ((SoftDateTimeEvent)T).timeS = adapterPlan.getTimeAbove(viewHolder.getAdapterPosition());
                        ((SoftDateTimeEvent)T).timeE = new LocalTime(((SoftDateTimeEvent) T).timeS).plusMinutes(5).toDateTimeToday().getMillis();
                    }

                    /*if (adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1) instanceof HardDateTimeEvent){
                        if (!(adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1) instanceof HeaderItem)){
                            if (!(adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1) instanceof Placeholder)){

                            }
                        }
                    }
                    else{
                        ((SoftDateTimeEvent)T).timeS = adapterPlan.getTimeAbove(viewHolder.getAdapterPosition());
                        ((SoftDateTimeEvent)T).timeE = new LocalTime(((SoftDateTimeEvent) T).timeS).plusMinutes(5).toDateTimeToday().getMillis();
                    }*/

                    /*if (adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1) instanceof HardDateTimeEvent &&
                            !(adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1) instanceof HeaderItem &&
                                    ((HardDateTimeEvent) adapterPlan.getItems().get(viewHolder.getAdapterPosition()+1)).getType() != ItemTypeInterfaсe.PLACEHOLDER)){

                    }
                    else{

                    }*/


                    eventViewModel.insert((SoftDateTimeEvent) T);
                    Log.d("ВУХАВУХА", ((SoftDateTimeEvent) T).title);
                }
                else if (T instanceof HardDateTimeEvent){
                    ((HardDateTimeEvent)T).timeS = adapterPlan.getTimeAbove(viewHolder.getAdapterPosition());
                    ((HardDateTimeEvent)T).timeE = adapterPlan.getTimeBelow(viewHolder.getAdapterPosition());

                    eventViewModel.insert((HardDateTimeEvent) T);
                    Log.d("ВУХАВУХА", ((HardDateTimeEvent) T).title);
                }


                //showingDatas.get(viewHolder.getAdapterPosition()).getEventItem().setTimeS(dta.getStartTimebyHeader(viewHolder.getAdapterPosition()));
                //showingDatas.get(viewHolder.getAdapterPosition()).getEventItem().setTimeE(dta.getEndTimebyHeader(viewHolder.getAdapterPosition()));

                //((ItemHolder)viewHolder).cvItem.setCardBackgroundColor(MaterialColors.getColor(getView(), R.attr.colorSurface));
               // Log.d("FULLPLS", Boolean.toString(dta.hourIsBusy(0)));
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
                    //if (viewHolder instanceof ItemHolder){
                       // ((ItemHolder)viewHolder).cvItem.setCardBackgroundColor(Color.LTGRAY);
                        //dta.initPlaceholders();
                    //}
                super.onSelectedChanged(viewHolder,actionState);
            }
        };



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);


        itemTouchHelper.attachToRecyclerView(rvPlan);

    }

    private List<Boolean> InitSelected(int size){
        List<Boolean> result = new ArrayList<>();
        for (int i = 0; i < size; i++){
            result.add(Boolean.FALSE);
        }
        return result;
    }
    private boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
    }

    @Override
    public void onItemFrogClick(Frog frogItem, int TAG) {
        PlanFragmentDirections.ActionNavigationPlanToNavigationEdit action = PlanFragmentDirections.actionNavigationPlanToNavigationEdit();
        action.setFrog(frogItem);

        Navigation.findNavController(requireParentFragment().requireParentFragment().getView()).navigate(action);
    }

    @Override
    public void onItemElephantClick(ElephantWithSteaks elephantItem, int TAG) {

    }

    @Override
    public void onItemSteakClick(Steak steakItem, int TAG) {
        PlanFragmentDirections.ActionNavigationPlanToEditSteakFragment action = PlanFragmentDirections.actionNavigationPlanToEditSteakFragment(steakItem);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void onItemFrogLongClick(Frog frogItem, int TAG) {

    }

    @Override
    public void onItemElephantLongClick(ElephantWithSteaks elephantItem, int TAG) {

    }

    @Override
    public void onItemSteakLongClick(Steak steakItem, int TAG) {

    }

    @Override
    public void onItemFrogCompleteClick(Frog frogItem, int TAG) {
        frogItem.isCompleted = !frogItem.isCompleted;
        eventViewModel.updateFrog(frogItem);
    }

    @Override
    public void onItemElephantCompleteClick(ElephantWithSteaks elephantItem, int TAG) {

    }

    @Override
    public void onItemSteakCompleteClick(Steak steakItem, int TAG) {

    }

    @Override
    public void onItemFrogContextClick(Frog frogItem, View view, int TAG) {
        showMenu(view, frogItem);
    }

    @Override
    public void onItemKairosWithEventsClick(KairosWithEvents kairosWithEvents, int TAG) {

    }

    @Override
    public void onItemEventClick(Event event, int TAG) {

    }

    @Override
    public void onItemKairosWithEventsLongClick(KairosWithEvents kairosWithEvents, int TAG) {

    }

    @Override
    public void onItemEventLongClick(Event event, int TAG) {

    }

    @Override
    public void onItemEventCompleteClick(Event event, int TAG) {

    }

    @Override
    public void onItemDateSteakClick(SteakView steakView, int TAG) {
        PlanFragmentDirections.ActionNavigationPlanToEditSteakFragment action = PlanFragmentDirections.actionNavigationPlanToEditSteakFragment(steakView.steak);
        Navigation.findNavController(requireParentFragment().requireParentFragment().getView()).navigate(action);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemDateSteakCompleteClick(SteakView steakView, int TAG) {


        if (steakView.steak.days.isEmpty() || !steakView.steak.days.contains(new LocalDate(showing.date).getDayOfWeek())){
            steakView.steak.isCompleted = !steakView.steak.isCompleted;
            steakView.isDailyCompleted = steakView.steak.isCompleted;



            if (!steakView.steak.isCompleted){
                eventViewModel.deleteDateSteakCrossRefsBySteak(steakView.steak.id);
                steakView.steak.date = 0;

            }
            eventViewModel.insertSteak(steakView.steak);

        }
        else{
            steakView.isDailyCompleted = !steakView.isDailyCompleted;


            eventViewModel.insertDateSteakCrossRef(showing, steakView.steak, steakView.isDailyCompleted);

            // Если ни одной записи о сделанном ивенте не было в DateSteakCrossRef, то снимать галку IsCompleted со стейка
        }



    }

    @Override
    public void onItemHardDateTimeClick(HardDateTimeEvent hardDateTimeEvent) {
        PlanFragmentDirections.ActionNavigationPlanToEditDTEFragment action = PlanFragmentDirections.actionNavigationPlanToEditDTEFragment();
        action.setHDTE(hardDateTimeEvent);
        Navigation.findNavController(requireParentFragment().requireParentFragment().getView()).navigate(action);
    }

    @Override
    public void onItemSoftDateTimeClick(SoftDateTimeEvent softDateTimeEvent) {
        PlanFragmentDirections.ActionNavigationPlanToEditDTEFragment action = PlanFragmentDirections.actionNavigationPlanToEditDTEFragment();
        action.setSDTE(softDateTimeEvent);
        Navigation.findNavController(requireParentFragment().requireParentFragment().getView()).navigate(action);
    }

    @Override
    public void onItemBudgetDateTimeClick(BudgetDateTimeEvent budgetDateTimeEvent) {
        PlanFragmentDirections.ActionNavigationPlanToEditDTEFragment action = PlanFragmentDirections.actionNavigationPlanToEditDTEFragment();
        action.setBDTE(budgetDateTimeEvent);
        Navigation.findNavController(requireParentFragment().requireParentFragment().getView()).navigate(action);
    }

    @Override
    public void onItemHardDateTimeLongClick(HardDateTimeEvent hardDateTimeEvent, int TAG) {
        //if (action_Mode == null){
        //    action_Mode = getActivity().startActionMode(callback);
        //}
        //action_Mode.setTitle(String.valueOf(adapterPlan.getSelectedItemCount()));
    }

    @Override
    public void onItemBudgetDateTimeLongClick(BudgetDateTimeEvent budgetDateTimeEvent, int TAG) {
        //if (action_Mode == null){
        //    action_Mode = getActivity().startActionMode(callback);
        //}
        //action_Mode.setTitle(String.valueOf(adapterPlan.getSelectedItemCount()));
    }

    @Override
    public void onItemSoftDateTimeLongClick(SoftDateTimeEvent softDateTimeEvent, int TAG) {
        //if (action_Mode == null){
        //    action_Mode = getActivity().startActionMode(callback);
        //}
        //action_Mode.setTitle(String.valueOf(adapterPlan.getSelectedItemCount()));
    }

    @Override
    public void onItemHardDateTimeCompleteClick(HardDateTimeEvent hardDateTimeEvent, int TAG) {
        hardDateTimeEvent.isCompleted = !hardDateTimeEvent.isCompleted;
        eventViewModel.insert(hardDateTimeEvent);
    }

    @Override
    public void onItemSoftDateTimeCompleteClick(SoftDateTimeEvent softDateTimeEvent, int TAG) {
        softDateTimeEvent.isCompleted = !softDateTimeEvent.isCompleted;
        eventViewModel.insert(softDateTimeEvent);
    }

    @Override
    public void onItemBudgetDateTimeCompleteClick(BudgetDateTimeEvent budgetDateTimeEvent, int TAG) {
        budgetDateTimeEvent.isCompleted = !budgetDateTimeEvent.isCompleted;
        eventViewModel.insert(budgetDateTimeEvent);
    }

    @Override
    public void onItemHardDateTimeContextClick(HardDateTimeEvent hardDateTimeEvent, View view, int TAG) {
        showMenu(view, hardDateTimeEvent);
    }

    @Override
    public void onItemSoftDateTimeContextClick(SoftDateTimeEvent softDateTimeEvent, View view, int TAG) {
        showMenu(view, softDateTimeEvent);
    }


    @Override
    public void onItemBudgetDateTimeContextClick(BudgetDateTimeEvent budgetDateTimeEvent, View view, int TAG) {
        showMenu(view, budgetDateTimeEvent);
    }

    @Override
    public void onItemDateTimeEvent(DTEventEWithSubDTEvents dateTimeEvent) {

    }

    @Override
    public void onItemSubDateTimeEvent(SubDateTimeEvent subDateTimeEvent) {

    }

    @Override
    public void onItemGoalClick(Goal goal, int TAG) {

    }

    @Override
    public void onItemGoalExpandClick(Goal goal, int TAG) {

    }

    private <T extends Frog> void showMenu(View v, T object){
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.event_menu_, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                long olddate = object.date;
                switch (item.getItemId()){
                    case (R.id.menu1):
                        object.date= new LocalDate().toDate().getTime();
                        
                        if (object instanceof BudgetDateTimeEvent){
                            eventViewModel.insert((BudgetDateTimeEvent) object);

                            if ( !((BudgetDateTimeEvent)object).isCompleted && olddate != object.date)
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_BUDGET_DTE, object.isCompleted));
                        }
                        else if (object instanceof HardDateTimeEvent){
                            eventViewModel.insert((HardDateTimeEvent) object);

                            if ( !((HardDateTimeEvent)object).isCompleted && olddate != object.date)
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_HARD_DTE, object.isCompleted));
                        }
                        else if (object instanceof SoftDateTimeEvent){
                            eventViewModel.insert((SoftDateTimeEvent) object);

                            if ( !((SoftDateTimeEvent)object).isCompleted && olddate != object.date)
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_SOFT_DTE, object.isCompleted));
                        }
                        else if (object instanceof Frog){
                            eventViewModel.updateFrog(object);
                        }
                        
                        return true;
                    case (R.id.menu2):
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                        MaterialDatePicker CalendarPicker = builder.build();
                        CalendarPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                LocalDate d = new LocalDate(selection);
                                object.date = d.toDate().getTime();

                                if (object instanceof BudgetDateTimeEvent){
                                    eventViewModel.insert((BudgetDateTimeEvent) object);

                                    if ( !((BudgetDateTimeEvent)object).isCompleted )
                                        eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_BUDGET_DTE, object.isCompleted));
                                }
                                else if (object instanceof HardDateTimeEvent){
                                    eventViewModel.insert((HardDateTimeEvent) object);

                                    if ( !((HardDateTimeEvent)object).isCompleted )
                                        eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_HARD_DTE, object.isCompleted));
                                }
                                else if (object instanceof SoftDateTimeEvent){
                                    eventViewModel.insert((SoftDateTimeEvent) object);

                                    if ( !((SoftDateTimeEvent)object).isCompleted )
                                        eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_SOFT_DTE, object.isCompleted));
                                }
                                else if (object instanceof Frog){
                                    eventViewModel.updateFrog(object);
                                }
                                
                            }
                        });

                        CalendarPicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
                        return true;
                    case (R.id.menu3):
                        object.date = -1;

                        if (object instanceof BudgetDateTimeEvent){
                            eventViewModel.insert((BudgetDateTimeEvent) object);

                            if ( !((BudgetDateTimeEvent)object).isCompleted )
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_BUDGET_DTE, object.isCompleted));
                        }
                        else if (object instanceof HardDateTimeEvent){
                            eventViewModel.insert((HardDateTimeEvent) object);

                            if ( !((HardDateTimeEvent)object).isCompleted )
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_HARD_DTE, object.isCompleted));
                        }
                        else if (object instanceof SoftDateTimeEvent){
                            eventViewModel.insert((SoftDateTimeEvent) object);

                            if ( !((SoftDateTimeEvent)object).isCompleted )
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_SOFT_DTE, object.isCompleted));
                        }
                        else if (object instanceof Frog){
                            eventViewModel.updateFrog(object);
                        }
                        
                        return true;
                    case (R.id.menu4):
                        object.date = -2;
                        
                        if (object instanceof BudgetDateTimeEvent){
                            eventViewModel.insert((BudgetDateTimeEvent) object);

                            if ( !((BudgetDateTimeEvent)object).isCompleted )
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_BUDGET_DTE, object.isCompleted));
                        }
                        else if (object instanceof HardDateTimeEvent){
                            eventViewModel.insert((HardDateTimeEvent) object);

                            if ( !((HardDateTimeEvent)object).isCompleted )
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_HARD_DTE, object.isCompleted));
                        }
                        else if (object instanceof SoftDateTimeEvent){
                            eventViewModel.insert((SoftDateTimeEvent) object);

                            if ( !((SoftDateTimeEvent)object).isCompleted )
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_SOFT_DTE, object.isCompleted));
                        }
                        else if (object instanceof Frog){
                            eventViewModel.updateFrog(object);
                        }
                        return true;
                }
                
                return false;
            }
        });

        popupMenu.show();
    }

    private ActionMode.Callback callback = new ActionMode.Callback(){

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.contextual_top_app_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.deleteIcon:
                    //adapterAll.deleteSelected();
                    adapterPlan.getSelectedItems().forEach(i -> {
                        if (i instanceof BudgetDateTimeEvent){
                            eventViewModel.delete((BudgetDateTimeEvent) i);
                        }
                        else if (i instanceof SoftDateTimeEvent){
                            eventViewModel.delete((SoftDateTimeEvent) i);
                        }
                        else if (i instanceof HardDateTimeEvent){
                            eventViewModel.delete((HardDateTimeEvent) i);
                        }
                    });
                    actionMode.finish();
                    Snackbar.make(getView(), "События были удалены", Snackbar.LENGTH_LONG).show();

                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            adapterPlan.clearSelection();
            action_Mode = null;
        }

    };



    /*ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.RIGHT) {
                Frog frog = (Frog) adapterFrog.getItem(position);
                frog.date = 0;
                eventViewModel.updateFrog(frog);
            }

        }
    };*/


    /*private void initDragnDrop(){

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, 0) {

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
                if (target.getAdapterPosition() != 0 && target.getAdapterPosition() != dta.getItemCount()-1)
                    return true;
                return false;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Log.d("onMove", Integer.toString(viewHolder.getAdapterPosition()) + " - " + Integer.toString(target.getAdapterPosition()));
                dta.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags =  viewHolder.getItemViewType() == Constants.ITEM_EVENT_TEXT_VIEWTYPE ? ItemTouchHelper.UP | ItemTouchHelper.DOWN : 0;
                int swipeFlags =  viewHolder.getItemViewType() == Constants.ITEM_EVENT_TEXT_VIEWTYPE ? ItemTouchHelper.START | ItemTouchHelper.END : 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                showingDatas.get(viewHolder.getAdapterPosition()).getEventItem().setTimeS(dta.getStartTimebyHeader(viewHolder.getAdapterPosition()));
                showingDatas.get(viewHolder.getAdapterPosition()).getEventItem().setTimeE(dta.getEndTimebyHeader(viewHolder.getAdapterPosition()));
                dta.notifyItemChanged(viewHolder.getAdapterPosition());


                ((ItemHolder)viewHolder).cvItem.setCardBackgroundColor(MaterialColors.getColor(getView(), R.attr.colorSurface));
                Log.d("FULLPLS", Boolean.toString(dta.hourIsBusy(0)));
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
                    if (viewHolder instanceof ItemHolder){
                        ((ItemHolder)viewHolder).cvItem.setCardBackgroundColor(Color.LTGRAY);
                        //dta.initPlaceholders();
                    }
                super.onSelectedChanged(viewHolder,actionState);
            }
        };



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvPlan);

    }*/

}