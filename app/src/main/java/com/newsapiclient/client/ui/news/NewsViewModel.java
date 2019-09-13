package com.newsapiclient.client.ui.news;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.newsapiclient.BuildConfig;
import com.newsapiclient.client.repository.NewsRepository;
import com.newsapiclient.model.News;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsViewModel extends ViewModel {

    private static final String TAG = "NewsViewModel";
    private static final String API_KEY = BuildConfig.newsapi_key;

    private MutableLiveData<News> news;
    private NewsRepository newsRepository;

    public NewsViewModel() {}

    public void loadArticles() {
        Log.d(TAG, "loadArticles: getting articles from repository");
        if (news != null) {
            return;
        }
        newsRepository = NewsRepository.getInstance();
        news = newsRepository.getNews("abc-news", API_KEY);

        // Load query parameters set up by user (stored in firebase and loaded on request)
        //final List<String> sources = Arrays.asList("aftenposten", "cnbc", "bbc-news", "abc-news");

        // Mediator list to store values from different news sources
        /*List<MutableLiveData<News>> tempNewsList = new ArrayList<>();
        for (int i = 0; i < sources.size(); i++) {
            Log.d(TAG, "loadArticles: from resouce: " + sources.get(i));
            //news = newsRepository.getNews(sources.get(i), API_KEY);
            tempNewsList.add(newsRepository.getNews(sources.get(i), API_KEY));
        }

        MediatorLiveData<News> mergedList = new MediatorLiveData<>();
        for (int i = 0; i < tempNewsList.size(); i++) {
            mergedList.addSource(tempNewsList.get(i), mergedList::setValue);
        }*/

        //news.setValue(mergedList);
    }

    public LiveData<News> getArticles() {
        return news;
    }

}