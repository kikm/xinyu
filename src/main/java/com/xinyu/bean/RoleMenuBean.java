package com.xinyu.bean;

import java.util.HashSet;
import java.util.Set;

public class RoleMenuBean {
	private String roleId;
	private String menuId;
	private Set<String> operSet=new HashSet<String>();
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public Set<String> getOperSet() {
		return operSet;
	}
	public void setOperSet(Set<String> operSet) {
		this.operSet = operSet;
	}
	public String getOper(){
		StringBuilder bder=new StringBuilder();
		for (String oper : operSet) {
			bder.append(oper).append(";");
		}
		return bder.toString();
		
	}

}
