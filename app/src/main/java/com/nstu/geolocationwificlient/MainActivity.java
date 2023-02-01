package com.nstu.geolocationwificlient;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.nstu.geolocationwificlient.wifi.scanner.WifiScannerState;
import com.nstu.geolocationwificlient.databinding.ActivityMainBinding;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

public class MainActivity extends AppCompatActivity{
    private final int REQUEST_CODE_PERMISSION_WIFI_STATE = 1;
    private final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 1;
    private Thread threadUpdateWifiList;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setWifiScannerState(WifiScannerState.getInstance());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_CODE_PERMISSION_WIFI_STATE);
        }

        binding.buttonUpdate.setOnClickListener(view -> {
            if(WifiScannerState.getInstance().getIsRunningObservable().get()){
                WifiScannerState.getInstance().getIsRunningObservable().set(false);
                threadUpdateWifiList.interrupt();
            }
            else{
                WifiScannerState.getInstance().getIsRunningObservable().set(true);
                threadUpdateWifiList = new Thread(WifiScanner.getInstance());
                threadUpdateWifiList.start();
            }
        });
    }
}