package com.example.spnfo;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.spnfo.databinding.RacerRowBinding;

public class RacerRowViewHolder extends RecyclerView.ViewHolder {

    public RacerRowBinding mBinding;
    public CheckBox mCheckBox;
    public TextView mTag;

    public RacerRowViewHolder(View rowView, RacerRowBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        mCheckBox = binding.racerCheckbox;
        mTag = binding.racerTag;
    }

    public void bind(RacerRow item) {
        mBinding.setModel(item);
        mBinding.executePendingBindings();
    }
}
