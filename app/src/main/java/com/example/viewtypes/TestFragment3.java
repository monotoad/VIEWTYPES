package com.example.viewtypes;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.viewtypes.ui.RecyclerViewClickInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment3 extends Fragment implements RecyclerViewClickInterface {

    EventViewModel eventViewModel;

    public ActionMode action_Mode = null;

    EventAdapter_Test adapterPlan;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TestFragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment3 newInstance(String param1, String param2) {
        TestFragment3 fragment = new TestFragment3();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test3, container, false);

        ExRecyclerView rvPlan = view.findViewById(R.id.rvPlan);

        adapterPlan = new EventAdapter_Test(getContext(), 9);
        adapterPlan.setOnClickListeners(this);
        rvPlan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlan.setAdapter(adapterPlan);

        eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);


        eventViewModel.getAllSDTEMonth().observe(getViewLifecycleOwner(), new Observer<List<SoftDateTimeEvent>>() {
            @Override
            public void onChanged(List<SoftDateTimeEvent> softDateTimeEvents) {
                adapterPlan.setValues(adapterPlan.setItems(softDateTimeEvents, ItemTypeInterfaсe.EVENT_SOFT_DTE));
            }
        });

        eventViewModel.getAllBDTEMonth().observe(getViewLifecycleOwner(), new Observer<List<BudgetDateTimeEvent>>() {
            @Override
            public void onChanged(List<BudgetDateTimeEvent> budgetDateTimeEvents) {
                adapterPlan.setValues(adapterPlan.setItems(budgetDateTimeEvents, ItemTypeInterfaсe.EVENT_BUDGET_DTE));
            }
        });

        eventViewModel.getAllHDTEMonth().observe(getViewLifecycleOwner(), new Observer<List<HardDateTimeEvent>>() {
            @Override
            public void onChanged(List<HardDateTimeEvent> hardDateTimeEvents) {
                adapterPlan.setValues(adapterPlan.setItems(hardDateTimeEvents, ItemTypeInterfaсe.EVENT_HARD_DTE));
            }
        });



        return view;
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

                                    if ( !((BudgetDateTimeEvent)object).isCompleted && olddate!=object.date)
                                        eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_BUDGET_DTE, object.isCompleted));
                                }
                                else if (object instanceof HardDateTimeEvent){
                                    eventViewModel.insert((HardDateTimeEvent) object);

                                    if ( !((HardDateTimeEvent)object).isCompleted && olddate!=object.date)
                                        eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_HARD_DTE, object.isCompleted));

                                }
                                else if (object instanceof SoftDateTimeEvent){
                                    eventViewModel.insert((SoftDateTimeEvent) object);

                                    if ( !((SoftDateTimeEvent)object).isCompleted && olddate!=object.date)
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

                            if ( !((BudgetDateTimeEvent)object).isCompleted && olddate!=object.date)
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_BUDGET_DTE, object.isCompleted));
                        }
                        else if (object instanceof HardDateTimeEvent){
                            eventViewModel.insert((HardDateTimeEvent) object);

                            if ( !((HardDateTimeEvent)object).isCompleted && olddate!=object.date)
                                eventViewModel.insert(new LogsHDTE(olddate, object.date, object.id, ItemTypeInterfaсe.EVENT_HARD_DTE, object.isCompleted));
                        }
                        else if (object instanceof SoftDateTimeEvent){
                            eventViewModel.insert((SoftDateTimeEvent) object);

                            if ( !((SoftDateTimeEvent)object).isCompleted && olddate!=object.date)
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

    }

    @Override
    public void onItemElephantLongClick(ElephantWithSteaks elephantItem, int TAG) {

    }

    @Override
    public void onItemSteakLongClick(Steak steakItem, int TAG) {

    }

    @Override
    public void onItemFrogCompleteClick(Frog frogItem, int TAG) {

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
        if (action_Mode == null){
            action_Mode = getActivity().startActionMode(callback);
        }
        action_Mode.setTitle(String.valueOf(adapterPlan.getSelectedItemCount()));
    }

    @Override
    public void onItemBudgetDateTimeLongClick(BudgetDateTimeEvent budgetDateTimeEvent, int TAG) {
        if (action_Mode == null){
            action_Mode = getActivity().startActionMode(callback);
        }
        action_Mode.setTitle(String.valueOf(adapterPlan.getSelectedItemCount()));
    }

    @Override
    public void onItemSoftDateTimeLongClick(SoftDateTimeEvent softDateTimeEvent, int TAG) {
        if (action_Mode == null){
            action_Mode = getActivity().startActionMode(callback);
        }
        action_Mode.setTitle(String.valueOf(adapterPlan.getSelectedItemCount()));
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
}