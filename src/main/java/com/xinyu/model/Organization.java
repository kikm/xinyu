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
    private String orgAddress;// 组织地址
    private String orgRemark;// 备注
    private Organization parent;// 父组织
    
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
	
	public String getOrgAddress() {
		return orgAddress;
	}
	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}
	
	public String getOrgRemark() {
		return orgRemark;
	}
	public void setOrgRemark(String orgRemark) {
		this.orgRemark = orgRemark;
	}
	
	public Organization getParent() {
		return parent;
	}
	public void setParent(Organization parent) {
		this.parent = parent;
	}
	
	/**
	 * 组织全路径
	 * @return
	 */
	public String getOrgPath() {
		String fullPath = "";
		return getFullPath(this, fullPath);
	}
	
	private String getFullPath(Organization org, String orgPath) {
		if (null != this) {
			orgPath = org.getOrgName() + "/" + orgPath;
		}
		while (null != org.getParent()) {
			return getFullPath(org.getParent(), orgPath);
		}
		return orgPath;
	}
	 
}







