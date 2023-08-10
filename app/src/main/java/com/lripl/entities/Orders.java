package com.lripl.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
