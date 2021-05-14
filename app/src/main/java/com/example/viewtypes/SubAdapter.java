package com.example.viewtypes;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewtypes.ui.RecyclerViewClickInterface;
import com.google.android.material.card.MaterialCardView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.viewtypes.Constants.getDayOfWeek;

public class SubAdapter<T extends ItemTypeInterfaсe> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<T> items;
    private RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();
    private static RecyclerViewClickInterface listener;
    private Context context;
    DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd MMMM");
    DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm");
    Locale locale = new Locale("ru");
    DateTime.Property pDow;

    private final int TAG;

    public SubAdapter(int newTag) {
        items = new ArrayList<>();
        TAG = newTag;
    }

    public SubAdapter(List<T> newItems, int newTag) {
        items = newItems;
        TAG = newTag;
    }

    class SteakViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView tvDate;
        private ImageView ivType;
        private ImageView ivComplete;



        public SteakViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            tvDate = itemView.findViewById(R.id.tvTime);
            ivType = itemView.findViewById(R.id.ivType);
            ivType.setImageResource(R.drawable.ic_steak);
            ivComplete = itemView.findViewById(R.id.ivComplete);
            //itemView.setBackgroundColor(Color.YELLOW);
            itemView.setOnClickListener(this);
            ivComplete.setOnClickListener(completeListener);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        void bind(Steak steak){
            title.setText(steak.id + steak.title);
            steak.days.forEach(i -> {
                tvDate.append(getDayOfWeek(i) + " ");
            });


            if (steak.isCompleted){
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
                listener.onItemSteakClick(((Steak) items.get(position)) , 5);
            }
        }

        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    listener.onItemSteakCompleteClick((Steak) items.get(position), TAG);
                }
            }
        };
    }

    /////////////////////////////////////////////////////////////////////////////////

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MaterialCardView cvItem;
        private TextView title;
        private ImageView ivComplete;
        private ImageView ivType;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            cvItem = itemView.findViewById(R.id.cvItem);
            cvItem.setOnClickListener(this);

            ivType = itemView.findViewById(R.id.ivType);
            ivType.setImageResource(R.drawable.ic_baseline_psychology_24);

            itemView.setBackgroundColor(Color.parseColor("#ffc8a8"));
            ivComplete = itemView.findViewById(R.id.ivComplete);
            ivComplete.setOnClickListener(completeListener);
        }

        void bind(Event event){
            title.setText(event.id + " = " + event.title);


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
                listener.onItemEventClick(((Event) items.get(position)) , 5);
            }
        }

        private View.OnClickListener completeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    listener.onItemEventCompleteClick((Event) items.get(position), TAG);
                }
            }
        };
    }


    class KairosViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public KairosViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            itemView.setBackgroundColor(Color.parseColor("#91b3f2"));
        }

        void bind(Kairos kairos){
            title.setText(kairos.kairosId + " = " + kairos.title);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////

    class SubDTEViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title, date;
        private CheckBox iv;

        public SubDTEViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            date = itemView.findViewById(R.id.tvTime);
            iv = itemView.findViewById(R.id.iv);
            //itemView.setBackgroundColor(Color.YELLOW);
            itemView.setOnClickListener(this);
        }

        void bind(SubDateTimeEvent subDateTimeEvent){
            title.setText(subDateTimeEvent.id + " " + subDateTimeEvent.title);
            date.setText(new LocalDate(subDateTimeEvent.dateS).toString(dateFormat) + " - " +  new LocalDate(subDateTimeEvent.date).toString(dateFormat));
            iv.setChecked(subDateTimeEvent.isCompleted);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemSubDateTimeEvent(((SubDateTimeEvent) items.get(position)) );
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemTypeInterfaсe.EVENT_STEAK:
                return new SteakViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_sub_new, parent, false));
            case ItemTypeInterfaсe.EVENT_KAIROS:
                return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_sub_new, parent, false));
            case ItemTypeInterfaсe.KAIROS:
                return new KairosViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_sub, parent, false));
            case ItemTypeInterfaсe.EVENT_SUB_DTE:
                return new SubDTEViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_sub, parent, false));
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ItemTypeInterfaсe.EVENT_STEAK:
                Steak steak = (Steak) items.get(holder.getAdapterPosition());
                ((SteakViewHolder) holder).bind(steak);
                Log.d("НЕНГ", "________Жаба = " + String.valueOf(steak.id));
                break;
            case ItemTypeInterfaсe.EVENT_KAIROS:
                Event event = (Event) items.get(position);
                ((EventViewHolder) holder).bind(event);
                break;
            case ItemTypeInterfaсe.KAIROS:
                Kairos kairos = (Kairos) items.get(position);
                ((KairosViewHolder) holder).bind(kairos);
                break;
            case ItemTypeInterfaсe.EVENT_SUB_DTE:
                SubDateTimeEvent subDateTimeEvent = (SubDateTimeEvent) items.get(holder.getAdapterPosition());
                ((SubDTEViewHolder) holder).bind(subDateTimeEvent);
                break;
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


    public void setValues(List<T> events){
        //final EventDiffCallback diffCallback = new EventDiffCallback(items, events);
        //final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, true);

        items.clear();
        items.addAll(events);
        //diffResult.dispatchUpdatesTo(this);
        sharedPool = new RecyclerView.RecycledViewPool();
        notifyDataSetChanged();
    }

    public void setOnClickListeners(RecyclerViewClickInterface newListener){
        listener = newListener;
    }



}
