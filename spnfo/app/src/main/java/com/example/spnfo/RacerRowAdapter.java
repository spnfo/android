package com.example.spnfo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.SystemClock;
import android.renderscript.ScriptGroup;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.spnfo.databinding.RacerRowBinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RacerRowAdapter extends RecyclerView.Adapter<RacerRowAdapter.RacerRowViewHolder> {

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
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

    });

    private LayoutInflater mInflater;
    private Context mCtx;
    private OnRacerRowChangeListener racerRowChangeCallback;
    private ViewGroup mParent;
    public static HashMap<String, Boolean> checkedItems = new HashMap<String, Boolean>();

    public RacerRowAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mCtx = context;
    }

    @Override
    public RacerRowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RacerRowBinding binding = RacerRowBinding.inflate(mInflater, parent, false);
        mParent = parent;
        return new RacerRowViewHolder(binding.getRoot(), binding);
    }

    public void onCheckBoxClicked(View v) {
        Log.v("ADAPTER", "here");
    }

    @Override
    public void onBindViewHolder(final RacerRowViewHolder holder, final int position) {
        final RacerRow model = mSortedList.get(position);

        if (model.getPositionInt() % 2 == 1) {
            holder.itemView.setBackgroundColor(mCtx.getResources().getColor(R.color.white, null));
        } else {
            holder.itemView.setBackgroundColor(mCtx.getResources().getColor(R.color.lightGray, null));
        }

        // Set imageView resource
        int resId = mCtx.getResources().getIdentifier(model.getTag().toLowerCase(), "drawable", mCtx.getPackageName());
        if (resId != 0) {
            // Profile image exists
            holder.itemView.findViewById(R.id.racer_avatar_image).setBackgroundResource(resId);
        } else {
            holder.itemView.findViewById(R.id.racer_avatar_image).setBackgroundResource(R.drawable.no_profile_pic);
        }

        if (model.getExpanded()) {
            holder.itemView.findViewById(R.id.data_drawer_constraint_layout).setVisibility(View.VISIBLE);
        } else {
            holder.itemView.findViewById(R.id.data_drawer_constraint_layout).setVisibility(View.GONE);
        }

        final NamedCheckBox ncb = (NamedCheckBox) holder.itemView.findViewById(R.id.racer_checkbox);
        ncb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ncb.getTagName() != null && racerRowChangeCallback != null) {
                    racerRowChangeCallback.onCheckBoxChanged(ncb.getTagName(), ((CheckBox) v).isChecked());
                }
            }
        });

        ConstraintLayout bannerConstraintLayout = holder.itemView.findViewById(R.id.racer_row_constraint_layout);

        bannerConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ConstraintLayout dataDrawerConstraintLayout = holder.itemView.findViewById(R.id.data_drawer_constraint_layout);

                Transition transition = new Slide(Gravity.BOTTOM);
                transition.setDuration(300);
                transition.addTarget(holder.itemView.findViewById(R.id.data_drawer_constraint_layout));

                TransitionManager.beginDelayedTransition(mParent, transition);

                if (dataDrawerConstraintLayout.getVisibility() == View.GONE) {
                    dataDrawerConstraintLayout.setVisibility(View.VISIBLE);
                    model.setExpanded(true);
                } else {
                    dataDrawerConstraintLayout.setVisibility(View.GONE);
                    model.setExpanded(false);
                }

                notifyItemChanged(position);

            }
        });

        holder.bind(model);
    }

    @BindingAdapter("checked")
    public static void setColor(View m, boolean isChecked) {
        Log.v("SETCOLOR", Boolean.toString(isChecked));
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

    public void checkAll(boolean checkedTrue) {
        mSortedList.beginBatchedUpdates();
        for (int i = 0; i < mSortedList.size(); i++) {
            RacerRow tempModel = mSortedList.get(i);
            tempModel.setChecked(checkedTrue);
            mSortedList.updateItemAt(i, tempModel);
        }
        mSortedList.endBatchedUpdates();

        notifyDataSetChanged();
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
        mSortedList.replaceAll(models);
    }

    public void updateAll() {
        mSortedList.beginBatchedUpdates();
        for (int i = 0; i < mSortedList.size(); i++) {
            mSortedList.updateItemAt(i, mSortedList.get(i));
        }
        mSortedList.endBatchedUpdates();
    }

    private OnRacerRowChangeListener getOnRacerRowChangeListener() {
        return racerRowChangeCallback;
    }

    public void setOnRacerRowChangeListener(OnRacerRowChangeListener callback) {
        racerRowChangeCallback = callback;
    }

    public interface OnRacerRowChangeListener {
        public void onCheckBoxChanged(String tag, Boolean checked);
    }

    static public class RacerRowViewHolder extends RecyclerView.ViewHolder {

        public RacerRowBinding mBinding;

        public RacerRowViewHolder(View rowView, RacerRowBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(RacerRow item) {
            mBinding.setModel(item);
            mBinding.executePendingBindings();
        }
    }

}
