package com.xinyu.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyu.bean.MenuBean;
import com.xinyu.dao.MenuDao;
import com.xinyu.model.Menu;
import com.xinyu.service.IMenuService;

@Service
public class MenuServiceImpl implements IMenuService {

	@Autowired
	private MenuDao menuDao;
	
	@Override
	public List<Menu> getAllMenu() {
		return menuDao.getAllMenu();
	}

	@Override
	public List<Map<String, Object>> getAllMenuAndRole() {
		
		return menuDao.getAllMenuAndRole();
	}

	@Override
	public List<MenuBean> getMenuListByUser(String userId) {
		List<Menu> list = menuDao.getMenuListByUser(userId);
		MenuBean menu = null;
		MenuBean preMenu = null;
		Map<Long,MenuBean> map = new LinkedHashMap<Long,MenuBean>();
		List<MenuBean> resultList = new ArrayList<MenuBean>();
		//为了避免SQL语句的排序影响父子节点的组装，先转换为MenuBean放到Map中，后面再处理父子关系
		menuListToMenuBeanList(list,map);
		
		for (Entry<Long,MenuBean> entry : map.entrySet()) {
			menu=entry.getValue();
			if((menu.getPid()==null)){
				resultList.add(menu);
			}
			if (menu.getPid()!=null) {
				preMenu = map.get(menu.getPid());
				if (preMenu!=null) {
					preMenu.addChildMenu(menu);
				}
			}
		}
		return resultList;
	}
	
	private List<MenuBean> menuListToMenuBeanList(List<Menu> list, Map<Long, MenuBean> map){
		MenuBean menu = null;
		List<MenuBean> result = new ArrayList<MenuBean>();
		for(Menu object : list){
			menu = new MenuBean();
			menu.setId(object.getId());
			menu.setName(object.getName());
			menu.setPid(object.getPid()==null?null:object.getPid());
			menu.setUrl(object.getUrl()==null ? "" : object.getUrl());
			menu.setMenuType(object.getMenuType());
			menu.setMenuTypeInfo(object.getMenuTypeInfo());
			result.add(menu);
			map.put(menu.getId(), menu);
		}
		
		
		return result;
		
	}

}
