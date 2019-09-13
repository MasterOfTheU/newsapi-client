package com.newsapiclient.client.ui.sources;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapiclient.R;
import com.newsapiclient.client.adapter.ArticleAdapter;
import com.newsapiclient.client.adapter.SourceAdapter;
import com.newsapiclient.client.ui.news.NewsViewModel;
import com.newsapiclient.model.Source;
import com.newsapiclient.model.SourceResponse;

import java.util.ArrayList;
import java.util.List;


public class SourcesFragment extends Fragment {

    private SourcesViewModel sourcesViewModel;
    private RecyclerView mRecyclerView;
    private SourceAdapter mAdapter;
    private ArrayList<Source> mSourcesList = new ArrayList<>();

    public SourcesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        sourcesViewModel =
                ViewModelProviders.of(this).get(SourcesViewModel.class);
        sourcesViewModel.loadSources();
        sourcesViewModel.getSources().observe(this, sourcesResponse -> {
            List<Source> sources = sourcesResponse.getSources();

        });

        View view = inflater.inflate(R.layout.fragment_sources, container, false);
        return view;
    }

    private void setupRecyclerView() {
        if (mAdapter == null) {
            mAdapter = new SourceAdapter(mSourcesList, getActivity());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setNestedScrollingEnabled(true);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

}