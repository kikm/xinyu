package com.xinyu.service;

import com.xinyu.bean.Layui;
import com.xinyu.bean.PageBean;
import com.xinyu.model.User;

public interface IUserService {

	public User getUserByOpenId(String openID);

	public Layui saveOrUpdateUser(User d, String deleteRole,String oldid);

	public Layui deleteUser(String id);

	public User getUserById(String id);

	public Layui getUserList(PageBean pageBean, User d);

	public void bankOpenId(String userId, String openId);

	public Layui getRoleList(String name);

	public Layui saveUnit(String unitName);

	public Layui getUserListByOrg(String orgId);

	public Layui updateUserOrg(String orgId, String userIds);

	public Boolean checkCanRepath(String depathUserOpenId, String orderId);

}
