package com.nstu.geolocationwificlient.ui.wifilist;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nstu.geolocationwificlient.DataRepository;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;
import com.nstu.geolocationwificlient.data.Wifi;

import java.util.List;

public class WifiListViewModel extends AndroidViewModel {

    private final LiveData<List<Wifi>> wifiList;

    public WifiListViewModel(@NonNull Application application) {
        super(application);

        WifiScanner wifiScanner = WifiScanner.getInstance();
        wifiScanner.setWifiManager(
                (WifiManager) application.
                        getApplicationContext().
                        getSystemService(Context.WIFI_SERVICE));

        DataRepository repository = DataRepository.getInstance(wifiScanner);

        this.wifiList = repository.getData();
    }

    public LiveData<List<Wifi>> getWifiList() {
        return this.wifiList;
    }
}