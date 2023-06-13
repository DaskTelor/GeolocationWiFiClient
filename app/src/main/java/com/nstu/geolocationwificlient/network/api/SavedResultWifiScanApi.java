package com.nstu.geolocationwificlient.network.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SavedResultWifiScanApi {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/collector")
    Call<String> postSavedResultWifiScan(@Body String savedResultWifiScan);
}
