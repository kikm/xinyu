package com.xinyu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xinyu.bean.PageBean;
import com.xinyu.model.Unit;
import com.xinyu.model.User;

@Mapper
public interface UserDao {
	
	public User findByUserId(String userId);

	public void updateStatusByOpenID(String openId, Integer status);
	
	public void unbankOpenID(String openId);

	public User findByOpenId(String openID);
	
	public List<User> getUserListByRole(String rolename);

	public void bankUserOpenId(String userId,String openId);

	public List<User> getUserListByUnit(Long id);

	public Integer getUserListCount(@Param("user")User user);

	public List<User> getUserList(@Param("page")PageBean pageBean, @Param("user")User d);

	public void deleteUserRole(String userId);

	public void saveOrUpdateUser(@Param("user")User user);

	public void saveUserRole(String userId, List<Long> list);
	
	public Integer checkUserIdExit(String userId);

	public void deleteUser(String userId);

	public Long saveUnit(@Param("un")Unit un);
	
	public Integer checkUnitByName(String unitName);

	public List<User> getUserByOrg(Long orgId);
	
	public List<User> getUserByEntiyOrg();

	public void updateUserOrg(Long org, List<String> list);
	
	public void clearUserOrg(Long org);
	
	public User getUserByOpenId(String openId);
}
