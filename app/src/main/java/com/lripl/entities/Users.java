package com.lripl.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@SuppressWarnings("serial")
@Entity(tableName = "User",indices = {@Index(value = {"user_id"},unique = true)})
public class Users  implements Serializable{

	@PrimaryKey(autoGenerate = true)
	public long id;
		
	@ColumnInfo(name = "user_id")
	public String user_id;

	@ColumnInfo(name = "fullname")
	public String fullname;
	
	@ColumnInfo(name = "firstname")
	public String firstname;
	
	@ColumnInfo(name = "lastname")
	public String lastname;
	
	@ColumnInfo(name = "emailid")
	public String emailid;
	
	@ColumnInfo(name = "phonenumber")
	public String phonenumber;
	
	@ColumnInfo(name = "gstnumber")
	public String gstnumber;
	
	@ColumnInfo(name = "companyname")
	public String companyname;
	
	@ColumnInfo(name = "roleid")
	public String roleid;
	
	@ColumnInfo(name = "state_id")
	public String state_id;
	
	@ColumnInfo(name = "isactive")
	public boolean isactive;
	
	@ColumnInfo(name = "profilepicurl")
	public String profilepicurl;
	
	@ColumnInfo(name = "createdat")
	public Date createdat;
	
	@ColumnInfo(name = "lastmodifiedat")
	public Date lastmodifiedat;

	@ColumnInfo(name = "token")
	public String token;


}
