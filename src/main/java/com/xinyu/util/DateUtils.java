package com.xinyu.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;

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
	
    public static void main(String[] args) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr("19586750", "HtGDo6ZG3OWYDjoZ5w9odhDw", "83UVGfq5bWwUrlXG9H7VM3HGsOTpzMaK");

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        String path = "E:/T36aa72ea3ab01df9a54212f1d7386c9.jpg";
        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        JSONArray words_result = (JSONArray)res.get("words_result");
        for(int i=0;i<words_result.length();i++) {
            System.out.println(words_result.getJSONObject(i).get("words"));
        }
    }

}
