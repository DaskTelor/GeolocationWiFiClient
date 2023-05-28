package com.nstu.geolocationwificlient.listener;

import com.nstu.geolocationwificlient.data.WifiSignals;

import java.util.Map;

public interface IWifiTrackedChangeListener {
    void onAddWifiTracked(Map.Entry<String, WifiSignals> entry);
}
