package com.xinyu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xinyu.model.Organization;

@Mapper 
public interface OrganizationDao {

	public List<Organization> getAllOrg();
	
	public List<Organization> getOrgChildrens(Long pid);
}
