package com.mtu.ceit.covidchart.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryArr {

    @SerializedName("countries")
    private List<Country> countryList;

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }
}
