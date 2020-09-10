package com.xinyu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyu.bean.OrgTree;
import com.xinyu.dao.OrganizationDao;
import com.xinyu.model.Organization;
import com.xinyu.service.IOrgService;

@Service
public class OrgServiceImpl implements IOrgService {

	@Autowired
	private OrganizationDao orgDao;
	
	@Override
	public OrgTree getOrgTree(){
		
		List<Organization> orgList = orgDao.getAllOrg();
		List<OrgTree> treeList = new ArrayList<OrgTree>();
		OrgTree rootNote = new OrgTree();
		for(Organization o : orgList) {
			OrgTree note = new OrgTree();
			note.setId(o.getId());
			note.setName(o.getOrgName());
			note.setChildrens(new ArrayList<OrgTree>());
			if(o.getPid() != null) {
				note.setPid(o.getPid());
				treeList.add(note);
			}else {
				rootNote = note;
			}
		}
		OrgTree tree = handleTree(treeList,rootNote);
		
		return tree;
	}
	
	private OrgTree handleTree(List<OrgTree> orgList,OrgTree parent) {
		for(OrgTree tree : orgList) {
			if(tree.getPid() == parent.getId()) {
				parent.getChildrens().add(tree);
				handleTree(orgList,tree);
			}
		}
		
		return parent;
		
	}

	@Override
	public List<Organization> getChildrens(Long pid) {
		List<Organization> orgList = orgDao.getOrgChildrens(pid);
		
		return orgList;
	}


	
}
