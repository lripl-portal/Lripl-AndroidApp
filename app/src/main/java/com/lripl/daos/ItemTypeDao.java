package com.lripl.daos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import com.lripl.entities.ItemType;
import com.lripl.entities.States;

import java.util.List;

@Dao
public interface ItemTypeDao  {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveItemType(ItemType itemType);

    @Query("select * from item_types")
    LiveData<List<ItemType>> getAllItemTypes();

    @Query("select * from item_types where item_type_id=:item_type_id")
    LiveData<ItemType> getItemType(String item_type_id);


}
