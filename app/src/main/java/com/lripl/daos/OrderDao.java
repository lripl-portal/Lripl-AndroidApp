package com.lripl.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.lripl.entities.Orders;
import com.lripl.entities.Users;

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
