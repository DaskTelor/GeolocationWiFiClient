package com.nstu.geolocationwificlient.ui.wifilist;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.nstu.geolocationwificlient.BuildConfig;
import com.nstu.geolocationwificlient.DataRepository;
import com.nstu.geolocationwificlient.data.ResultWifiScan;
import com.nstu.geolocationwificlient.network.NetworkService;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;
import com.nstu.geolocationwificlient.data.Wifi;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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