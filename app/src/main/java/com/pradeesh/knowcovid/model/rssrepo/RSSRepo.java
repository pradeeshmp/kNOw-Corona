package com.pradeesh.knowcovid.model.rssrepo;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.pradeesh.knowcovid.api.ApiClient;
import com.pradeesh.knowcovid.api.ApiInterface;
import com.pradeesh.knowcovid.model.Article;
import com.pradeesh.knowcovid.model.RSSResponseModel;
import com.pradeesh.knowcovid.utils.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pradeesh.knowcovid.utils.Constant.RSSAPIKEY;
import static com.pradeesh.knowcovid.utils.Constant.RSSTOPIC;

public class RSSRepo {


    private MutableLiveData<List<Article>> mRSSMutableLiveData;
    private Application application;

    public RSSRepo(Application application) {
        this.application = application;
        // Database initiation not required
        // RSS api and database will be upgraded on v2
    }


    public MutableLiveData<List<Article>> getRSSMutableLiveData() {
        if (mRSSMutableLiveData == null) {
            mRSSMutableLiveData = new MutableLiveData<>();
        }
        ApiInterface apiInterface = ApiClient.getRSSApiClient().create(ApiInterface.class);
        apiInterface.getLatestNews(RSSTOPIC, RSSAPIKEY)
                .enqueue(new Callback<RSSResponseModel>() {
                    @Override
                    public void onResponse(Call<RSSResponseModel> call, Response<RSSResponseModel> response) {
                        if (response.isSuccessful()) {
                            RSSResponseModel newsRes = response.body();
                            List<Article> articleList = newsRes.getArticles();
                            mRSSMutableLiveData.setValue(articleList);
                        }
                    }

                    @Override
                    public void onFailure(Call<RSSResponseModel> call, Throwable t) {
                        //TODO Error code handling same as other models
                    }
                });
        return mRSSMutableLiveData;
    }

}