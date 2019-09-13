package com.newsapiclient.client.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapiclient.R;
import com.newsapiclient.model.Source;

import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceViewHolder> {

    private static final String TAG = "SourceAdapter";

    private List<Source> sources;
    private Context context;
    private SourceAdapter.OnItemClickListener onItemClickListener;

    public SourceAdapter(List<Source> sources, Context context) {
        this.sources = sources;
        this.context = context;
    }

    @NonNull
    @Override
    public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.source, parent, false);
        return new SourceViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceViewHolder holder, int position) {
        final SourceViewHolder sourceViewHolder = holder;
        Source source = sources.get(position);
        Log.d(TAG, "onBindViewHolder: source: " + source.toString());

        //TODO set fields from source object
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class SourceViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        OnItemClickListener onItemClickListener;

        public SourceViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
