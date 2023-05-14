package com.nstu.geolocationwificlient.data;

import android.net.wifi.ScanResult;

import androidx.annotation.Nullable;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Wifi  {
    @Expose(serialize = false)
    private long id;
    @SerializedName("ch")
    @Expose
    private final int channelNumber;
    @SerializedName("mac")
    @Expose
    private final String bssid;
    @SerializedName("packet_count")
    @Expose
    private final int packetCount;
    @SerializedName("type_bitmask")
    @Expose
    private final byte typeBitmask;
    @SerializedName("timestamp")
    @Expose
    private final long timestamp;
    @SerializedName("rssi_array")
    @Expose
    private final List<Integer> level;
    @SerializedName("ssid")
    @Expose
    private final String ssid;
    @SerializedName("distance_array")
    @Expose
    private final List<Integer> distance;
    @SerializedName("radarDataId")
    @Expose
    private final int  radarId;

    public Wifi(int channelNumber,
                String bssid,
                int packetCount,
                byte typeBitmask,
                long timestamp,
                List<Integer> level,
                String ssid,
                List<Integer> distance,
                int radarId) {
        this.channelNumber = channelNumber;
        this.bssid = bssid;
        this.packetCount = packetCount;
        this.typeBitmask = typeBitmask;
        this.timestamp = timestamp;
        this.level = level;
        this.ssid = ssid;
        this.distance = distance;
        this.radarId = radarId;
    }
    public Wifi(ScanResult scanResult){
        this(0,
                scanResult.BSSID,
                0,
                (byte) 0b0,
                scanResult.timestamp,
                Collections.singletonList(scanResult.level),
                scanResult.SSID,
                Collections.singletonList(-1),
                7);
    }

    public String getSSID() {
        return ssid;
    }

    public String getBSSID() {
        return bssid;
    }

    public List<Integer> getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Wifi wifi = (Wifi) o;
        return timestamp == wifi.timestamp && bssid.equals(wifi.bssid);
    }
    @Override
    public int hashCode() {
        return Objects.hash(bssid, timestamp);
    }

    @Override
    public String toString() {
        return "Wifi{" +
                "bssid='" + bssid + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
