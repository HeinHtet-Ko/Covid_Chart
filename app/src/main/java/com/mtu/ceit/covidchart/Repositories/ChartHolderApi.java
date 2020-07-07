package com.mtu.ceit.covidchart.Repositories;

import com.mtu.ceit.covidchart.Models.Chart;
import com.mtu.ceit.covidchart.Models.CountryArr;
import com.mtu.ceit.covidchart.Models.Data;
import com.mtu.ceit.covidchart.Models.Globe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChartHolderApi {

    @GET("{Burma}")
    Call<Chart> getSingleCountry(@Path("Burma") String country);

    @GET("api")
    Call<Globe> getGlobalData();


    @GET("countries")
    Call<CountryArr> getCountryNames();

}
