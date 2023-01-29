package com.nstu.geolocationwificlient;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.nstu.geolocationwificlient.data.Wifi;

import java.util.List;

public class DataRepository {

    // Singleton instance
    private static volatile DataRepository instance;

    private static WifiScanner wifiScanner;
    private final MediatorLiveData<List<Wifi>> observableData;

    private DataRepository(WifiScanner wifiScanner) {
        observableData = new MediatorLiveData<>();
        DataRepository.wifiScanner = wifiScanner;

        observableData.addSource(DataRepository.wifiScanner.loadAll(),
                wifiList -> {
            observableData.postValue(wifiList);
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
