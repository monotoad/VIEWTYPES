package com.example.viewtypes.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.viewtypes.BudgetDateTimeEvent;
import com.example.viewtypes.Constants;
import com.example.viewtypes.Elephant;
import com.example.viewtypes.ElephantWithSteaks;
import com.example.viewtypes.Frog;
import com.example.viewtypes.HardDateTimeEvent;
import com.example.viewtypes.ItemTypeInterfaсe;
import com.example.viewtypes.R;
import com.example.viewtypes.SoftDateTimeEvent;
import com.example.viewtypes.ui.dashboard.EditAnimalFragmentArgs;
import com.example.viewtypes.ui.dashboard.EditAnimalFragmentDirections;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDTEFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDTEFragment extends Fragment {

    RelativeLayout rlTime;
    RelativeLayout rlDuration;
    TextInputLayout tilEventPriority;
    TextInputLayout tilDate;

    LocalTime timeSLT, timeELT;
    Long date;

    DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm");
    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMMM");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private MaterialToolbar topBar;


    public EditDTEFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditDTEFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDTEFragment newInstance(String param1, String param2) {
        EditDTEFragment fragment = new EditDTEFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_plan_event, container, false);

        TextInputEditText etName = view.findViewById(R.id.etEventName);
        tilEventPriority = view.findViewById(R.id.tilEventPriority);
        tilDate = view.findViewById(R.id.tilDate);

        AutoCompleteTextView tvEventPriority = view.findViewById(R.id.etEventPriority);
        List listTypeEvent = new ArrayList();
        Collections.addAll(listTypeEvent, "Нет", "Безотлагательное", "Уточняющее");
        ArrayAdapter adapterEvent = new ArrayAdapter(getActivity(), R.layout.list_item, listTypeEvent);
        tvEventPriority.setAdapter(adapterEvent);


        AutoCompleteTextView tvEventType = view.findViewById(R.id.etEventType);
        List listTypeEvent1 = new ArrayList();
        Collections.addAll(listTypeEvent1, "Неважно", "Диапазон времени", "Объём времени");
        ArrayAdapter adapterEvent1 = new ArrayAdapter(getActivity(), R.layout.list_item, listTypeEvent1);
        tvEventType.setAdapter(adapterEvent1);

        rlTime = view.findViewById(R.id.rlTime);
        rlDuration = view.findViewById(R.id.rlDuration);

        AutoCompleteTextView tvDate = view.findViewById(R.id.etDate);
        List dates = new ArrayList();
        Collections.addAll(dates, "Сегодня", "В ближайшую неделю", "В ближайший месяц", "Указать дату");
        ArrayAdapter adapterEvent2 = new ArrayAdapter(getActivity(), R.layout.list_item, dates);
        tvDate.setAdapter(adapterEvent2);

        tvDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Указать дату")){
                    MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

                    MaterialDatePicker CalendarPicker = builder.build();

                    CalendarPicker.addOnPositiveButtonClickListener(selection -> {

                        LocalDate d = new LocalDate(selection);

                        date = d.toDate().getTime();
                        tvDate.setText(dateFormat.print(date), false);
                    });

                    CalendarPicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
                }
                else if (adapterView.getItemAtPosition(i).equals("Сегодня")){
                    date = new LocalDate().toDate().getTime();
                }
                else if (adapterView.getItemAtPosition(i).equals("В ближайшую неделю")){
                    date = Long.valueOf(-1);
                }
                else  date = Long.valueOf(-2);
            }
        });
        EditText timeHour = view.findViewById(R.id.timeHour);
        EditText timeMin = view.findViewById(R.id.timeMin);

        TextView startTime = view.findViewById(R.id.startTime);
        TextView endTime = view.findViewById(R.id.endTime);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker.Builder ma = new MaterialTimePicker.Builder();
                ma.setTimeFormat(TimeFormat.CLOCK_24H);

                MaterialTimePicker materialTimePicker = ma.build();
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeSLT = new LocalTime(materialTimePicker.getHour(), materialTimePicker.getMinute());
                        startTime.setText(timeSLT.toString(timeFormat));
                    }
                });
                materialTimePicker.show(getActivity().getSupportFragmentManager(), "TIME_PICKER");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker.Builder ma = new MaterialTimePicker.Builder();
                ma.setTimeFormat(TimeFormat.CLOCK_24H);

                MaterialTimePicker materialTimePicker = ma.build();
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeELT = new LocalTime(materialTimePicker.getHour(), materialTimePicker.getMinute());
                        endTime.setText(timeELT.toString(timeFormat));
                    }
                });
                materialTimePicker.show(getActivity().getSupportFragmentManager(), "TIME_PICKER");
            }
        });


        topBar = view.findViewById(R.id.topBar);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EditDTEFragmentArgs args = EditDTEFragmentArgs.fromBundle(getArguments());
                EditDTEFragmentDirections.ActionEditDTEFragmentToNavigationPlan d = EditDTEFragmentDirections.actionEditDTEFragmentToNavigationPlan();

                switch (item.getItemId()){
                    case (R.id.miDone):
                        if (tvEventType.getText().toString().equals("Неважно")) {

                            SoftDateTimeEvent softDateTimeEvent;
                            if (args.getSDTE() != null)
                                softDateTimeEvent = args.getSDTE();
                            else softDateTimeEvent = new SoftDateTimeEvent();


                            softDateTimeEvent.title= etName.getText().toString();

                            if (tvEventPriority.getText().toString().equals("Нет")) {
                                softDateTimeEvent.priority = Constants.EVENT_NONE;
                            }
                            else if (tvEventPriority.getText().toString().equals("Безотлагательное")){
                                softDateTimeEvent.priority = Constants.EVENT_URGENT;
                            }
                            else if (tvEventPriority.getText().toString().equals("Уточняющее")){
                                softDateTimeEvent.priority = Constants.EVENT_CLARIFY;
                            }


                            softDateTimeEvent.date = date;


                            d.setSDTE(softDateTimeEvent);
                        }
                        else if (tvEventType.getText().toString().equals("Объём времени")){

                            BudgetDateTimeEvent budgetDateTimeEvent;

                            if (args.getBDTE() != null){
                                budgetDateTimeEvent = args.getBDTE();
                            }
                            else budgetDateTimeEvent = new BudgetDateTimeEvent();


                            budgetDateTimeEvent.title = etName.getText().toString();

                            Period period = new Period(Integer.valueOf(timeHour.getText().toString()), Integer.valueOf(timeMin.getText().toString()), 0, 0);
                            budgetDateTimeEvent.duration = period.toStandardDuration().getMillis();

                            if (tvEventPriority.getText().toString().equals("Нет")) {
                                budgetDateTimeEvent.priority = Constants.EVENT_NONE;
                            }
                            else if (tvEventPriority.getText().toString().equals("Безотлагательное")){
                                budgetDateTimeEvent.priority = Constants.EVENT_URGENT;
                            }
                            else if (tvEventPriority.getText().toString().equals("Уточняющее")){
                                budgetDateTimeEvent.priority = Constants.EVENT_CLARIFY;
                            }

                            budgetDateTimeEvent.date = date;

                            d.setBDTE(budgetDateTimeEvent);

                        }
                        else{

                            HardDateTimeEvent hardDateTimeEvent;

                            if (args.getHDTE() != null){
                                hardDateTimeEvent = args.getHDTE();
                            }
                            else hardDateTimeEvent = new HardDateTimeEvent();

                            hardDateTimeEvent.title = etName.getText().toString();

                            hardDateTimeEvent.timeS = timeSLT.toDateTimeToday().getMillis();

                            hardDateTimeEvent.timeE = timeELT.toDateTimeToday().getMillis();

                            hardDateTimeEvent.date = date;

                            d.setHDTE(hardDateTimeEvent);
                        }
                        Navigation.findNavController(getView()).navigate(d);
                        break;
                }
                return true;
            }
        });

        tvEventType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Неважно")){
                    InitItems(ItemTypeInterfaсe.EVENT_SOFT_DTE);
                }
                else if (adapterView.getItemAtPosition(i).equals("Объём времени")){
                    InitItems(ItemTypeInterfaсe.EVENT_BUDGET_DTE);
                }
                else if (adapterView.getItemAtPosition(i).equals("Диапазон времени")){
                    InitItems(ItemTypeInterfaсe.EVENT_HARD_DTE);
                }
            }
        });

        EditDTEFragmentArgs args = EditDTEFragmentArgs.fromBundle(getArguments());

        if (args.getSDTE() != null){
            date = args.getSDTE().date;
            InitItems(ItemTypeInterfaсe.EVENT_SOFT_DTE);
            etName.setText(args.getSDTE().title);
            tvEventType.setText(adapterEvent1.getItem(0).toString(), false);

            if (args.getSDTE().priority == Constants.EVENT_NONE){
                tvEventPriority.setText(adapterEvent.getItem(0).toString(), false);
            }
            else if (args.getSDTE().priority == Constants.EVENT_URGENT){
                tvEventPriority.setText(adapterEvent.getItem(1).toString(), false);
            }
            else {
                tvEventPriority.setText(adapterEvent.getItem(2).toString(), false);
            }


            if (args.getSDTE().date == new LocalDate().toDate().getTime()){
                tvDate.setText(adapterEvent2.getItem(0).toString(), false);
            }
            else if (args.getSDTE().date == -1){
                tvDate.setText(adapterEvent2.getItem(1).toString(), false);
            }
            else if (args.getSDTE().date == -2){
                tvDate.setText(adapterEvent2.getItem(2).toString(), false);
            }
            else tvDate.setText(dateFormat.print(args.getSDTE().date), false);
        }

        if (args.getBDTE() != null){
            date = args.getBDTE().date;
            InitItems(ItemTypeInterfaсe.EVENT_BUDGET_DTE);
            etName.setText(args.getBDTE().title);
            tvEventType.setText(adapterEvent1.getItem(2).toString(), false);

            if (args.getBDTE().priority == Constants.EVENT_NONE){
                tvEventPriority.setText(adapterEvent.getItem(0).toString(), false);
            }
            else if (args.getBDTE().priority == Constants.EVENT_URGENT){
                tvEventPriority.setText(adapterEvent.getItem(1).toString(), false);
            }
            else {
                tvEventPriority.setText(adapterEvent.getItem(2).toString(), false);
            }


            if (args.getBDTE().date == new LocalDate().toDate().getTime()){
                tvDate.setText(adapterEvent2.getItem(0).toString(),false);
            }
            else if (args.getBDTE().date == -1){
                tvDate.setText(adapterEvent2.getItem(1).toString(),false);
            }
            else if (args.getBDTE().date == -2){
                tvDate.setText(adapterEvent2.getItem(2).toString(),false);
            }
            else tvDate.setText(dateFormat.print(args.getBDTE().date), false);

            timeHour.setText(String.valueOf(new Period(args.getBDTE().duration).getHours()));
            timeMin.setText(String.valueOf(new Period(args.getBDTE().duration).getMinutes()));

        }

        if (args.getHDTE() != null){
            date = args.getHDTE().date;
            timeSLT = new LocalTime(args.getHDTE().timeS);
            timeELT = new LocalTime(args.getHDTE().timeE);

            InitItems(ItemTypeInterfaсe.EVENT_HARD_DTE);
            etName.setText(args.getHDTE().title);
            tvEventType.setText(adapterEvent1.getItem(1).toString(), false);

            if (args.getHDTE().date == new LocalDate().toDate().getTime()){
                tvDate.setText(adapterEvent2.getItem(0).toString(), false);
            }
            else if (args.getHDTE().date == -1){
                tvDate.setText(adapterEvent2.getItem(1).toString(), false);
            }
            else if (args.getHDTE().date == -2){
                tvDate.setText(adapterEvent2.getItem(2).toString(), false);
            }
            else tvDate.setText(dateFormat.print(args.getHDTE().date), false);

            startTime.setText(timeFormat.print(args.getHDTE().timeS));
            endTime.setText(timeFormat.print(args.getHDTE().timeE));

        }

        return view;
    }

    protected void InitItems(int type){
        switch (type){
            case ItemTypeInterfaсe.EVENT_SOFT_DTE:
                rlTime.setVisibility(GONE);
                rlDuration.setVisibility(GONE);
                tilEventPriority.setVisibility(VISIBLE);
                break;
            case ItemTypeInterfaсe.EVENT_BUDGET_DTE:
                rlTime.setVisibility(GONE);
                rlDuration.setVisibility(VISIBLE);
                tilEventPriority.setVisibility(VISIBLE);
                break;
            case ItemTypeInterfaсe.EVENT_HARD_DTE:
                rlTime.setVisibility(VISIBLE);
                rlDuration.setVisibility(GONE);
                tilEventPriority.setVisibility(GONE);
                break;
        }
    }
}