package com.mtu.ceit.covidchart.Models;

import com.google.gson.annotations.SerializedName;

public class Chart {

    @SerializedName("confirmed")
    private Data confirmed_cases;

    @SerializedName("deaths")
    private Data death_cases;

    @SerializedName("recovered")
    private Data recovered_cases;

    private String lastUpdate;

    public Data getConfirmed_cases() {
        return confirmed_cases;
    }

    public void setConfirmed_cases(Data confirmed_cases) {
        this.confirmed_cases = confirmed_cases;
    }

    public Data getDeath_cases() {
        return death_cases;
    }

    public void setDeath_cases(Data death_cases) {
        this.death_cases = death_cases;
    }

    public Data getRecovered_cases() {
        return recovered_cases;
    }

    public void setRecovered_cases(Data recovered_cases) {
        this.recovered_cases = recovered_cases;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
