package com.nstu.geolocationwificlient.ui.wifilist;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nstu.geolocationwificlient.databinding.FragmentWifiListBinding;


public class WifiListFragment extends Fragment {

    private FragmentWifiListBinding binding;
    private WifiListAdapter adapter;

    public WifiListFragment(){
        super();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWifiListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WifiListViewModel mViewModel = new ViewModelProvider(this).get(WifiListViewModel.class);

        adapter = new WifiListAdapter();

        binding.wifiListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.wifiListRecyclerView.setAdapter(adapter);

        mViewModel.getWifiList().
                observe(getViewLifecycleOwner(), wifiList -> {
                    adapter.setData(wifiList);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        adapter = null;
    }

}