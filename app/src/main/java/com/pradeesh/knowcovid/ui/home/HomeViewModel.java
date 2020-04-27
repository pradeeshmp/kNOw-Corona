package com.pradeesh.knowcovid.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pradeesh.knowcovid.api.ApiClient;
import com.pradeesh.knowcovid.api.ApiInterface;
import com.pradeesh.knowcovid.database.AppDatabase;
import com.pradeesh.knowcovid.model.TotalCases;
import com.pradeesh.knowcovid.utils.AppExecutor;
import com.pradeesh.knowcovid.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<TotalCases> totalCasesLiveData;
    private AppDatabase database;
    private LiveData<TotalCases> roomTotalCasesLiveData;
    public static String status = "";
    public static String errorCode;
    private int error_response;

    public HomeViewModel(Application application) {
        super(application);
        totalCasesLiveData = new MutableLiveData<>();
        database = AppDatabase.getInstance(this.getApplication());
        getTotalCases();
        roomTotalCasesLiveData = database.coronaDAO().loadTotalCases();
    }

    public LiveData<TotalCases>getLocal(){

        AppExecutor appExecutor = AppExecutor.getInstance();
        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (database.coronaDAO().checkIfTableCaseEmpty() <= 0){
                    status = Constant.ERROR;
                } else {
                    roomTotalCasesLiveData = database.coronaDAO().loadTotalCases();
                }
            }
        });
        return roomTotalCasesLiveData;
    }

    public LiveData<TotalCases> getTotalCases() {
        status = "";
        ApiInterface apiInterface = ApiClient.getCoronaApiClient().create(ApiInterface.class);
        database = AppDatabase.getInstance(this.getApplication());
        Call<TotalCases> callTotal = apiInterface.getTotalCases();
        callTotal.enqueue(new Callback<TotalCases>() {
            @Override
            public void onResponse(Call<TotalCases> call, Response<TotalCases> response) {
                if (response.isSuccessful() && response.body() != null) {
                    status = Constant.SUCCESS;
                    totalCasesLiveData.postValue(response.body());
                    AppExecutor appExecutor = AppExecutor.getInstance();
                    appExecutor.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (database.coronaDAO().checkIfTableCaseEmpty() <= 0) {
                                database.coronaDAO().insertTotalCases(response.body());
                            } else {
                                database.coronaDAO().deleteTableTotalCases();
                                database.coronaDAO().insertTotalCases(response.body());
                            }
                        }
                    });
                } else {
                    status = Constant.ERROR;
                    error_response = response.code();
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<TotalCases> call, Throwable t) {
                status = Constant.FAILURE;
                AppExecutor appExecutor = AppExecutor.getInstance();
                appExecutor.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (database.coronaDAO().checkIfTableCaseEmpty() <= 0) {
                            //TODO TO be handled
                        } else {
                            totalCasesLiveData.postValue(database.coronaDAO().loadTotalCases().getValue());
                        }
                    }
                });
            }
        });
        return totalCasesLiveData;
    }

    public LiveData<String> getText() {
        return mText;
    }
}