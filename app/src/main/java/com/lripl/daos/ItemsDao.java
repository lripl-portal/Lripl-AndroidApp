package com.lripl.daos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.lripl.entities.ItemType;
import com.lripl.entities.Items;

import java.util.List;

@Dao
public interface ItemsDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveItems(List<Items> items);

    @Query("select count(*) from items where item_type_id=:item_type_id")
    LiveData<Integer> getItemsCount(String item_type_id);

    @Query("select count(*) from products where item_id=:item_id")
    LiveData<Integer> getProductCount(String item_id);

    @Query("select * from items where item_type_id=:item_type_id ORDER BY name ASC")
    LiveData<List<Items>> getItemsList(String item_type_id);

    @Query("select * from items where item_id=:item_id")
    LiveData<Items> getItem(String item_id);
}

