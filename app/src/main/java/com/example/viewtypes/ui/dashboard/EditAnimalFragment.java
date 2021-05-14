package com.example.viewtypes.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewtypes.BudgetDateTimeEvent;
import com.example.viewtypes.Constants;
import com.example.viewtypes.DTEventEWithSubDTEvents;
import com.example.viewtypes.Elephant;
import com.example.viewtypes.ElephantWithSteaks;
import com.example.viewtypes.Event;
import com.example.viewtypes.EventAdapter;
import com.example.viewtypes.EventAdapter_Test;
import com.example.viewtypes.Frog;
import com.example.viewtypes.Goal;
import com.example.viewtypes.HardDateTimeEvent;
import com.example.viewtypes.Item;
import com.example.viewtypes.ItemTypeInterfaсe;
import com.example.viewtypes.KairosWithEvents;
import com.example.viewtypes.R;
import com.example.viewtypes.SoftDateTimeEvent;
import com.example.viewtypes.Steak;
import com.example.viewtypes.SteakView;
import com.example.viewtypes.SubDateTimeEvent;
import com.example.viewtypes.ui.RecyclerViewClickInterface;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditAnimalFragment extends Fragment implements RecyclerViewClickInterface {


    //DashboardFragmentArgs args;

    private MaterialButton btnAddSteak;

    private EditText et;

    //private Switch sw;
    private SwitchMaterial swRepeatSteaks, swFrog;
    protected ChipGroup chipGroup;
    protected Chip chip1, chip2, chip3, chip4, chip5, chip6, chip7;

    private TextInputLayout tilEventName, tilEventType, tilStakeName;

    private TextInputEditText etEventName, etSteakName;
    private AutoCompleteTextView etEventType;

    private MaterialToolbar topBar;

    private RecyclerView rvSteaks;

    private List<Steak> steaks;

    private EventAdapter_Test adapterSteaks;

    public ActionMode action_Mode = null;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_edit_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_edit_animal, container, false);

        etEventName = root.findViewById(R.id.etEventName);
        etEventType = root.findViewById(R.id.etEventType);
        etSteakName = root.findViewById(R.id.etSteakName);

        swRepeatSteaks = root.findViewById(R.id.swRepeatSteaks);
        swFrog = root.findViewById(R.id.swFrogDay);

        tilEventName = root.findViewById(R.id.tilEventName);
        tilEventType = root.findViewById(R.id.tilEventType);
        tilStakeName = root.findViewById(R.id.tilSteak);

        chipGroup = root.findViewById(R.id.chipGroup);

        chip1 = root.findViewById(R.id.chip1);
        chip2 = root.findViewById(R.id.chip2);
        chip3 = root.findViewById(R.id.chip3);
        chip4 = root.findViewById(R.id.chip4);
        chip5 = root.findViewById(R.id.chip5);
        chip6 = root.findViewById(R.id.chip6);
        chip7 = root.findViewById(R.id.chip7);


        swRepeatSteaks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    chipGroup.setVisibility(View.VISIBLE);
                }
                else{
                    chipGroup.setVisibility(View.GONE);
                }
            }
        });


        rvSteaks = root.findViewById(R.id.rvSteaks);
        steaks = new ArrayList<>();
        adapterSteaks = new EventAdapter_Test(getContext(), 1);
        adapterSteaks.setOnClickListeners(this);
        rvSteaks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSteaks.setAdapter(adapterSteaks);



        List listTypeEvent = new ArrayList();
        Collections.addAll(listTypeEvent, "Лягушка", "Слон");
        ArrayAdapter adapterEvent = new ArrayAdapter(getContext(), R.layout.list_item, listTypeEvent);
        etEventType.setAdapter(adapterEvent);



        btnAddSteak = root.findViewById(R.id.btnAddSteak);

        btnAddSteak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSteakName()){
                    Steak steak = new Steak();
                    steak.title = etSteakName.getText().toString();
                    steak.days = getDateListFromChipGroup();
                    steak.isCompleted = false;

                    steaks.add(steak);

                    adapterSteaks.setValues(adapterSteaks.setItems(steaks,ItemTypeInterfaсe.EVENT_STEAK));

                    chipGroup.clearCheck();
                    etSteakName.getText().clear();

                    //Navigation.findNavController(getView()).navigate(R.id.bottomDialogFragment);
                }
            }
        });

        topBar = root.findViewById(R.id.topBar);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EditAnimalFragmentArgs args = EditAnimalFragmentArgs.fromBundle(getArguments());
                EditAnimalFragmentDirections.ActionNavigationEditToNavigationAnimal direction = EditAnimalFragmentDirections.actionNavigationEditToNavigationAnimal();
                switch (item.getItemId()){
                    case (R.id.miDone):
                        if (etEventType.getText().toString().equals("Лягушка")){
                            //if (etEventType.getListSelection() == 0)
                            Frog frog;
                            if (args.getFrog() != null)
                                frog = args.getFrog();
                            else frog = new Frog();

                            if (!TextUtils.isEmpty(etEventName.getText()))
                                frog.title = etEventName.getText().toString();

                            if (swFrog.isChecked()){
                                frog.date = new LocalDate().toDate().getTime();
                            }

                            direction.setFrog(frog);
                        }
                        else if (etEventType.getText().toString().equals("Слон")){
                            ElephantWithSteaks elephantWithSteaks;

                            if (args.getElephantWithSteaks() != null){
                                elephantWithSteaks = args.getElephantWithSteaks();

                            }
                            else {
                                Elephant elephant = new Elephant();
                                elephant.isCompleted = false;

                                elephantWithSteaks = new ElephantWithSteaks(elephant, steaks);
                            }

                            if (!TextUtils.isEmpty(etEventName.getText())){
                                elephantWithSteaks.elephant.title = etEventName.getText().toString();
                            }

                            direction.setElephantWithSteaks(elephantWithSteaks);
                        }

                        Navigation.findNavController(getView()).navigate(direction);
                        break;
                }
                return true;
            }
        });

        InitItems(ItemTypeInterfaсe.EVENT_FROG);


        etEventType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Лягушка")){
                    //tilStakeName.setEnabled(false);
                    //btnSteak.setEnabled(false);
                    InitItems(ItemTypeInterfaсe.EVENT_FROG);
                }
                else{
                    //tilStakeName.setEnabled(true);
                    //btnSteak.setEnabled(true);
                    InitItems(ItemTypeInterfaсe.EVENT_ELEPHANT);
                }
            }
        });

        EditAnimalFragmentArgs args = EditAnimalFragmentArgs.fromBundle(getArguments());

        if (args.getFrog() != null){
            InitItems(ItemTypeInterfaсe.EVENT_FROG);
            etEventName.setText(args.getFrog().title);
            if (args.getFrog().date == new LocalDate().toDate().getTime()){
                swFrog.setChecked(true);
            }
            etEventType.setText((CharSequence) adapterEvent.getItem(0));
        }
        else if (args.getElephantWithSteaks() != null){
            InitItems(ItemTypeInterfaсe.EVENT_ELEPHANT);
            etEventName.setText(args.getElephantWithSteaks().elephant.title);
            etEventType.setText((CharSequence) adapterEvent.getItem(1));
            steaks = args.getElephantWithSteaks().steaks;
            adapterSteaks.setValues(adapterSteaks.setItems(args.getElephantWithSteaks().steaks, ItemTypeInterfaсe.EVENT_STEAK));
        }
        else if (args.getSteak() != null){
            Steak steak = args.getSteak();
            List<Steak> steaks = new ArrayList<>();
            steaks.add(steak);
            adapterSteaks.setValues(adapterSteaks.setItems(steaks, ItemTypeInterfaсe.EVENT_STEAK));
        }


        return root;
    }


    private boolean validateEventName() {
        String NameInput = etEventName.getText().toString().trim();
        if (NameInput.isEmpty()) {
            tilEventName.setError("Field can't be empty");
            return false;
        } else {
            tilEventName.setError(null);
            return true;
        }
    }

    private boolean validateEventType() {
        String TypeInput = etEventType.getText().toString().trim();
        if (TypeInput.isEmpty()) {
            tilEventType.setError("Field can't be empty");
            return false;
        } else {
            tilEventType.setError(null);
            return true;
        }
    }

    private boolean validateSteakName() {
        String SteakInput = etSteakName.getText().toString().trim();
        if (SteakInput.isEmpty()) {
            tilStakeName.setError("Field can't be empty");
            return false;
        } else {
            tilStakeName.setError(null);
            return true;
        }
    }

    protected void InitItems(int type){
        switch (type){
            case ItemTypeInterfaсe.EVENT_ELEPHANT:
                tilStakeName.setVisibility(View.VISIBLE);
                btnAddSteak.setVisibility(View.VISIBLE);
                swRepeatSteaks.setVisibility(View.VISIBLE);
                swFrog.setVisibility(View.GONE);
                break;
            case ItemTypeInterfaсe.EVENT_FROG:
                tilStakeName.setVisibility(View.GONE);
                btnAddSteak.setVisibility(View.GONE);
                steaks.clear();
                //.notifyDataSetChanged();
                etSteakName.getText().clear();
                swRepeatSteaks.setVisibility(View.GONE);
                chipGroup.setVisibility(View.GONE);
                swFrog.setVisibility(View.VISIBLE);
                break;
        }
    }

    protected List<Integer> getDateListFromChipGroup(){
        List<Integer> res = new ArrayList<>();
        if (chip1.isChecked())
            res.add(DateTimeConstants.MONDAY);
        if (chip2.isChecked())
            res.add(DateTimeConstants.TUESDAY);
        if (chip3.isChecked())
            res.add(DateTimeConstants.WEDNESDAY);
        if (chip4.isChecked())
            res.add(DateTimeConstants.THURSDAY);
        if (chip5.isChecked())
            res.add(DateTimeConstants.FRIDAY);
        if (chip6.isChecked())
            res.add(DateTimeConstants.SATURDAY);
        if (chip7.isChecked())
            res.add(DateTimeConstants.SUNDAY);
        return res;
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
                    adapterSteaks.getSelectedItems().forEach(i -> {
                        steaks.remove(i);
                    });
                    adapterSteaks.setValues(adapterSteaks.setItems(steaks, ItemTypeInterfaсe.EVENT_STEAK));
                    actionMode.finish();
                    Snackbar.make(getView(), "Бифштексы были удалены", Snackbar.LENGTH_LONG).show();

                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            adapterSteaks.clearSelection();
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
        if (action_Mode == null){
            action_Mode = getActivity().startActionMode(callback);
        }
        action_Mode.setTitle(String.valueOf(adapterSteaks.getSelectedItemCount()));
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