package com.nstu.geolocationwificlient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nstu.geolocationwificlient.data.Wifi;

import java.util.ArrayList;
import java.util.List;

public class WifiScanner {
    private static volatile WifiScanner instance;

    private final MutableLiveData<List<Wifi>> wifiList;
    private final ArrayList<Wifi> mWifiArrayList;

    private WifiScanner(){
        this.wifiList = new MutableLiveData<>();
        this.mWifiArrayList = new ArrayList<Wifi>();

        this.wifiList.postValue(this.mWifiArrayList);
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
    public LiveData<List<Wifi>> loadAll(){
        return wifiList;
    }

    public void update() {
        mWifiArrayList.add(new Wifi());
        wifiList.setValue(mWifiArrayList);
    }
}
