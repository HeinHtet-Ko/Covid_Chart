package com.mtu.ceit.covidchart.Models;

import com.google.gson.annotations.SerializedName;

public class Data {


    private int value;

    @SerializedName("detail")
    private String detail_url;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }
}
