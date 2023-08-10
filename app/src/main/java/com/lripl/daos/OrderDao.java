package com.lripl.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lripl.entities.Orders;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Orders> orders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Orders orders);

    @Update
    void update(Orders order);

    @Delete
    void delete(Orders order);

    @Query("select * from Orders where user_id=:user_id")
    List<Orders> getAllOrdersByuser(String user_id);
}
