package com.lripl.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
