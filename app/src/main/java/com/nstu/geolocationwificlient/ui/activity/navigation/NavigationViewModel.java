package com.nstu.geolocationwificlient.ui.activity.navigation;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.nstu.geolocationwificlient.BuildConfig;
import com.nstu.geolocationwificlient.DataRepository;
import com.nstu.geolocationwificlient.data.ResultWifiScan;
import com.nstu.geolocationwificlient.data.Wifi;
import com.nstu.geolocationwificlient.db.AppDatabase;
import com.nstu.geolocationwificlient.network.NetworkService;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationViewModel extends AndroidViewModel{
    private final WifiScanner mWifiScanner;
    private final DataRepository mDataRepository;
    private Thread threadUpdateWifiList;

    public NavigationViewModel(@NonNull Application application) {
        super(application);
        mWifiScanner = WifiScanner.getInstance();
        mDataRepository = DataRepository.getInstance();
    }


    public void stopWifiScanner(){
        mWifiScanner.setIsRunning(false);
        threadUpdateWifiList.interrupt();
    }
    public void startWifiScanner(){
        mWifiScanner.setIsRunning(true);
        threadUpdateWifiList = new Thread(mWifiScanner);
        threadUpdateWifiList.start();
    }
    public LiveData<Boolean> isRunning(){
        return mWifiScanner.getIsRunningLiveData();
    }
}
