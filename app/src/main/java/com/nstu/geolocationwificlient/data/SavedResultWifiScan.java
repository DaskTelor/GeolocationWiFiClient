package com.nstu.geolocationwificlient.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SavedResultWifiScan {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String resultWifiScan;
    public SavedResultWifiScan(String resultWifiScan) {
        this.resultWifiScan = resultWifiScan;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getResultWifiScan() {
        return resultWifiScan;
    }
    public void setResultWifiScan(String resultWifiScan) {
        this.resultWifiScan = resultWifiScan;
    }
    @Override
    public String toString() {
        return "SavedResultWifiScan{" +
                "id=" + id +
                ", resultWifiScan='" + resultWifiScan + '\'' +
                '}';
    }
}