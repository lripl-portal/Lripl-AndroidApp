package com.lripl.daos;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;

import com.lripl.entities.ItemType;

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
