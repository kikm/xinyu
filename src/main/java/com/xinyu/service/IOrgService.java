package com.xinyu.service;


import java.util.List;

import com.xinyu.bean.OrgTree;
import com.xinyu.model.Organization;

public interface IOrgService {
	
	public OrgTree getOrgTree();
	
	public List<Organization> getChildrens(Long pid);

}
