package com.xinyu.bean;

import java.util.ArrayList;
import java.util.List;

import com.xinyu.model.Menu;

public class MenuBean extends Menu {
	
	private MenuBean parentMenu;
	private List<MenuBean> subMenuList=new ArrayList<MenuBean>();
	
	private String localeName;//用于国际化展示
	
	public MenuBean getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(MenuBean parentMenu) {
		this.parentMenu = parentMenu;
	}
	public List<MenuBean> getSubMenuList() {
		return subMenuList;
	}
	public void setSubMenuList(List<MenuBean> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public void addChildMenu(MenuBean menuBean){
		this.subMenuList.add(menuBean);
	}
	public String getLocaleName() {
		return localeName;
	}
	public void setLocaleName(String localeName) {
		this.localeName = localeName;
	}

}
