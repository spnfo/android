package com.example.spnfo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class RacerSearchBarFragment extends Fragment {

    private OnSearchChangeListener searchChangeCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.racer_search_bar, container, false);
        final EditText et = (EditText) v.findViewById(R.id.search_edit_text);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchChangeCallback != null)
                    searchChangeCallback.filterDataset(et.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    public void setOnSearchChangeListener(OnSearchChangeListener callback) {
        this.searchChangeCallback = callback;
    }

    public interface OnSearchChangeListener {
        public void filterDataset(String searchStr);
    }
}
