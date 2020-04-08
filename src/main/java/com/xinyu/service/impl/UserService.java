package com.xinyu.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinyu.bean.AjaxMsg;
import com.xinyu.bean.Layui;
import com.xinyu.bean.PageBean;
import com.xinyu.dao.RoleDao;
import com.xinyu.dao.UserDao;
import com.xinyu.model.Role;
import com.xinyu.model.Unit;
import com.xinyu.model.User;
import com.xinyu.service.IUserService;

@Service
public class UserService implements UserDetailsService,IUserService {

	@Autowired
	private UserDao userDAO;
	
	@Autowired
	private RoleDao roleDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String openId ="";
		if(username.contains("-")) {
			openId = username.substring(username.indexOf("-")+1, username.length());
			username = username.substring(0, username.indexOf("-"));
		}
		User user = userDAO.findByUserId(username);
		if (user == null) {
			throw new UsernameNotFoundException("用户名不存在");
		}
		String pwd = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(pwd);
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		// 用于添加用户的权限
		for (Role role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(Long.toString(role.getId())));
		}
		if(StringUtils.isNoneBlank(openId)) {
			userDAO.bankUserOpenId(user.getId(),openId);
		}
		return new org.springframework.security.core.userdetails.User(user.getId(), user.getPassword(),
				authorities);
	}
	
	public AjaxMsg SysUserCheck(String username,String password) {
		AjaxMsg msg = new AjaxMsg();
		if(username == null || password == null) {
			return msg;
		}
		User user = userDAO.findByUserId(username);
		if (user == null) {
			msg.setContent("用户名不存在");
		}
		if (!user.getPassword().equals(password)) {
			msg.setContent("用户密码错误");
		}
		msg.setResult(true);
		
		return msg;
		
	}

	@Override
	public User getUserByOpenId(String openID) {
		User user = userDAO.findByOpenId(openID);
		return user;
	}

	@Override
	public void bankOpenId(String userId, String openId) {
		userDAO.bankUserOpenId(userId,openId);
	}

	@Override
	public Layui getUserList(PageBean pageBean, User d) {
		Integer count = userDAO.getUserListCount(d);
		pageBean.setCount(count);
		List<User> userList = userDAO.getUserList(pageBean,d);
		return Layui.data("成功", 0, count, userList);
	}
	
	@Override
	public User getUserById(String id) {
		User d = userDAO.findByUserId(id);
		
		return d;
	}

	@Override
	@Transactional
	public Layui deleteUser(String id) {
		if(StringUtils.isNotBlank(id)) {
			userDAO.deleteUser(id);
			userDAO.deleteUserRole(id);
 		}
		return Layui.data("删除成功", 0, 0, null);
	}

	@Override
	@Transactional
	public Layui saveOrUpdateUser(User d,String roles) {
		synchronized(d) {
        	try {
        		userDAO.saveOrUpdateUser(d);
        		userDAO.deleteUserRole(d.getId());
        		List<Long> roleIdList = new ArrayList<Long>();
     			List<String> list = Arrays.asList(roles.split(","));
        		for(String roleId : list) {
        			roleIdList.add(Long.valueOf(roleId));
        		}
        		userDAO.saveUserRole(d.getId(),roleIdList);
             } catch (Exception e) {
            	 e.printStackTrace();
            	 return Layui.data("保存错误",1, 0, null);
             }    
        	
		}
		
		return Layui.data("保存成功",0, 0, null);
	}

	@Override
	public Layui getRoleList(String name) {
		
		List<Role> roleList = roleDAO.getAllRole(name);
		return Layui.data("成功",0, 0, roleList);
	}

	@Override
	public Layui saveUnit(String unitName) {
		Integer count = userDAO.checkUnitByName(unitName);
		if(count > 0) {
			return Layui.data("单位名称已存在", 1, 0, null);
		}
		Unit u = new Unit();
		u.setName(unitName);
		userDAO.saveUnit(u);
		List<Unit> list = new ArrayList<Unit>();
		list.add(u);
		return Layui.data("保存成功", 0, 0, list);
	}

}
