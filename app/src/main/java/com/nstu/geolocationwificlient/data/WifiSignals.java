package com.nstu.geolocationwificlient.data;

import android.util.Log;

import com.nstu.geolocationwificlient.listener.IWifiSignalsChangeListener;

import java.util.ArrayList;

public class WifiSignals {
    private ArrayList<Integer> mRssi;
    private ArrayList<Integer> mTimeSteps;
    private IWifiSignalsChangeListener mIWifiSignalsChangeListener;

    public WifiSignals(){
        mRssi = new ArrayList<>();
        mTimeSteps = new ArrayList<>();
    }
    public void addItem(int rssi, int timeStep){
        mRssi.add(rssi);
        mTimeSteps.add(timeStep);

        Log.d("Charts", this.toString());

        if(mIWifiSignalsChangeListener == null)
            return;

        Log.d("Charts", this.toString() + " Hi");

        mIWifiSignalsChangeListener.onAddWifiSignal(rssi, timeStep);
    }
    public ArrayList<Integer> getRssi(){
        return mRssi;
    }
    public ArrayList<Integer> getTimeSteps(){
        return mTimeSteps;
    }
    public void setChangeListener(IWifiSignalsChangeListener IWifiSignalsChangeListener){
        mIWifiSignalsChangeListener = IWifiSignalsChangeListener;
    }
}
