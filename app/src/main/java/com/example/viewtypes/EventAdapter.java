package com.example.viewtypes;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewtypes.ui.RecyclerViewClickInterface;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Item> items;
    private static RecyclerViewClickInterface listener;
    private RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();


    public EventAdapter() {
        items = new ArrayList<>();
    }

    public EventAdapter(List<Item> newItems){
        items = newItems;


    }


    static class FrogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private CheckBox iv;

        public FrogViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);
            itemView.setBackgroundColor(Color.CYAN);
            itemView.setOnClickListener(this);
        }

        void bind(Frog frog){
            title.setText(frog.id + frog.title);
            iv.setChecked(frog.isCompleted);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemFrogClick(frog);
                }
            });*/
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                //listener.onItemFrogClick( ((Frog) items.get(position).object), TAG );
            }
        }
    }

    class ElephantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView title;
        private CheckBox iv;
        private RecyclerView rv;

        public ElephantViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);
            rv = itemView.findViewById(R.id.rvSteaks);
            itemView.setBackgroundColor(Color.GRAY);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        /*void bind(Elephant elephant, RecyclerViewClickInterface listener){
            title.setText(elephant.id + elephant.title);
            iv.setChecked(elephant.isCompleted);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemElephantClick(elephant);
                }
            });

            //LinearLayoutManager layoutManager = new LinearLayoutManager()
        }*/

        @RequiresApi(api = Build.VERSION_CODES.N)
        void bind(ElephantWithSteaks elephantWithSteaks){
            Log.d("НЕНГ", "Слон = " + String.valueOf(elephantWithSteaks.elephant.id));
            title.setText(elephantWithSteaks.elephant.id + " " + elephantWithSteaks.elephant.title + '\n');
            iv.setChecked(elephantWithSteaks.elephant.isCompleted);

            elephantWithSteaks.steaks.forEach(i -> title.append(i.title + " "));

            List<Item> itemsSteaks = new ArrayList<>();
            elephantWithSteaks.steaks.forEach(i -> itemsSteaks.add(new Item(Constants.EVENT_STEAK, i)));



            LinearLayoutManager layoutManager = new LinearLayoutManager(rv.getContext(), LinearLayoutManager.VERTICAL, false);




            SubAdapter childAdapter = new SubAdapter(itemsSteaks, 4);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(childAdapter);
            rv.setRecycledViewPool(sharedPool);


            //childAdapter.setHasStableIds(true);
            //childAdapter.setValues(setSteaks(elephantWithSteaks.steaks));



            rv.setAdapter(childAdapter);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                //listener.onItemElephantClick( ((ElephantWithSteaks) items.get(position).object) );
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                //listener.onItemElephantLongClick( ((ElephantWithSteaks) items.get(position).object) );
                return true;
            }
            return false;
        }
    }

    static class SteakViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private CheckBox iv;

        public SteakViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);
            itemView.setBackgroundColor(Color.YELLOW);
            itemView.setOnClickListener(this);
        }

        void bind(Steak steak){
            title.setText(steak.id + steak.title);
            iv.setChecked(steak.isCompleted);


            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemSteakClick(steak);
                }
            });*/
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                //listener.onItemSteakClick( ((Steak) items.get(position).object) );
            }
        }
    }

