package com.nstu.geolocationwificlient.ui.wifilist;

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

    //TODO: Try to rework with more efficient method

    //неэффективный метод для полной замены существующего списка
    public void setData(List<Wifi> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WifiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //создаем объект байндинга для элемента списка
        WifiItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.wifi_item, parent, false);

        return new WifiHolder(binding);
    }
    //Отображаем объект на position
    @Override
    public void onBindViewHolder(@NonNull WifiHolder holder, int position) {
        holder.bind(items.get(position));
    }

    //получаем количество всех элементов
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Объект который будет отображать видимые элементы
    static class WifiHolder extends RecyclerView.ViewHolder{
        WifiItemBinding binding;


        public WifiHolder(@NonNull WifiItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        //бинд конкретного элемента в данном случае с помощью DataBinding
        public void bind(Wifi wifi) {
            binding.setWifi(wifi);
            binding.executePendingBindings();
        }
    }
}
