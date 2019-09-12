package com.newsapiclient.client;

import com.newsapiclient.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NewsApi {

    @GET("top-headlines")
    Call<News> getNews(
            @Query("sources") String sources,
            @Query("apiKey") String apiKey
    );

}
