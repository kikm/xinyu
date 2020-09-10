package com.xinyu.bean;

import java.util.List;

public class OrgTree {
	private String name;
	private Long id;
	private Long pid;
	private List<OrgTree> childrens;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long long1) {
		this.id = long1;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long long1) {
		this.pid = long1;
	}
	public List<OrgTree> getChildrens() {
		return childrens;
	}
	public void setChildrens(List<OrgTree> childrens) {
		this.childrens = childrens;
	}
	
	
	
	

}
