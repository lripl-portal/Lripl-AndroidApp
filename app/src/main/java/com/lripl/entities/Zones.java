package com.lripl.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

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
