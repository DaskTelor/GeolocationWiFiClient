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


    private Thread threadUpdateWifiList;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getSupportActionBar().hide();
    }
}