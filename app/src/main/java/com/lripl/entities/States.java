package com.lripl.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")

@Entity(tableName = "states",indices = {@Index(value = {"state_id"},unique = true)})
public class States implements Serializable {

	@PrimaryKey(autoGenerate = true)
	public long id;

	@ColumnInfo(name = "state_id")
	public String state_id;

	@ColumnInfo(name = "short_name")
	public String short_name;
	
	@ColumnInfo(name = "name")
	public String name;
	
	@ColumnInfo(name = "zone_id")
	public String zone_id;
	
	@ColumnInfo(name = "createdat")
	public Date createdat;
	
	@ColumnInfo(name = "lastmodifiedat")
	public Date lastmodifiedat;
	

}
