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
    private MutableLiveData<WifiSortType> mSortType = new MutableLiveData<>();
    private MutableLiveData<Boolean> mAscending = new MutableLiveData<>();

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
        mAscending.setValue(ascending);
    }
    public LiveData<Boolean> getAscending(){
        return mAscending;
    }
    public void setSortType(WifiSortType sortType) {
        mSortType.setValue(sortType);
    }
    public LiveData<WifiSortType> getSortType(){
        return mSortType;
    }
}
