package com.example.spnfo;

import android.os.Bundle;
import android.util.Log;
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

public class RacerListFragment extends Fragment implements
        RacerSearchBarFragment.OnSearchChangeListener, RacerRowAdapter.OnRacerRowChangeListener {

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
        mAdapter.setOnRacerRowChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mModels = new ArrayList<>();
        for (int i = 0; i < TAGS.length; i++) {
            mModels.add(new RacerRow(TAGS[i], i+1));
        }

        mAdapter.add(mModels);

        RacerSearchBarFragment rsbf = (RacerSearchBarFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.racer_search_bar_fragment);
        if (rsbf != null) {
            rsbf.setOnSearchChangeListener(this);
        }

        return v;
    }

    public void globalCheck(Boolean checkTrue) {
        mAdapter.checkAll(checkTrue);
    }

    public void onCheckBoxChanged(String tag, Boolean isChecked) {
        ((MainActivity) getActivity()).onCheckSelected(tag, isChecked);
    }

    public void filterDataset(String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<RacerRow> filteredModelList = new ArrayList<>();
        for (RacerRow model : mModels) {
            final String compareText = model.getTag().toLowerCase();
            if (compareText.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }

        mAdapter.replaceAll(filteredModelList);
        mRecyclerView.scrollToPosition(0);
    }
}
