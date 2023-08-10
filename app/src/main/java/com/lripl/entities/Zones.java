package com.lripl.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")

@Entity(tableName = "zones",indices = {@Index(value = {"zone_id"},unique = true)})
public class Zones implements Serializable {

	@PrimaryKey(autoGenerate = true)
	public long id;

	@ColumnInfo(name = "zone_id")
	public String zone_id;

	@ColumnInfo(name = "short_name")
	public String short_name;
	
	@ColumnInfo(name = "name")
	public String name;
	
	@ColumnInfo(name = "createdat")
	public Date createdat;
	
	@ColumnInfo(name = "lastmodifiedat")
	public Date lastmodifiedat;
	
	@Ignore
	public List<States> statesList;
	

}
