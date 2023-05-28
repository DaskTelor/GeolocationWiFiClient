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
    private final WifiListAdapter mWifiListAdapter;
    private final DataRepository mDataRepository;

    public WifiListViewModel(@NonNull Application application) {
        super(application);

        mDataRepository = DataRepository.getInstance();
        mWifiListAdapter = new WifiListAdapter();
    }

    public WifiListAdapter getWifiListAdapter() {
        return mWifiListAdapter;
    }
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner){
        mWifiListAdapter.changeLifecycleOwner(lifecycleOwner);
    }

    public LiveData<List<Wifi>> getWifiListLiveData() {
        return mDataRepository.getWifiList();
    }
    public Wifi getContextClickItem(){
        return mWifiListAdapter.getContextClickItem();
    }
    public void stopScan(){
        ((App)getApplication()).stopWifiScan();
    }

    public void startTrackingSelectedItem(){
        getContextClickItem().setIsTracked(true);

        ((App)getApplication()).addTrackedBssid(getContextClickItem().getBSSID());
    }
    public void stopTrackingSelectedItem(){
        getContextClickItem().setIsTracked(false);

        ((App)getApplication()).removeTrackedBssid(getContextClickItem().getBSSID());
    }
}