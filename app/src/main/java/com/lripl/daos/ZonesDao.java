package com.lripl.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.lripl.entities.Zones;

@Dao
public interface ZonesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveZone(Zones zones);

    @Update
    void updateZone(Zones zones);
}
