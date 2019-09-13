package com.newsapiclient.client.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.newsapiclient.R;
import com.newsapiclient.client.adapter.ArticleAdapter;
import com.newsapiclient.model.Article;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;

    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private ArrayList<Article> mArticleList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public NewsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Prevents fragments from laying on top of each other
        if (container != null) {
            container.removeAllViews();
        }

        newsViewModel =
                ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.loadArticles();
        newsViewModel.getArticles().observe(this, newsResponse -> {
            List<Article> newsArticles = newsResponse.getArticles();
            mArticleList.addAll(newsArticles);
            mAdapter.notifyDataSetChanged();
        });

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (!mArticleList.isEmpty()) {
                mArticleList.clear();
                newsViewModel.loadArticles();
                mAdapter.notifyDataSetChanged();
            } else {
                newsViewModel.loadArticles();
                mAdapter.notifyDataSetChanged();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mRecyclerView = view.findViewById(R.id.articles_container);
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        if (mAdapter == null) {
            mAdapter = new ArticleAdapter(mArticleList, getActivity());
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setNestedScrollingEnabled(true);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

}