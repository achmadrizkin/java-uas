package com.rcyono.schedulereskul.model.schedule;

import com.google.gson.annotations.SerializedName;

public class Schedule {

	@SerializedName("date")
	private String date;

	@SerializedName("time_start")
	private String timeStart;

	@SerializedName("id")
	private String id;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("place")
	private String place;

	@SerializedName("time_end")
	private String timeEnd;

	@SerializedName("title_type")
	private String titleType;

	@SerializedName("icon_type")
	private String iconType;

	@SerializedName("desc")
	private String desc;

	public String getDate(){
		return date;
	}

	public String getTimeStart(){
		return timeStart;
	}

	public String getId(){
		return id;
	}

	public String getIdUser(){
		return idUser;
	}

	public String getPlace(){
		return place;
	}

	public String getTimeEnd(){
		return timeEnd;
	}

	public String getTitleType(){
		return titleType;
	}

	public String getIconType(){
		return iconType;
	}

	public String getDesc(){
		return desc;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}