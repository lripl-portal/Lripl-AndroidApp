package com.lripl.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "OrderItem",indices = {@Index(value = {"order_item_id"},unique = true)})
public class OrderItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "order_item_id")
    public String order_item_id;

    @ColumnInfo(name = "order_id")
    public String order_id;

    @ColumnInfo(name = "product_id")
    public String product_id;

    @ColumnInfo(name = "product_name")
    public String product_name;

    @ColumnInfo(name = "product_image")
    public String product_image;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "status")
    public String status;

    @ColumnInfo(name = "user_id")
    public String user_id;

    @ColumnInfo(name = "createdat")
    public Date createdat;

    @ColumnInfo(name = "lastmodifiedat")
    public Date lastmodifiedat;

}
