package com.xinyu.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xinyu.bean.Layui;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Role;
import com.xinyu.service.IRoleService;

@Controller
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private IRoleService roleService;
	
	
	@RequestMapping("/roleList")
    public ModelAndView getOrderList() {
		ModelAndView mov = new ModelAndView();
		
		mov.setViewName("roleList");
		
        return mov;
    }
	
	@RequestMapping("/menuData")
	@ResponseBody
    public Layui getAllMenu() {
		Layui result = roleService.getMenuTree();
        return result;
    }
	
	@RequestMapping("/roleTableData")
	@ResponseBody
    public Layui getRoleData(PageBean pageBean,String roleName) {
		Role d = new Role();
		if(StringUtils.isNotEmpty(roleName)){
			d.setCname(roleName);
		}
		Layui result = roleService.getRoleList(pageBean, d);
		
        return result;
    }
	
	
	
	@RequestMapping("/updateRole")
	@ResponseBody
    public Layui saveOrUpdaterole(Role d,String roleMenu) {
		Layui result = roleService.updateRole(d, roleMenu);
		
        return result;
    }
	
	@RequestMapping("/getRoleInfo")
	@ResponseBody
    public Role getroleInfo(Long id) {
		Role result = roleService.getRoleById(id);
		
        return result;
    }
	
	

}
