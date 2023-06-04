package com.nstu.geolocationwificlient.network;

import com.nstu.geolocationwificlient.network.api.ResultWifiScanApi;
import com.nstu.geolocationwificlient.network.api.SavedResultWifiScanApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "http://172.16.5.198";
    private Retrofit mRetrofit;
    private NetworkService(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static NetworkService getInstance() {
        if(mInstance == null){
            synchronized (NetworkService.class){
                if(mInstance == null) {
                    mInstance = new NetworkService();
                }
            }
        }
        return mInstance;
    }
    public ResultWifiScanApi getResultWifiScanApi() {
        return mRetrofit.create(ResultWifiScanApi.class);
    }
    public SavedResultWifiScanApi getSavedResultWifiScanApi() {
        return mRetrofit.create(SavedResultWifiScanApi.class);
    }
}
