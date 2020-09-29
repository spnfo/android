package com.example.spnfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spnfo.databinding.RacerRowBinding;

import java.util.ArrayList;
import java.util.List;

public class RacerListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RacerRowAdapter mAdapter;
    private List<RacerRow> mModels;

    private static final String[] TAGS = new String[]{ "AAAAAA", "BBBBBB", "CCCCCC", "DDDDDD", "EEEEEE", "FFFFFF", "GGGGGG", "HHHHHH", "IIIIII", "JJJJJJ" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.racer_list_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new RacerRowAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

//        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
//        mLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(mLayoutManager);

        mModels = new ArrayList<>();
        for (String tag : TAGS) {
            mModels.add(new RacerRow(tag));
        }

        mAdapter.add(mModels);

        return v;
    }

    public void updateDataSet() {

    }

    public void filterDataSet() {

    }
}
