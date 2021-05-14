package com.example.viewtypes.ui.home;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewtypes.BudgetDateTimeEvent;
import com.example.viewtypes.DTEventEWithSubDTEvents;
import com.example.viewtypes.Date;
import com.example.viewtypes.DateSteakCrossRef;
import com.example.viewtypes.ElephantWithSteaks;
import com.example.viewtypes.Event;
import com.example.viewtypes.EventAdapter_Test;
import com.example.viewtypes.EventViewModel;
import com.example.viewtypes.Frog;
import com.example.viewtypes.Goal;
import com.example.viewtypes.HardDateTimeEvent;
import com.example.viewtypes.ItemTypeInterfaсe;
import com.example.viewtypes.KairosWithEvents;
import com.example.viewtypes.R;
import com.example.viewtypes.SoftDateTimeEvent;
import com.example.viewtypes.Steak;
import com.example.viewtypes.SteakView;
import com.example.viewtypes.SubDateTimeEvent;
import com.example.viewtypes.ui.RecyclerViewClickInterface;
import com.example.viewtypes.ui.dashboard.EditAnimalFragmentArgs;
import com.example.viewtypes.ui.dashboard.EditAnimalFragmentDirections;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class AnimalFragment extends Fragment implements RecyclerViewClickInterface {


    private Button btnFrog, b2, btnClr, btnSteak, btnDate;
    private FloatingActionButton btnNewAnimalEvent, btnNewElephant, btnNewFrog;

    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMMM");
    DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm");
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    Locale locale = new Locale("ru");

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;

    private boolean clicked = false;

    private EditText et;

    private long showingDate;

    private MaterialToolbar topBar;

    public ActionMode action_Mode = null;

    private EventAdapter_Test adapterAll;

    EventViewModel eventViewModel;

    public static final int RV_DAY = 1;
    public static final int RV_ALL = 2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        showingDate = new LocalDate().toDate().getTime();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.animal_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_animal, container, false);


        rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);

        TextView tvDate = root.findViewById(R.id.date);
        tvDate.setText(new LocalDate(showingDate).toString(dateFormat));

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        //eventViewModel.setShowingDateEvents(showingDate);
        eventViewModel.setShowingDateEvents(showingDate);
        eventViewModel.setShowingDateEventsEnd(showingDate);

        Date date = new Date();
        date.date = showingDate;
        eventViewModel.initSteaksForDatePeriod(date.date, date.date);

        btnNewAnimalEvent = root.findViewById(R.id.btnNewAnimalEvent);
        btnNewFrog = root.findViewById(R.id.btnAddFrog);
        btnNewElephant = root.findViewById(R.id.btnAddElephant);

        btnNewAnimalEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(clicked);
                setAnimation(clicked);
                setClickable(clicked);
                clicked = !clicked;
            }
        });


        btnNewAnimalEvent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AnimalFragmentDirections.ActionNavigationAnimalToNavigationEdit direction = AnimalFragmentDirections.actionNavigationAnimalToNavigationEdit();
                Navigation.findNavController(view).navigate(direction);
                return true;
            }
        });

        RecyclerView rvAll = root.findViewById(R.id.rvAll);

        adapterAll = new EventAdapter_Test(getContext(), RV_ALL);
        rvAll.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAll.setAdapter(adapterAll);

        adapterAll.setOnClickListeners(this);


        eventViewModel.getAllFrogs().observe(getViewLifecycleOwner(), frogs -> adapterAll.setValues(adapterAll.setItems(frogs, ItemTypeInterfaсe.EVENT_FROG)));

        eventViewModel.getAllElephantsWithSteaks().observe(getViewLifecycleOwner(), new Observer<List<ElephantWithSteaks>>() {
            @Override
            public void onChanged(List<ElephantWithSteaks> elephantWithSteaks) {
                adapterAll.setValues(adapterAll.setItems(elephantWithSteaks, ItemTypeInterfaсe.EVENT_ELEPHANT));
            }
        });

        AnimalFragmentArgs args = AnimalFragmentArgs.fromBundle(getArguments());
        if (args.getFrog() != null){
            eventViewModel.insertFrog(args.getFrog());
        }

        if (args.getElephantWithSteaks() != null){
            eventViewModel.insertElephantWithSteaks(args.getElephantWithSteaks());
        }
        if (args.getSteak() != null){
            eventViewModel.insertSteak(args.getSteak());
            eventViewModel.deleteDateSteakCrossRefsBySteak(args.getSteak().id);
        }

        RecyclerView rvDay = root.findViewById(R.id.rvDay);
        EventAdapter_Test adapterDay = new EventAdapter_Test(getContext(), RV_DAY);
        rvDay.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDay.setAdapter(adapterDay);


        eventViewModel.getSteaksWithCompletenessByDate().observe(getViewLifecycleOwner(), new Observer<List<SteakView>>() {
            @Override
            public void onChanged(List<SteakView> steakViews) {
                if(android.os.Debug.isDebuggerConnected())
                    android.os.Debug.waitForDebugger();
                adapterDay.setValues(adapterDay.setItems(steakViews, ItemTypeInterfaсe.EVENT_STEAKDATE));
            }
        });

        eventViewModel.getShowingFrogs().observe(getViewLifecycleOwner(), new Observer<List<Frog>>() {
            @Override
            public void onChanged(List<Frog> frogs) {
                if(android.os.Debug.isDebuggerConnected())
                    android.os.Debug.waitForDebugger();

                adapterDay.setValues(adapterDay.setItems(frogs, ItemTypeInterfaсe.EVENT_FROG));

            }
        });

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        MaterialDatePicker CalendarPicker = builder.build();

        CalendarPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                LocalDate d = new LocalDate(selection);

                showingDate = d.toDate().getTime();


                eventViewModel.setShowingDateEvents(showingDate);

                Date date = new Date();
                date.date = showingDate;

                tvDate.setText(new LocalDate(showingDate).toString(dateFormat));

                eventViewModel.initSteaksForDatePeriod(showingDate, showingDate);
            }
        });


        topBar = root.findViewById(R.id.topBar);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.calendar):
                        CalendarPicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
                        break;
                    case (R.id.chart):
                        AnimalFragmentDirections.ActionNavigationAnimalToStatisticsFragment direction = AnimalFragmentDirections.actionNavigationAnimalToStatisticsFragment(showingDate, showingDate);
                        Navigation.findNavController(getView()).navigate(direction);
                        break;
                }
                return false;
            }
        });


        return root;
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
                    adapterAll.getSelectedItems().forEach(i -> {
                        if (i instanceof Frog){
                            eventViewModel.delete((Frog) i);
                        }
                        else if (i instanceof ElephantWithSteaks){
                            eventViewModel.deleteElephant( ((ElephantWithSteaks)i).elephant.id);
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
            adapterAll.clearSelection();
            action_Mode = null;
        }

    };


    private void setVisibility(boolean clicked){
        if (!clicked){
            btnNewFrog.setVisibility(View.VISIBLE);
            btnNewElephant.setVisibility(View.VISIBLE);
        }
        else{
            btnNewFrog.setVisibility(View.INVISIBLE);
            btnNewElephant.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked){
        if (!clicked){
            btnNewFrog.startAnimation(fromBottom);
            btnNewElephant.startAnimation(fromBottom);
            btnNewAnimalEvent.startAnimation(rotateOpen);
        }
        else{
            btnNewFrog.startAnimation(toBottom);
            btnNewElephant.startAnimation(toBottom);
            btnNewAnimalEvent.startAnimation(rotateClose);
        }
    }

    private void setClickable(boolean clicked){
        if (!clicked){
            btnNewFrog.setClickable(false);
            btnNewElephant.setClickable(false);
        }
        else{
            btnNewFrog.setClickable(true);
            btnNewElephant.setClickable(true);
        }
    }


    @Override
    public void onItemFrogClick(Frog frogItem, int TAG) {
        switch (TAG){
            case RV_ALL:
                Toast.makeText(getContext(), "ADAPTER_ALL + FROGCLICK", Toast.LENGTH_SHORT).show();

                AnimalFragmentDirections.ActionNavigationAnimalToNavigationEdit action = AnimalFragmentDirections.actionNavigationAnimalToNavigationEdit();
                action.setFrog(frogItem);

                Navigation.findNavController(getView()).navigate(action);
                break;
            case RV_DAY:
                Toast.makeText(getContext(), "ADAPTER_DAY + FROGCLICK", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onItemElephantClick(ElephantWithSteaks elephantItem, int TAG) {
        switch (TAG){
            case RV_ALL:
                Toast.makeText(getContext(), "ADAPTER_ALL + FROGCLICK", Toast.LENGTH_SHORT).show();

                AnimalFragmentDirections.ActionNavigationAnimalToNavigationEdit action = AnimalFragmentDirections.actionNavigationAnimalToNavigationEdit();
                action.setElephantWithSteaks(elephantItem);

                Navigation.findNavController(getView()).navigate(action);
                break;
            case RV_DAY:
                Toast.makeText(getContext(), "ADAPTER_DAY + FROGCLICK", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onItemSteakClick(Steak steakItem, int TAG) {
        AnimalFragmentDirections.ActionNavigationAnimalToEditSteakFragment action = AnimalFragmentDirections.actionNavigationAnimalToEditSteakFragment(steakItem);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void onItemFrogLongClick(Frog frogItem, int TAG) {
        if (action_Mode == null){
            action_Mode = getActivity().startActionMode(callback);
        }

        action_Mode.setTitle(String.valueOf(adapterAll.getSelectedItemCount()));
    }

    @Override
    public void onItemElephantLongClick(ElephantWithSteaks elephantItem, int TAG) {
        switch (TAG){
            case RV_ALL:
                Toast.makeText(getContext(), "ADAPTER_ALL + ELEPHANT_CLICK", Toast.LENGTH_SHORT).show();

                if (action_Mode == null){
                    action_Mode = getActivity().startActionMode(callback);
                }
                action_Mode.setTitle(String.valueOf(adapterAll.getSelectedItemCount()));
                break;
            case RV_DAY:
                Toast.makeText(getContext(), "ADAPTER_DAY + ELEPHANT_CLICK", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemSteakLongClick(Steak steakItem, int TAG) {

    }

    @Override
    public void onItemFrogCompleteClick(Frog frogItem, int TAG) {
        switch (TAG){
            case RV_ALL:
                Toast.makeText(getContext(), "ADAPTER_ALL + FROG_CLICK", Toast.LENGTH_SHORT).show();

                frogItem.isCompleted = !frogItem.isCompleted;
                if (!frogItem.isCompleted){
                    frogItem.date = 0;
                }
                else {
                    frogItem.date = showingDate;
                }

                eventViewModel.updateFrog(frogItem);

                break;
            case RV_DAY:
                Toast.makeText(getContext(), "ADAPTER_DAY + FROG_CLICK", Toast.LENGTH_SHORT).show();


                frogItem.isCompleted = !frogItem.isCompleted;
                if (!frogItem.isCompleted){
                    frogItem.date = 0;
                }
                else {
                    frogItem.date = showingDate;
                }

                eventViewModel.updateFrog(frogItem);

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemElephantCompleteClick(ElephantWithSteaks elephantItem, int TAG) {
        Date date = new Date();
        date.date = showingDate;
        if (!elephantItem.elephant.isCompleted){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

            builder.setTitle("Завершение слона");

            builder.setMessage("Вы хотите завершить выполнение слона? Все бифштексы будут также считаться выполненными");

            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    elephantItem.elephant.isCompleted = true;
                    eventViewModel.insertElephant(elephantItem.elephant);
                    elephantItem.steaks.forEach(j -> {
                        j.isCompleted = true;
                        j.date = showingDate;
                        eventViewModel.insertSteak(j);
                        eventViewModel.deleteDSCRabove(showingDate, j.id);
                        Date date = new Date();
                        date.date = showingDate;
                        eventViewModel.insertDateSteakCrossRef(date, j, true);
                    });
                    eventViewModel.insert(date, elephantItem.elephant);

                }
            });
            builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();
        }
        else{
            elephantItem.elephant.isCompleted = false;
            eventViewModel.insertElephant(elephantItem.elephant);
            elephantItem.steaks.forEach(j -> {
                j.isCompleted = false;
                j.date = 0;
                eventViewModel.insertSteak(j);
            });
            eventViewModel.delete(date, elephantItem.elephant);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSteakCompleteClick(Steak steakItem, int TAG) {
        if (!steakItem.isCompleted){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

            builder.setTitle("Завершение бифштекса");

            builder.setMessage("Вы хотите завершить выполнение бифштекса? В плане он больше не появится");

            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    steakItem.isCompleted = true;
                    steakItem.date = showingDate;
                    eventViewModel.deleteDSCRabove(showingDate, steakItem.id);

                    Date date = new Date();
                    date.date = showingDate;
                    eventViewModel.insertDateSteakCrossRef(date, steakItem, true);

                    /*steakItem.isCompleted = true;
                    steakItem.date = showingDate;
                    eventViewModel.deleteDSCRabove(showingDate, steakItem.id);

                    eventViewModel.insertSteak(steakItem);*/

                }
            });
            builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();
        }
        else{
            steakItem.isCompleted = false;
            steakItem.date = 0;
            eventViewModel.insertSteak(steakItem);

            Date date = new Date();
            date.date = showingDate;
            DateSteakCrossRef dateSteakCrossRef = new DateSteakCrossRef();
            dateSteakCrossRef.date = showingDate;
            dateSteakCrossRef.id = steakItem.id;
            eventViewModel.delete(dateSteakCrossRef);

            //steakItem.isCompleted = false;
            //steakItem.date = 0;
            //eventViewModel.insertSteak(steakItem);
        }





        /*if (!steakItem.isCompleted){
            steakItem.isCompleted = true;
            steakItem.date = showingDate;
            eventViewModel.deleteDSCRabove(showingDate, steakItem.id);

            DateSteakCrossRef dateSteakCrossRef = new DateSteakCrossRef();
            dateSteakCrossRef.date = showingDate;
            dateSteakCrossRef.id = steakItem.id;
            dateSteakCrossRef.isDailyCompleted = true;
            eventViewModel.insert(dateSteakCrossRef);

            if (steakItem.days.isEmpty()){
                Date date = new Date();
                date.date = showingDate;
                eventViewModel.insertDateSteakCrossRef(date, steakItem, true);
            }
        }
        else {
            steakItem.isCompleted = false;
            steakItem.date = 0;

            DateSteakCrossRef dateSteakCrossRef1 = new DateSteakCrossRef();
            dateSteakCrossRef1.date = showingDate;
            dateSteakCrossRef1.id = steakItem.id;
            dateSteakCrossRef1.isDailyCompleted = true;
            eventViewModel.delete(dateSteakCrossRef1);

            if (steakItem.days.isEmpty()){
                Date date = new Date();
                date.date = showingDate;
                DateSteakCrossRef dateSteakCrossRef = new DateSteakCrossRef();
                dateSteakCrossRef.date = showingDate;
                dateSteakCrossRef.id = steakItem.id;
                dateSteakCrossRef.isDailyCompleted = false;
                eventViewModel.delete(dateSteakCrossRef);
            }
        }
        eventViewModel.insertSteak(steakItem);*/


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
        switch (TAG){
            case RV_ALL:
                Toast.makeText(getContext(), "ADAPTER_ALL + STEAKVIEW_CLICK", Toast.LENGTH_SHORT).show();
                break;
            case RV_DAY:
                Toast.makeText(getContext(), "ADAPTER_DAY + STEAKVIEW_CLICK", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemDateSteakCompleteClick(SteakView steakView, int TAG) {

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
}