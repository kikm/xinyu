package com.xinyu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.xinyu.bean.MenuBean;
import com.xinyu.model.Menu;

@Mapper
public interface MenuDao {
	
	public List<Map<String, Object>> getAllMenuAndRole();
	
	public List<Menu> getAllMenu();

	public List<Menu> getMenuListByUser(String userId);

}
