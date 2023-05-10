package com.nstu.geolocationwificlient;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.nstu.geolocationwificlient.data.Wifi;
import com.nstu.geolocationwificlient.db.AppDatabase;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

import java.util.List;

public class DataRepository {
    private static volatile DataRepository mInstance;
    private AppDatabase mAppDatabase;
    private WifiScanner mWifiScanner;
    public DataRepository(AppDatabase appDatabase, WifiScanner wifiScanner) {
        mAppDatabase = appDatabase;
        mWifiScanner = wifiScanner;
    }
    public static DataRepository getInstance(AppDatabase appDatabase, WifiScanner wifiScanner){
        if(mInstance == null){
            synchronized (DataRepository.class){
                if(mInstance == null){
                    mInstance = new DataRepository(appDatabase, wifiScanner);
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
    public LiveData<List<Wifi>> getWifiList(){
        return mWifiScanner.getWifiListLiveData();
    }
}
