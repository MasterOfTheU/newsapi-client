package com.newsapiclient.client.repository;

import androidx.lifecycle.MutableLiveData;

import com.newsapiclient.client.RetrofitService;
import com.newsapiclient.client.NewsApi;
import com.newsapiclient.model.News;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsRepository {

    private static NewsRepository newsRepository;

    public static NewsRepository getInstance() {
        if (newsRepository == null){
            newsRepository = new NewsRepository();
        }
        return newsRepository;
    }

    private NewsApi newsApi;

    public NewsRepository() {
        newsApi = RetrofitService.createService(NewsApi.class);
    }

    public MutableLiveData<News> getNews(String source, String apiKey) {
        MutableLiveData<News> newsData = new MutableLiveData<>();
        newsApi.getNews(source, apiKey)
                .enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        if (response.isSuccessful()) {
                            newsData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                        newsData.setValue(null);
                    }
                });
        return newsData;
    }


}
