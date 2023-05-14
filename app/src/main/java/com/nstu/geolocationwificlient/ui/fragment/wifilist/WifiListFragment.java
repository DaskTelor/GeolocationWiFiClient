package com.nstu.geolocationwificlient.ui.fragment.wifilist;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nstu.geolocationwificlient.databinding.FragmentWifiListBinding;


public class WifiListFragment extends Fragment {
    private final int REQUEST_CODE_PERMISSION_WIFI_STATE = 1;
    private final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 2;
    private final int REQUEST_CODE_PERMISSION_CHANGE_WIFI_STATE = 3;
    private FragmentWifiListBinding binding;

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

        WifiListViewModel viewModel = new ViewModelProvider(this).get(WifiListViewModel.class);

        int displaySizeMark = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        int countSpan = displaySizeMark * getResources().getConfiguration().orientation - 1;

        binding.wifiListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), countSpan <= 0 ? 1 : countSpan));
        binding.wifiListRecyclerView.setAdapter(viewModel.getWifiListAdapter());

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getWifiListLiveData().
                observe(getViewLifecycleOwner(), wifiList -> {
                    viewModel.getWifiListAdapter().setData(wifiList);
                });

        registerForContextMenu(binding.wifiListRecyclerView);

    }
    public  boolean requestPermissions(){
        boolean haveAllPermissions = true;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_FINE_LOCATION);
            haveAllPermissions = false;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_CODE_PERMISSION_WIFI_STATE);
            haveAllPermissions = false;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CHANGE_WIFI_STATE}, REQUEST_CODE_PERMISSION_CHANGE_WIFI_STATE);
            haveAllPermissions = false;
        }
        return haveAllPermissions;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}