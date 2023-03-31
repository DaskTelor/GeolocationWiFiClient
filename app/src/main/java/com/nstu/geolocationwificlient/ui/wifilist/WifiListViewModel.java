package com.nstu.geolocationwificlient.ui.wifilist;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.nstu.geolocationwificlient.data.ResultWifiScan;
import com.nstu.geolocationwificlient.network.NetworkService;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;
import com.nstu.geolocationwificlient.data.Wifi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WifiListViewModel extends AndroidViewModel {
    private Thread threadUpdateWifiList;
    private final LiveData<List<Wifi>> mWifiListLiveData;
    private final WifiScanner mWifiScanner;

    public WifiListViewModel(@NonNull Application application) {
        super(application);

        mWifiScanner = WifiScanner.getInstance();
        mWifiScanner.setWifiManager(
                (WifiManager) application.
                        getApplicationContext().
                        getSystemService(Context.WIFI_SERVICE));

        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplication().registerReceiver(WifiScanner.getInstance(), intentFilter);

        mWifiListLiveData = mWifiScanner.getWifiListLiveData();
        mWifiListLiveData.observeForever(new Observer<List<Wifi>>() {
            @Override
            public void onChanged(List<Wifi> wifiList) {
                if(wifiList.isEmpty())
                    return;
                postResultWifiScan(
                        new ResultWifiScan(
                        "macId",
                        "1.0",
                        "GeolocationWifiClient",
                        wifiList));
            }
        });
    }

    public LiveData<List<Wifi>> getWifiListLiveData() {
        return this.mWifiListLiveData;
    }

    public LiveData<Boolean> getWifiScannerRunning(){
        return mWifiScanner.getIsRunningLiveData();
    }
    public void stopWifiScanner(){
        mWifiScanner.setIsRunning(false);
        threadUpdateWifiList.interrupt();
    }
    public void startWifiScanner(){
        mWifiScanner.setIsRunning(true);
        threadUpdateWifiList = new Thread(mWifiScanner);
        threadUpdateWifiList.start();
    }
    public void postResultWifiScan(ResultWifiScan resultWifiScan){
        Call<ResultWifiScan> call = NetworkService.getInstance()
                .getResultWifiScanApi()
                .postResultWifiList(resultWifiScan);
        call.enqueue(new Callback<ResultWifiScan>() {
            @Override
            public void onResponse(@NonNull Call<ResultWifiScan> call, @NonNull Response<ResultWifiScan> response) {
                Log.d("Network", "onResponse");
                if (response.isSuccessful()) {
                    Log.d("Network", "Successful");
                } else {
                    Log.d("Network", "SomeError");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResultWifiScan> call, @NonNull Throwable t) {
                Log.d("Network", "onFailure");
            }
        });
    }
}