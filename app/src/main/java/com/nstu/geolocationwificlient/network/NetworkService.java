package com.nstu.geolocationwificlient.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "http://000.00.0.000";
    private Retrofit mRetrofit;
    private NetworkService(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
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
}
