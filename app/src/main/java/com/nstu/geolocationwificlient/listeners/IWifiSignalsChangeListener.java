package com.nstu.geolocationwificlient.listeners;

public interface IWifiSignalsChangeListener {
    void onAddWifiSignal(int rssi, int timeStep);
}
