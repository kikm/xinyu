package com.xinyu.security;

import java.util.Set;

import org.springframework.security.access.ConfigAttribute;

/**
 *
 * @author xujianhui
 *
 */
public class MenuOperConfigAttribute implements ConfigAttribute{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2803059285440647550L;
	private String role;
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Deprecated
	public Set<String> getOperSet() {
		return operSet;
	}

	@Deprecated
	public void setOperSet(Set<String> operSet) {
		this.operSet = operSet;
	}

	private Set<String> operSet;

	@Override
	public String getAttribute() {
		return null;
	}

	public MenuOperConfigAttribute(String role,Set<String> operationSet) {
		this.role=role;
		//this.operSet=operationSet;
	}
	
	
	


}
