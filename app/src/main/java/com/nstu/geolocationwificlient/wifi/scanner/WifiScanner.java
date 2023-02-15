package com.nstu.geolocationwificlient.wifi.scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nstu.geolocationwificlient.data.Wifi;

import java.util.ArrayList;
import java.util.List;

public class WifiScanner extends BroadcastReceiver implements Runnable{

    // Singleton instance
    @SuppressLint("StaticFieldLeak")
    private static WifiScanner instance;
    private WifiManager wifiManager;
    private final MutableLiveData<List<Wifi>> wifiList;
    private final ArrayList<Wifi> mWifiList;
    private  volatile int delay;

    private WifiScanner() {
        this.wifiList = new MutableLiveData<>();
        this.mWifiList = new ArrayList<>();

        delay = 1000;
        this.wifiList.postValue(this.mWifiList);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
/*        String action = intent.getAction();//Метод getAction(),для поиска действий, связанных с намерением. Считываем действие

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);//Получаем объект класса ConnectivityManager, который следит за состоянием сети
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();//Получаем объект класса NetworkInfo для получения описания состояния сети

        if (activeNetwork != null &&  activeNetwork.isConnectedOrConnecting())
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){

            }*/
    }

    public static WifiScanner getInstance() {
        if (instance == null) {
            synchronized (WifiScanner.class) {
                if (instance == null) {
                    instance = new WifiScanner();
                }
            }
        }
        return instance;
    }

    public LiveData<List<Wifi>> loadAll() {
        return wifiList;
    }

    public void setWifiManager(WifiManager wifiManager) {
        synchronized (WifiScanner.class) {
            instance.wifiManager = wifiManager;
        }
    }
    private void startScan() {
        wifiManager.startScan();
    }
    private void update() {
        Log.d("WifiScanner", "start update");
        synchronized (WifiScanner.class) {
            List<ScanResult> scanResults;

            try {
                scanResults = wifiManager.getScanResults();
            }catch (SecurityException securityException){
                Log.d("WifiScanner", "wrong update");
                return;
            }
            mWifiList.clear();
            for (ScanResult scanResult : scanResults) {
                mWifiList.add(new Wifi(scanResult.SSID, scanResult.toString(), scanResult.level));
            }
        }
        wifiList.postValue(mWifiList);
        Log.d("WifiScanner", "success update");
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            this.update();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                this.mWifiList.clear();
                this.wifiList.postValue(mWifiList);
                break;
            }
        }
    }

}
