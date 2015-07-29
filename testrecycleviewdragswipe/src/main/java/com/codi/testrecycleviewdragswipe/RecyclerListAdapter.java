package com.codi.testrecycleviewdragswipe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.codi.testrecycleviewdragswipe.helper.ItemTouchHelperAdapter;
import com.codi.testrecycleviewdragswipe.helper.ItemTouchHelperViewHolder;
import com.codi.testrecycleviewdragswipe.helper.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Codi on 2015/7/27 0027.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ListViewHolder> implements ItemTouchHelperAdapter {

    private Context mContext;
    private OnStartDragListener mItemTouchListener;
    private ArrayList<String> mItems;

    public RecyclerListAdapter(Context context, ArrayList<String> items, OnStartDragListener itemTouchListener) {
        mContext = context;
        mItems = items;
        mItemTouchListener = itemTouchListener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder listViewHolder, final int i) {

        listViewHolder.text.setText(mItems.get(i));

        listViewHolder.handle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_DOWN == event.getAction()) {
                    if (mItemTouchListener != null) {
                        mItemTouchListener.onStartDrag(listViewHolder);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        String fromValue = mItems.remove(fromPosition);
        mItems.add(toPosition > fromPosition ? toPosition - 1 : toPosition, fromValue);
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    static class ListViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        FrameLayout item;
        TextView text;
        ImageView handle;

        public ListViewHolder(View itemView) {
            super(itemView);

            item = (FrameLayout) itemView.findViewById(R.id.item);
            text = (TextView) itemView.findViewById(R.id.text);
            handle = (ImageView) itemView.findViewById(R.id.handle);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }
}
