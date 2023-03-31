package com.nstu.geolocationwificlient.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wifi {
    @SerializedName("ch")
    @Expose
    private int channelNumber;
    @SerializedName("bss_mac")
    @Expose
    private final String bssid;
    @SerializedName("type_bitmask")
    @Expose
    private byte typeBitmask;
    @SerializedName("timestamp")
    @Expose
    private long timestamp;
    @SerializedName("rssi")
    @Expose
    private final int level;
    @SerializedName("ssid")
    @Expose
    private final String ssid;
    @SerializedName("distance")
    @Expose
    private int distance;

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

    @Override
    public String toString() {
        return "Wifi{" +
                "channelNumber=" + channelNumber +
                ", bssid='" + bssid + '\'' +
                ", typeBitmask=" + typeBitmask +
                ", timestamp=" + timestamp +
                ", level=" + level +
                ", ssid='" + ssid + '\'' +
                ", distance=" + distance +
                '}';
    }
}
