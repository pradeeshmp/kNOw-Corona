package com.pradeesh.knowcovid.ui.country;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pradeesh.knowcovid.api.ApiClient;
import com.pradeesh.knowcovid.api.ApiInterface;
import com.pradeesh.knowcovid.database.AppDatabase;
import com.pradeesh.knowcovid.model.Countries;
import com.pradeesh.knowcovid.utils.AppExecutor;
import com.pradeesh.knowcovid.utils.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private LiveData<List<Countries>> countriesListLiveData;
    private List<Countries> asiaCountriesList;
    //private MutableLiveData<List<Countries>> listOfCountries;
    //private List<Countries> list;
    public static String status;
    public static String errorCode;
    private int error_response;
    AppDatabase database;

    public CountryViewModel(Application application) {
        super(application);

        AppExecutor appExecutor = AppExecutor.getInstance();
        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database = AppDatabase.getInstance(application);
                //listOfCountries = new MutableLiveData<>();
                getListOfCountries();
                countriesListLiveData = database.coronaDAO().loadCountries();
                asiaCountriesList = database.coronaDAO().loadAsianCountries();
            }
        });


    }

    public List<Countries> getAsiaCountriesList(){
        return asiaCountriesList;
    }

    public LiveData<List<Countries>> getCountries() {
        AppExecutor appExecutor = AppExecutor.getInstance();
        appExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (database.coronaDAO().checkIfTableCountriesEmpty() <= 0) {
                    status = Constant.ERROR;
                }
            }
        });
        return countriesListLiveData;
    }

    public void getListOfCountries() {

        status = "";
        ApiInterface apiInterface = ApiClient.getCoronaApiClient().create(ApiInterface.class);
        Call<List<Countries>> callTotal = apiInterface.getAllCountries();
        callTotal.enqueue(new Callback<List<Countries>>() {
            @Override
            public void onResponse(Call<List<Countries>> call, Response<List<Countries>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    status = Constant.SUCCESS;
                    //list = response.body();
                    // listOfCountries.postValue(response.body());
                    AppExecutor appExecutor = AppExecutor.getInstance();
                    appExecutor.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (database.coronaDAO().checkIfTableCountriesEmpty() <= 0) {
                                database.coronaDAO().insertCountries(response.body());
                            } else {
                                database.coronaDAO().deleteTableCountries();
                                database.coronaDAO().insertCountries(response.body());
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
            public void onFailure(Call<List<Countries>> call, Throwable t) {
                AppExecutor appExecutor = AppExecutor.getInstance();
                appExecutor.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (database.coronaDAO().checkIfTableCaseEmpty() <= 0) {
                            status = Constant.FAILURE;
                        }
                    }
                });

            }
        });


    }

    public LiveData<String> getText() {
        return mText;
    }
}