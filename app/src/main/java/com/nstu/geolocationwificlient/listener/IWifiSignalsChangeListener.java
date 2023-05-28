package com.nstu.geolocationwificlient.listener;

public interface IWifiSignalsChangeListener {
    void onAddWifiSignal(int rssi, int timeStep);
}
