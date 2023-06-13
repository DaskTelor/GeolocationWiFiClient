package com.nstu.geolocationwificlient.network.api;

import com.nstu.geolocationwificlient.data.ResultWifiScan;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ResultWifiScanApi {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/collector")
    Call<ResultWifiScan> postResultWifiList(@Body ResultWifiScan resultWifiScan);
}
