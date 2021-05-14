package com.example.viewtypes;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewtypes.ui.RecyclerViewClickInterface;
import com.google.android.material.card.MaterialCardView;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import static com.example.viewtypes.Constants.getDayOfWeek;

public class EventAdapter_Test<T extends ItemTypeInterfaсe> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> items;
    private static RecyclerViewClickInterface listener;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();

    long hourMin = new LocalTime("8:00").toDateTimeToday().getMillis();
    long hourMax = new LocalTime("22:00").toDateTimeToday().getMillis();

    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMMM");
    DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm");
    Locale locale = new Locale("ru");
    private List<T> selectedItems;

    private Context context;
    private int checkedPosition = -1;

    private final int TAG;


    public EventAdapter_Test(Context newContext, int newTAG) {
        items = new ArrayList<>();
        selectedItems = new ArrayList<>();
        context = newContext;
        TAG = newTAG;
    }

    public EventAdapter_Test(List<T> newItems, Context newContext, int newTAG){
        items = newItems;
        selectedItems = new ArrayList<>();
        context = newContext;
        TAG = newTAG;
    }




    class FrogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private MaterialCardView cvItem;
        private TextView title;
        private ImageView ivType;
        private ImageView ivComplete;
        private ImageView ivMenu;

        public FrogViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cvItem);
            cvItem.setOnClickListener(this);
            cvItem.setOnLongClickListener(this);

            title = itemView.findViewById(R.id.tv);
            ivType = itemView.findViewById(R.id.ivType);
            ivType.setImageResource(R.drawable.ic_frog_prince);

            ivComplete = itemView.findViewById(R.id.ivComplete);
            ivComplete.setOnClickListener(completeListener);

            ivMenu = itemView.findViewById(R.id.ivMenu);
            ivMenu.setOnClickListener(menuListener);
        }

        void bind(Frog frog){
            title.setText(frog.id + frog.title);

            if (frog.isCompleted){
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_green);
            }
            else{
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_red);
            }

            if (selectedItems.contains(frog)){
                cvItem.setCardBackgroundColor(context.getResources().getColor(R.color.selected));
            }
            else{
                cvItem.setCardBackgroundColor(context.getResources().getColor(R.color.design_default_color_surface));
            }
        }

        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemFrogCompleteClick((Frog) items.get(position), TAG);
                }
            }
        };


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemFrogClick( ((Frog) items.get(position)), TAG );
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                toggleSelection(items.get(position));
                listener.onItemFrogLongClick( ((Frog) items.get(position)), TAG );
            }
            return true;
        }

        private View.OnClickListener menuListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemFrogContextClick((Frog) items.get(position), view, TAG);
                }
            }
        };
    }

    class ElephantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title;
        private MaterialCardView cvItem;
        //private CheckBox iv;
        private ImageView ivType;
        private ImageView ivComplete;
        private RecyclerView rv;

        public ElephantViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            ivType = itemView.findViewById(R.id.ivType);
            rv = itemView.findViewById(R.id.rvSteaks);
            ivType.setImageResource(R.drawable.ic_elephant__1_);
            cvItem = itemView.findViewById(R.id.cvItem);
            ivComplete = itemView.findViewById(R.id.ivComplete);
            ivComplete.setOnClickListener(completeListener);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        void bind(ElephantWithSteaks elephantWithSteaks){
            Log.d("НЕНГ", "Слон = " + String.valueOf(elephantWithSteaks.elephant.id));
            title.setText(elephantWithSteaks.elephant.id + " " + elephantWithSteaks.elephant.title + '\n');
            //iv.setChecked(elephantWithSteaks.elephant.isCompleted);


            if (elephantWithSteaks.elephant.isCompleted){
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_green);
            }
            else{
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_red);
            }

            if (selectedItems.contains(elephantWithSteaks)){
                cvItem.setCardBackgroundColor(context.getResources().getColor(R.color.selected));
            }
            else{
                cvItem.setCardBackgroundColor(context.getResources().getColor(R.color.design_default_color_surface));
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(rv.getContext(), LinearLayoutManager.VERTICAL, false);


            SubAdapter childAdapter = new SubAdapter(elephantWithSteaks.steaks, TAG);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(childAdapter);
            rv.setRecycledViewPool(sharedPool);


            childAdapter.setOnClickListeners(new RecyclerViewClickInterface() {
                @Override
                public void onItemFrogClick(Frog frogItem, int TAG) {

                }

                @Override
                public void onItemElephantClick(ElephantWithSteaks elephantItem, int TAG) {

                }

                @Override
                public void onItemSteakClick(Steak steakItem, int TAG) {
                    listener.onItemSteakClick(steakItem, 5);
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
                    listener.onItemSteakCompleteClick(steakItem, TAG);
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
            });


            rv.setAdapter(childAdapter);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemElephantClick( ((ElephantWithSteaks) items.get(position)), TAG );
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                toggleSelection(items.get(position));
                listener.onItemElephantLongClick( ((ElephantWithSteaks) items.get(position)), TAG );
            }
            return true;
        }

        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemElephantCompleteClick((ElephantWithSteaks) items.get(position), TAG);
                }
            }
        };
    }

    class SteakViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MaterialCardView cvItem;
        private TextView title;
        private TextView tvDate;
        private ImageView ivType;
        private ImageView ivComplete;

        public SteakViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cvItem);
            title = itemView.findViewById(R.id.tv);
            tvDate = itemView.findViewById(R.id.tvTime);
            ivType = itemView.findViewById(R.id.ivType);
            ivType.setImageResource(R.drawable.ic_steak);
            ivComplete = itemView.findViewById(R.id.ivComplete);
            ivComplete.setOnClickListener(completeListener);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        void bind(Steak steak){
            title.setText(steak.id + steak.title);
            tvDate.setText(getDaysOfWeek(steak.days));


            if (selectedItems.contains(steak)){
                cvItem.setCardBackgroundColor(context.getResources().getColor(R.color.selected));
            }
            else{
                cvItem.setCardBackgroundColor(context.getResources().getColor(R.color.design_default_color_surface));
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemSteakClick( ((Steak) items.get(position)), TAG );
            }
        }

        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemSteakCompleteClick((Steak) items.get(position), TAG);
                }
            }
        };


        @RequiresApi(api = Build.VERSION_CODES.N)
        private String getDaysOfWeek(List<Integer> dow){
            StringBuilder res = new StringBuilder();
            dow.forEach(i -> {
                res.append(getDayOfWeek(i) + " ");
            });
            return res.toString();
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                toggleSelection(items.get(position));
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                listener.onItemSteakLongClick((Steak) items.get(position), TAG);
            }
            return true;
        }
    }

    class DateSteakViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private ImageView ivType;
        private ImageView ivComplete;

        public DateSteakViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            ivComplete = itemView.findViewById(R.id.ivComplete);
            ivType = itemView.findViewById(R.id.ivType);
            ivType.setImageResource(R.drawable.ic_steak);
            itemView.setOnClickListener(this);
            ivComplete.setOnClickListener(completeListener);
        }

        void bind(SteakView steak){
            title.setText(steak.steak.id + steak.steak.title);

            if (steak.isDailyCompleted){
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_green);
            }
            else{
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_red);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemDateSteakClick( ((SteakView) items.get(position)), TAG);
            }
        }

        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemDateSteakCompleteClick((SteakView) items.get(position), TAG);
                }
            }
        };
    }

