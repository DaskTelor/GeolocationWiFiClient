package com.nstu.geolocationwificlient.data;

import java.util.ArrayList;
import java.util.List;

public class WifiSignals {
    private ArrayList<Integer> mRssi;

    public WifiSignals(){
        mRssi = new ArrayList<>();
    }
    public void addRssi(int rssi){
        mRssi.add(rssi);
    }
    public ArrayList<Integer> getRssi(){
        return mRssi;
    }
}
