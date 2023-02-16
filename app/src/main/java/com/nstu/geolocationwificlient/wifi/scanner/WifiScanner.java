package com.nstu.geolocationwificlient.wifi.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nstu.geolocationwificlient.data.Wifi;

import java.util.ArrayList;
import java.util.List;

public class WifiScanner extends BroadcastReceiver implements Runnable{

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

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WifiScanner", "onReceive()");

        updateScanResults();
        this.wifiList.postValue(mWifiList);
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
    private void updateScanResults() {
        Log.d("WifiScanner", "start update");
        synchronized (WifiScanner.class) {
            List<ScanResult> scanResults;
            try {
                scanResults = wifiManager.getScanResults();
            }catch (SecurityException securityException){
                Log.d("WifiScanner", "wrong update");
                return;
            }
            mWifiList.clear();
            for (ScanResult scanResult : scanResults) {
                mWifiList.add(new Wifi(scanResult.SSID, scanResult.BSSID, scanResult.level));
            }
        }

        Log.d("WifiScanner", "success update");
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            wifiManager.startScan();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
