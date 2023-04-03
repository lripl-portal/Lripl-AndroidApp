package com.lripl.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@SuppressWarnings("serial")

@Entity(tableName = "items",indices = {@Index(value = {"item_id"},unique = true)})
public class Items implements Serializable {


	@PrimaryKey(autoGenerate = true)
	public long id;
	
	@ColumnInfo(name = "item_id")
	public String item_id;
	
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
	

}
