package com.nstu.geolocationwificlient.ui.fragment.wifilist;

import android.annotation.SuppressLint;
import android.database.Observable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.nstu.geolocationwificlient.R;
import com.nstu.geolocationwificlient.data.Wifi;
import com.nstu.geolocationwificlient.databinding.WifiItemBinding;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.WifiHolder>{
    private final List<Wifi> mItems = new LinkedList<>();
    private WifiSortType mSortType = WifiSortType.BSSID;
    private boolean mSortByAscending = false;
    private Wifi mContextClickItem = null;
    private final MutableLiveData<LifecycleOwner> mLifecycleOwnerLiveData = new MutableLiveData<>();


    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Wifi> data) {
        mItems.clear();
        mItems.addAll(data);

        switch(mSortType){
            case SSID:
                Collections.sort(mItems,
                        Comparator.comparing(Wifi::getSSID));
                break;
            case BSSID:
                Collections.sort(mItems,
                        Comparator.comparing(Wifi::getBSSID));
                break;
            case LEVEL:
                Collections.sort(mItems,
                        Comparator.comparingInt((Wifi o) -> o.getLevel().size() > 0 ? o.getLevel().get(0) : 0));
                break;
        }

        if (!mSortByAscending)
            Collections.reverse(mItems);

        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WifiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        WifiItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.wifi_item, parent, false);

        WifiHolder holder = new WifiHolder(binding, mLifecycleOwnerLiveData);

        binding.getRoot().setOnCreateContextMenuListener(
                (contextMenu, view, contextMenuInfo) -> {
                    if(mItems.size() < holder.getAdapterPosition()) {
                        mContextClickItem = null;
                        return;
                    }
                    mContextClickItem = mItems.get(holder.getAdapterPosition());
                });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WifiHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return  mItems.size();
    }
    public Wifi getContextClickItem(){
        return mContextClickItem;
    }
    public void changeLifecycleOwner(LifecycleOwner lifecycleOwner){
        mLifecycleOwnerLiveData.setValue(lifecycleOwner);
    }

    static class WifiHolder extends RecyclerView.ViewHolder{
        private int id;
        WifiItemBinding mBinding;
        public WifiHolder(@NonNull WifiItemBinding binding, MutableLiveData<LifecycleOwner> lifecycleOwnerLiveData) {
            super(binding.getRoot());
            mBinding = binding;
            lifecycleOwnerLiveData.observeForever(lifecycleOwner -> {
                mBinding.setLifecycleOwner(lifecycleOwner);
            });
        }

        public void bind(Wifi wifi) {
            mBinding.setWifi(wifi);
            mBinding.executePendingBindings();
        }
    }
}
