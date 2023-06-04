package com.nstu.geolocationwificlient.listeners;

import com.nstu.geolocationwificlient.data.WifiSignals;

import java.util.Map;

public interface IWifiTrackedChangeListener {
    void onAddWifiTracked(Map.Entry<String, WifiSignals> entry);
}
