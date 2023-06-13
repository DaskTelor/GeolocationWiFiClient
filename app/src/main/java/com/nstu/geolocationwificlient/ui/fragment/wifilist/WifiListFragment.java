package com.nstu.geolocationwificlient.ui.fragment.wifilist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.nstu.geolocationwificlient.R;
import com.nstu.geolocationwificlient.databinding.FragmentWifiListBinding;


public class WifiListFragment extends Fragment {
    private final int REQUEST_CODE_PERMISSION_WIFI_STATE = 1;
    private final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 2;
    private final int REQUEST_CODE_PERMISSION_CHANGE_WIFI_STATE = 3;
    private FragmentWifiListBinding binding;
    private WifiListViewModel viewModel;
    private WifiListAdapter wifiListAdapter;

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

        viewModel = new ViewModelProvider(this).get(WifiListViewModel.class);

        int displaySizeMark = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        int countSpan = displaySizeMark * getResources().getConfiguration().orientation - 1;

        wifiListAdapter = new WifiListAdapter();

        binding.wifiListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), countSpan <= 0 ? 1 : countSpan));
        binding.wifiListRecyclerView.setAdapter(wifiListAdapter);
        wifiListAdapter.changeLifecycleOwner(getViewLifecycleOwner());

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getWifiListLiveData().
                observe(getViewLifecycleOwner(), wifiList -> {
                    wifiListAdapter.setData(wifiList);
                });
        registerForContextMenu(binding.wifiListRecyclerView);

        viewModel.getAscending().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAscending) {
                wifiListAdapter.setSortByAscending(isAscending);
            }
        });
        viewModel.getSortType().observe(getViewLifecycleOwner(), new Observer<WifiSortType>() {
            @Override
            public void onChanged(WifiSortType wifiSortType) {
                wifiListAdapter.setSortType(wifiSortType);
            }
        });

        requestPermissions();
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
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(wifiListAdapter.getContextClickItem() == null)
            return;

        if(Boolean.TRUE.equals(wifiListAdapter.getContextClickItem().getIsTracked().getValue()))
            new MenuInflater(getContext()).inflate(R.menu.wifi_action_tracked_menu, menu);
        else
            new MenuInflater(getContext()).inflate(R.menu.wifi_action_menu, menu);

        viewModel.stopScan();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case (R.id.action_start_tracking):
                viewModel.startTrackingSelectedItem(wifiListAdapter.getContextClickItem());
                break;
            case (R.id.action_stop_tracking):
                viewModel.stopTrackingSelectedItem(wifiListAdapter.getContextClickItem());
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}