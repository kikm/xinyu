package com.xinyu.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinyu.bean.Layui;
import com.xinyu.bean.MenuBean;
import com.xinyu.bean.MenuTree;
import com.xinyu.bean.PageBean;
import com.xinyu.dao.RoleDao;
import com.xinyu.model.Part;
import com.xinyu.model.Role;
import com.xinyu.model.User;
import com.xinyu.service.IMenuService;
import com.xinyu.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private IMenuService menuService;
	
	
	@Override
	public Layui getRoleList(PageBean pagebean, Role d){
		int count = roleDao.getCountRoleCriteriaQuery(d);
		pagebean.setCount(count);
		List<Role> data = roleDao.getRoleList(pagebean,d);
		for(Role r : data) {
			String name = "";
			for(User p : r.getUsers()) {
				name += p.getName()+" , ";
			}
			if(StringUtils.isNotBlank(name)) {
				name = name.substring(0, name.length()-2);
			}
			r.setRemark(name);
		}
		return Layui.data("查询成功", 0, count, data);
	}

	@Override
	public Role getRoleById(Long id) {
		Role d = roleDao.getRoleById(id);
		
		return d;
	}

	@Override
	@Transactional
	public Layui updateRole(Role d,String roleMenu) {
		synchronized(d) {
        	try {
        		roleDao.updateRole(d);
        		roleDao.deleteRoleMenu(d.getId());
        		List<Long> menuIdList = new ArrayList<Long>();
     			List<String> list = Arrays.asList(roleMenu.split(","));
        		for(String roleId : list) {
        			menuIdList.add(Long.valueOf(roleId));
        		}
        		roleDao.saveRoleMenu(d.getId(),menuIdList);
             } catch (Exception e) {
            	 e.printStackTrace();
            	 return Layui.data("保存角色错误",1, 0, null);
             }    
        	
		}
		
		return Layui.data("保存成功",0, 0, null);
	}

	@Override
	public Layui getMenuTree() {
		List<MenuTree> data = new ArrayList<MenuTree>();
		List<MenuBean> menuList = menuService.getMenuListByUser(null);
		for(MenuBean bean : menuList) {
			MenuTree mt = new MenuTree();
			mt.setTitle(bean.getName());
			mt.setId(String.valueOf(bean.getId()));
			mt.setChecked(false);
			mt.setSpread(false);
			List<MenuTree> children = new ArrayList<MenuTree>();
			for(MenuBean subbean : bean.getSubMenuList()) {
				MenuTree submt = new MenuTree();
				submt.setTitle(subbean.getName());
				submt.setId(String.valueOf(subbean.getId()));
				submt.setChecked(false);
				submt.setSpread(false);
				children.add(submt);
			}
			mt.setChildren(children);
			data.add(mt);
		}
		return Layui.data("成功", 0, 0, data);
	}
}
