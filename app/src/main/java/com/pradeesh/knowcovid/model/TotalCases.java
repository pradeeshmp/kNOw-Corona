package com.pradeesh.knowcovid.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "total_cases")
public class TotalCases {

    @PrimaryKey(autoGenerate = true)
    private int casedId;

    @SerializedName("cases")
    @Expose
    private int cases;

    @SerializedName("deaths")
    @Expose
    private int deaths;

    @SerializedName("recovered")
    @Expose
    private int recovered;

    @Ignore
    public TotalCases(int cases, int deaths, int recovered) {
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public TotalCases(int casedId, int cases, int deaths, int recovered) {
        this.casedId = casedId;
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getCasedId() {
        return casedId;
    }

    public void setCasedId(int casedId) {
        this.casedId = casedId;
    }
}
