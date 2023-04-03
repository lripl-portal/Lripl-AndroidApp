package com.lripl.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@SuppressWarnings("serial")

@Entity(tableName = "item_types",indices = {@Index(value = {"item_type_id"},unique = true)})
public class ItemType implements Serializable {

	@PrimaryKey(autoGenerate = true)
	public long id;
	
	@ColumnInfo(name = "item_type_id")
	public String item_type_id;
	
	@ColumnInfo(name = "name")
	public String name;
		
	@ColumnInfo(name = "createdat")
	public Date createdat;
	
	@ColumnInfo(name = "lastmodifiedat")
	public Date lastmodifiedat;

	@ColumnInfo(name = "imageurl")
	public String imageurl;
	
	@Ignore
	public List<Items> itemslist;

	@Ignore
	public List<Products> productsList;
	


}
