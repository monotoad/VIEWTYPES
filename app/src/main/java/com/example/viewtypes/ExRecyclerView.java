package com.example.viewtypes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ExRecyclerView extends RecyclerView {
    private View mEmptyView;

    public ExRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ExRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initEmptyView(){
        if (mEmptyView != null){
            mEmptyView.setVisibility(getAdapter() == null || getAdapter().getItemCount() == 0 ? VISIBLE : GONE);
            setVisibility(getAdapter() == null || getAdapter().getItemCount() == 0 ? GONE : VISIBLE);
        }
    }

    final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            initEmptyView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            initEmptyView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            initEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            initEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            initEmptyView();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            initEmptyView();
        }


    };



    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        super.setAdapter(adapter);

        if (oldAdapter != null){
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        if (adapter != null){
            adapter.registerAdapterDataObserver(observer);
        }
        initEmptyView();
    }

    public void setEmptyView(View view){
        this.mEmptyView = view;
        initEmptyView();
    }
}
