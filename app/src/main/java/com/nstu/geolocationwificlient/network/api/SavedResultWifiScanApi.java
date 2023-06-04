package com.nstu.geolocationwificlient.network.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SavedResultWifiScanApi {
    @POST("/collector")
    Call<String> postSavedResultWifiScan(@Body String savedResultWifiScan);
}
