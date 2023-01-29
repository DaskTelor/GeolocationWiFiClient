package com.nstu.geolocationwificlient;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nstu.geolocationwificlient.databinding.ActivityMainBinding;
import com.nstu.geolocationwificlient.ui.wifilist.WifiListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonUpdate.setOnClickListener(view -> WifiScanner.getInstance().update());
    }
}