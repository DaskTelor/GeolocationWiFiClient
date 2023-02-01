package com.nstu.geolocationwificlient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.nstu.geolocationwificlient.data.Wifi;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

import java.util.List;

public class DataRepository {

    // Singleton instance
    private static DataRepository instance;

    private static WifiScanner wifiScanner;
    private final MediatorLiveData<List<Wifi>> observableData;

    private DataRepository(WifiScanner wifiScanner) {
        observableData = new MediatorLiveData<>();
        DataRepository.wifiScanner = wifiScanner;

        observableData.addSource(DataRepository.wifiScanner.loadAll(),
                wifiList -> {
            observableData.setValue(wifiList);
        });
    }

    public static DataRepository getInstance(final WifiScanner wifiScanner) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(wifiScanner);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Wifi>> getData() {
        return observableData;
    }

}
