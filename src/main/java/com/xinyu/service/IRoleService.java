package com.xinyu.service;

import com.xinyu.bean.Layui;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Role;

public interface IRoleService {

	public Role getRoleById(Long id);

	public Layui updateRole(Role d, String userRole);

	public Layui getRoleList(PageBean pageBean, Role role);

	public Layui getMenuTree();
}
