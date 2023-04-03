package com.lripl.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

import com.lripl.utils.BrandConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

