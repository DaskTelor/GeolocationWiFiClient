package com.nstu.geolocationwificlient.ui.fragment.wifilist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.nstu.geolocationwificlient.App;
import com.nstu.geolocationwificlient.DataRepository;
import com.nstu.geolocationwificlient.data.Wifi;

import java.util.List;

public class WifiListViewModel extends AndroidViewModel {
    private final DataRepository mDataRepository;
    private final App mApp;

    public WifiListViewModel(@NonNull Application application) {
        super(application);

        mApp = (App) application;
        mDataRepository = DataRepository.getInstance();

    }


    public LiveData<List<Wifi>> getWifiListLiveData() {
        return mDataRepository.getWifiList();
    }
    public void stopScan(){
        ((App)getApplication()).stopWifiScan();
    }

    public void startTrackingSelectedItem(Wifi wifi){
        wifi.setIsTracked(true);

        ((App)getApplication()).addTrackedBssid(wifi.getBSSID());
    }
    public void stopTrackingSelectedItem(Wifi wifi){
        wifi.setIsTracked(false);

        ((App)getApplication()).removeTrackedBssid(wifi.getBSSID());
    }
    public LiveData<Boolean> getAscending(){
        return mApp.getAscending();
    }
    public LiveData<WifiSortType> getSortType(){
        return mApp.getSortType();
    }
}