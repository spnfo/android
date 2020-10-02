package com.example.spnfo;

import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;
import com.example.spnfo.databinding.RacerRowBinding;

public class RacerRowViewHolder extends RecyclerView.ViewHolder {

    private final RacerRowBinding mBinding;
    public CheckBox mCheckBox;

    public RacerRowViewHolder(RacerRowBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        mCheckBox = binding.racerCheckbox;
    }

    public void bind(RacerRow item) {
        mBinding.setModel(item);
    }
}
