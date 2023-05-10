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
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nstu.geolocationwificlient.data.Wifi;

import java.lang.reflect.Array;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WifiScanner extends BroadcastReceiver implements Runnable{

    private final String LogWifiScanner = "WifiScanner";
    // Singleton instance
    private static WifiScanner instance;
    private WifiManager mWifiManager;
    private MutableLiveData<Boolean> isRunningLiveData;
    private final MutableLiveData<List<Wifi>> wifiList;
    private final ArrayList<Wifi> mWifiList;
    private  volatile int delay;

    private WifiScanner(WifiManager wifiManager) {
        this.wifiList = new MutableLiveData<>();
        this.mWifiList = new ArrayList<>();
        this.isRunningLiveData = new MutableLiveData<>(false);
        this.mWifiManager = wifiManager;
        delay = 1000;
        this.wifiList.postValue(this.mWifiList);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Boolean.FALSE.equals(isRunningLiveData.getValue()))
            return;
        Log.d(LogWifiScanner, "OnReceive");
        if(updateScanResults())
        {
            Log.d(LogWifiScanner, "Update");
            this.wifiList.postValue(mWifiList);
        }
    }
    private boolean updateScanResults() {
        synchronized (WifiScanner.class) {
            List<ScanResult> scanResults;

            try {
                scanResults = mWifiManager.getScanResults();
            }catch (SecurityException securityException){
                Log.d(LogWifiScanner, "securityException");
                return false;
            }

            ArrayList<Wifi> newWifiList = new ArrayList<>();

            for (ScanResult scanResult : scanResults) {
                newWifiList.add(new Wifi(scanResult));
            }

            boolean prevEquals = true;

            if(newWifiList.size() == mWifiList.size()){
                for (int i = 0; i < mWifiList.size(); i++){
                    if (!mWifiList.get(i).equals(newWifiList.get(i)))
                        prevEquals = false;
                }
            } else
                prevEquals = false;

            if(!prevEquals){
                mWifiList.clear();
                mWifiList.addAll(newWifiList);
            }

            Log.d(LogWifiScanner, "prev is equals: " + prevEquals);

            return !prevEquals;
        }
    }

    public static WifiScanner getInstance() {
        if (instance == null) {
            throw new NullPointerException();
        }
        return instance;
    }
    public static WifiScanner getInstance(WifiManager wifiManager) {
        if (instance == null) {
            synchronized (WifiScanner.class) {
                if (instance == null) {
                    instance = new WifiScanner(wifiManager);
                }
            }
        }
        return instance;
    }
    public static String getMacAddress(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    public LiveData<List<Wifi>> getWifiListLiveData() {
        return wifiList;
    }



    public LiveData<Boolean> getIsRunningLiveData(){
        return isRunningLiveData;
    }
    public void setIsRunning(boolean value){
        isRunningLiveData.setValue(value);
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            //you need to set Developer Options > Networking > Wi-Fi scan throttling
            mWifiManager.startScan();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
