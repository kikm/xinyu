package com.xinyu.service;

import com.xinyu.bean.Layui;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Unit;
import com.xinyu.model.User;

public interface IUserService {

	public User getUserByOpenId(String openID);

	public Layui saveOrUpdateUser(User d, String deleteRole);

	public Layui deleteUser(String id);

	public User getUserById(String id);

	public Layui getUserList(PageBean pageBean, User d);

	public void bankOpenId(String userId, String openId);

	public Layui getRoleList(String name);

	public Layui saveUnit(String unitName);

}
