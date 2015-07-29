package com.codi.testrecycleviewdragswipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.codi.testrecycleviewdragswipe.helper.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Codi on 2015/7/10 0010.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<String> mItems;
    private Context mContext;

    public MyAdapter(Context context, ArrayList<String> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int i) {
        viewHolder.title.setText(mItems.get(i));
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
