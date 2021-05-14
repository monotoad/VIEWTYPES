package com.example.viewtypes;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.viewtypes.ui.RecyclerViewClickInterface;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditKairosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditKairosFragment extends Fragment implements RecyclerViewClickInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<Event> ALLevents;
    List<Event> ALLSelectedEvents;
    EventViewModel eventViewModel;

    EventAdapter_Test adapterEvents;

    public ActionMode action_Mode = null;

    List<Boolean> Selected;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditKairosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditKairosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditKairosFragment newInstance(String param1, String param2) {
        EditKairosFragment fragment = new EditKairosFragment();
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_edit_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_kairos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Selected = new ArrayList<>();
        ALLSelectedEvents = new ArrayList<>();

        TextInputEditText etName = view.findViewById(R.id.etKairosName);

        ExRecyclerView rvSteaks = view.findViewById(R.id.rvSteaks);
        List<Event> existedEvents = new ArrayList<>();
        adapterEvents = new EventAdapter_Test(getContext(), 1);
        adapterEvents.setOnClickListeners(this);

        rvSteaks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSteaks.setAdapter(adapterEvents);

        Button btnAddKairos = view.findViewById(R.id.btnAddKairos);

        btnAddKairos.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                List<String> titles = new ArrayList<>();
                ALLevents.forEach(i -> {
                    titles.add(i.title);
                });

                final String[] work = titles.toArray(new String[0]);
                final boolean[] selected = toPrimitiveArray(Selected);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Выберите существующие");
                builder.setMultiChoiceItems(work, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        selected[i] = b;
                    }
                });
                builder.setPositiveButton("Выбрать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ALLSelectedEvents.removeAll(existedEvents);
                        existedEvents.clear();

                        for (int j = 0; j < selected.length; j++){
                            if (selected[j]){
                                existedEvents.add(ALLevents.get(j));
                                //adapterSteaks.notifyDataSetChanged();
                            }

                        }
                        ALLSelectedEvents.addAll(existedEvents);
                        adapterEvents.setValues(adapterEvents.setItems(ALLSelectedEvents, ItemTypeInterfaсe.EVENT_KAIROS));
                    }
                });
                builder.show();
            }
        });

        ALLevents = new ArrayList<>();

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Event> Allevents) {
                ALLevents = Allevents;
                ALLevents.forEach(i -> {
                    Selected.add(false);
                });
            }
        });

        EditText editText = view.findViewById(R.id.etNewEvent);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)){
                    Event event = new Event();
                    event.title = editText.getText().toString();
                    ALLSelectedEvents.add(event);
                    adapterEvents.setValues(adapterEvents.setItems(ALLSelectedEvents, ItemTypeInterfaсe.EVENT_KAIROS));
                    return true;
                }
                return false;
            }
        });

        MaterialToolbar topBar = view.findViewById(R.id.topBar);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                EditKairosFragmentDirections.ActionEditKairosFragmentToNavigationNotifications direction = EditKairosFragmentDirections.actionEditKairosFragmentToNavigationNotifications();

                EditKairosFragmentArgs args = EditKairosFragmentArgs.fromBundle(getArguments());

                switch (item.getItemId()){
                    case (R.id.miDone):

                        Kairos kairos;

                        if (args.getKairosWithEvents() != null){
                            kairos = args.getKairosWithEvents().kairos;
                        }
                        else kairos = new Kairos();

                        kairos.title = etName.getText().toString();



                        List<Event> events = ALLSelectedEvents;

                        KairosWithEvents kairosWithEvents = new KairosWithEvents(kairos, events);

                        direction.setKairosWithEvents(kairosWithEvents);

                        Navigation.findNavController(getView()).navigate(direction);


                        break;
                }
                return true;
            }
        });

        EditKairosFragmentArgs args = EditKairosFragmentArgs.fromBundle(getArguments());
        if (args.getKairosWithEvents() != null){
            etName.setText(args.getKairosWithEvents().kairos.title);
            ALLSelectedEvents = args.getKairosWithEvents().events;
            adapterEvents.setValues(adapterEvents.setItems(ALLSelectedEvents, ItemTypeInterfaсe.EVENT_KAIROS));
        }

    }

    private boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
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
                    adapterEvents.getSelectedItems().forEach(i -> {
                        ALLSelectedEvents.remove(i);
                    });
                    adapterEvents.setValues(adapterEvents.setItems(ALLSelectedEvents, ItemTypeInterfaсe.EVENT_KAIROS));
                    actionMode.finish();
                    Snackbar.make(getView(), "Бифштексы были удалены", Snackbar.LENGTH_LONG).show();

                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            adapterEvents.clearSelection();
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

    }

    @Override
    public void onItemEventClick(Event event, int TAG) {

    }

    @Override
    public void onItemKairosWithEventsLongClick(KairosWithEvents kairosWithEvents, int TAG) {

    }

    @Override
    public void onItemEventLongClick(Event event, int TAG) {
        if (action_Mode == null){
            action_Mode = getActivity().startActionMode(callback);
        }
        action_Mode.setTitle(String.valueOf(adapterEvents.getSelectedItemCount()));
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