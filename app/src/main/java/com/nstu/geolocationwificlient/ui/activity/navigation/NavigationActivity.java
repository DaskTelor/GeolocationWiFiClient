package com.nstu.geolocationwificlient.ui.activity.navigation;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nstu.geolocationwificlient.R;
import com.nstu.geolocationwificlient.databinding.ActivityNavigationBinding;
import com.nstu.geolocationwificlient.databinding.DialogSortWifiListBinding;

import java.util.Objects;

public class NavigationActivity extends AppCompatActivity {

    private ActivityNavigationBinding binding;
    private NavigationViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(NavigationViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        Objects.requireNonNull(getSupportActionBar()).hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_wifi_list, R.id.navigation_locate, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.fabScan.setOnClickListener(view -> {
            if(Boolean.FALSE.equals(viewModel.isRunning().getValue())){
                viewModel.startWifiScanner();
            }else{
                viewModel.stopWifiScanner();
            }
        });

        binding.fabSort.setOnClickListener(view -> {
            DialogSortWifiListBinding dialogBinding = DialogSortWifiListBinding.inflate(getLayoutInflater());



            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_sort_wifi_list)
                    .setView(dialogBinding.getRoot())
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> {

                    })
                    .create()
                    .show();
        });
    }

}