package com.nstu.geolocationwificlient;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.google.gson.Gson;
import com.nstu.geolocationwificlient.data.ResultWifiScan;
import com.nstu.geolocationwificlient.data.SavedResultWifiScan;
import com.nstu.geolocationwificlient.data.Wifi;
import com.nstu.geolocationwificlient.data.WifiSignals;
import com.nstu.geolocationwificlient.db.AppDatabase;
import com.nstu.geolocationwificlient.listener.IWifiTrackedChangeListener;
import com.nstu.geolocationwificlient.network.NetworkService;
import com.nstu.geolocationwificlient.network.NetworkUtils;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

import java.util.AbstractMap;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends Application {

    private Thread currentScanThread;
    private WifiScanner mWifiScanner;
    private DataRepository mDataRepository;
    private  AppDatabase mAppDatabase;
    private int countOfScanning = 0;
    private HashMap<String, WifiSignals> mTrackedBssidSet;
    private IWifiTrackedChangeListener IWifiTrackedChangeListener;
    @Override
    public void onCreate() {
        super.onCreate();

        mTrackedBssidSet = new HashMap<>();

        mWifiScanner = WifiScanner.getInstance((WifiManager) getApplicationContext().
                getSystemService(Context.WIFI_SERVICE), mTrackedBssidSet);

        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").build();

        mDataRepository = DataRepository.getInstance(
                mAppDatabase,
                mWifiScanner,
                mTrackedBssidSet);

        mWifiScanner.getWifiListLiveData().observeForever(wifiList -> {
            if(wifiList.isEmpty())
                return;

            postResultWifiScan(
                    new ResultWifiScan(
                            NetworkUtils.getMACAddress("wlan0"),
                            BuildConfig.VERSION_NAME,
                            getApplicationInfo().processName,
                            wifiList));

            countOfScanning++;

            for (Wifi wifi: wifiList) {

                if(Boolean.FALSE.equals(wifi.getIsTracked().getValue()))
                    continue;

                WifiSignals wifiSignalsByBssid = mTrackedBssidSet.get(wifi.getBSSID());

                if(wifiSignalsByBssid == null)
                    continue;

                wifiSignalsByBssid.addItem(wifi.getLevel().get(0), countOfScanning);
            }
        });

        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(mWifiScanner, intentFilter);
    }


    public void postResultWifiScan(ResultWifiScan resultWifiScan){
        Call<ResultWifiScan> call = NetworkService.getInstance()
                .getResultWifiScanApi()
                .postResultWifiList(resultWifiScan);
        call.enqueue(new Callback<ResultWifiScan>() {
            @Override
            public void onResponse(@NonNull Call<ResultWifiScan> call, @NonNull Response<ResultWifiScan> response) {
                Log.d("Network", "onResponse");
                if (response.isSuccessful()) {
                    Log.d("Network", "Successful");
                } else {
                    Log.d("Network", "Some error");
                    new Thread(() -> mAppDatabase.resultWifiScanDao()
                            .insertAll(new SavedResultWifiScan(new Gson().toJson(resultWifiScan))));
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResultWifiScan> call, @NonNull Throwable t) {
                Log.d("Network", "onFailure");
                new Thread(() -> mAppDatabase.resultWifiScanDao()
                        .insertAll(new SavedResultWifiScan(new Gson().toJson(resultWifiScan))));

            }
        });
    }

    public void startWifiScan(){
        currentScanThread = new Thread(mWifiScanner);
        mWifiScanner.setIsRunning(true);
        currentScanThread.start();
    }
    public void stopWifiScan(){
        currentScanThread.interrupt();
        mWifiScanner.setIsRunning(false);
    }
    public void addTrackedBssid(String bssid){
        WifiSignals wifiSignals = new WifiSignals();
        mTrackedBssidSet.put(bssid, wifiSignals);

        if(IWifiTrackedChangeListener == null)
            return;

        IWifiTrackedChangeListener.onAddWifiTracked(new AbstractMap.SimpleEntry<>(bssid, wifiSignals));
    }
    public void removeTrackedBssid(String bssid){
        mTrackedBssidSet.remove(bssid);
    }
    public void setWifiTrackedChangeListener(IWifiTrackedChangeListener IWifiTrackedChangeListener){
        this.IWifiTrackedChangeListener = IWifiTrackedChangeListener;
    }
}



