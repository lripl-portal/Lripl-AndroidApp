package com.lripl.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lripl.entities.Users;

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
