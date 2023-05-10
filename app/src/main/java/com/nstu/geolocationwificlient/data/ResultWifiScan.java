package com.nstu.geolocationwificlient.data;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultWifiScan {
    @SerializedName("device_type")
    @Expose
    private final int deviceType = 1;
    @SerializedName("mac_id")
    @Expose
    private final String macId;
    @SerializedName("version")
    @Expose
    private final String appVersion;
    @SerializedName("ap_name")
    @Expose
    private final String appName;
    @SerializedName("probe_requests")
    @Expose
    private final List<Wifi> mWifiList;

    public ResultWifiScan(String macId, String appVersion, String appName, List<Wifi> mWifiList){
        this.macId = macId;
        this.appVersion = appVersion;
        this.appName = appName;
        this.mWifiList = mWifiList;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public String getMacId() {
        return macId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getAppName() {
        return appName;
    }

}
