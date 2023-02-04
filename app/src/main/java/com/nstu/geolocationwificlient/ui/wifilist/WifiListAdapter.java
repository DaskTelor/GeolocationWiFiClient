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

import java.util.LinkedList;
import java.util.List;

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.WifiHolder>{
    private final List<Wifi> items = new LinkedList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Wifi> data) {
        items.clear();
        items.addAll(data);
        //TODO: Try to rework with more efficient method
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
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
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
