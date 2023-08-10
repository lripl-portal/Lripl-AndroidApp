package com.lripl.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

