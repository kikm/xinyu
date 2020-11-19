package com.xinyu.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.xinyu.Constance;
import com.xinyu.bean.City;
import com.xinyu.bean.Layui;
import com.xinyu.bean.MenuBean;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Role;
import com.xinyu.model.Unit;
import com.xinyu.model.User;
import com.xinyu.service.IMenuService;
import com.xinyu.service.IOrderService;
import com.xinyu.service.IUserService;
import com.xinyu.util.WeiXinUtil;

import net.sf.json.JSONObject;

@Controller
//@RequestMapping("/user")
public class UserController {
	
	
	@Autowired
	private IMenuService menuService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IOrderService orderService;
	
	@RequestMapping("/login")
    public String loginAuthentication() {
		
		return "login";
	}
	
	@RequestMapping("/echarts")
    public String echarts() {
		
		return "echarts";
	}
	
	@RequestMapping("/main")
    public ModelAndView toIndex(@SessionAttribute(Constance.USER_ID)String userId) {
		ModelAndView mav=new ModelAndView("main");

		List<MenuBean> list = menuService.getMenuListByUser(userId);//菜单
		//mav.addObject("newsList", newslist);	
		mav.addObject("menuList", list);
		mav.addObject("userId", userId);
		return mav;	
    }
	
	@RequestMapping(value = "/mobileCusLogin", method = RequestMethod.GET)
    public ModelAndView mobileCusLoginAuthentication(String code,String openId) {
		ModelAndView mov = new ModelAndView("/mobile/bandCount");//未绑定账号\客户页面
		JSONObject jsonOpenID = null; 
		if(code != null){
			jsonOpenID = WeiXinUtil.getOpenID(code); 
			openId =  (String)jsonOpenID.get("openid");//"oCnlEuFjrHbyecP-JwXMeT0Jcoh8";
		}
		User userOri = userService.getUserByOpenId(openId); //
		//WeiXinUtil.getUserInfo(openID); 
		if(userOri != null) { 
			mov = new ModelAndView("/mobile/orderList");//已绑定账号 
		} 
		mov.addObject("user",userOri == null?new User():userOri); 
		mov.addObject("openId", openId); 
		mov.addObject("type", "cus");
//		mov = new ModelAndView("/mobile/orderList");//已绑定账号
//		mov.addObject("type","cus"); 
//		mov.addObject("openId", "onIRYuB19EGw1E9ojhwSJZe6Wxuo");//oCnlEuB6eJ9dhV-ubMV1uP2_r7iY
		
		return mov;
	}
	
	@RequestMapping(value = "/mobileTenLogin", method = RequestMethod.GET)
    public ModelAndView mobileTenLoginAuthentication(String code,String openId) {
		ModelAndView mov = new ModelAndView("/mobile/bandCount"); //未绑定账号\客户页面
		JSONObject jsonOpenID = null; 
		if(StringUtils.isNotBlank(code)){
			jsonOpenID = WeiXinUtil.getOpenID(code); 
			openId =  (String)jsonOpenID.get("openid");//"oCnlEuFjrHbyecP-JwXMeT0Jcoh8";
		}
		User userOri = userService.getUserByOpenId(openId); //
		if(userOri != null) { 
			Boolean canView = false;
			for(Role r : userOri.getRoles()) {
				if(r.getName().equals("customerService")||r.getName().equals("technician")||r.getName().equals("admin"))
					canView = true;//账号有客服角色，展示所有
			}
			if(canView) {
				mov = new ModelAndView("/mobile/orderList");//已绑定账号 
			}else {
				mov = new ModelAndView("/mobile/error");
				mov.addObject("msg", "没有操作权限，请联系管理员"); 
			}
		} 
		mov.addObject("type", "ten");
		mov.addObject("user",userOri == null?new User():userOri); 
		mov.addObject("openId", openId);
		
//		mov = new ModelAndView("/mobile/orderList");//已绑定账号
//		mov.addObject("type","ten"); 
//		mov.addObject("openId", "onIRYuB19EGw1E9ojhwSJZe6Wxuo");
		 

		return mov;
	}
	
	
	
	@RequestMapping("/userList")
    public ModelAndView getUserList() {
		ModelAndView mov = new ModelAndView("userList");
		List<Unit> unitList = orderService.getUnit();
		mov.addObject("cityList", City.values());
		mov.addObject("unitList", unitList);
        return mov;
    }
	
	@RequestMapping("/userTableData")
	@ResponseBody
    public Layui getUserData(PageBean pageBean,String userName) {
		User d = new User();
		if(StringUtils.isNotEmpty(userName)){
			d.setName(userName);
		}
		Layui result = userService.getUserList(pageBean, d);
		
        return result;
    }
	
	@RequestMapping("/saveOrUpdateUser")
	@ResponseBody
    public Layui saveOrUpdateUser(User d,String addRoles,String oldid) {
		Layui result = userService.saveOrUpdateUser(d, addRoles,oldid);
		
        return result;
    }
	
	@RequestMapping("/roleData")
	@ResponseBody
    public Layui getRoleData(String name) {
		Layui result = userService.getRoleList(name);
		
        return result;
    }
	
	@RequestMapping("/deleteUser")
	@ResponseBody
    public Layui deleteUser(String userId) { 
		Layui result = userService.deleteUser(userId);
		
        return result;
    }
	
	@RequestMapping("/getUserInfo")
	@ResponseBody
    public User getUserInfo(String userId) { 
		User u = userService.getUserById(userId);
		
        return u;
    }
	
	@RequestMapping("/saveUnit")
	@ResponseBody
    public Layui saveUnit(String unitName) { 
		Layui result = userService.saveUnit(unitName);
		
        return result;
    }
	

}
