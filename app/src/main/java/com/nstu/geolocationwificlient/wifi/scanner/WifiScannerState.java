package com.nstu.geolocationwificlient.wifi.scanner;

import androidx.databinding.ObservableBoolean;

public class WifiScannerState {
    private volatile static WifiScannerState instance;
    private ObservableBoolean isRunningObservable;
    private WifiScannerState(){
        isRunningObservable = new ObservableBoolean(false);
    }
    public static WifiScannerState getInstance() {
        if(instance == null) {
            synchronized (WifiScannerState.class) {
                if(instance == null) {
                    instance = new WifiScannerState();
                }
            }
        }
        return instance;
    }
    public ObservableBoolean getIsRunningObservable() {
        return isRunningObservable;
    }
}
