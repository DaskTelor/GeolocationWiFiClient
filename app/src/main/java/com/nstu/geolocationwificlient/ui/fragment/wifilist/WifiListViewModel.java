package com.nstu.geolocationwificlient.ui.fragment.wifilist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

    public LiveData<List<Wifi>> getWifiListLiveData() {
        return mDataRepository.getWifiList();
    }
}