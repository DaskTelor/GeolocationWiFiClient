package com.nstu.geolocationwificlient.ui.activity.navigation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nstu.geolocationwificlient.App;
import com.nstu.geolocationwificlient.DataRepository;
import com.nstu.geolocationwificlient.ui.fragment.wifilist.WifiSortType;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

public class NavigationViewModel extends AndroidViewModel{
    private final WifiScanner mWifiScanner;
    private final DataRepository mDataRepository;
    private final App mApp;

    public NavigationViewModel(@NonNull Application application) {
        super(application);
        mWifiScanner = WifiScanner.getInstance();
        mDataRepository = DataRepository.getInstance();
        mApp = (App) application;
    }

    public void stopWifiScanner(){
        mApp.stopWifiScan();
    }
    public void startWifiScanner(){
        mApp.startWifiScan();
    }
    public LiveData<Boolean> isRunning(){
        return mWifiScanner.getIsRunningLiveData();
    }

    public void setAscending(boolean ascending) {
        mApp.setAscending(ascending);
    }
    public LiveData<Boolean> getAscending(){
        return mApp.getAscending();
    }
    public void setSortType(WifiSortType sortType) {
        mApp.setSortType(sortType);
    }
    public LiveData<WifiSortType> getSortType(){
        return mApp.getSortType();
    }
    public LiveData<Integer> getUnpostedCount(){
        return mApp.getUnpostedCount();
    }
}
