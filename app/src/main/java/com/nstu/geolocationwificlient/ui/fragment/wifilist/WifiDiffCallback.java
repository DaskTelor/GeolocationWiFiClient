package com.nstu.geolocationwificlient.ui.fragment.wifilist;

import androidx.recyclerview.widget.DiffUtil;

import com.nstu.geolocationwificlient.data.Wifi;

import java.util.List;

public class WifiDiffCallback extends DiffUtil.Callback {
    private final List<Wifi> mOldWifiList;
    private final List<Wifi> mNewWifiList;
    public WifiDiffCallback(List<Wifi> oldWifiList, List<Wifi> newWifiList){
        mOldWifiList = oldWifiList;
        mNewWifiList = newWifiList;
    }
    @Override
    public int getOldListSize() {
        return mOldWifiList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewWifiList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldWifiList.get(oldItemPosition).getBSSID().equals(mNewWifiList.get(newItemPosition).getBSSID());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Wifi oldWifi = mOldWifiList.get(oldItemPosition);
        final Wifi newWifi = mNewWifiList.get(newItemPosition);

        return oldWifi.getLevel().get(0).equals(newWifi.getLevel().get(0));
    }
}
