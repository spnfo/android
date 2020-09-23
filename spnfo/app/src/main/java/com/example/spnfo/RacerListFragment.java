package com.example.spnfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RacerListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> mDataSet = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.racer_list_fragment, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        return v;
    }


    public void setDataSet(ArrayList<String> ds) {
        mDataSet = ds;

        mAdapter = new RacerRowAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
    }


    public void updateDataSet() {

    }


    public void filterDataSet() {

    }
}
