package com.example.dto;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable{

	private static final long serialVersionUID = 3585836189321275567L;

	private String name;
	
	private Date date;
	
	private String type;
	
	private boolean alarmSet;

	public Record(String name, Date date, String type) {
		super();
		this.name = name;
		this.date = date;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public boolean isAlarmSet() {
		return alarmSet;
	}

	public void setAlarmSet(boolean alarmSet) {
		this.alarmSet = alarmSet;
	}
	
}
