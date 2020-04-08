package com.xinyu.bean;

import java.util.HashMap;
import java.util.List;

public class Layui extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Layui data(String msg,int code,Integer count,List<?> data){
	    Layui r = new Layui();
	    r.put("code", code);
	    r.put("msg", msg);
	    r.put("count", count);
	    r.put("data", data);
	    return r;
	}
}
