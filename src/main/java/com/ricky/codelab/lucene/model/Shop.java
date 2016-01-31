package com.ricky.codelab.lucene.model;

import java.util.List;

public class Shop {
	private long id;
	private String third_id;
	private String name;
	private String address;
	private List<String> phone;
	private int city_id = -1;
	private double lat = -361;
	private double lng = -361;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getThirdId() {
		return third_id;
	}
	public void setThirdId(String third_id) {
		this.third_id = third_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<String> getPhone() {
		return phone;
	}
	public void setPhone(List<String> phone) {
		this.phone = phone;
	}
	public int getCityId() {
		return city_id;
	}
	public void setCityId(int city_id) {
		this.city_id = city_id;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	
}
