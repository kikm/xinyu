package com.xinyu.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;


/**
 * 用户的角色
 * @author zhuwenxin
 *
 */
public class Role implements GrantedAuthority{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;// id主键
    private String name;// 角色名称
    private String cname;// 角色名称
    private Date updateTime;// 更新时间
    private String remark;// 描述
    private Set<User> users = new HashSet<User>();// 用户
	private Set<Menu> menus;//permissions?
    
    
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
	
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public Set<Menu> getMenus() {
		return menus;
	}

	
	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	
     
}



