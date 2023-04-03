package com.lripl.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.lripl.entities.States;

import java.util.List;

@Dao
public interface StatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveStates(List<States> states);

    @Insert
    void saveState(States state);

    @Query("select * from states ORDER BY name ASC")
    LiveData<List<States>> getAllStates();


}
