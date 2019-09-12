package com.newsapiclient.client.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapiclient.R;
import com.newsapiclient.client.adapter.ArticleAdapter;
import com.newsapiclient.model.Article;

import java.util.List;


public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;

    private RecyclerView mRecyclerView;
    private ArticleAdapter adapter;

    public NewsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Prevents fragments from laying on top of each other
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mRecyclerView = view.findViewById(R.id.articles_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);

        newsViewModel.getArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                adapter = new ArticleAdapter(articles, getActivity());
                adapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(adapter);
            }
        });
        return view;
    }

}