package com.xinyu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xinyu.bean.Layui;
import com.xinyu.bean.OrgTree;
import com.xinyu.service.IOrgService;
import com.xinyu.service.IUserService;

@Controller
@RequestMapping("/org")
public class OrgController {
	
	@Autowired
	private IOrgService orgService;
	@Autowired
	private IUserService userService;
	
	
	@RequestMapping("/orgList")
    public ModelAndView getOrderList() {
		ModelAndView mov = new ModelAndView();
		
		mov.setViewName("jOrgChart");
		
        return mov;
    }
	
	@RequestMapping("/orgTree")
	@ResponseBody
    public List<OrgTree> getOrgTree() {
		OrgTree t = orgService.getOrgTree();
		List<OrgTree> tree = new ArrayList<OrgTree>();
		tree.add(t);
        return tree;
    }
	
	@RequestMapping("/orgUser")
	@ResponseBody
    public Layui getOrgUser(String orgId) {
		Layui result = userService.getUserListByOrg(orgId);
        return result;
    }
	
	@RequestMapping("/updateUserOrg")
	@ResponseBody
    public Layui updateUserOrg(String orgId,String userIds) {
		Layui result = userService.updateUserOrg(orgId,userIds);
        return result;
    }
	
	
	

}
