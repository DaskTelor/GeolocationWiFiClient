package com.nstu.geolocationwificlient.wifi.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nstu.geolocationwificlient.data.Wifi;
import com.nstu.geolocationwificlient.data.WifiSignals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class WifiScanner extends BroadcastReceiver implements Runnable{

    private final String LogWifiScanner = "WifiScanner";
    // Singleton instance
    private static WifiScanner instance;
    private Thread mCurrentThread;
    private WifiManager mWifiManager;
    private MutableLiveData<Boolean> isRunningLiveData;
    private final MutableLiveData<List<Wifi>> mWifiListLiveData;
    private final ArrayList<Wifi> mWifiList;
    private  volatile int mDelay;
    private volatile HashMap<String, List<WifiSignals>> mTrackedBssidSet;

    private WifiScanner(WifiManager wifiManager, HashMap<String, List<WifiSignals>> trackedBssid) {
        this.mWifiListLiveData = new MutableLiveData<>();
        this.mWifiList = new ArrayList<>();
        this.isRunningLiveData = new MutableLiveData<>(false);
        this.mWifiManager = wifiManager;
        mDelay = 1000;
        this.mWifiListLiveData.postValue(this.mWifiList);
        mTrackedBssidSet = trackedBssid;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Boolean.FALSE.equals(isRunningLiveData.getValue()))
            return;
        Log.d(LogWifiScanner, "OnReceive");
        if(updateScanResults())
        {
            Log.d(LogWifiScanner, "Update");
            this.mWifiListLiveData.postValue(mWifiList);
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
            for(int i = 0; i < mWifiList.size(); i++)
            {
                Wifi wifi = mWifiList.get(i);
                if(mTrackedBssidSet.containsKey(wifi.getBSSID()))
                    wifi.setIsTracked(true);
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
    public static WifiScanner getInstance(WifiManager wifiManager, HashMap<String, List<WifiSignals>> trackedBssid) {
        if (instance == null) {
            synchronized (WifiScanner.class) {
                if (instance == null) {
                    instance = new WifiScanner(wifiManager, trackedBssid);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Wifi>> getWifiListLiveData() {
        return mWifiListLiveData;
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
                Thread.sleep(mDelay);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
