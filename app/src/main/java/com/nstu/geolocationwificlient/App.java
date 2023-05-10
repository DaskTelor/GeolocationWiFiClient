package com.nstu.geolocationwificlient;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.nstu.geolocationwificlient.data.ResultWifiScan;
import com.nstu.geolocationwificlient.db.AppDatabase;
import com.nstu.geolocationwificlient.network.NetworkService;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends Application {

    private WifiScanner mWifiScanner;
    private DataRepository mDataRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        mWifiScanner = WifiScanner.getInstance((WifiManager) getApplicationContext().
                getSystemService(Context.WIFI_SERVICE));

        mDataRepository = DataRepository.getInstance(
                Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").build(),
                mWifiScanner);

        mWifiScanner.getWifiListLiveData().observeForever(wifiList -> {
            if(wifiList.isEmpty())
                return;
            postResultWifiScan(
                    new ResultWifiScan(
                            WifiScanner.getMacAddress(),
                            BuildConfig.VERSION_NAME,
                            getApplicationInfo().processName,
                            wifiList));
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
                    Toast.makeText(getApplicationContext(), "POST: Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Network", "Some error");
                    Toast.makeText(getApplicationContext(), "POST: Some error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResultWifiScan> call, @NonNull Throwable t) {
                Log.d("Network", "onFailure");
                Toast.makeText(getApplicationContext(), "POST: Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



