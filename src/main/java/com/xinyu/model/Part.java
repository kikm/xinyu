package com.xinyu.model;

public class Part {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String category;
	private String brand;
	private String url;
	private Float partCost;
	private Long deviceId;
	private Integer frequent;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getPartCost() {
		return partCost;
	}
	public void setPartCost(Float partCost) {
		this.partCost = partCost;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getFrequent() {
		return frequent;
	}
	public void setFrequent(Integer frequent) {
		this.frequent = frequent;
	}
	

	
	
	
	
	
	
	

}
