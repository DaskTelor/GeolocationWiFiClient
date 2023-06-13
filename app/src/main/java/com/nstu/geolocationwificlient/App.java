package com.nstu.geolocationwificlient;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nstu.geolocationwificlient.data.ResultWifiScan;
import com.nstu.geolocationwificlient.data.SavedResultWifiScan;
import com.nstu.geolocationwificlient.data.Wifi;
import com.nstu.geolocationwificlient.data.WifiSignals;
import com.nstu.geolocationwificlient.db.AppDatabase;
import com.nstu.geolocationwificlient.listeners.IWifiTrackedChangeListener;
import com.nstu.geolocationwificlient.network.NetworkService;
import com.nstu.geolocationwificlient.ui.fragment.wifilist.WifiSortType;
import com.nstu.geolocationwificlient.wifi.scanner.WifiScanner;

import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends Application {
    private Thread currentScanThread;
    private WifiScanner mWifiScanner;
    private DataRepository mDataRepository;
    private String mac;
    private AppDatabase mAppDatabase;
    private Date dateOfStart;
    private HashMap<String, WifiSignals> mTrackedBssidSet;
    private IWifiTrackedChangeListener IWifiTrackedChangeListener;
    private String androidId;
    private MutableLiveData<WifiSortType> mSortType = new MutableLiveData<>(WifiSortType.SSID);
    private MutableLiveData<Boolean> mAscending = new MutableLiveData<>(true);
    private final Object lock = new Object();

    private final Gson gson = new GsonBuilder().create();

    @Override
    public void onCreate() {
        super.onCreate();

        androidId = Utils.getAndroidId(this);
        dateOfStart = new Date();

        mTrackedBssidSet = new HashMap<>();

        mWifiScanner = WifiScanner.createInstance((WifiManager) getApplicationContext().
                getSystemService(Context.WIFI_SERVICE), mTrackedBssidSet);

        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").build();

        mDataRepository = DataRepository.createInstance(
                mAppDatabase,
                mWifiScanner,
                mTrackedBssidSet);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.d("App", "" + mAppDatabase.resultWifiScanDao().getCount());
            }
        });


        mac = Utils.getMACAddress("wlan0");

        mDataRepository.getWifiList().observeForever(wifiList -> {
            if (wifiList.isEmpty())
                return;

            ResultWifiScan wifiScan =
                    new ResultWifiScan(
                            mac.equals("") ? androidId : mac,
                            BuildConfig.VERSION_NAME,
                            getApplicationInfo().processName,
                            wifiList);

            postResultWifiScan(wifiScan);

            int secondsPassed = (int) ((new Date().getTime() - dateOfStart.getTime()) / 1000);

            for (Wifi wifi : wifiList) {

                if (Boolean.FALSE.equals(wifi.getIsTracked().getValue()))
                    continue;

                WifiSignals wifiSignalsByBssid = mTrackedBssidSet.get(wifi.getBSSID());

                if (wifiSignalsByBssid == null)
                    continue;

                wifiSignalsByBssid.addItem(wifi.getLevel().get(0), secondsPassed);
            }
        });

        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(mWifiScanner, intentFilter);
    }


    public void postResultWifiScan(ResultWifiScan resultWifiScan) {
        Call<ResultWifiScan> call = NetworkService.getInstance()
                .getResultWifiScanApi()
                .postResultWifiList(resultWifiScan);
        call.enqueue(new Callback<ResultWifiScan>() {
            @Override
            public void onResponse(@NonNull Call<ResultWifiScan> call, @NonNull Response<ResultWifiScan> response) {
                if (response.isSuccessful()) {
                    new Thread(() ->
                            retryPostResultWifiScan(mAppDatabase.resultWifiScanDao().getOne()))
                            .start();
                    return;
                }
                saveResultToDatabase(resultWifiScan);
            }

            @Override
            public void onFailure(@NonNull Call<ResultWifiScan> call, @NonNull Throwable t) {
                saveResultToDatabase(resultWifiScan);
            }
        });
    }

    public void startWifiScan() {
        currentScanThread = new Thread(mWifiScanner);
        mWifiScanner.setIsRunning(true);
        currentScanThread.start();
    }

    public void stopWifiScan() {
        currentScanThread.interrupt();
        mWifiScanner.setIsRunning(false);
    }

    private void retryPostResultWifiScan(List<SavedResultWifiScan> list) {
        if (list.size() == 0)
            return;

        SavedResultWifiScan savedResultWifiScan = list.get(0);

        Call<String> call = NetworkService.getInstance()
                .getSavedResultWifiScanApi()
                .postSavedResultWifiScan(savedResultWifiScan.getResultWifiScan());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        deleteFromDatabase(savedResultWifiScan);
                        retryPostResultWifiScan(mAppDatabase.resultWifiScanDao().getOne());
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("App", "saveResultToDatabase: failure");
            }
        });
    }

    public void addTrackedBssid(String bssid) {
        WifiSignals wifiSignals = new WifiSignals();
        mTrackedBssidSet.put(bssid, wifiSignals);

        if (IWifiTrackedChangeListener == null)
            return;

        IWifiTrackedChangeListener.onAddWifiTracked(new AbstractMap.SimpleEntry<>(bssid, wifiSignals));
    }

    public void removeTrackedBssid(String bssid) {
        mTrackedBssidSet.remove(bssid);
    }

    public void setWifiTrackedChangeListener(IWifiTrackedChangeListener IWifiTrackedChangeListener) {
        this.IWifiTrackedChangeListener = IWifiTrackedChangeListener;
    }

    private void saveResultToDatabase(ResultWifiScan resultWifiScan) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            String json = gson.toJson(resultWifiScan);
            mAppDatabase.resultWifiScanDao()
                    .insertAll(new SavedResultWifiScan(json));
            Log.d("App", "saveResultToDatabase: success" + json);
        });
    }

    private void deleteFromDatabase(SavedResultWifiScan savedResultWifiScan) {
        Log.d("App", "deleteFromDatabase");
        AppExecutors.getInstance().diskIO().execute(() -> {
            mAppDatabase.resultWifiScanDao().delete(savedResultWifiScan);
        });

    }
    public void setAscending(boolean ascending) {
        mAscending.setValue(ascending);
    }

    public LiveData<Boolean> getAscending() {
        return mAscending;
    }

    public void setSortType(WifiSortType sortType) {
        mSortType.setValue(sortType);
    }

    public LiveData<WifiSortType> getSortType() {
        return mSortType;
    }

}



