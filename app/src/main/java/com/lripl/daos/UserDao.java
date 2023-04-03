package com.lripl.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.lripl.entities.Users;

import java.util.List;

@Dao
public interface UserDao {

  @Query("select * from User where user_id =:userid")
  LiveData<Users> getUserEntity(String userid);

  @Query("select * from User LIMIT 1")
  LiveData<Users> getUserEntity();


  @Query("select count(*) from User")
  LiveData<Integer> getUserCount();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(Users userEntity);

  @Update
  void update(Users userEntity);

  @Delete
  void delete(Users userEntity);




}