////////////////////////////////////

    class KairosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, RecyclerViewClickInterface {

        private CardView cvItem;
        private TextView title;
        private ImageView iv;
        private RecyclerView rv;

        public KairosViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cvItem);

            title = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.ivType);
            iv.setImageResource(R.drawable.ic_folder);
            rv = itemView.findViewById(R.id.rvSteaks);
            cvItem.setOnClickListener(this);
            cvItem.setOnLongClickListener(this);

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        void bind(KairosWithEvents kairosWithEvents){
            title.setText(kairosWithEvents.kairos.kairosId + " = " + kairosWithEvents.kairos.title);

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    rv.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);

            if (selectedItems.contains(kairosWithEvents)){
                cvItem.setCardBackgroundColor(context.getResources().getColor(R.color.selected));
            }
            else{
                cvItem.setCardBackgroundColor(context.getResources().getColor(R.color.design_default_color_surface));
            }

            layoutManager.setInitialPrefetchItemCount(kairosWithEvents.events.size());

            SubAdapter childAdapter = new SubAdapter(TAG);

            childAdapter.setOnClickListeners(this);

            childAdapter.setValues(kairosWithEvents.events);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(childAdapter);

            rv.setRecycledViewPool(sharedPool);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemKairosWithEventsClick( ((KairosWithEvents) items.get(position)), TAG );
            }
        }



        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                toggleSelection(items.get(position));
                listener.onItemKairosWithEventsLongClick( ((KairosWithEvents) items.get(position)), TAG );
            }
            return true;
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
            listener.onItemEventClick(event, TAG);
        }

        @Override
        public void onItemKairosWithEventsLongClick(KairosWithEvents kairosWithEvents, int TAG) {

        }

        @Override
        public void onItemEventLongClick(Event event, int TAG) {

        }

        @Override
        public void onItemEventCompleteClick(Event event, int TAG) {
            listener.onItemEventCompleteClick(event, TAG);
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

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MaterialCardView cvItem;
        private TextView title;
        private ImageView ivComplete;
        private ImageView ivType;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cvItem);
            cvItem.setOnClickListener(this);
            cvItem.setOnLongClickListener(this::onLongClick);

            title = itemView.findViewById(R.id.tv);

            ivType = itemView.findViewById(R.id.ivType);
            ivType.setImageResource(R.drawable.ic_baseline_psychology_24);

            itemView.setBackgroundColor(Color.parseColor("#ffc8a8"));
            ivComplete = itemView.findViewById(R.id.ivComplete);

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        void bind(Event event){

            title.setText(event.id + " = " + event.title);


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemEventClick( ((Event) items.get(position)), TAG );
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                toggleSelection(items.get(position));
                listener.onItemEventLongClick( ((Event) items.get(position)), TAG );
            }
            return true;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    class HardDateTimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private MaterialCardView cvItem;
        private TextView title;
        private TextView tvDate;
        private TextView tvTime;
        private ImageView ivType;
        private ImageView ivComplete;
        private ImageView ivMenu;

        public HardDateTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cvItem);
            title = itemView.findViewById(R.id.tv);
            ivType = itemView.findViewById(R.id.ivType);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            ivComplete = itemView.findViewById(R.id.ivComplete);
            ivComplete.setOnClickListener(completeListener);
            ivType.setImageResource(R.drawable.ic_baseline_access_time_24);
            //cvItem.setCardBackgroundColor(Color.parseColor("#ffa8b0"));
            ivMenu.setOnClickListener(menuListener);
            cvItem.setOnClickListener(this);
            cvItem.setOnLongClickListener(this);
        }

        void bind(HardDateTimeEvent event){
            title.setText(event.id + " " + event.title);

            LocalTime ltS = new LocalTime(event.timeS);
            LocalTime ltE = new LocalTime(event.timeE);
            tvTime.setText(ltS.toString(timeFormat) + " - " + ltE.toString(timeFormat));

            if (event.date == -1 || event.date == -2){
                ivComplete.setVisibility(View.GONE);
            }
            else{
                ivComplete.setVisibility(View.VISIBLE);
            }

            if (event.date == -1){
                tvDate.setText("на неделе");
            }
            else if (event.date == -2){
                tvDate.setText("в месяц");
            }
            else{
                tvDate.setText(dateFormat.print(event.date));
            }

            if (event.isCompleted){
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_green);
            }
            else{
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_red);
            }




        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemHardDateTimeClick( ((HardDateTimeEvent) items.get(position)) );
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                toggleSelection(items.get(position));
                listener.onItemHardDateTimeLongClick( ((HardDateTimeEvent) items.get(position)), TAG );
            }
            return true;
        }

        private View.OnClickListener menuListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemHardDateTimeContextClick((HardDateTimeEvent) items.get(position), view, TAG);
                }
            }
        };

        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemHardDateTimeCompleteClick((HardDateTimeEvent) items.get(position), TAG);
                }
            }
        };

    }

    class SoftDateTimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MaterialCardView cvItem;
        private TextView title;
        private TextView tvTime;
        private TextView tvDate;
        private ImageView ivType;
        private ImageView ivComplete;
        private ImageView ivMenu;

        public SoftDateTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cvItem);
            title = itemView.findViewById(R.id.tv);
            ivType = itemView.findViewById(R.id.ivType);
            ivComplete = itemView.findViewById(R.id.ivComplete);
            ivComplete.setOnClickListener(completeListener);
            ivMenu = itemView.findViewById(R.id.ivMenu);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvTime.setText("когда-угодно");

            tvDate = itemView.findViewById(R.id.tvDate);

            //cvItem.setCardBackgroundColor(Color.parseColor("#bed2f7"));
            cvItem.setOnClickListener(this);
            cvItem.setOnLongClickListener(this);
            ivMenu.setOnClickListener(menuListener);
        }

        void bind(SoftDateTimeEvent event){
            title.setText(event.id + " " + event.title);

            if (event.timeS == -1 && event.timeE == -1){
                tvTime.setText("когда угодно - ");
            }
            else {
                tvTime.setText("когда угодно - " + timeFormat.print(event.timeS) + " - " + timeFormat.print(event.timeE));
            }


            if (event.date == -1){
                tvDate.setText("на неделе");
            }
            else if (event.date == -2){
                tvDate.setText("в месяц");
            }
            else {
                tvDate.setText(dateFormat.print(event.date));
            }

            if (event.isCompleted){
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_green);
            }
            else{
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_red);
            }

            if (event.priority == Constants.EVENT_URGENT){
                ivType.setVisibility(View.VISIBLE);
                ivType.setImageResource(R.drawable.ic_exclamation_mark);
            }
            else if (event.priority == Constants.EVENT_CLARIFY){
                ivType.setVisibility(View.VISIBLE);
                ivType.setImageResource(R.drawable.ic_exclamation_mark__1_);
            }
            else ivType.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemSoftDateTimeClick( ((SoftDateTimeEvent) items.get(position)) );
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                toggleSelection(items.get(position));
                listener.onItemSoftDateTimeLongClick( ((SoftDateTimeEvent) items.get(position)), TAG );
            }
            return true;
        }

        private View.OnClickListener menuListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemSoftDateTimeContextClick((SoftDateTimeEvent) items.get(position), view, TAG);
                }
            }
        };

        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemSoftDateTimeCompleteClick((SoftDateTimeEvent) items.get(position), TAG);
                }
            }
        };
    }

    class BudgetDateTimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MaterialCardView cvItem;
        private TextView title;
        private ImageView ivType;
        private TextView duration;
        private TextView tvDate;
        private ImageView ivComplete;
        private ImageView ivMenu;

        public BudgetDateTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cvItem);
            title = itemView.findViewById(R.id.tv);
            duration = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivType = itemView.findViewById(R.id.ivType);
            ivComplete = itemView.findViewById(R.id.ivComplete);
            ivComplete.setOnClickListener(completeListener);

            ivMenu = itemView.findViewById(R.id.ivMenu);
            //cvItem.setCardBackgroundColor(Color.parseColor("#fcf1b6"));

            cvItem.setOnClickListener(this);
            cvItem.setOnLongClickListener(this);
            ivMenu.setOnClickListener(menuListener);
        }

        void bind(BudgetDateTimeEvent event){

            title.setText(event.id + " " + event.title);
            //date.setText(new LocalDate(event.date).toString(dateFormat));
            duration.setText(PeriodFormat.wordBased(locale).print(new Period(event.duration)) + " - " + timeFormat.print(event.timeS) + " - " + timeFormat.print(event.timeE));


            if (event.date == -1){
                tvDate.setText("на неделе");
            }
            else if (event.date == -2){
                tvDate.setText("в месяц");
            }
            else {
                tvDate.setText(dateFormat.print(event.date));
            }

            if (event.isCompleted){
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_green);
            }
            else{
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_red);
            }

            if (event.priority == Constants.EVENT_URGENT){
                ivType.setImageResource(R.drawable.ic_exclamation_mark);
            }
            else if (event.priority == Constants.EVENT_CLARIFY){
                ivType.setImageResource(R.drawable.ic_exclamation_mark__1_);
            }

        }

        void setCardColor(int color){
            cvItem.setCardBackgroundColor(color);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemBudgetDateTimeClick( ((BudgetDateTimeEvent) items.get(position)) );
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                toggleSelection(items.get(position));
                listener.onItemBudgetDateTimeLongClick( ((BudgetDateTimeEvent) items.get(position)), TAG );
            }
            return true;
        }

        private View.OnClickListener menuListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemBudgetDateTimeContextClick((BudgetDateTimeEvent) items.get(position), view, TAG);
                }
            }
        };

        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemBudgetDateTimeCompleteClick((BudgetDateTimeEvent) items.get(position), TAG);
                }
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    class DTEViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView date;
        private CheckBox iv;
        private RecyclerView rv;

        public DTEViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            date = itemView.findViewById(R.id.tvTime);
            iv = itemView.findViewById(R.id.iv);
            rv = itemView.findViewById(R.id.rvSteaks);
            itemView.setBackgroundColor(Color.GRAY);
            itemView.setOnClickListener(this);

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        void bind(DTEventEWithSubDTEvents dtEventEWithSubDTEvents){


            title.setText(dtEventEWithSubDTEvents.dateTimeEvent.id + " " + dtEventEWithSubDTEvents.dateTimeEvent.title);
            iv.setChecked(dtEventEWithSubDTEvents.dateTimeEvent.isCompleted);
            date.setText(new LocalDate(dtEventEWithSubDTEvents.dateTimeEvent.date).toString(dateFormat));

            LinearLayoutManager layoutManager = new LinearLayoutManager(rv.getContext(), LinearLayoutManager.VERTICAL, false);


            SubAdapter childAdapter = new SubAdapter(dtEventEWithSubDTEvents.SubEvents, TAG);


            rv.setLayoutManager(layoutManager);
            rv.setAdapter(childAdapter);
            rv.setRecycledViewPool(sharedPool);


            rv.setAdapter(childAdapter);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemDateTimeEvent( ((DTEventEWithSubDTEvents) items.get(position)) );
            }
        }

    }

    class GoalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MaterialCardView cvItem;
        private TextView title;
        private TextView tvProgress;
        private TextView tvDeadline;
        private TextView tvForecast;
        private TextView unitCount;
        private TextView spentHours;
        private TextView tvLeftHours;
        private TextView tvUnitPerHour;
        private TextView budgetHoursPerWeek;
        private TextView tvLeftWeekWork;
        private TextView tvLeftWeekDeadLine;

        private ImageView ivComplete;
        private ImageView ivMenu;

        private RelativeLayout rlExpand;
        private ImageView ivExpand;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cvItem);
            cvItem.setOnClickListener(this);

            title = itemView.findViewById(R.id.tvTitle);
            tvProgress = itemView.findViewById(R.id.tvProgress);

            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            tvForecast = itemView.findViewById(R.id.tvForecast);

            unitCount = itemView.findViewById(R.id.unitCount);

            spentHours = itemView.findViewById(R.id.spentHours);
            tvLeftHours = itemView.findViewById(R.id.tvLeftHours);
            tvUnitPerHour = itemView.findViewById(R.id.tvUnitPerHour);
            budgetHoursPerWeek = itemView.findViewById(R.id.budgetHoursPerWeek);
            tvLeftWeekWork = itemView.findViewById(R.id.tvLeftWeekWork);
            tvLeftWeekDeadLine = itemView.findViewById(R.id.tvLeftWeekDeadLine);

            ivComplete = itemView.findViewById(R.id.ivComplete);
            ivMenu = itemView.findViewById(R.id.ivMenu);

            rlExpand = itemView.findViewById(R.id.rlExpand);
            ivExpand = itemView.findViewById(R.id.ivExpand);

            ivExpand.setOnClickListener(expandListener);
        }

        void bind(Goal goal){
            title.setText(goal.getId() + " " + goal.getTitle());

            unitCount.setText("Сделано: " + goal.getCompleteUnitCount() + " / " + goal.getUnitCount());
            spentHours.setText("Затрачено часов: " + String.valueOf(goal.getHoursSpent()));

            double h = goal.getHoursLeft();
            tvLeftHours.setText("Осталось часов: " + df.format(h));

            tvUnitPerHour.setText("Знаков в час: " + String.valueOf(goal.getUnitsPerHour()));

            double d = goal.getUnitsPerHour();
            tvUnitPerHour.setText("Знаков в час: " + df.format(d));

            double b = goal.getBudgetWeek();
            budgetHoursPerWeek.setText("Бюджет ч/нед: " + df.format(b));

            tvLeftWeekWork.setText("Осталось недель работы: " + String.valueOf(goal.getWeeksWork()));
            tvLeftWeekDeadLine.setText("Осталось недель до дедлайна: " + String.valueOf(goal.getWeeksBeforeDeadline()));

            tvProgress.setText("Прогресс: " + String.valueOf((goal.getCompleteUnitCount() / goal.getUnitCount()) * 100));
            tvDeadline.setText("Дедлайн: " + dateFormat.print(goal.getDeadline()));

            float k = goal.getWeeksWork() * 7;

            LocalDate ld = new LocalDate().plusDays(Math.round(k));

            tvForecast.setText("Дата окончания работы: " + dateFormat.print(ld.toDate().getTime()));


            if (goal.isCompleted()){
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_green);
            }
            else{
                ivComplete.setImageResource(R.drawable.ic_baseline_done_24_red);
            }


            rlExpand.setVisibility(goal.isExpanded() ? View.VISIBLE : View.GONE);

            ivExpand.setImageResource(goal.isExpanded() ? R.drawable.ic_baseline_expand_less_24 : R.drawable.ic_baseline_expand_more_24);
        }



        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    listener.onItemFrogCompleteClick((Frog) items.get(position), TAG);
                }
            }
        };

        private View.OnClickListener expandListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    listener.onItemGoalExpandClick((Goal) items.get(position), TAG);
                }
            }
        };


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemGoalClick((Goal) items.get(position), TAG);
            }
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvHour;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHour = itemView.findViewById(R.id.tv_item_title);
        }

        void bind(HeaderItem headerItem){
            tvHour.setText(headerItem.getHeaderText());
        }

    }

    class PlaceholderViewHolder extends RecyclerView.ViewHolder{

        private CardView cv;
        private TextView tv;

        public PlaceholderViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cvItem);
            tv = itemView.findViewById(R.id.tv);
        }

        void bind(Placeholder placeholder){

            //PeriodFormat.wordBased(locale).print(new Period(event.duration)) + " - " + timeFormat.print(event.timeS) + " - " + timeFormat.print(event.timeE))

            Period period = new Period(placeholder.timeS, placeholder.timeE);

            if (period.toStandardSeconds().getSeconds() > 0){
                tv.setText(PeriodFormat.wordBased(locale).print(period));
            }

            tv.setGravity(Gravity.CENTER_HORIZONTAL);



                //tv.setText(timeFormat.print(placeholder.timeS) + " - " + timeFormat.print(placeholder.timeE));
            //CardView.LayoutParams params = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,
            //        period.toStandardMinutes().getMinutes()*2);

            //cv.setLayoutParams(params);


        }

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case ItemTypeInterfaсe.EVENT_FROG:
                return new FrogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_new, parent, false));
            case ItemTypeInterfaсe.EVENT_ELEPHANT:
                return new ElephantViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_ex_new, parent, false));
            case ItemTypeInterfaсe.EVENT_STEAK:
                return new SteakViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_sub_new, parent, false));
            case ItemTypeInterfaсe.KAIROS:
                return new KairosViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_kairos_ex_new, parent, false));
            case ItemTypeInterfaсe.EVENT_KAIROS:
                return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_new, parent, false));
            case ItemTypeInterfaсe.EVENT_STEAKDATE:
                return new DateSteakViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_new, parent, false));
            case ItemTypeInterfaсe.EVENT_HARD_DTE:
                return new HardDateTimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_hardtime_item, parent, false));
            case ItemTypeInterfaсe.EVENT_SOFT_DTE:
                return new SoftDateTimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_hardtime_item, parent, false));
            case ItemTypeInterfaсe.EVENT_BUDGET_DTE:
                return new BudgetDateTimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_hardtime_item, parent, false));
            case ItemTypeInterfaсe.EVENT_DTE:
                return new DTEViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_ex, parent, false));
            case ItemTypeInterfaсe.GOAL:
                return new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_goal1, parent, false));
            case ItemTypeInterfaсe.HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_item, parent, false));
            case ItemTypeInterfaсe.PLACEHOLDER:
                return new PlaceholderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_placeholder, parent, false));
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
           case ItemTypeInterfaсe.EVENT_FROG:
               Frog frog = (Frog) items.get(position);
               ((FrogViewHolder) holder).bind(frog);
               break;
           case ItemTypeInterfaсe.EVENT_ELEPHANT:
               ElephantWithSteaks elephant = ((ElephantWithSteaks) items.get(position));
               ((ElephantViewHolder) holder).bind(elephant);
               break;
           case ItemTypeInterfaсe.EVENT_STEAK:
               Steak steak = (Steak) items.get(position);
               ((SteakViewHolder) holder).bind(steak);
               break;
           case ItemTypeInterfaсe.KAIROS:
               KairosWithEvents kairos = ((KairosWithEvents) items.get(position));
               ((KairosViewHolder) holder).bind(kairos);
               break;
           case ItemTypeInterfaсe.EVENT_KAIROS:
               Event event = ((Event) items.get(position));
               ((EventViewHolder) holder).bind(event);
               break;
           case ItemTypeInterfaсe.EVENT_STEAKDATE:
               SteakView steakView = ((SteakView) items.get(position));
               ((DateSteakViewHolder) holder).bind(steakView);
               break;
           case ItemTypeInterfaсe.EVENT_HARD_DTE:
               HardDateTimeEvent hardDateTimeEvent = ((HardDateTimeEvent) items.get(position));
               ((HardDateTimeViewHolder) holder).bind(hardDateTimeEvent);
               break;
           case ItemTypeInterfaсe.EVENT_SOFT_DTE:
               SoftDateTimeEvent softDateTimeEvent = ((SoftDateTimeEvent) items.get(position));
               ((SoftDateTimeViewHolder) holder).bind(softDateTimeEvent);
               break;
           case ItemTypeInterfaсe.EVENT_BUDGET_DTE:
               BudgetDateTimeEvent budgetDateTimeEvent = ((BudgetDateTimeEvent) items.get(position));
               ((BudgetDateTimeViewHolder) holder).bind(budgetDateTimeEvent);
               break;
           case ItemTypeInterfaсe.EVENT_DTE:
               DTEventEWithSubDTEvents dtEventEWithSubDTEvents = ((DTEventEWithSubDTEvents) items.get(position));
               ((DTEViewHolder) holder).bind(dtEventEWithSubDTEvents);
               break;
           case ItemTypeInterfaсe.GOAL:
                Goal goal = ((Goal) items.get(position));
                ((GoalViewHolder) holder).bind(goal);
                break;
           case ItemTypeInterfaсe.HEADER:
               HeaderItem header = ((HeaderItem) items.get(position));
               ((HeaderViewHolder) holder).bind(header);
               break;
           case ItemTypeInterfaсe.PLACEHOLDER:
               Placeholder placeholder = (Placeholder) items.get(position);
               ((PlaceholderViewHolder) holder).bind(placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initHours(long hourMin, long hourMax){
        removeByType(ItemTypeInterfaсe.HEADER);

        LocalTime hourMi = new LocalTime(hourMin);
        LocalTime hourMa = new LocalTime(hourMax);

        if (hourMi.toDateTimeToday().getMillis() == new LocalTime("7:00").toDateTimeToday().getMillis()){
            int k =0;
        }

        while (hourMi.toDateTimeToday().getMillis() <= hourMa.toDateTimeToday().getMillis()){
            HeaderItem headerItem = new HeaderItem(hourMi.toString("HH:mm"));
            items.add((T) headerItem);
            //hourMi = hourMi.plusMinutes(15);
            //if (!hourWithinEvent(hourMi.toDateTimeToday().getMillis())){
                hourMi = hourMi.plusHours(1);
            //}

        }

        setValues(items);

        notifyDataSetChanged();
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean hourWithinEvent(long time){

        return items.stream().anyMatch(date -> date instanceof HardDateTimeEvent && time < ((HardDateTimeEvent) date).timeE && time > ((HardDateTimeEvent) date).timeS);


        /*items.forEach(i -> {
            if (i instanceof HardDateTimeEvent){
                if (time > ((HardDateTimeEvent) i).timeS && time < ((HardDateTimeEvent) i).timeE){
                    return true;
                }
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateHours(){
        //initHours(hourMin, hourMax);

        //List<T> newItems = sort(items);
        //items.clear();

        //items.addAll(newItems);
        //notifyDataSetChanged();


        ListIterator<T> iterator = items.listIterator();

        while (iterator.hasNext()){
            T item = iterator.next();
            if (item.getType() == ItemTypeInterfaсe.HEADER){
                long time = new LocalTime( ((HeaderItem)item).getHeaderText() ).toDateTimeToday().getMillis();
                if (hourWithinEvent( time)){
                    iterator.remove();
                }
            }
        }

        //items.forEach(i -> {
        //    if (i.getType() == ItemTypeInterfaсe.HEADER){
        //        if (hourWithinEvent( (HeaderItem)i ))
        //    }
        //});
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initPlaceHolders(){

        // Удаляем все плейсхолдеры
        removeByType(ItemTypeInterfaсe.PLACEHOLDER);

        List<T> newItems = new ArrayList<>();
        newItems.addAll(items);

        for (int i = 0; i < items.size()-1; i++){
            if (items.get(i) instanceof HeaderItem){
                long timeS = new LocalTime(((HeaderItem) items.get(i)).getHeaderText()).toDateTimeToday().getMillis();

                if (items.get(i+1) instanceof HardDateTimeEvent){
                    long timeE = new LocalTime(((HardDateTimeEvent) items.get(i+1)).timeS).toDateTimeToday().getMillis();

                    Placeholder placeholder = new Placeholder();
                    placeholder.timeS = timeS;
                    placeholder.timeE = timeE;
                    Period period = new Period(timeS, timeE);
                    if (period.toStandardSeconds().getSeconds()>0){
                        newItems.add(newItems.indexOf(items.get(i))+1, (T) placeholder);
                    }
                }
                else if (items.get(i+1) instanceof HeaderItem){
                    long timeE = new LocalTime(((HeaderItem) items.get(i+1)).getHeaderText()).toDateTimeToday().getMillis();
                    Placeholder placeholder = new Placeholder();
                    placeholder.timeS = timeS;
                    placeholder.timeE = timeE;
                    Period period = new Period(timeS, timeE);
                    if (period.toStandardSeconds().getSeconds()>0){
                        newItems.add(newItems.indexOf(items.get(i))+1, (T) placeholder);
                    }
                }
            }
            else if (items.get(i) instanceof HardDateTimeEvent){
                long timeS = new LocalTime(((HardDateTimeEvent) items.get(i)).timeE).toDateTimeToday().getMillis();

                if (items.get(i+1) instanceof HeaderItem){
                    long timeE = new LocalTime(((HeaderItem) items.get(i+1)).getHeaderText()).toDateTimeToday().getMillis();
                    Placeholder placeholder = new Placeholder();
                    placeholder.timeS = timeS;
                    placeholder.timeE = timeE;
                    Period period = new Period(timeS, timeE);
                    if (period.toStandardSeconds().getSeconds()>0){
                        newItems.add(newItems.indexOf(items.get(i))+1, (T) placeholder);
                    }
                }
                else if (items.get(i+1) instanceof HardDateTimeEvent){
                    long timeE = new LocalTime(((HardDateTimeEvent) items.get(i+1)).timeS).toDateTimeToday().getMillis();

                    Placeholder placeholder = new Placeholder();
                    placeholder.timeS = timeS;
                    placeholder.timeE = timeE;
                    Period period = new Period(timeS, timeE);
                    if (period.toStandardSeconds().getSeconds()>0){
                        newItems.add(newItems.indexOf(items.get(i))+1, (T) placeholder);
                    }
                }
            }
        }


        /*for (int i = 0; i < items.size()-1; i++){
            if (items.get(i).getType() == ItemTypeInterfaсe.HEADER){
                if (items.get(i+1) instanceof HardDateTimeEvent){
                    LocalTime header = new LocalTime(((HeaderItem)items.get(i)).getHeaderText());
                    LocalTime hdte =  new LocalTime(((HardDateTimeEvent)items.get(i+1)).timeS);

                    if (header.toDateTimeToday().getMillis() < hdte.toDateTimeToday().getMillis()){
                        Placeholder placeholder = new Placeholder(header.toDateTimeToday().getMillis(), hdte.toDateTimeToday().getMillis());
                        newItems.add(i+1, (T) placeholder);
                    }
                }
            }
            else if (items.get(i) instanceof HardDateTimeEvent){
                if (items.get(i+1) instanceof  HardDateTimeEvent){
                    LocalTime startTime = new LocalTime(((HardDateTimeEvent)items.get(i)).timeE);
                    LocalTime endTime = new LocalTime(((HardDateTimeEvent)items.get(i+1)).timeS);

                    if (startTime.toDateTimeToday().getMillis() < endTime.toDateTimeToday().getMillis()){
                        Placeholder placeholder = new Placeholder(startTime.toDateTimeToday().getMillis(), endTime.toDateTimeToday().getMillis());
                        newItems.add(i+1, (T) placeholder);
                    }
                }
                else if (items.get(i+1) instanceof HeaderItem){
                    LocalTime startTime = new LocalTime(((HardDateTimeEvent)items.get(i)).timeE);
                    LocalTime header = new LocalTime(((HeaderItem)items.get(i+1)).getHeaderText());

                    if (startTime.toDateTimeToday().getMillis() < header.toDateTimeToday().getMillis()){
                        Placeholder placeholder = new Placeholder(startTime.toDateTimeToday().getMillis(), header.toDateTimeToday().getMillis());
                        newItems.add(i+1, (T) placeholder);
                    }
                }
            }
        }*/

        /*items.forEach(i ->{
            if (items.indexOf(i) != items.size() - 1 && i.getType()==ItemTypeInterfaсe.HEADER){

                // Получаем следующий час
                int endPos = indexOfNextHour(items.indexOf(i));

                // Получаем все элементы в пределах часа
                List<T> allHourItems = items.subList(items.indexOf(i), endPos);



                Placeholder placeholder = new Placeholder();
                newItems.add(newItems.indexOf(i)+1, (T) placeholder);
            }
        });*/

        items.clear();

        //items = sort(newItems);

        items.addAll(newItems);
        notifyDataSetChanged();
    }


    public List<T> getAllItemsBetween(long timeStart, long timeEnd){
        List<T> result = new ArrayList<>();
        ListIterator<T> listIterator = items.listIterator(getHeaderByTime(timeStart));
        while (listIterator.hasNext()){
            T t = listIterator.next();
            if (t.getType() == ItemTypeInterfaсe.HEADER)
                break;
            result.add(t);
        }
        return result;
    }

    public int getHeaderByTime(long time){
        int res = -1;
        for (T item: items){
            if (item.getType() == ItemTypeInterfaсe.HEADER){
                if ( ((HeaderItem)item).getHeaderText().equals(timeFormat.print(time)) ){
                    return items.indexOf(item);
                }
            }
        }
        return -1;
    }

    protected int indexOfNextHour(int position){
        ListIterator<T> listIterator = items.listIterator(position);
        while (listIterator.hasNext()){
            T t = listIterator.next();
            if (t.getType() == ItemTypeInterfaсe.HEADER)
                return items.indexOf(t);
        }
        return -1;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeByType(int type){
        Iterator<T> listIterator = items.iterator();
        while (listIterator.hasNext()){
            T t = listIterator.next();
            if (t.getType() == type){
                listIterator.remove();
            }
        }

        /*items.forEach(i -> {
            if (i.getType() == type){
                items.remove(i);
            }
        });*/
        notifyDataSetChanged();
    }

    public long getTimeAbove(int position){
        ListIterator<T> listIterator = items.listIterator(position);
        while (listIterator.hasPrevious()){
            T t = listIterator.previous();
            if (t.getType()!=ItemTypeInterfaсe.PLACEHOLDER){
                if (t.getType() == ItemTypeInterfaсe.HEADER){
                    LocalTime lt = new LocalTime(((HeaderItem)t).getHeaderText());
                    return lt.toDateTimeToday().getMillis();
                }
               else{
                   LocalTime lt = new LocalTime(((HardDateTimeEvent)t).timeE);
                   return lt.toDateTimeToday().getMillis();
                }

            }

        }
        return -1;
    }

    public long getTimeBelow(int position){


        ListIterator<T> listIterator = items.listIterator(position+1);
        while (listIterator.hasNext()){
            T t = listIterator.next();
            if (t.getType()!=ItemTypeInterfaсe.PLACEHOLDER){
                if (t.getType() == ItemTypeInterfaсe.HEADER){
                    LocalTime lt = new LocalTime(((HeaderItem)t).getHeaderText());
                    return lt.toDateTimeToday().getMillis();
                }
                else{
                    LocalTime lt = new LocalTime(((HardDateTimeEvent)t).timeS);
                    return lt.toDateTimeToday().getMillis();
                }

            }

        }
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setHourMin(long hourMin){
        this.hourMin = hourMin;
        initPlaceHolders();
        initHours(hourMin, hourMax);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setHourMax(long hourMax){
        this.hourMax = hourMax;
        initPlaceHolders();
        initHours(hourMin, hourMax);
    }

    public void setItems(List<T> newItems){
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<T> setItems(List<T> newItems, int type){
        List<T> result = new ArrayList<>();
        result.addAll(items);

        for (T item: items){
            if (item.getType() == type){
                result.remove(item);
            }
        }

        result.addAll(newItems);

        //newItems.forEach(i -> {
        //    result.add(0, i);
       // });
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setValues(List<T> events){
        List<T> sorted = sort(events);
            //final EventDiffCallback diffCallback = new EventDiffCallback(items, sorted);
            //final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, true);

        items.clear();
        items.addAll(sorted);

        //initPlaceHolders();
            //diffResult.dispatchUpdatesTo(this);
        sharedPool = new RecyclerView.RecycledViewPool();

        //initPlaceHolders();
        notifyDataSetChanged();
    }

    public void remove(int position){
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void set(int position, T item){
        items.set(position, item);
        notifyDataSetChanged();
    }

    public T get(int position){
        return items.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<T> sort(List<T> events){
        List<T> res = new ArrayList<>();
        res.addAll(events);
        Collections.sort(res, new Comparator<T>() {
            @Override
            public int compare(T t, T t1) {

                if (t instanceof HardDateTimeEvent && t1 instanceof HardDateTimeEvent){
                    return Long.compare(((HardDateTimeEvent) t).timeS, ((HardDateTimeEvent) t1).timeS);
                }

                if ( (t instanceof HeaderItem && t1 instanceof HardDateTimeEvent) || (t instanceof HardDateTimeEvent && t1 instanceof HeaderItem) ){
                    long timestart = t instanceof HeaderItem ? new LocalTime(((HeaderItem) t).getHeaderText()).toDateTimeToday().getMillis() : ((HardDateTimeEvent) t).timeS;
                    long timeend = t1 instanceof HeaderItem ? new LocalTime(((HeaderItem) t1).getHeaderText()).toDateTimeToday().getMillis() : ((HardDateTimeEvent) t1).timeS;

                    return Long.compare(timestart, timeend);
                }

                if (t instanceof Frog && t1 instanceof Frog){
                    if (((Frog) t).isCompleted && !((Frog) t1).isCompleted){
                        return 1;
                    }
                    else if (!((Frog) t).isCompleted && ((Frog) t1).isCompleted){
                        return -1;
                    }
                    return 0;
                }

                if (t instanceof ElephantWithSteaks && t1 instanceof ElephantWithSteaks){
                    if (((ElephantWithSteaks) t).elephant.isCompleted && !((ElephantWithSteaks) t1).elephant.isCompleted){
                        return 1;
                    }
                    else if (!((ElephantWithSteaks) t).elephant.isCompleted && ((ElephantWithSteaks) t1).elephant.isCompleted){
                        return -1;
                    }
                    return 0;
                }

                return 0;

            }
        });

        return res;
    }

    public void clearSelection(){
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public List<T> getSelectedItems(){
        return selectedItems;
    }

    public void toggleSelection(T item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        notifyItemChanged(items.indexOf(item));
    }


    public void setOnClickListeners(RecyclerViewClickInterface newListener){
        listener = newListener;
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<T> getItems(){
        return items;
    }

    public T getItem(int position){
        return items.get(position);
    }



}
