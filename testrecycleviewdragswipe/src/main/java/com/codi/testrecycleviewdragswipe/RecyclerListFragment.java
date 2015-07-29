package com.codi.testrecycleviewdragswipe;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codi.testrecycleviewdragswipe.helper.OnStartDragListener;

import java.util.ArrayList;

/**
 * Created by Codi on 2015/7/27 0027.
 */
public class RecyclerListFragment extends Fragment implements OnStartDragListener {

    private RecyclerView mRecyclerView;
    private RecyclerListAdapter mAdapter;
    private ItemTouchHelper mHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> datas = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            datas.add("Item " + i);
        }

        mAdapter = new RecyclerListAdapter(getActivity().getApplicationContext(), datas, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mHelper = new ItemTouchHelper(callback);
        mHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mHelper.startDrag(viewHolder);
    }
}
