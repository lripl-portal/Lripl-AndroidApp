package com.lripl.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.databinding.BaseObservable;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Products extends BaseObservable implements Comparable<Products>, Serializable{

	@PrimaryKey(autoGenerate = true)
	public long id;

	@ColumnInfo(name = "product_id")
	public String product_id;
	
	@ColumnInfo(name = "name")
	public String name;
	
	@ColumnInfo(name = "item_type_id")
	public String item_type_id;
	
	@ColumnInfo(name = "item_id")
	public String item_id;
	
	@ColumnInfo(name = "zone_id")
	public String zone_id;
	
	@ColumnInfo(name = "state_id")
	public String state_id;
	
	@ColumnInfo(name = "isactive")
	public boolean isactive;
	
	@ColumnInfo(name = "imageurl")
	public String imageurl;
	
	@ColumnInfo(name = "company")
	public String company;

	@ColumnInfo(name = "description")
	public String description;

	@ColumnInfo(name = "createdat")
	public Date createdat;

	@ColumnInfo(name = "lastmodifiedat")
	public Date lastmodifiedat;

	@Ignore
	public int quantity=0;

	@ColumnInfo(name = "brandName")
	public String brandName;

	@Override
	public int compareTo(@NonNull Products products) {
		 return products.name.compareTo(this.name);
	}
}

