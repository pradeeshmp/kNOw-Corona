package com.pradeesh.knowcovid.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pradeesh.knowcovid.model.Countries;
import com.pradeesh.knowcovid.model.TotalCases;

import java.util.List;

@Dao
public interface CoronaDAO {
    @Query("SELECT * FROM total_cases ")
    LiveData<TotalCases> loadTotalCases();

    @Query("SELECT * FROM countries ")
    LiveData<List<Countries>> loadCountries();

    @Query("SELECT count(*) FROM total_cases")
    int checkIfTableCaseEmpty();

    @Query("SELECT count(*) FROM countries")
    int checkIfTableCountriesEmpty();

    @Insert
    void insertTotalCases(TotalCases totalCases);

    @Insert
    void insertCountries(List<Countries> countries);


    @Query("SELECT * FROM countries WHERE country = :country ")
    Countries loadCountry(String country);

    @Query("SELECT * FROM countries WHERE country LIKE '%' || :country || '%' ")
    List<Countries> loadSpecificCountries(String country);

    //Query to filter Continent based list - utilised in v2
    @Query("SELECT * FROM countries WHERE (country = 'India') OR (country = 'China') OR (country = 'Pakistan')" +
            " OR (country = 'Malaysia') OR (country = 'Singapore') OR (country ='Japan') OR (country = 'Thailand')" +
            " OR (country = 'Iran') OR (country = 'Srilanka') OR (country = 'Nepal') OR (country = 'Afghanistan')" +
            " OR (country = 'Indonesia') OR (country = 'Vietnam') ")
    List<Countries> loadAsianCountries();

    @Query("DELETE FROM countries")
    public void deleteTableCountries();

    @Query("DELETE FROM total_cases")
    public void deleteTableTotalCases();

}
