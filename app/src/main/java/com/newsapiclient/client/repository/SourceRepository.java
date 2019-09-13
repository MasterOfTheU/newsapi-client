package com.newsapiclient.client.repository;

import androidx.lifecycle.MutableLiveData;

import com.newsapiclient.client.NewsApi;
import com.newsapiclient.client.RetrofitService;
import com.newsapiclient.model.Source;
import com.newsapiclient.model.SourceResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SourceRepository {

    private static SourceRepository sourceRepository;

    public static SourceRepository getInstance() {
        if (sourceRepository == null) {
            sourceRepository = new SourceRepository();
        }
        return sourceRepository;
    }

    private NewsApi newsApi;

    public SourceRepository() {
        newsApi = RetrofitService.createService(NewsApi.class);
    }

    public MutableLiveData<SourceResponse> getSources(String apiKey) {
        MutableLiveData<SourceResponse> sourceData = new MutableLiveData<>();
        newsApi.getSources(apiKey)
                .enqueue(new Callback<SourceResponse> () {
                    @Override
                    public void onResponse(Call<SourceResponse> call,
                                           Response<SourceResponse> response) {
                        if (response.isSuccessful()) {
                            sourceData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<SourceResponse> call, Throwable t) {
                        sourceData.setValue(null);
                    }
                });
        return sourceData;
    }

}
