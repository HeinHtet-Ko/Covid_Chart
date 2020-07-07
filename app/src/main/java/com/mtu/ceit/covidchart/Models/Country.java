package com.mtu.ceit.covidchart.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {

    @Expose
    @SerializedName("name")
    private String Country_Name;

    public String getCountry_Name() {
        return Country_Name;
    }

    public void setCountry_Name(String country_Name) {
        Country_Name = country_Name;
    }
}
