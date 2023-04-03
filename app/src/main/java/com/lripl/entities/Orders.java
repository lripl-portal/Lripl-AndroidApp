package com.lripl.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(tableName = "Orders",indices = {@Index(value = {"order_id"},unique = true)})
public class Orders implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "order_id")
    public String order_id;

    @ColumnInfo(name = "order_number")
    public String order_number;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "user_id")
    public String user_id;

    @ColumnInfo(name = "createdat")
    public Date createdat;

    @ColumnInfo(name = "lastmodifiedat")
    public Date lastmodifiedat;

    @Ignore
    public List<OrderItem> orderItemObjs;

    @Ignore
    public boolean isGroupExpanded;




}
