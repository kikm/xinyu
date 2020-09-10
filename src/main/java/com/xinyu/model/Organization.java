package com.xinyu.model;

import java.io.Serializable;

/**
 * 组织架构
 */
public class Organization implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -869096056306088683L;
	private Long id;// id主键
    private String orgName;// 组织名称
    private Long pid;// 父组织
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	
	
	 
}







