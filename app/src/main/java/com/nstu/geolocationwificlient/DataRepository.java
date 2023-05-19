package com.nstu.geolocationwificlient;

import androidx.lifecycle.LiveData;

import com.nstu.geolocationwificlient.data.Wifi;
import com.nstu.geolocationwificlient.data.WifiSignals;
import com.nstu.geolocationwificlient.db.AppDatabase;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataRepository {
    private static volatile DataRepository mInstance;
    private AppDatabase mAppDatabase;
    private WifiScanner mWifiScanner;
    private HashMap<String, List<WifiSignals>> mTrackedBssidSet;

    public DataRepository(AppDatabase appDatabase, WifiScanner wifiScanner, HashMap<String, List<WifiSignals>> trackedBssidSet) {
        mAppDatabase = appDatabase;
        mWifiScanner = wifiScanner;
        mTrackedBssidSet = trackedBssidSet;
    }
    public static DataRepository getInstance(AppDatabase appDatabase, WifiScanner wifiScanner, HashMap<String, List<WifiSignals>> trackedBssidSet){
        if(mInstance == null){
            synchronized (DataRepository.class){
                if(mInstance == null){
                    mInstance = new DataRepository(appDatabase, wifiScanner, trackedBssidSet);
                }
            }
        }
        return mInstance;
    }
    public static DataRepository getInstance(){
        if(mInstance == null){
            throw new NullPointerException();
        }
        return mInstance;
    }

    public HashMap<String, List<WifiSignals>> getTrackedBssidSet(){
        return mTrackedBssidSet;
    }

    public LiveData<List<Wifi>> getWifiList(){
        return mWifiScanner.getWifiListLiveData();
    }
}
