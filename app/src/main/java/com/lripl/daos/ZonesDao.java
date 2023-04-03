package com.lripl.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.lripl.entities.Zones;

@Dao
public interface ZonesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveZone(Zones zones);

    @Update
    void updateZone(Zones zones);
}
