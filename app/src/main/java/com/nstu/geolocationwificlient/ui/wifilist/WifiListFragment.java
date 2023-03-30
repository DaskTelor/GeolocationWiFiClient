package com.nstu.geolocationwificlient.ui.wifilist;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        binding.wifiListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.wifiListRecyclerView.setAdapter(adapter);

        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);



        mViewModel.getWifiList().
                observe(getViewLifecycleOwner(), wifiList -> {
                    Log.d("WifiScanner", "observe: " + wifiList.toString());
                    adapter.setData(wifiList);
                });

        requestPermissions();

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