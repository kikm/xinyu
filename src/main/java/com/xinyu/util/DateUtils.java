package com.xinyu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DateUtils {

	public static String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String DATEFORMAT_1 = "yyyy-MM-dd";

	public static String SIPMLE_CHINESE = "yyyy年MM月dd日 EEE HH:mm:ss";

	public static String formatDate(Date date) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATEFORMAT);
		return format.format(date);
	}

	public static String formatDate(Date date, String formatString) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(date);
	}

	public static Date parse(String date) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATEFORMAT);
		return format.parse(date);
	}

	public static Date parse(String date, String dateFormat) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.parse(date);
	}

	public static String format(Date date, String dateFormat, Locale locale) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat, locale);
		return format.format(date);
	}

	public static String formatToSimpleChinese(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(SIPMLE_CHINESE, Locale.CHINESE);
		return format.format(date);
	}
	
	public static String getThisWeekDay() {
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
		// 中国习惯：周一是一周的开始
		if(dayOfWeek==1){
			dayOfWeek = 7;
		}else{
			dayOfWeek--;
		}
		Calendar cal = (Calendar) ca.clone();
		cal.add(Calendar.DATE,1-dayOfWeek);
		Date date1 = cal.getTime();
		String start = f.format(date1);
		
		return start;
	}
	
	public static String getLastWeekDay(String date1) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date date = null;
		try {
			date = f.parse(date1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTime(date);
		cal.add(Calendar.DATE, -7);
		Date d = cal.getTime();
		String start = f.format(d);
		return start;
	}

	public static void main(String[] args) {
		String s = getThisWeekDay();
    }

}
