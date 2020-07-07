package com.mtu.ceit.covidchart.Models;

import com.google.gson.annotations.Expose;

public class Globe {

    @Expose
    private Data confirmed;
    @Expose
    private Data recovered;
    @Expose
    private Data deaths;
//    private String dailySummary;
//    private String dailyTimeSeries;
//    private String example;
//    private String image;
//    private String source;

    public Data getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Data confirmed) {
        this.confirmed = confirmed;
    }

    public Data getRecovered() {
        return recovered;
    }

    public void setRecovered(Data recovered) {
        this.recovered = recovered;
    }

    public Data getDeaths() {
        return deaths;
    }

    public void setDeaths(Data deaths) {
        this.deaths = deaths;
    }
}
