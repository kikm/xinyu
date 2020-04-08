package com.xinyu.model;

import java.io.Serializable;


public class RoleEntityPermission implements Serializable,Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2022441873611221907L;
	private String org1;
	private String org2;
	private String org3;
	private String org4;
	private String org5;
	private String orgCode;
	
	private String ca;
	private String subca;
	private Long roleId;
	
	private String entityId;


	

	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getWeight(){
		int i=0;
		if(org1==null){
			i+=10000000;
		}
		if(org2==null){
			i+=1000000;
		}
		if(org3==null){
			i+=100000;
		}
		if(org4==null){
			i+=10000;
		}
		if(org5==null){
			i+=1000;
		}
		if(ca==null){
			i+=100;
		}
		if(subca==null){
			i+=10;
		}
		if(entityId==null){
			i+=11;
		}
		return i;
	}

	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	

	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrg1() {
		return org1;
	}

	public void setOrg1(String org1) {
		this.org1 = org1;
	}

	public String getOrg2() {
		return org2;
	}

	public void setOrg2(String org2) {
		this.org2 = org2;
	}

	public String getOrg3() {
		return org3;
	}

	public void setOrg3(String org3) {
		this.org3 = org3;
	}

	public String getOrg4() {
		return org4;
	}

	public void setOrg4(String org4) {
		this.org4 = org4;
	}

	public String getOrg5() {
		return org5;
	}

	public void setOrg5(String org5) {
		this.org5 = org5;
	}

	public String getCa() {
		return ca;
	}

	public void setCa(String ca) {
		this.ca = ca;
	}

	public String getSubca() {
		return subca;
	}

	public void setSubca(String subca) {
		this.subca = subca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ca == null) ? 0 : ca.hashCode());
		result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
		result = prime * result + ((org1 == null) ? 0 : org1.hashCode());
		result = prime * result + ((org2 == null) ? 0 : org2.hashCode());
		result = prime * result + ((org3 == null) ? 0 : org3.hashCode());
		result = prime * result + ((org4 == null) ? 0 : org4.hashCode());
		result = prime * result + ((org5 == null) ? 0 : org5.hashCode());
		result = prime * result + ((orgCode == null) ? 0 : orgCode.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((subca == null) ? 0 : subca.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleEntityPermission other = (RoleEntityPermission) obj;
		if (ca == null) {
			if (other.ca != null)
				return false;
		} else if (!ca.equals(other.ca))
			return false;
		if (entityId == null) {
			if (other.entityId != null)
				return false;
		} else if (!entityId.equals(other.entityId))
			return false;
		if (org1 == null) {
			if (other.org1 != null)
				return false;
		} else if (!org1.equals(other.org1))
			return false;
		if (org2 == null) {
			if (other.org2 != null)
				return false;
		} else if (!org2.equals(other.org2))
			return false;
		if (org3 == null) {
			if (other.org3 != null)
				return false;
		} else if (!org3.equals(other.org3))
			return false;
		if (org4 == null) {
			if (other.org4 != null)
				return false;
		} else if (!org4.equals(other.org4))
			return false;
		if (org5 == null) {
			if (other.org5 != null)
				return false;
		} else if (!org5.equals(other.org5))
			return false;
		if (orgCode == null) {
			if (other.orgCode != null)
				return false;
		} else if (!orgCode.equals(other.orgCode))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (subca == null) {
			if (other.subca != null)
				return false;
		} else if (!subca.equals(other.subca))
			return false;
		return true;
	}
	
	
}
