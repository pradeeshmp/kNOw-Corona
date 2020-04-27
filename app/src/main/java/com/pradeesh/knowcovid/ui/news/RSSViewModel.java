package com.pradeesh.knowcovid.ui.news;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pradeesh.knowcovid.model.Article;
import com.pradeesh.knowcovid.model.rssrepo.RSSRepo;

import java.util.List;

public class RSSViewModel extends AndroidViewModel {

    RSSRepo rssRepo;

    public RSSViewModel(@NonNull Application application) {
        super(application);
        rssRepo = new RSSRepo(application);
    }

    public LiveData<List<Article>> getAllArticle() {
        return rssRepo.getRSSMutableLiveData();
    }
}
