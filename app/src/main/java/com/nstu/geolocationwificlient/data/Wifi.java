package com.nstu.geolocationwificlient.data;

import androidx.annotation.NonNull;

public class Wifi {
    private final String ssid;
    private final String bssid;
    private int level;

    public Wifi() {
        this("SSID", "BSSID", 0);
    }
    public Wifi(String ssid,String bssid, int level) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.level = level;
    }

    public String getSSID() {
        return ssid;
    }

    public String getBSSID() {
        return bssid;
    }

    public int getLevel() {
        return level;
    }

    @NonNull
    @Override
    public String toString() {
        return "{SSID:" + this.ssid + ";"
                + "BSSID:" + this.bssid + ";"
                + "level:" + this.level + "}";
    }
}
