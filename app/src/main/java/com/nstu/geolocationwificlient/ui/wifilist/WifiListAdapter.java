package com.nstu.geolocationwificlient.ui.wifilist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nstu.geolocationwificlient.R;
import com.nstu.geolocationwificlient.data.Wifi;
import com.nstu.geolocationwificlient.databinding.WifiItemBinding;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.WifiHolder>{
    private final List<Wifi> mItems = new LinkedList<>();
    private WifiSortType mSortType = WifiSortType.BSSID;
    private boolean mSortByAscending = false;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Wifi> data) {
        switch(mSortType){
            case SSID:
                Collections.sort(data,
                        Comparator.comparing(Wifi::getSSID));
                break;
            case BSSID:
                Collections.sort(data,
                        Comparator.comparing(Wifi::getBSSID));
                break;
            case LEVEL:
                Collections.sort(data,
                        Comparator.comparingInt((Wifi o) -> o.getLevel().size() > 0 ? o.getLevel().get(0) : 0));
                break;
        }

        if (!mSortByAscending)
            Collections.reverse(data);

        mItems.clear();
        mItems.addAll(data);

        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WifiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        WifiItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.wifi_item, parent, false);

        return new WifiHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WifiHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return  mItems.size();
    }

    static class WifiHolder extends RecyclerView.ViewHolder{
        WifiItemBinding binding;

        public WifiHolder(@NonNull WifiItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Wifi wifi) {
            binding.setWifi(wifi);
            binding.executePendingBindings();
        }
    }
}
