package com.nstu.geolocationwificlient.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nstu.geolocationwificlient.data.SavedResultWifiScan;

import java.util.List;

@Dao
public interface SavedResultWifiScanDao {
    @Insert
    void insertAll(SavedResultWifiScan... results);
    @Delete
    void delete(SavedResultWifiScan user);
    @Query("SELECT * FROM savedresultwifiscan")
    List<SavedResultWifiScan> getAll();
    @Query("SELECT * FROM savedresultwifiscan LIMIT 1")
    List<SavedResultWifiScan> getOne();
}