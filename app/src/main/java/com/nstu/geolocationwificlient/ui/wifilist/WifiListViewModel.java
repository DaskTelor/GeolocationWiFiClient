package com.nstu.geolocationwificlient.ui.wifilist;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;
import com.nstu.geolocationwificlient.data.Wifi;

import java.util.List;

public class WifiListViewModel extends AndroidViewModel {
    private Thread threadUpdateWifiList;
    private final LiveData<List<Wifi>> wifiList;
    private final WifiScanner wifiScanner;

    public WifiListViewModel(@NonNull Application application) {
        super(application);

        this.wifiScanner = WifiScanner.getInstance();
        this.wifiScanner.setWifiManager(
                (WifiManager) application.
                        getApplicationContext().
                        getSystemService(Context.WIFI_SERVICE));

        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplication().registerReceiver(WifiScanner.getInstance(), intentFilter);

        this.wifiList = wifiScanner.getWifiListLiveData();
    }

    public LiveData<List<Wifi>> getWifiList() {
        return this.wifiList;
    }

    public LiveData<Boolean> getWifiScannerRunning(){
        return wifiScanner.getIsRunningLiveData();
    }
    public void startStopWifiScanner(){
        if(Boolean.TRUE.equals(wifiScanner.getIsRunningLiveData().getValue())){
            wifiScanner.setIsRunning(false);
            threadUpdateWifiList.interrupt();
        }
        else{
            wifiScanner.setIsRunning(true);
            threadUpdateWifiList = new Thread(wifiScanner);
            threadUpdateWifiList.start();
        }
    }
}