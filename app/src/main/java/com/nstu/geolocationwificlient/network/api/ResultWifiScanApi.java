package com.nstu.geolocationwificlient.network.api;
import com.nstu.geolocationwificlient.data.ResultWifiScan;
import com.nstu.geolocationwificlient.data.Wifi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ResultWifiScanApi {
    @POST("/collector")
    Call<ResultWifiScan> postResultWifiList(@Body ResultWifiScan resultWifiScan);
}
