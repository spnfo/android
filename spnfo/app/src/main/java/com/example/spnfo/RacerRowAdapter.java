package com.example.spnfo;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.spnfo.databinding.RacerRowBinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RacerRowAdapter extends RecyclerView.Adapter<RacerRowViewHolder> {

    private static Comparator<RacerRow> mComparator = new Comparator<RacerRow>() {
        @Override
        public int compare(RacerRow a, RacerRow b) {
            return a.getPositionInt() - b.getPositionInt();
        }
    };

    private SortedList<RacerRow> mSortedList = new SortedList<>(RacerRow.class, new SortedList.Callback<RacerRow>() {
        @Override
        public int compare(RacerRow o1, RacerRow o2) {
            return mComparator.compare(o1, o2);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(RacerRow oldItem, RacerRow newItem) {
            return oldItem.hashCode() == newItem.hashCode();
        }

        @Override
        public boolean areItemsTheSame(RacerRow item1, RacerRow item2) {
            return item1.getTag().equals(item2.getTag());
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });

    private LayoutInflater mInflater;
    private Context mCtx;

    public RacerRowAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mCtx = context;
    }

    @Override
    public RacerRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RacerRowBinding binding = RacerRowBinding.inflate(mInflater, parent, false);
        return new RacerRowViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RacerRowViewHolder holder, int position) {
        RacerRow model = mSortedList.get(position);

        if (position % 2 == 1)
            holder.itemView.setBackgroundColor(mCtx.getResources().getColor(R.color.white, null));

        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

    public void add(RacerRow model) {
        mSortedList.add(model);
    }

    public void remove(RacerRow model) {
        mSortedList.remove(model);
    }

    public void add(List<RacerRow> models) {
        mSortedList.addAll(models);
    }

    public void remove(List<RacerRow> models) {
        mSortedList.beginBatchedUpdates();
        for (RacerRow model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<RacerRow> models) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            RacerRow model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }
}
