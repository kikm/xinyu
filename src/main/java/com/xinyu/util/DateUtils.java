package com.xinyu.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	 
	public static String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String DATEFORMAT_1 = "yyyy-MM-dd";
	
	public static String SIPMLE_CHINESE = "yyyy年MM月dd日 EEE HH:mm:ss";
	
	public static String formatDate(Date date) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATEFORMAT);
		return format.format(date);
	}
	
	public static String formatDate(Date date,String formatString) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(date);
	}
	
	public static Date parse(String date) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATEFORMAT);
		return format.parse(date);
	}
	
	public static Date parse(String date,String dateFormat) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.parse(date);
	}
	
	public static String format(Date date,String dateFormat ,Locale locale){
		SimpleDateFormat format = new SimpleDateFormat(dateFormat,locale);
		return format.format(date);
	}
	
	public static String formatToSimpleChinese(Date date){
		SimpleDateFormat format = new SimpleDateFormat(SIPMLE_CHINESE,Locale.CHINESE);
		return format.format(date);
	}

}
