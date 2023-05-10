package com.nstu.geolocationwificlient.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.nstu.geolocationwificlient.data.ResultWifiScan;
import com.nstu.geolocationwificlient.data.SavedResultWifiScan;
import com.nstu.geolocationwificlient.db.dao.SavedResultWifiScanDao;

@Database(entities = {SavedResultWifiScan.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedResultWifiScanDao resultWifiScanDao();
}
