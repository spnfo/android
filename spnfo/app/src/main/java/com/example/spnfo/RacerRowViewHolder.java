package com.example.spnfo;

import androidx.recyclerview.widget.RecyclerView;
import com.example.spnfo.databinding.RacerRowBinding;

public class RacerRowViewHolder extends RecyclerView.ViewHolder {

    private final RacerRowBinding mBinding;

    public RacerRowViewHolder(RacerRowBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(RacerRow item) {
        mBinding.setModel(item);
    }
}
