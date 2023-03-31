package com.nstu.geolocationwificlient.ui.wifilist;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nstu.geolocationwificlient.data.ResultWifiScan;
import com.nstu.geolocationwificlient.databinding.FragmentWifiListBinding;


public class WifiListFragment extends Fragment {
    private final int REQUEST_CODE_PERMISSION_WIFI_STATE = 1;
    private final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 2;
    private final int REQUEST_CODE_PERMISSION_CHANGE_WIFI_STATE = 3;
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

        int displaySizeMark = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        int countSpan = displaySizeMark * getResources().getConfiguration().orientation - 1;

        binding.wifiListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), countSpan <= 0 ? 1 : countSpan));
        binding.wifiListRecyclerView.setAdapter(adapter);

        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);

        mViewModel.getWifiListLiveData().
                observe(getViewLifecycleOwner(), wifiList -> {
                    adapter.setData(wifiList);
                });

        binding.buttonUpdate.setOnClickListener(view -> {
            if(Boolean.FALSE.equals(mViewModel.getWifiScannerRunning().getValue()))
            {
                requestPermissions();
                mViewModel.startWifiScanner();
            } else{
                mViewModel.stopWifiScanner();
            }
        });
    }
    public void requestPermissions(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_CODE_PERMISSION_WIFI_STATE);
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CHANGE_WIFI_STATE}, REQUEST_CODE_PERMISSION_CHANGE_WIFI_STATE);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        adapter = null;
    }

}