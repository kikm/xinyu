package com.xinyu;

import java.util.HashMap;
import java.util.Map;

/**
 * 静态数据定义
 * 
 * @author weilian.luo
 */
public class Constance {

	public static final String USER_ID="userId"; 
	public static final String USER_NAME="username";
	public static final String LOGIN_PAGE="/login.jsp";
	public static final String REDIRECT_INDEX_PAGE="redirect:index";
	public static final String USER="loginUser";
	
	public static final String BAIDUOCR_APP_ID = "19586750";
    public static final String BAIDUOCR_API_KEY = "HtGDo6ZG3OWYDjoZ5w9odhDw";
    public static final String BAIDUOCR_SECRET_KEY = "83UVGfq5bWwUrlXG9H7VM3HGsOTpzMaK";
	
	public static final String EFFECT_Config="effectConfig";
	
	public static final String LOCALE = "locale";//国家化 语言环境 中文：zh_CN 英文：en_US
	public static final String USER_DEFAULT_AUTHORITY = "CustomerService";

	public static Map<String, String> eventTypeMap = new HashMap<String, String>();

	static {
		
	}
		
}
