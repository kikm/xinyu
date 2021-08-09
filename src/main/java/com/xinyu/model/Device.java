package com.xinyu.model;

import java.util.List;

public class Device {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String agency;
	private Integer frequent;
	private Long unitId;
	private String unitName;
	private List<Part> partList;
	private String partListName;
	
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
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public List<Part> getPartList() {
		return partList;
	}
	public void setPartList(List<Part> partList) {
		this.partList = partList;
	}
	public String getPartListName() {
		return partListName;
	}
	public void setPartListName(String partListName) {
		this.partListName = partListName;
	}
	public Integer getFrequent() {
		return frequent;
	}
	public void setFrequent(Integer frequent) {
		this.frequent = frequent;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	
	
	
	
	
	
	
}
