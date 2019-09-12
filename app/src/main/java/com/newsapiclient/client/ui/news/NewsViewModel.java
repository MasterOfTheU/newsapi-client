package com.newsapiclient.client.ui.news;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.newsapiclient.BuildConfig;
import com.newsapiclient.client.ApiClient;
import com.newsapiclient.client.ApiInterface;
import com.newsapiclient.model.Article;
import com.newsapiclient.model.News;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsViewModel extends ViewModel {

    private static final String TAG = "NewsViewModel";
    private static final String API_KEY = BuildConfig.newsapi_key;
    
    private MutableLiveData<List<Article>> articles;

    public NewsViewModel() { }

    public MutableLiveData<List<Article>> getArticles() {
        if (articles == null) {
            articles = new MutableLiveData<>();
            loadArticles();
        }
        return articles;
    }

    private void loadArticles() {
        Log.d(TAG, "loadArticles: fetching data from server");
        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface api = retrofit.create(ApiInterface.class);

        // Load query parameters set up by user (stored in firebase and loaded on request)
        final List<String> sources = Arrays.asList("aftenposten", "cnbc", "bbc-news", "abc-news");

        Call<News> call;
        final List<Article> tempList = new ArrayList<>();
        for (int i = 0; i < sources.size(); i++) {
            call = api.getNews(sources.get(i), API_KEY);
            Log.d(TAG, "loadArticles: fetching news from " + sources.get(i) + " source");

            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (response.isSuccessful() && response.body().getArticle() != null) {
                        tempList.addAll(response.body().getArticle());
                        Log.d(TAG, "onResponse: " + response.body().toString());
                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    // Throw exception
                }
            });
        }

        // Loading articles from all resources into the list
        articles.setValue(tempList);

    }

}