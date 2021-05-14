package com.example.viewtypes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.viewtypes.ui.EditDTEFragmentArgs;
import com.example.viewtypes.ui.EditDTEFragmentDirections;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditGoalFragment extends Fragment {

    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMMM");
    long deadline = 0;
    float hourmin = 0;
    float hour = 0, min = 0;
    float weeksBeforeDeadline = 0;
    float unitsPerHour = 0;
    float unitComplete = 0;
    float hoursSpent = 0;
    float left = 0;
    float leftHours = 0;
    float maxCount = 0;
    float weekwork = 0;

    private MaterialToolbar topBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditGoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditGoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditGoalFragment newInstance(String param1, String param2) {
        EditGoalFragment fragment = new EditGoalFragment();
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
        return inflater.inflate(R.layout.fragment_edit_goal1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText etGoalName = view.findViewById(R.id.etGoalName);
        TextInputEditText etDeadline = view.findViewById(R.id.etDeadline);
        TextInputEditText etCount = view.findViewById(R.id.tvCount);
        TextInputEditText etMeasure = view.findViewById(R.id.tvMeasure);
        TextInputEditText etUnitComplete = view.findViewById(R.id.etUnitComplete);
        TextInputEditText etHoursSpent = view.findViewById(R.id.etHoursSpent);
        EditText timeHour = view.findViewById(R.id.timeHour);
        EditText timeMin = view.findViewById(R.id.timeMin);
        TextView tvLeftWeekWork = view.findViewById(R.id.tvLeftWeekWork);
        TextView tvLeftWeekDeadline = view.findViewById(R.id.tvLeftWeekDeadLine);
        TextView tvUnitPerHours = view.findViewById(R.id.tvUnitPerHour);
        TextView tvLeftHours = view.findViewById(R.id.tvLeftHours);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        MaterialDatePicker CalendarPicker = builder.build();

        CalendarPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                deadline = new LocalDate(selection).toDate().getTime();
                etDeadline.setText(dateFormat.print(deadline));
            }
        });




        etDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarPicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        etDeadline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LocalDate d = new LocalDate(deadline);

                LocalDate today = new LocalDate();

                int days = Days.daysBetween(today, d).getDays();

                weeksBeforeDeadline = (float)days / 7;

                tvLeftWeekDeadline.setText("Осталось недель до дедлайна: " + weeksBeforeDeadline);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etUnitComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etUnitComplete.getText().toString())) {
                    unitComplete = Float.parseFloat(etUnitComplete.getText().toString());
                }
                else {
                    unitComplete = 0;
                }

                if (hoursSpent != 0){
                    hoursSpent = Float.parseFloat(etHoursSpent.getText().toString());

                    unitsPerHour = unitComplete / hoursSpent;
                }
                else {
                    unitsPerHour = 0;
                }
                tvUnitPerHours.setText("Знаков в час: " + String.valueOf(unitsPerHour));

                if (hourmin != 0 && unitsPerHour != 0){
                    weekwork = (maxCount - unitComplete) / unitsPerHour / hourmin;
                    tvLeftWeekWork.setText("Осталось недель работы:" + String.valueOf(weekwork));
                }
                Log.d("MOLMOL", String.valueOf(weekwork));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etHoursSpent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etHoursSpent.getText().toString())){
                    hoursSpent = Float.parseFloat(etHoursSpent.getText().toString());
                }
                else{
                    hoursSpent = 0;
                }


                if (hoursSpent != 0){
                    hoursSpent = Float.parseFloat(etHoursSpent.getText().toString());

                    unitsPerHour = unitComplete / hoursSpent;
                }
                else {
                    unitsPerHour = 0;
                }
                tvUnitPerHours.setText("Знаков в час: " + String.valueOf(unitsPerHour));

                if (hourmin != 0 && unitsPerHour != 0){
                    weekwork = (maxCount - unitComplete) / unitsPerHour / hourmin;
                    tvLeftWeekWork.setText("Осталось недель работы:" + String.valueOf(weekwork));
                }
                Log.d("MOLMOL", String.valueOf(weekwork));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvLeftWeekWork.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        timeHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(timeHour.getText().toString())){
                    hour = Float.parseFloat(timeHour.getText().toString());
                    hourmin = hour + (min/60);
                }
                else{
                    hour = 0;
                    hourmin = (min/60);
                }
                leftHours = hourmin * weeksBeforeDeadline;
                tvLeftHours.setText("Осталось часов:" + String.valueOf(leftHours));

                if (hourmin != 0 && unitsPerHour != 0){
                    weekwork = (maxCount - unitComplete) / unitsPerHour / hourmin;
                    tvLeftWeekWork.setText("Осталось недель работы:" + String.valueOf(weekwork));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        timeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(timeMin.getText().toString())){
                    min = Float.parseFloat(timeMin.getText().toString());
                    hourmin = hour + (min/60);
                }
                else{
                    min = 0;
                    hourmin = hour;
                }

                leftHours = hourmin * weeksBeforeDeadline;
                tvLeftHours.setText("Осталось часов:" + String.valueOf(leftHours));

                if (hourmin != 0 && unitsPerHour != 0){
                    weekwork = (maxCount - unitComplete) / unitsPerHour / hourmin;
                    tvLeftWeekWork.setText("Осталось недель работы:" + String.valueOf(weekwork));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvLeftWeekDeadline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                leftHours = hourmin * weeksBeforeDeadline;
                tvLeftHours.setText("Осталось часов:" + String.valueOf(leftHours));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etCount.getText().toString())){
                    maxCount = Float.parseFloat(etCount.getText().toString());
                }
                else {
                    maxCount = 0;
                }

                if (hourmin != 0 && unitsPerHour != 0){
                    weekwork = (maxCount - unitComplete) / unitsPerHour / hourmin;
                    tvLeftWeekWork.setText("Осталось недель работы:" + String.valueOf(weekwork));
                }
                Log.d("MOLMOL", String.valueOf(weekwork));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        topBar = view.findViewById(R.id.topBar);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EditGoalFragmentDirections.ActionEditGoalFragmentToNavigationBiggoals direction = EditGoalFragmentDirections.actionEditGoalFragmentToNavigationBiggoals();

                EditGoalFragmentArgs args = EditGoalFragmentArgs.fromBundle(getArguments());

                switch (item.getItemId()){
                    case (R.id.miDone):

                        Goal goal;

                        if (args.getGoal() != null){
                            goal = args.getGoal();
                            goal.setTitle(etGoalName.getText().toString());
                            goal.setDeadline(deadline);
                            goal.setBudgetWeek(hourmin);
                        }
                        else{
                            goal = new Goal(maxCount, unitComplete, hourmin, hoursSpent ,deadline);
                            goal.setTitle(etGoalName.getText().toString());
                        }

                        direction.setGoal(goal);
                        Navigation.findNavController(getView()).navigate(direction);
                        break;
                }
                return true;
            }
        });

        EditGoalFragmentArgs args = EditGoalFragmentArgs.fromBundle(getArguments());
        if (args.getGoal() != null){
            etGoalName.setText(args.getGoal().getTitle());
            deadline = args.getGoal().getDeadline();
            etDeadline.setText(dateFormat.print(args.getGoal().getDeadline()));
            etCount.setText(String.valueOf(args.getGoal().getUnitCount()));
            etUnitComplete.setText(String.valueOf(args.getGoal().getCompleteUnitCount()));
            etHoursSpent.setText(String.valueOf(args.getGoal().getHoursSpent()));
            timeHour.setText(String.valueOf((int)args.getGoal().getBudgetWeek()));

            float min = args.getGoal().getBudgetWeek()-(int)args.getGoal().getBudgetWeek();

            timeMin.setText(String.valueOf(Math.round(min*60)));

            etCount.setClickable(false);
            etCount.setActivated(false);
            etCount.setEnabled(false);

            etUnitComplete.setClickable(false);
            etUnitComplete.setActivated(false);
            etUnitComplete.setEnabled(false);

            etHoursSpent.setClickable(false);
            etHoursSpent.setActivated(false);
            etHoursSpent.setEnabled(false);


        }

    }
}