package com.pradeesh.knowcovid.api;

import com.pradeesh.knowcovid.model.Countries;
import com.pradeesh.knowcovid.model.RSSResponseModel;
import com.pradeesh.knowcovid.model.TotalCases;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("all")
    Call<TotalCases> getTotalCases();

    @GET("countries")
    Call<List<Countries>> getAllCountries();

    @GET("countries")
    Call<Countries> getAllCountries(@Query("sort") String sort);

    @GET("countries/{country-name}")
    Call<Countries> getCountry(@Path("country-name") String country_name);

    @GET("top-headlines")
    Call<RSSResponseModel> getLatestNews(@Query("sources") String source, @Query("apiKey") String apiKey);
}
