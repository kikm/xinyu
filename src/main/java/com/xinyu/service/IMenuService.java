package com.xinyu.service;

import java.util.List;
import java.util.Map;

import com.xinyu.bean.MenuBean;
import com.xinyu.model.Menu;

public interface IMenuService {

	public List<Menu> getAllMenu();

	public List<Map<String, Object>> getAllMenuAndRole();

	public List<MenuBean> getMenuListByUser(String userId);

}
