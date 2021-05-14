package com.example.viewtypes;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.viewtypes.ui.RecyclerViewClickInterface;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends Fragment implements RecyclerViewClickInterface {
    EventViewModel eventViewModel;
    SubDateTimeEvent subDateTimeEvent;

    List<Frog> nonCompletenessFrogs;

    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMMM");
    DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm");
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    EventAdapter_Test adapterFrog;

    CharSequence show[] = {"Дата", "Месяц", "Год"};

    private MaterialToolbar topBar;

    public ActionMode action_Mode = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private long showingDate;
    private Date showing;

    public static final int RV_FROG = 3;
    public static final int RV_STEAKS = 4;
    public static final int RV_PLAN = 5;

    ExRecyclerView rvPlan;

    public DayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayFragment newInstance(String param1, String param2) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        showingDate = new LocalDate().toDate().getTime();
        showing = new Date();
        showing.date = showingDate;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.daily_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_day_new, container, false);

        MaterialCardView empty_frog_view = view.findViewById(R.id.empty_frog_rv);

        nonCompletenessFrogs = new ArrayList<>();

        empty_frog_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                List<String> titles = new ArrayList<>();
                nonCompletenessFrogs.forEach(i -> {
                    titles.add(i.title);
                });


                final String[] fr = titles.toArray(new String[0]);
                final boolean[] selected = toPrimitiveArray(InitSelected(nonCompletenessFrogs.size()));

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
                builder.show();
            }
        });


        ExRecyclerView rvFrogs = view.findViewById(R.id.rvFrogs);
        rvFrogs.setEmptyView(empty_frog_view);

        eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);

        eventViewModel.setShowingDateEvents(showingDate);

        Date date = new Date();
        date.date = showingDate;

        adapterFrog = new EventAdapter_Test(getContext(), RV_FROG);
        adapterFrog.setOnClickListeners(this);

        rvFrogs.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFrogs.setAdapter(adapterFrog);

        eventViewModel.getShowingFrogs().observe(getViewLifecycleOwner(), new Observer<List<Frog>>() {
            @Override
            public void onChanged(List<Frog> frogs) {
                if(android.os.Debug.isDebuggerConnected())
                    android.os.Debug.waitForDebugger();
                adapterFrog.setValues(adapterFrog.setItems(frogs, ItemTypeInterfaсe.EVENT_FROG));
            }
        });



        eventViewModel.initSteaksForDatePeriod(date.date,date.date);

        MaterialCardView empty_steaks_view = view.findViewById(R.id.empty_steak_rv);
        ExRecyclerView rvSteaks = view.findViewById(R.id.rvSteaks);
        rvSteaks.setEmptyView(empty_steaks_view);

        EventAdapter_Test adapterSteak = new EventAdapter_Test(getContext(), RV_STEAKS);
        rvSteaks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSteaks.setAdapter(adapterSteak);

        eventViewModel.getSteaksWithCompletenessByDate().observe(getViewLifecycleOwner(), new Observer<List<SteakView>>() {
            @Override
            public void onChanged(List<SteakView> steakViews) {
                if(android.os.Debug.isDebuggerConnected())
                    android.os.Debug.waitForDebugger();
                adapterSteak.setValues(adapterSteak.setItems(steakViews, ItemTypeInterfaсe.EVENT_STEAKDATE));
            }
        });

        eventViewModel.getNonCompletedFrogs().observe(getViewLifecycleOwner(), new Observer<List<Frog>>() {
            @Override
            public void onChanged(List<Frog> frogs) {
                nonCompletenessFrogs = frogs;

            }
        });

        FloatingActionButton fab = view.findViewById(R.id.btnNewEvent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_daily_to_editDTEFragment);
            }
        });

        DayFragmentArgs args = DayFragmentArgs.fromBundle(getArguments());
        if (args.getHardDateTimeEvent() != null){
            eventViewModel.insert(args.getHardDateTimeEvent());
        }

        if (args.getBudgetDateTimeEvent() != null){
            eventViewModel.insert(args.getBudgetDateTimeEvent());
        }

        if (args.getSoftDateTimeEvent() != null){
            eventViewModel.insert(args.getSoftDateTimeEvent());
        }

        rvPlan = view.findViewById(R.id.rvPlan);

        EventAdapter_Test adapterPlan = new EventAdapter_Test(getContext(), RV_PLAN);
        rvPlan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlan.setAdapter(adapterPlan);

        eventViewModel.getShowingDateHardEvents().observe(getViewLifecycleOwner(), new Observer<List<HardDateTimeEvent>>() {
            @Override
            public void onChanged(List<HardDateTimeEvent> hardDateTimeEvents) {
                adapterPlan.setValues(adapterPlan.setItems(hardDateTimeEvents, ItemTypeInterfaсe.EVENT_HARD_DTE));
            }
        });

        eventViewModel.getShowingDateBudgetEvents().observe(getViewLifecycleOwner(), new Observer<List<BudgetDateTimeEvent>>() {
            @Override
            public void onChanged(List<BudgetDateTimeEvent> budgetDateTimeEvents) {
                adapterPlan.setValues(adapterPlan.setItems(budgetDateTimeEvents, ItemTypeInterfaсe.EVENT_BUDGET_DTE));
            }
        });

        eventViewModel.getShowingDateSoftEvents().observe(getViewLifecycleOwner(), new Observer<List<SoftDateTimeEvent>>() {
            @Override
            public void onChanged(List<SoftDateTimeEvent> softDateTimeEvents) {
                adapterPlan.setValues(adapterPlan.setItems(softDateTimeEvents, ItemTypeInterfaсe.EVENT_SOFT_DTE));
            }
        });

        topBar = view.findViewById(R.id.topBar);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.calendar):
                        showMenu(view.findViewById(R.id.calendar));
                        break;
                }

                return true;
            }
        });

        topBar.setTitle(new LocalDate(showingDate).toString(dateFormat));

        return view;
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

    }

    @Override
    public void onItemElephantClick(ElephantWithSteaks elephantItem, int TAG) {

    }

    @Override
    public void onItemSteakClick(Steak steakItem, int TAG) {

    }

    @Override
    public void onItemFrogLongClick(Frog frogItem, int TAG) {
        if (action_Mode == null){
            action_Mode = getActivity().startActionMode(callbackFrog);
        }
        action_Mode.setTitle(String.valueOf(adapterFrog.getSelectedItemCount()));
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

    }

    @Override
    public void onItemDateSteakCompleteClick(SteakView steakView, int TAG) {
        eventViewModel.insertDateSteakCrossRef(showing, steakView.steak, !steakView.isDailyCompleted);
    }

    @Override
    public void onItemHardDateTimeClick(HardDateTimeEvent hardDateTimeEvent) {

    }

    @Override
    public void onItemSoftDateTimeClick(SoftDateTimeEvent softDateTimeEvent) {

    }

    @Override
    public void onItemBudgetDateTimeClick(BudgetDateTimeEvent budgetDateTimeEvent) {

    }

    @Override
    public void onItemHardDateTimeLongClick(HardDateTimeEvent hardDateTimeEvent, int TAG) {

    }

    @Override
    public void onItemBudgetDateTimeLongClick(BudgetDateTimeEvent budgetDateTimeEvent, int TAG) {

    }

    @Override
    public void onItemSoftDateTimeLongClick(SoftDateTimeEvent softDateTimeEvent, int TAG) {

    }

    @Override
    public void onItemHardDateTimeCompleteClick(HardDateTimeEvent hardDateTimeEvent, int TAG) {

    }

    @Override
    public void onItemSoftDateTimeCompleteClick(SoftDateTimeEvent softDateTimeEvent, int TAG) {

    }

    @Override
    public void onItemBudgetDateTimeCompleteClick(BudgetDateTimeEvent budgetDateTimeEvent, int TAG) {

    }

    @Override
    public void onItemHardDateTimeContextClick(HardDateTimeEvent hardDateTimeEvent, View view, int TAG) {

    }

    @Override
    public void onItemSoftDateTimeContextClick(SoftDateTimeEvent softDateTimeEvent, View view, int TAG) {

    }

    @Override
    public void onItemBudgetDateTimeContextClick(BudgetDateTimeEvent budgetDateTimeEvent, View view, int TAG) {

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


    private ActionMode.Callback callbackFrog = new ActionMode.Callback(){

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
                    adapterFrog.getSelectedItems().forEach(i -> {
                        ((Frog)i).date = 0;
                        eventViewModel.updateFrog((Frog)i);
                    });
                    actionMode.finish();
                    Snackbar.make(getView(), "Лягушка удалена из плана", Snackbar.LENGTH_LONG).show();

                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            adapterFrog.clearSelection();
            action_Mode = null;
        }

    };

    private void showMenu(View v){
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.event_menu_, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.menu1):
                        showingDate = new LocalDate().toDate().getTime();
                        showing = new Date();
                        showing.date = showingDate;

                        eventViewModel.setShowingDateEvents(showingDate);
                        topBar.setTitle(new LocalDate(showingDate).toString(dateFormat));
                        break;
                    case (R.id.menu2):
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

                        MaterialDatePicker CalendarPicker = builder.build();

                        CalendarPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onPositiveButtonClick(Object selection) {

                                LocalDate d = new LocalDate(selection);

                                showingDate = d.toDate().getTime();

                                eventViewModel.setShowingDateEvents(showingDate);

                                Date date = new Date();
                                date.date = showingDate;


                                eventViewModel.initSteaksForDatePeriod(date.date,date.date);

                                topBar.setTitle(new LocalDate(showingDate).toString(dateFormat));
                            }
                        });

                        CalendarPicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
                        break;
                    case (R.id.menu3):
                        topBar.setTitle("Неделя");

                }
                return false;
            }
        });

        popupMenu.show();
    }
}

