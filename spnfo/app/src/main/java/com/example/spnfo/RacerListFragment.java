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
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.spnfo.databinding.RacerRowBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RacerListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RacerRowAdapter mAdapter;

//    private static final String[] TAGS = new String[]{ "AAAAAA", "BBBBBB", "CCCCCC", "DDDDDD", "EEEEEE", "FFFFFF", "GGGGGG", "HHHHHH", "IIIIII", "JJJJJJ",
//                                                       "KKKKKK", "LLLLLL", "MMMMMM", "NNNNNN", "OOOOOO", "PPPPPP", "QQQQQQ", "RRRRRR", "SSSSSS", "TTTTTT",
//                                                       "UUUUUU", "VVVVVV", "WWWWWW", "XXXXXX", "YYYYYY", "ZZZZZZ" };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.racer_list_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemViewCacheSize(0);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

//        RacerSearchBarFragment rsbf = (RacerSearchBarFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.racer_search_bar_fragment);
//        if (rsbf != null) {
//            rsbf.setOnSearchChangeListener(this);
//        }

        return v;
    }

    public void setAdapter(RacerRowAdapter adapter) {
        mAdapter = adapter;

        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void globalCheck(Boolean checkTrue) {
        mAdapter.checkAll(checkTrue);
    }

//    public void onCheckBoxChanged(String tag, Boolean isChecked) {
//        Log.v("CHECKED", "here");
//        ((MainActivity) getActivity()).onCheckSelected(tag, isChecked);
//    }

    public void filterDataset(ArrayList<RacerRow> filterdModels) {
        mRecyclerView.scrollToPosition(0);
    }
}
