package com.newsapiclient.client.repository;

import com.newsapiclient.client.RetrofitService;
import com.newsapiclient.client.NewsApi;

public class NewsRepository {

    private static NewsRepository newsRepository;

    public static NewsRepository getInstance(){
        if (newsRepository == null){
            newsRepository = new NewsRepository();
        }
        return newsRepository;
    }

    private NewsApi newsApi;

    public NewsRepository() {
        newsApi = RetrofitService.createService(NewsApi.class);
    }

}
