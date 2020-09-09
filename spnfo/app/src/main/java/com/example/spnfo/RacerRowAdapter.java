package com.example.spnfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RacerRowAdapter extends RecyclerView.Adapter<RacerRowAdapter.MyViewHolder> {

    private String[] mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView racerPosition;
        public TextView racerTag;
        public TextView racerSplit;
        public ImageView racerAvatar;

        public MyViewHolder(View v) {
            super(v);

            racerPosition = v.findViewById(R.id.racer_position);
            racerTag = v.findViewById(R.id.racer_tag);
            racerSplit = v.findViewById(R.id.racer_split);
            racerAvatar = v.findViewById(R.id.racer_avatar_image);
        }
    }

    public RacerRowAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RacerRowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.racer_row, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.racerPosition.setText(Integer.toString(position));
        holder.racerTag.setText(mDataset[position]);
        holder.racerSplit.setText("+1.46");
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
