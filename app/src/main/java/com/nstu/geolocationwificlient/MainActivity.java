package com.nstu.geolocationwificlient;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.nstu.geolocationwificlient.databinding.ActivityMainBinding;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

public class MainActivity extends AppCompatActivity{
    private final int REQUEST_CODE_PERMISSION_WIFI_STATE = 1;
    private final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 2;
    private final int REQUEST_CODE_PERMISSION_CHANGE_WIFI_STATE = 3;

    private Thread threadUpdateWifiList;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setWifiScanner(WifiScanner.getInstance());
        binding.setLifecycleOwner(this);

        requestPermissions();

        binding.buttonUpdate.setOnClickListener(view -> {
            if(Boolean.TRUE.equals(WifiScanner.getInstance().getIsRunningLiveData().getValue())){
                WifiScanner.getInstance().setIsRunning(false);
                threadUpdateWifiList.interrupt();
            }
            else{
                WifiScanner.getInstance().setIsRunning(true);
                threadUpdateWifiList = new Thread(WifiScanner.getInstance());
                threadUpdateWifiList.start();
            }
        });
    }

    public void requestPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_CODE_PERMISSION_WIFI_STATE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, REQUEST_CODE_PERMISSION_CHANGE_WIFI_STATE);
        }
    }
}