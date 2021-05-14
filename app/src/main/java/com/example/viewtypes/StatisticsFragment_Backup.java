package com.example.viewtypes;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment_Backup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment_Backup extends Fragment {

    EventViewModel eventViewModel;
    List<Frog> ALLfrogs, CompletedFrogs;

    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMMM");
    DateTimeFormatter dateFormat1 = DateTimeFormat.forPattern("dd.MM");

    List<HardDateTimeEvent> HardEvents;
    List<SoftDateTimeEvent> SoftEvents;
    List<BudgetDateTimeEvent> BudgetEvents;

    TableLayout tableLayout, tableLayoutHeaders;

    TableLayout.LayoutParams tableParams;
    TableRow.LayoutParams rowParams;

    List<SteakView> ALLSteaks;

    List<Long> dates;

    int freeFrogsCount = 0;

    int dailySteaks, ComplHardEvents, ComplSoftEvents, ComplBudgetEvents;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MaterialToolbar topBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticsFragment_Backup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment_Backup newInstance(String param1, String param2) {
        StatisticsFragment_Backup fragment = new StatisticsFragment_Backup();
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
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ALLfrogs = new ArrayList<>();
        CompletedFrogs = new ArrayList<>();

        ALLSteaks = new ArrayList<>();

        HardEvents = new ArrayList<>();
        BudgetEvents = new ArrayList<>();
        SoftEvents = new ArrayList<>();

        topBar = view.findViewById(R.id.topBar);

        dates = new ArrayList<>();

        TextView tvFrog = view.findViewById(R.id.tvFrog);
        TextView tvSteak = view.findViewById(R.id.tvSteak);
        TextView tvPlan = view.findViewById(R.id.tvPlan);
        TextView tvDates = view.findViewById(R.id.tvDates);

        StatisticsFragmentArgs args = StatisticsFragmentArgs.fromBundle(getArguments());

        tvDates.setText(dateFormat.print(args.getDateStart()));

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Выберите диапазон");

        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

        tvDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                tvDates.setText(dateFormat.print(selection.first) + " - " + dateFormat.print(selection.second));

                LocalDate f = new LocalDate(selection.first);
                LocalDate s = new LocalDate(selection.second);

                eventViewModel.setShowingDateEvents(f.toDate().getTime());
                eventViewModel.setShowingDateEventsEnd(s.toDate().getTime());

                Date date = new Date();
                date.date = selection.first;

                long temp = f.toDate().getTime();
                while (temp <= s.toDate().getTime()){
                    dates.add(temp);
                    temp = new LocalDate(temp).plusDays(1).toDate().getTime();
                }

                eventViewModel.initSteaksForDatePeriod(f.toDate().getTime(), s.toDate().getTime());
            }
        });

        topBar.setTitle(String.valueOf(args.getDateStart()));

        tableLayout = view.findViewById(R.id.tlStat);
        tableLayoutHeaders = view.findViewById(R.id.tlHeaders);


        tableParams = new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tableLayout.setLayoutParams(tableParams);
        rowParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        //rowParams.setMargins(5, 0, 5, 0);
        rowParams.weight = 1;

        tableLayoutHeaders.setLayoutParams(tableParams);

        //eventViewModel.setShowingDateEvents(args.getDateStart());
        eventViewModel.setShowingDateEvents(args.getDateStart());
        eventViewModel.setShowingDateEventsEnd(args.getDateStart());



        eventViewModel.getShowingFrogs().observe(getViewLifecycleOwner(), new Observer<List<Frog>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Frog> frogs) {
                CompletedFrogs.clear();
                ALLfrogs = frogs;
                ALLfrogs.forEach(i -> {
                    Log.d("VVVVVVV", i.title);
                    if (i.isCompleted) CompletedFrogs.add(i);
                });
                tvFrog.setText("Сделано: " + CompletedFrogs.size() + "/" + "1");


                clearTable(dates);
                initTable(HardEvents, SoftEvents, BudgetEvents, ALLfrogs, ALLSteaks);

            }
        });

        eventViewModel.getFreeFrogs().observe(getViewLifecycleOwner(), new Observer<List<Frog>>() {
            @Override
            public void onChanged(List<Frog> frogs) {
                freeFrogsCount = frogs.size();
            }
        });

        Date date = new Date();
        date.date = args.getDateStart();
        eventViewModel.initSteaksForDatePeriod(date.date, date.date);
        eventViewModel.getSteaksWithCompletenessByDate().observe(getViewLifecycleOwner(), new Observer<List<SteakView>>() {
            @Override
            public void onChanged(List<SteakView> steakViews) {
                dailySteaks = 0;
                ALLSteaks = steakViews;
                dailySteaks = (int) ALLSteaks.stream().filter(i -> i.isDailyCompleted || i.steak.isCompleted).count();
                tvSteak.setText("Сделано:" + String.valueOf(dailySteaks) + "/" + ALLSteaks.size());

                ALLSteaks.forEach(i -> {
                    //Log.d("VVVVVVV", i.steak.title + " - " + i.isDailyCompleted);
                });
                //Log.d("VVVVVVV", "вфывфвфы");

                clearTable(dates);
                initTable(HardEvents, SoftEvents, BudgetEvents, ALLfrogs, ALLSteaks);
            }
        });


        eventViewModel.getAllStatHDTE().observe(getViewLifecycleOwner(), new Observer<List<HardDateTimeEvent>>() {
            @Override
            public void onChanged(List<HardDateTimeEvent> hardDateTimeEvents) {
                ComplHardEvents = 0;
                HardEvents = hardDateTimeEvents;
                ComplHardEvents = (int) hardDateTimeEvents.stream().filter(i -> i.isCompleted).count();
                tvPlan.setText("Сделано:" + String.valueOf(ComplHardEvents + ComplBudgetEvents + ComplSoftEvents) + "/" + String.valueOf(HardEvents.size()+SoftEvents.size()+BudgetEvents.size()));

                //Log.d("VVVVVVV", "сися");

                clearTable(dates);
                initTable(HardEvents, SoftEvents, BudgetEvents, ALLfrogs, ALLSteaks);
            }
        });


        eventViewModel.getAllStatSDTE().observe(getViewLifecycleOwner(), new Observer<List<SoftDateTimeEvent>>() {
            @Override
            public void onChanged(List<SoftDateTimeEvent> softDateTimeEvents) {
                ComplSoftEvents = 0;
                SoftEvents = softDateTimeEvents;
                ComplSoftEvents = (int) softDateTimeEvents.stream().filter(i -> i.isCompleted).count();
                tvPlan.setText("Сделано:" + String.valueOf(ComplHardEvents + ComplBudgetEvents + ComplSoftEvents) + "/" + String.valueOf(HardEvents.size()+SoftEvents.size()+BudgetEvents.size()));

                clearTable(dates);
                initTable(HardEvents, SoftEvents, BudgetEvents, ALLfrogs, ALLSteaks);
            }
        });

        eventViewModel.getAllStatBDTE().observe(getViewLifecycleOwner(), new Observer<List<BudgetDateTimeEvent>>() {
            @Override
            public void onChanged(List<BudgetDateTimeEvent> budgetDateTimeEvents) {
                ComplBudgetEvents = 0;
                BudgetEvents = budgetDateTimeEvents;
                ComplBudgetEvents = (int) budgetDateTimeEvents.stream().filter(i -> i.isCompleted).count();
                tvPlan.setText("Сделано:" + String.valueOf(ComplHardEvents + ComplBudgetEvents + ComplSoftEvents) + "/" + String.valueOf(HardEvents.size()+SoftEvents.size()+BudgetEvents.size()));

                clearTable(dates);
                initTable(HardEvents, SoftEvents, BudgetEvents, ALLfrogs, ALLSteaks);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initTable(List<HardDateTimeEvent> hdte, List<SoftDateTimeEvent> sdte, List<BudgetDateTimeEvent> bdte, List<Frog> frogs, List<SteakView> steaks){

        TableRow frogTitleRow = new TableRow(getContext());
        frogTitleRow.setLayoutParams(rowParams);

        TextView frogTitle = new TextView(getContext());
        frogTitle.setText("Жабы");
        frogTitle.setTextColor(Color.BLACK);
        frogTitle.setTypeface(frogTitle.getTypeface(), Typeface.BOLD);
        frogTitle.setLayoutParams(rowParams);
        frogTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        frogTitleRow.addView(frogTitle);
        tableLayout.addView(frogTitleRow);

        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(rowParams);

        TextView title = new TextView(getContext());
        title.setText("Любая жаба: ");
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(rowParams);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.addView(title);

        dates.forEach(i -> {
            //boolean isContain = ALLfrogs.stream().anyMatch(o -> o.date == i);
            //if (isContain){

            //}
            TextView completeness = new TextView(getContext());
            completeness.setTextColor(Color.BLACK);
            completeness.setLayoutParams(rowParams);
            completeness.setGravity(Gravity.CENTER_HORIZONTAL);


            List<Frog> f =new ArrayList<>();
            // Найдём жабы, находящиеся в памяти под этой датой
            ALLfrogs.stream().filter(o -> o.date == i).forEach(f::add);

            // Если жаб для этой даты нет, то хз
            if (f.size() == 0){
                if (freeFrogsCount == 0)
                    completeness.setText("жаб нет");
                else{
                    completeness.setText("-");
                }
            }
            else{
                // Если в жабах есть выполненная, то всё ок
                if (f.stream().anyMatch(p -> p.isCompleted == Boolean.TRUE)){
                    completeness.setText("+");
                }
                else {
                    completeness.setText("-");
                }
            }

            tableRow.addView(completeness);
        });

        tableLayout.addView(tableRow);



        TableRow steakTitleRow = new TableRow(getContext());
        steakTitleRow.setLayoutParams(rowParams);

        TextView steakTitle = new TextView(getContext());
        steakTitle.setText("Бифштексы");
        steakTitle.setTextColor(Color.BLACK);
        steakTitle.setTypeface(steakTitle.getTypeface(), Typeface.BOLD);
        steakTitle.setLayoutParams(rowParams);
        steakTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        steakTitleRow.addView(steakTitle);

        tableLayout.addView(steakTitleRow);


        List<Steak> a = new ArrayList<>();
        ALLSteaks.forEach(j -> {
            if (a.contains(j.steak)) return;
            else a.add(j.steak);



            TableRow tableRow1 = new TableRow(getContext());
            tableRow1.setLayoutParams(rowParams);

            TextView title1 = new TextView(getContext());
            title1.setText(j.steak.title);
            title1.setTextColor(Color.BLACK);
            title1.setLayoutParams(rowParams);
            title1.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow1.addView(title1);



            dates.forEach(i -> {
                TextView completeness = new TextView(getContext());
                completeness.setTextColor(Color.BLACK);
                completeness.setLayoutParams(rowParams);
                completeness.setGravity(Gravity.CENTER_HORIZONTAL);




                SteakView finded = ALLSteaks.stream().filter( test -> (test.steak.date == i)   ).filter(l -> l.steak.id == j.steak.id).findFirst().orElse(null);

                if (finded != null){
                    if (finded.isDailyCompleted)
                        completeness.setText("+");
                    else completeness.setText("-");
                }
                else completeness.setText("?");

                tableRow1.addView(completeness);



                /*List<SteakView> f =new ArrayList<>();
                // Найдём стейки, находящиеся в памяти под этой датой
                ALLSteaks.stream().filter(o -> o.steak.date == i).forEach(f::add);

                // Если стейков для этой даты нет, то хз
                if (f.size() == 0){
                    completeness.setText(" ");
                }
                else{
                    // Если в стейках есть выполненная, то всё ок
                    if (f.stream().anyMatch(p -> p.isDailyCompleted == Boolean.TRUE)){
                        completeness.setText("+");
                    }
                    else {
                        completeness.setText("-");
                    }
                }*/

                //tableRow.addView(completeness);
            });

            tableLayout.addView(tableRow1);
        });




        //tableLayout.addView(steakTitleRow);

        /*for (int i = 0; i < frogs.size(); i++){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(rowParams);

            TextView title = new TextView(getContext());
            title.setText(frogs.get(i).title);
            title.setTextColor(Color.BLACK);
            title.setLayoutParams(rowParams);
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(title);

            TextView completeness = new TextView(getContext());
            completeness.setText(Boolean.toString(frogs.get(i).isCompleted));
            completeness.setTextColor(Color.BLACK);
            completeness.setLayoutParams(rowParams);
            completeness.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(completeness);

            tableLayout.addView(tableRow);
        }*/

        /*TableRow steakTitleRow = new TableRow(getContext());
        steakTitleRow.setLayoutParams(rowParams);

        TextView steakTitle = new TextView(getContext());
        steakTitle.setText("Бифштексы");
        steakTitle.setTextColor(Color.BLACK);
        steakTitle.setTypeface(steakTitle.getTypeface(), Typeface.BOLD);
        steakTitle.setLayoutParams(rowParams);
        steakTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        steakTitleRow.addView(steakTitle);
        tableLayout.addView(steakTitleRow);

        for (int i = 0; i < steaks.size(); i++){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(rowParams);

            TextView title = new TextView(getContext());
            title.setText(steaks.get(i).steak.title + "-" + dateFormat.print(steaks.get(i).steak.date));
            title.setTextColor(Color.BLACK);
            title.setLayoutParams(rowParams);
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(title);

            TextView completeness = new TextView(getContext());
            completeness.setText(Boolean.toString(steaks.get(i).isDailyCompleted));
            completeness.setTextColor(Color.BLACK);
            completeness.setLayoutParams(rowParams);
            completeness.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(completeness);

            tableLayout.addView(tableRow);
        }


        TableRow DTTitleRow = new TableRow(getContext());
        DTTitleRow.setLayoutParams(rowParams);

        TextView DTTitle = new TextView(getContext());
        DTTitle.setText("План");
        DTTitle.setTextColor(Color.BLACK);
        DTTitle.setTypeface(DTTitle.getTypeface(), Typeface.BOLD);
        DTTitle.setLayoutParams(rowParams);
        DTTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        DTTitleRow.addView(DTTitle);

        tableLayout.addView(DTTitleRow);

        for (int i = 0; i < hdte.size(); i++){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(rowParams);

            TextView title = new TextView(getContext());
            title.setText(hdte.get(i).title);
            title.setTextColor(Color.BLACK);
            title.setLayoutParams(rowParams);
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(title);

            TextView completeness = new TextView(getContext());
            completeness.setText(Boolean.toString(hdte.get(i).isCompleted));
            completeness.setTextColor(Color.BLACK);
            completeness.setLayoutParams(rowParams);
            completeness.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(completeness);

            tableLayout.addView(tableRow);
        }

        for (int i = 0; i < sdte.size(); i++){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(rowParams);

            TextView title = new TextView(getContext());
            title.setText(sdte.get(i).title);
            title.setTextColor(Color.BLACK);
            title.setLayoutParams(rowParams);
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(title);

            TextView completeness = new TextView(getContext());
            completeness.setText(Boolean.toString(sdte.get(i).isCompleted));
            completeness.setTextColor(Color.BLACK);
            completeness.setLayoutParams(rowParams);
            completeness.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(completeness);

            tableLayout.addView(tableRow);
        }

        for (int i = 0; i < bdte.size(); i++){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(rowParams);

            TextView title = new TextView(getContext());
            title.setText(bdte.get(i).title);
            title.setTextColor(Color.BLACK);
            title.setLayoutParams(rowParams);
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(title);

            TextView completeness = new TextView(getContext());
            completeness.setText(Boolean.toString(bdte.get(i).isCompleted));
            completeness.setTextColor(Color.BLACK);
            completeness.setLayoutParams(rowParams);
            completeness.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.addView(completeness);

            tableLayout.addView(tableRow);
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clearTable(List<Long> dates){
        tableLayout.removeAllViews();

        TableRow titles = new TableRow(getContext());
        titles.setLayoutParams(rowParams);

        TextView tv = new TextView(getContext());
        tv.setText("Название");
        tv.setBackgroundColor(getResources().getColor(R.color.black));
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setTypeface(null, Typeface.BOLD);
        tv.setLayoutParams(rowParams);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);

        titles.addView(tv);

        dates.forEach(i -> {
            TextView tv1 = new TextView(getContext());
            //tv1.setText(dateFormat1.print(i));
            tv1.setText(" " + Constants.getDayOfWeek(new LocalDate(i).getDayOfWeek()) + " ");
            tv1.setBackgroundColor(getResources().getColor(R.color.black));
            tv1.setTextColor(getResources().getColor(R.color.white));
            tv1.setTypeface(null, Typeface.BOLD);
            tv1.setLayoutParams(rowParams);
            tv1.setGravity(Gravity.CENTER_HORIZONTAL);

            titles.addView(tv1);
        });

        /*TextView tv1 = new TextView(getContext());
        tv1.setText("ПН");
        tv1.setBackgroundColor(getResources().getColor(R.color.black));
        tv1.setTextColor(getResources().getColor(R.color.white));
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setLayoutParams(rowParams);
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);

        titles.addView(tv1);*/

        tableLayout.addView(titles);

/*
        TableRow Frogs = new TableRow(getContext());
        Frogs.setLayoutParams(rowParams);

        TextView tv9 = new TextView(getContext());
        tv9.setText("Поесть корма с котом");
        tv9.setBackgroundColor(getResources().getColor(R.color.black));
        tv9.setTextColor(getResources().getColor(R.color.white));
        tv9.setTypeface(null, Typeface.BOLD);
        tv9.setLayoutParams(rowParams);
        tv9.setGravity(Gravity.CENTER_HORIZONTAL);

        Frogs.addView(tv9);

        tableLayout.addView(Frogs);


        TableRow first = new TableRow(getContext());
        first.setLayoutParams(rowParams);

        TextView tv2 = new TextView(getContext());
        tv2.setText("Поесть корма с котом");
        tv2.setBackgroundColor(getResources().getColor(R.color.black));
        tv2.setTextColor(getResources().getColor(R.color.white));
        tv2.setTypeface(null, Typeface.BOLD);
        tv2.setLayoutParams(rowParams);
        tv2.setGravity(Gravity.CENTER_HORIZONTAL);

        first.addView(tv2);

        TextView tv3 = new TextView(getContext());
        tv3.setText("ПН");
        tv3.setTypeface(null, Typeface.BOLD);
        tv3.setBackgroundColor(getResources().getColor(R.color.grey));
        tv3.setLayoutParams(rowParams);
        tv3.setGravity(Gravity.CENTER_HORIZONTAL);

        first.addView(tv3);


        tableLayout.addView(first);

*/



    }
}