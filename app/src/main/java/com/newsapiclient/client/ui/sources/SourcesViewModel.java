package com.newsapiclient.client.ui.sources;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.newsapiclient.BuildConfig;
import com.newsapiclient.client.repository.SourceRepository;
import com.newsapiclient.model.Source;
import com.newsapiclient.model.SourceResponse;


public class SourcesViewModel extends ViewModel {

    private static final String TAG = "SourcesViewModel";
    private static final String API_KEY = BuildConfig.newsapi_key;

    private MutableLiveData<SourceResponse> sources;
    private SourceRepository sourceRepository;

    public SourcesViewModel() {
    }

    public void loadSources() {
        Log.d(TAG, "loadSources: getting list of sources");

        if (sources != null) {
            return;
        }
        sourceRepository = SourceRepository.getInstance();
        sources = sourceRepository.getSources(API_KEY);
    }

    public LiveData<SourceResponse> getSources() {
        return sources;
    }

}