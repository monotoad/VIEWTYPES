package com.example.viewtypes;

import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanFragment extends Fragment {

    private FloatingActionButton btnNewAnimalEvent, btnNewElephant, btnNewFrog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private MaterialToolbar topBar;

    private long showingDate;
    private Date showing;
    private long startTime;
    private long endTime;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;

    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMMM");
    DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm");

    public PlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanFragment newInstance(String param1, String param2) {
        PlanFragment fragment = new PlanFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_plan, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);

        NavController navController = Navigation.findNavController(this.getActivity(), R.id.nested_nav_host);

        PlanViewModel planViewModel = new ViewModelProvider(getActivity()).get(PlanViewModel.class);


        tabLayout = view.findViewById(R.id.tabLayout);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        navController.navigate(R.id.testFragment);
                        topBar.setTitle(new LocalDate(showingDate).toString(dateFormat));
                        break;
                    case 1:
                        navController.navigate(R.id.testFragment2);
                        //topBar.setTitle("Неделя");
                        break;
                    case 2:
                        navController.navigate(R.id.testFragment3);
                        //topBar.setTitle("Месяц");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        topBar = view.findViewById(R.id.topBar);
        topBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.calendar):
                        //navController.navigate(R.id.testFragment2);
                        //tabLayout.getTabAt(1).select();
                        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

                        MaterialDatePicker CalendarPicker = builder.build();

                        CalendarPicker.addOnPositiveButtonClickListener(selection -> {

                            LocalDate d = new LocalDate(selection);

                            showingDate = d.toDate().getTime();

                            planViewModel.setDate(showingDate);

                            tabLayout.getTabAt(0).select();
                        });

                        CalendarPicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
                        break;
                    case (R.id.chart):
                        PlanFragmentDirections.ActionNavigationPlanToStatisticsFragment direction = PlanFragmentDirections.actionNavigationPlanToStatisticsFragment(showingDate, showingDate);
                        Navigation.findNavController(getView()).navigate(direction);
                        break;
                    case (R.id.time):
                        MaterialTimePicker.Builder ma = new MaterialTimePicker.Builder();
                        ma.setTimeFormat(TimeFormat.CLOCK_24H);
                        ma.setHour(new LocalTime(endTime).getHourOfDay());
                        ma.setMinute(new LocalTime(endTime).getMinuteOfHour());
                        ma.setTitleText("Конечное время");
                        MaterialTimePicker materialTimePicker = ma.build();
                        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LocalTime ti = new LocalTime(materialTimePicker.getHour(), materialTimePicker.getMinute());
                                planViewModel.setTimeMax(ti.toDateTimeToday().getMillis());
                            }
                        });
                        materialTimePicker.show(getActivity().getSupportFragmentManager(), "TIME_PICKER");



                        MaterialTimePicker.Builder ma1 = new MaterialTimePicker.Builder();
                        ma1.setTimeFormat(TimeFormat.CLOCK_24H);
                        ma1.setHour(new LocalTime(startTime).getHourOfDay());
                        ma1.setMinute(new LocalTime(startTime).getMinuteOfHour());
                        ma1.setTitleText("Начальное время");
                        MaterialTimePicker materialTimePicker1 = ma1.build();
                        materialTimePicker1.addOnPositiveButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LocalTime ti = new LocalTime(materialTimePicker1.getHour(), materialTimePicker1.getMinute());
                                planViewModel.setTimeMin(ti.toDateTimeToday().getMillis());




                            }
                        });
                        materialTimePicker1.show(getActivity().getSupportFragmentManager(), "TIME_PICKER");

                        break;

                }

                return true;
            }
        });


        planViewModel.getDate().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                showingDate = aLong;
                showing.date = showingDate;

                //topBar.setTitle(new LocalDate(showingDate).toString(dateFormat) + " (" + timeFormat.print(startTime) + " - " + timeFormat.print(endTime) + ")");
                topBar.setTitle(" (" + timeFormat.print(startTime) + " - " + timeFormat.print(endTime) + ")");
            }
        });


        planViewModel.getTimeMin().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                startTime = aLong;
                //topBar.setTitle(new LocalDate(showingDate).toString(dateFormat) + " (" + timeFormat.print(startTime) + " - " + timeFormat.print(endTime) + ")");
                topBar.setTitle(" (" + timeFormat.print(startTime) + " - " + timeFormat.print(endTime) + ")");
                Log.d("Time", "TimeMin PLAN onChanged: " + timeFormat.print(startTime));
            }
        });

        planViewModel.getTimeMax().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                endTime = aLong;
                //topBar.setTitle(new LocalDate(showingDate).toString(dateFormat) + " (" + timeFormat.print(startTime) + " - " + timeFormat.print(endTime) + ")");
                topBar.setTitle(" (" + timeFormat.print(startTime) + " - " + timeFormat.print(endTime) + ")");
                Log.d("Time", "TimeMax PLAN onChanged: " + timeFormat.print(endTime));
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.btnNewEvent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.editDTEFragment);
                //setVisibility(clicked);
                //setAnimation(clicked);
                //setClickable(clicked);
                //clicked = !clicked;
            }
        });



        EventViewModel eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        PlanFragmentArgs args = PlanFragmentArgs.fromBundle(getArguments());
        if (args != null){
            if (args.getSDTE() != null){
                eventViewModel.insert(args.getSDTE());
                if (args.getSDTE().date != -1 && args.getSDTE().date != -2){
                    showingDate = args.getSDTE().date;
                    planViewModel.setDate(args.getSDTE().date);
                    topBar.setTitle(new LocalDate(showingDate).toString(dateFormat));
                }
                else if (args.getSDTE().date == -1){
                    tabLayout.getTabAt(1).select();
                }
                else if (args.getSDTE().date == -2){
                    tabLayout.getTabAt(2).select();
                }
            }
            else if (args.getBDTE() != null){
                BudgetDateTimeEvent budgetDateTimeEvent = args.getBDTE();
                budgetDateTimeEvent.timeE = budgetDateTimeEvent.timeS + budgetDateTimeEvent.duration;

                eventViewModel.insert(budgetDateTimeEvent);
                if (args.getBDTE().date != -1 && args.getBDTE().date != -2){
                    showingDate = args.getBDTE().date;
                    planViewModel.setDate(args.getBDTE().date);
                    topBar.setTitle(new LocalDate(showingDate).toString(dateFormat));
                }
                else if (args.getBDTE().date == -1){
                    tabLayout.getTabAt(1).select();
                }
                else if (args.getBDTE().date == -2){
                    tabLayout.getTabAt(2).select();
                }
            }
            else if (args.getHDTE() != null){
                eventViewModel.insert(args.getHDTE());
                if (args.getHDTE().date != -1 && args.getHDTE().date != -2){
                    showingDate = args.getHDTE().date;
                    planViewModel.setDate(args.getHDTE().date);
                    topBar.setTitle(new LocalDate(showingDate).toString(dateFormat));
                }
                else if (args.getHDTE().date == -1){
                    tabLayout.getTabAt(1).select();

                }
                else if (args.getHDTE().date == -2){
                    tabLayout.getTabAt(2).select();
                }
            }
        }


        super.onViewCreated(view, savedInstanceState);
    }


}