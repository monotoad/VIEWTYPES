package com.example.viewtypes.ui.notifications;

import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewtypes.BudgetDateTimeEvent;
import com.example.viewtypes.Constants;
import com.example.viewtypes.DTEventEWithSubDTEvents;
import com.example.viewtypes.ElephantWithSteaks;
import com.example.viewtypes.Event;
import com.example.viewtypes.EventAdapter;
import com.example.viewtypes.EventAdapter_Test;
import com.example.viewtypes.EventViewModel;
import com.example.viewtypes.EventWithKairoses;
import com.example.viewtypes.EventsWithKairos;
import com.example.viewtypes.Frog;
import com.example.viewtypes.Goal;
import com.example.viewtypes.HardDateTimeEvent;
import com.example.viewtypes.Item;
import com.example.viewtypes.ItemTypeInterfaсe;
import com.example.viewtypes.Kairos;
import com.example.viewtypes.KairosWithEvents;
import com.example.viewtypes.R;
import com.example.viewtypes.SoftDateTimeEvent;
import com.example.viewtypes.Steak;
import com.example.viewtypes.SteakView;
import com.example.viewtypes.SubDateTimeEvent;
import com.example.viewtypes.ui.RecyclerViewClickInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment implements RecyclerViewClickInterface {

    private FloatingActionButton btnAdd, btnDelete, btnSteak;

    private RecyclerView rvKairoses;

    EventViewModel eventViewModel;

    EventAdapter_Test ea; 

    public ActionMode action_Mode = null;

    private EditText et;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);

        btnAdd = root.findViewById(R.id.btnNewKairos);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.editKairosFragment);

            }
        });

        btnAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.editEventFragment);
                return true;
            }
        });



        /////////////////////////////////////////////////////////////
        rvKairoses = root.findViewById(R.id.rvKairoses);
        rvKairoses.setLayoutManager(new LinearLayoutManager(getActivity()));


        ea = new EventAdapter_Test(getContext(), 6);
        ea.setOnClickListeners(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                rvKairoses.getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        layoutManager.setStackFromEnd(true);

        rvKairoses.setAdapter(ea);



        eventViewModel.getAllKairosesWithEvents().observe(getViewLifecycleOwner(), new Observer<List<KairosWithEvents>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<KairosWithEvents> kairosesWithEvents) {
                ea.setValues(ea.setItems(kairosesWithEvents, ItemTypeInterfaсe.KAIROS));
            }
        });

        NotificationsFragmentArgs args = NotificationsFragmentArgs.fromBundle(getArguments());

        if (args.getKairosWithEvents() != null){
            eventViewModel.insertKairosWithEvents(args.getKairosWithEvents().kairos, args.getKairosWithEvents().events);
        }


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
                    //adapterAll.deleteSelected();
                    ea.getSelectedItems().forEach(i -> {
                        eventViewModel.deleteKairosById( ((KairosWithEvents)i).kairos.kairosId );
                    });

                    //adapterSteaks.setValues(adapterSteaks.setItems(steaks, ItemTypeInterfaсe.EVENT_STEAK));
                    actionMode.finish();
                    Snackbar.make(getView(), "Кайросы были удалены", Snackbar.LENGTH_LONG).show();

                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            ea.clearSelection();
            action_Mode = null;
        }

    };

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
        NotificationsFragmentDirections.ActionNavigationNotificationsToEditKairosFragment direction = NotificationsFragmentDirections.actionNavigationNotificationsToEditKairosFragment();
        direction.setKairosWithEvents(kairosWithEvents);
        Navigation.findNavController(getView()).navigate(direction);
    }

    @Override
    public void onItemEventClick(Event event, int TAG) {
        //NotificationsFragmentDirections.ActionNavigationNotificationsToEditKairosFragment direction = NotificationsFragmentDirections.actionNavigationNotificationsToEditKairosFragment();
        //direction.setEventWithKairoses(event)
        //NotificationsFragmentDirections.ActionNavigationNotificationsToEditEventFragment direction = NotificationsFragmentDirections.actionNavigationNotificationsToEditEventFragment();
        //EventWithKairoses eventWithKairoses = new EventWithKairoses();
        //eventWithKairoses.kairos = event;
        //direction.setEventWithKairoses(eventWithKairoses);
    }

    @Override
    public void onItemKairosWithEventsLongClick(KairosWithEvents kairosWithEvents, int TAG) {
        if (action_Mode == null){
            action_Mode = getActivity().startActionMode(callback);
        }
        action_Mode.setTitle(String.valueOf(ea.getSelectedItemCount()));
    }

    @Override
    public void onItemEventLongClick(Event event, int TAG) {

    }

    @Override
    public void onItemEventCompleteClick(Event event, int TAG) {
        event.isCompleted = !event.isCompleted;
        if (event.isCompleted){
            event.date = new LocalDate().toDate().getTime();
        }
        else{
            event.date = 0;
        }
        eventViewModel.insert(event);
    }

    @Override
    public void onItemDateSteakClick(SteakView steakView, int TAG) {

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