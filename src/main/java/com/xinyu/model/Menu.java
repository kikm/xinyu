package com.xinyu.model;

import java.io.Serializable;
import java.util.Set;

public class Menu implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String url;
	private Integer menuLevel;
	private Long pid;
	private Boolean enabled;


	private Integer orderNum;
	private String menuType;
	private String menuTypeInfo;
	


	private Set<Role> roles;
	

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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	
//	@Transient
//	@Deprecated
//	public String getOperDefine() {
//		return operDefine;
//	}
//	@Deprecated
//	public void setOperDefine(String operDefine) {
//		this.operDefine = operDefine;
//	}
	
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	
	public String getMenuTypeInfo() {
		return menuTypeInfo;
	}
	public void setMenuTypeInfo(String menuTypeInfo) {
		this.menuTypeInfo = menuTypeInfo;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}
