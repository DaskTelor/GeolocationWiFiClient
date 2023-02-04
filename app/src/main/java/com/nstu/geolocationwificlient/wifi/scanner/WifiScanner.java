package com.nstu.geolocationwificlient.wifi.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nstu.geolocationwificlient.data.Wifi;

import java.util.ArrayList;
import java.util.List;

public class WifiScanner implements Runnable{

    // Singleton instance
    @SuppressLint("StaticFieldLeak")
    private static WifiScanner instance;
    private WifiManager wifiManager;
    private final MutableLiveData<List<Wifi>> wifiList;
    private final ArrayList<Wifi> mWifiList;
    private  volatile int delay;

    private WifiScanner() {
        this.wifiList = new MutableLiveData<>();
        this.mWifiList = new ArrayList<>();

        delay = 1000;
        this.wifiList.postValue(this.mWifiList);
    }

    public static WifiScanner getInstance() {
        if (instance == null) {
            synchronized (WifiScanner.class) {
                if (instance == null) {
                    instance = new WifiScanner();
                }
            }
        }
        return instance;
    }

    public LiveData<List<Wifi>> loadAll() {
        return wifiList;
    }

    public void setWifiManager(WifiManager wifiManager) {
        synchronized (WifiScanner.class) {
            instance.wifiManager = wifiManager;
        }
    }
    public void update() {
        synchronized (WifiScanner.class) {
            List<ScanResult> scanResults;
            try {
                scanResults = wifiManager.getScanResults();
            }catch (SecurityException securityException){
                return;
            }
            mWifiList.clear();
            for (ScanResult scanResult : scanResults) {
                mWifiList.add(new Wifi(scanResult.SSID, "Empty", scanResult.level));
            }
        }
        wifiList.postValue(mWifiList);
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            this.update();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                this.mWifiList.clear();
                this.wifiList.postValue(mWifiList);
                break;
            }
        }
    }

}