////////////////////////////////////

    class KairosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView title;
        private CheckBox iv;
        private RecyclerView rv;

        public KairosViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);
            rv = itemView.findViewById(R.id.rvSteaks);
            itemView.setBackgroundColor(Color.parseColor("#91b3f2"));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        void bind(KairosWithEvents kairosWithEvents){

            title.setText(kairosWithEvents.kairos.kairosId + " = " + kairosWithEvents.kairos.title);

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    rv.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);

            //layoutManager.setStackFromEnd(true);


            List<Item> itemsEvents = new ArrayList<>();

            kairosWithEvents.events.forEach(i -> itemsEvents.add(new Item(Constants.EVENT_KAIROS, i)));


            layoutManager.setInitialPrefetchItemCount(itemsEvents.size());

            SubAdapter childAdapter = new SubAdapter(itemsEvents,3);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(childAdapter);



            rv.setRecycledViewPool(sharedPool);




        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION){
                listener.onItemKairosWithEventsClick( ((KairosWithEvents) items.get(position).object), 1 );
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView title;
        private CheckBox iv;
        private RecyclerView rv;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);
            rv = itemView.findViewById(R.id.rvSteaks);
            itemView.setBackgroundColor(Color.parseColor("#ffc8a8"));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        void bind(EventsWithKairos eventsWithKairos){

            title.setText(eventsWithKairos.event.id + " = " + eventsWithKairos.event.title);

            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    rv.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);



            List<Item> itemsKairoses = new ArrayList<>();

            eventsWithKairos.kairoses.forEach(i -> itemsKairoses.add(new Item(Constants.KAIROS, i)));


            layoutManager.setInitialPrefetchItemCount(itemsKairoses.size());

            SubAdapter childAdapter = new SubAdapter(itemsKairoses, 5);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(childAdapter);

            rv.setRecycledViewPool(sharedPool);



        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case Constants.EVENT_FROG:
                return new FrogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
            case Constants.EVENT_ELEPHANT:
                return new ElephantViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_ex, parent, false));
            case Constants.EVENT_STEAK:
                return new SteakViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
            case Constants.KAIROS:
                return new KairosViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_ex, parent, false));
            case Constants.EVENT_KAIROS:
                return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_ex, parent, false));
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       switch (getItemViewType(position)){
           case Constants.EVENT_FROG:
               Frog frog = (Frog) items.get(position).object;
               ((FrogViewHolder) holder).bind(frog);
               break;
           case Constants.EVENT_ELEPHANT:
               ElephantWithSteaks elephant = ((ElephantWithSteaks) items.get(holder.getAdapterPosition()).object);
               ((ElephantViewHolder) holder).bind(elephant);
               break;
           case Constants.EVENT_STEAK:
               Steak steak = (Steak) items.get(position).object;
               ((SteakViewHolder) holder).bind(steak);
               break;
           case Constants.KAIROS:
               KairosWithEvents kairos = ((KairosWithEvents) items.get(position).object);
               ((KairosViewHolder) holder).bind(kairos);
               break;
           case Constants.EVENT_KAIROS:
               EventsWithKairos event = ((EventsWithKairos) items.get(position).object);
               ((EventViewHolder) holder).bind(event);
               break;
       }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }

    public void setItems(List<Item> newItems){
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }


    public List<Item> setItems(List<Item> newItems, int type){
        List<Item> result = new ArrayList<>();
        result.addAll(items);

        for (Item item: items){
            if (item.type == type){
                result.remove(item);
            }
        }

        result.addAll(newItems);
        return result;
    }

    public void setValues(List<Item> events){
        List<Item> sorted = sort(events);
        final EventDiffCallback diffCallback = new EventDiffCallback(items, sorted);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, true);

        items.clear();
        items.addAll(sorted);
        diffResult.dispatchUpdatesTo(this);
        sharedPool = new RecyclerView.RecycledViewPool();
        notifyDataSetChanged();
    }

    public void add(Item item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<Item> newitems){
        items.addAll(newitems);
        notifyDataSetChanged();
    }

    public List<Item> sort(List<Item> events){

        List<Item> res = new ArrayList<>();
        res.addAll(events);
        /*Collections.sort(res, new Comparator<Item>() {
            @Override
            public int compare(Item item, Item t1) {
                if (!t1.isCompleted() && item.isCompleted()){
                    return 1;
                }
                if (t1.isCompleted() && !item.isCompleted()){
                    return -1;
                }
                return Integer.compare(item.getId(), t1.getId());
            }
        });*/
        return res;
    }



    public void setOnClickListeners(RecyclerViewClickInterface newListener){
        listener = newListener;
    }

    public List<Item> getItems(){
        return items;
    }



}
