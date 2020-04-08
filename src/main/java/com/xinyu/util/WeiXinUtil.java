package com.xinyu.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.xinyu.bean.AccessToken;
import com.xinyu.bean.OrderStatus;
import com.xinyu.bean.wxmsg.menubutton.Button;
import com.xinyu.bean.wxmsg.menubutton.CommonButton;
import com.xinyu.bean.wxmsg.menubutton.ComplexButton;
import com.xinyu.bean.wxmsg.menubutton.Menu;
import com.xinyu.bean.wxmsg.menubutton.ViewButton;
import com.xinyu.bean.wxmsg.req.TextMessage;
import com.xinyu.bean.wxmsg.resp.Article;
import com.xinyu.bean.wxmsg.resp.MusicMessage;
import com.xinyu.bean.wxmsg.resp.NewsMessage;
import com.xinyu.model.User;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class WeiXinUtil
{
	// 获取access_token的接口地址（GET） 限200（次/天）  
		public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";  
		// 菜单创建（POST） 限100（次/天）  
		public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
		
		public static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
		
		public static String oauth_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=LAST_CODE&grant_type=authorization_code";
		
		public static String userinfo_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
		
		public static String getUserList_url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
		
		public static String setIndustry_url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
		
		public static String getTemplateId_url = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
		
		public static String sendTemplateMsg_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
		
		public final static String appid = "wx8a556ef7c6e66c7e";//"wxaa2c5483f0eeb6ff";
		
		public final static String AppSecret = "c3f81e702075355137b68fa15cef4128";//"b18343831c4f4f892ec4d03b2b23572c";
		
		public final static String encodingAESKey = "F9f8V2GP5ZnOHjLGQUJiFgMvgUdYezrk1vGcNXAP7Kn";
		
		public final static String templateId_depath = "SiqAzxib9MSyomsOac-_N2Fzlhf9kRbI_8cJlzBv2kg";
		
		public final static String templateId_feedback = "2oFVIB-ZhDf5XOLyUe92y9ilAXI3AfUJUiU0rg0NS4o";
		
		public final static String templateId_confirm = "KX13m9veQYyxL6SUV5gPd7BOgb7kMLyXYcfr9NPEVlk";
		
		public final static String templateId_complete = "mU4Hjuhz1fyl-DU6TEZhQ1bd7rPoUluRs0KDYQ863XI";
		
		public final static String templateId_price = "zdUWjbrxkshwA7W3aLNSiK2EiVPo781eQUSqbK833z0";
		
		public final static AccessToken accessToken = new AccessToken();
		/** 
		 * 获取access_token 
		 *  
		 * @param appid 凭证 
		 * @return 
		 */  
		public static AccessToken getAccessToken() {  
			if(StringUtils.isBlank(accessToken.getToken())||accessToken.getExpireDate().getTime() > new Date().getTime()) {
				String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", AppSecret);  
				JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
				// 如果请求成功  
				if (null != jsonObject) {  
					try {  
						accessToken.setToken(jsonObject.getString("access_token"));  
						accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
						accessToken.setExpireDate(new Date(System.currentTimeMillis()+accessToken.getExpiresIn()));
					} catch (JSONException e) {  
						// 获取token失败  
						e.printStackTrace();  
					}  
				}  
			}
		    return accessToken;  
		}
		
		/** 
		 * 获取关注的用户列表
		 *  
		 * @param 
		 * @return 
		 */  
		public static AccessToken getUserList() {  
			WeiXinUtil.getAccessToken();
		    String requestUrl = getUserList_url.replace("ACCESS_TOKEN", accessToken.getToken());  
		    JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
		    // 如果请求成功  
		    if (null != jsonObject) {  
		        try {
		        	System.out.println(jsonObject.toString());
		        } catch (JSONException e) {  
		            // 获取token失败  
		            e.printStackTrace();  
		        }  
		    }  
		    return accessToken;  
		}
		
		/** 
		 * 删除菜单 
		 *  
		 * @param menu 菜单实例 
		 * @param accessToken 有效的access_token 
		 * @return 0表示成功，其他值表示失败 
		 */ 
		public static int deleteMenu() {  
			int result = 0;  
			getAccessToken();
		    // 拼装创建菜单的url  
		    String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken.getToken());  
		    JSONObject jsonObject = httpRequest(url, "POST", null);  
		  
		    if (null != jsonObject) {  
		        if (0 == jsonObject.getInt("errcode")) {  
		            result = jsonObject.getInt("errcode"); 
		            System.out.println("删除菜单成功");
		        }  
		    }  
		  
		    return result;  
		}
		/** 
		 * 创建菜单 
		 *  
		 * @param menu 菜单实例 
		 * @param accessToken 有效的access_token 
		 * @return 0表示成功，其他值表示失败 
		 */  
		
		 public static int createMenu(Menu menu) { 
			 int result = 0;
			 WeiXinUtil.getAccessToken();
			 String url = menu_create_url.replace("ACCESS_TOKEN",accessToken.getToken()); // 将菜单对象转换成json字符串 
			 String jsonMenu = JSONObject.fromObject(menu).toString(); // 调用接口创建菜单 
			 JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);
		  
			 if (null != jsonObject) { if (0 != jsonObject.getInt("errcode")) { 
				 result = jsonObject.getInt("errcode"); 
				 System.out.println("创建菜单失败"); } }
		 
			 return result; 
		 
		 }
		 
		
		public static JSONObject getOpenID(String code){
			String url = oauth_url.replace("LAST_CODE", code).replace("APPSECRET", AppSecret).replace("APPID", appid);
			JSONObject jsonObject = httpRequest(url, "GET", null);
			System.out.println(jsonObject.toString());
			return jsonObject;
			
		}
		
		public static User getUserInfo(String openID){
			WeiXinUtil.getAccessToken();
			String url = userinfo_url.replace("ACCESS_TOKEN", accessToken.getToken()).replace("OPENID", openID);
			JSONObject jsonObject = httpRequest(url, "GET", null);
			String nickname = (String)jsonObject.get("nickname");
			String language = (String)jsonObject.get("language");
			String country = (String)jsonObject.get("province");
			String province = (String)jsonObject.get("province");
			Integer sex = (Integer)jsonObject.get("sex");
			String city = (String)jsonObject.get("city");
			String headimgurl = (String)jsonObject.get("headimgurl");
			User user = new User();
			user.setOpenID(openID);
			user.setStatus(0);
			if(nickname != null){
				user.setNickName(nickname);
			}
			if(headimgurl != null){
				user.setHeadImgUrl(headimgurl);
			}
			if(city != null){
				user.setCity(city);
			}
			if(country != null){
				user.setCountry(country);
			}
			if(language != null){
				user.setLanguage(language);
			}
			if(province != null){
				user.setProvince(province);
			}
			if(sex != null){
				user.setSex(sex);
			}
			return user;
			
		}
		
		/** 
	     * 发起https请求并获取结果 
	     *  
	     * @param requestUrl 请求地址 
	     * @param requestMethod 请求方式（GET、POST） 
	     * @param outputStr 提交的数据 
	     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
	     */  
	    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {  
	        JSONObject jsonObject = null; 
	        StringBuffer buffer = new StringBuffer();  
	        try {  
	            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
	            TrustManager[] tm = { new MyX509TrustManager() };  
	            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
	            sslContext.init(null, tm, new java.security.SecureRandom());  
	            // 从上述SSLContext对象中得到SSLSocketFactory对象  
	            SSLSocketFactory ssf = sslContext.getSocketFactory();  
	  
	            URL url = new URL(requestUrl);  
	            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
	            httpUrlConn.setSSLSocketFactory(ssf);  
	  
	            httpUrlConn.setDoOutput(true);  
	            httpUrlConn.setDoInput(true);  
	            httpUrlConn.setUseCaches(false);  
	            // 设置请求方式（GET/POST）  
	            httpUrlConn.setRequestMethod(requestMethod);  
	  
	            if ("GET".equalsIgnoreCase(requestMethod))  
	                httpUrlConn.connect();  
	  
	            // 当有数据需要提交时  
	            if (null != outputStr) {  
	                OutputStream outputStream = httpUrlConn.getOutputStream();  
	                // 注意编码格式，防止中文乱码  
	                outputStream.write(outputStr.getBytes("UTF-8"));  
	                outputStream.close();  
	            }  
	  
	            // 将返回的输入流转换成字符串  
	            InputStream inputStream = httpUrlConn.getInputStream();  
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
	  
	            String str = null;  
	            while ((str = bufferedReader.readLine()) != null) {  
	                buffer.append(str);  
	            }  
	            bufferedReader.close();  
	            inputStreamReader.close();  
	            // 释放资源  
	            inputStream.close();  
	            inputStream = null;  
	            httpUrlConn.disconnect();  
	            jsonObject = JSONObject.fromObject(buffer.toString());  
	        } catch (ConnectException ce) {  
	            System.out.println("Weixin server connection timed out.");  
	        } catch (Exception e) {  
	             e.printStackTrace();  
	        }  
	        return jsonObject;  
	    } 
	    
	    private static String token = "xinyu";
		/*
		 * 验证签名
		 */
		public static boolean checkSignature(String signature, String timestamp, String nonce){
			String[] arr = new String[]{token, timestamp, nonce};
			System.out.println(token);
			Arrays.sort(arr);
			StringBuilder content = new StringBuilder();
	        for (int i = 0; i < arr.length; i++) {  
	            content.append(arr[i]);  
	        }  
	        MessageDigest md = null;  
	        String tmpStr = null;  
	  
	        try {  
	            md = MessageDigest.getInstance("SHA-1");  
	            // 将三个参数字符串拼接成一个字符串进行sha1加密  
	            byte[] digest = md.digest(content.toString().getBytes());  
	            tmpStr = byteToStr(digest);  
	        } catch (NoSuchAlgorithmException e) {  
	            e.printStackTrace();  
	        }  
	  
	        content = null;  
	        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信  
	        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;  
	    }  
	  
	    /** 
	     * 将字节数组转换为十六进制字符串 
	     *  
	     * @param byteArray 
	     * @return 
	     */  
	    private static String byteToStr(byte[] byteArray) {  
	        String strDigest = "";  
	        for (int i = 0; i < byteArray.length; i++) {  
	            strDigest += byteToHexStr(byteArray[i]);  
	        }  
	        return strDigest;  
	    }  
	  
	    /** 
	     * 将字节转换为十六进制字符串 
	     *  
	     * @param mByte 
	     * @return 
	     */  
	    private static String byteToHexStr(byte mByte) {  
	        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
	        char[] tempArr = new char[2];  
	        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];  
	        tempArr[1] = Digit[mByte & 0X0F];  
	  
	        String s = new String(tempArr);  
	        return s;  
	    }
	    
	    /** 
	     * 返回消息类型：文本 
	     */  
	    public static final String RESP_MESSAGE_TYPE_TEXT = "text";  
	  
	    /** 
	     * 返回消息类型：音乐 
	     */  
	    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";  
	  
	    /** 
	     * 返回消息类型：图文 
	     */  
	    public static final String RESP_MESSAGE_TYPE_NEWS = "news";  
	  
	    /** 
	     * 请求消息类型：文本 
	     */  
	    public static final String REQ_MESSAGE_TYPE_TEXT = "text";  
	  
	    /** 
	     * 请求消息类型：图片 
	     */  
	    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";  
	  
	    /** 
	     * 请求消息类型：链接 
	     */  
	    public static final String REQ_MESSAGE_TYPE_LINK = "link";  
	  
	    /** 
	     * 请求消息类型：地理位置 
	     */  
	    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";  
	  
	    /** 
	     * 请求消息类型：音频 
	     */  
	    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";  
	  
	    /** 
	     * 请求消息类型：推送 
	     */  
	    public static final String REQ_MESSAGE_TYPE_EVENT = "event";  
	  
	    /** 
	     * 事件类型：subscribe(订阅) 
	     */  
	    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";  
	  
	    /** 
	     * 事件类型：unsubscribe(取消订阅) 
	     */  
	    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";  
	  
	    /** 
	     * 事件类型：CLICK(自定义菜单点击事件) 
	     */  
	    public static final String EVENT_TYPE_CLICK = "CLICK";  
		/** 
		 * 解析微信发来的请求（XML） 
		 *  
		 * @param request 
		 * @return 
		 * @throws Exception 
		 */  
		@SuppressWarnings("unchecked")  
		public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {  
		    // 将解析结果存储在HashMap中  
		    Map<String, String> map = new HashMap<String, String>();  
		  
		    // 从request中取得输入流  
		    InputStream inputStream = request.getInputStream();  
		    // 读取输入流  
		    SAXReader reader = new SAXReader();  
		    Document document = reader.read(inputStream);  
		    // 得到xml根元素  
		    Element root = document.getRootElement();  
		    // 得到根元素的所有子节点  
		    List<Element> elementList = root.elements();  
		  
		    // 遍历所有子节点  
		    for (Element e : elementList)  
		        map.put(e.getName(), e.getText());  
		  
		    // 释放资源  
		    inputStream.close();  
		    inputStream = null;  
		  
		    return map; 
		}
		
		/** 
		 * 文本消息对象转换成xml 
		 *  
		 * @param textMessage 文本消息对象 
		 * @return xml 
		 */  
		public static String textMessageToXml(TextMessage textMessage) {  
		    xstream.alias("xml", textMessage.getClass());  
		    return xstream.toXML(textMessage);  
		}  
		  
		/** 
		 * 音乐消息对象转换成xml 
		 *  
		 * @param musicMessage 音乐消息对象 
		 * @return xml 
		 */  
		public static String musicMessageToXml(MusicMessage musicMessage) {  
		    xstream.alias("xml", musicMessage.getClass());  
		    return xstream.toXML(musicMessage);  
		}  
		  
		/** 
		 * 图文消息对象转换成xml 
		 *  
		 * @param newsMessage 图文消息对象 
		 * @return xml 
		 */  
		public static String newsMessageToXml(NewsMessage newsMessage) {  
		    xstream.alias("xml", newsMessage.getClass());  
		    xstream.alias("item", new Article().getClass());  
		    return xstream.toXML(newsMessage);  
		}  
		
		/** 
		 * xiaoqrobot的主菜单 
		 *  
		 * @return 
		 */  
		public static String getMainMenu() {  
		    StringBuffer buffer = new StringBuffer();  
		    buffer.append("服务器繁忙，休息一下啊啊啊啊啊~");  
		    // buffer.append("1 ").append("\n");  
		    // buffer.append("回复“?”显示此帮助菜单");  
		    return buffer.toString();  
		}  
		  
		/** 
		 * 扩展xstream，使其支持CDATA块 
		 *  
		 * @date 2013-05-19 
		 */  
		private static XStream xstream = new XStream(new XppDriver() {  
		    public HierarchicalStreamWriter createWriter(Writer out) {  
		        return new PrettyPrintWriter(out) {  
		            // 对所有xml节点的转换都增加CDATA标记  
		            boolean cdata = true;  
		  
		            @SuppressWarnings("unchecked")  
		            public void startNode(String name, Class clazz) {  
		                super.startNode(name, clazz);  
		            }  
		  
		            protected void writeText(QuickWriter writer, String text) {  
		                if (cdata) {  
		                    writer.write("<![CDATA[");  
		                    writer.write(text);  
		                    writer.write("]]>");  
		                } else {  
		                    writer.write(text);  
		                }  
		            }  
		        };  
		    }  
		});
		
		/** 
	     * 组装菜单数据 
	     *  
	     * @return 
	     */  
	    public static Menu getMenu() {  
	  
	        CommonButton btn11 = new CommonButton();  
	        btn11.setName("维修电脑");  
	        btn11.setType("click");  
	        btn11.setKey("11");  
	  
	        CommonButton btn12 = new CommonButton();  
	        btn12.setName("维护网络");  
	        btn12.setType("click");  
	        btn12.setKey("12");  
	  
	        ViewButton btn13 = new ViewButton();  
	        btn13.setName("技术员入口");  
	        btn13.setType("view");  
	        btn13.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8a556ef7c6e66c7e&"
	    			+ "redirect_uri=http%3a%2f%2fwww.xywxfw.com%2fxinyu%2fmobileTenLogin&response_type=code&scope=snsapi_base&state=1#wechat_redirect"); 
	    
	        ViewButton btn21 = new ViewButton();  
	        btn21.setName("历史订单 ");  
	        btn21.setType("view");  
	        btn21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8a556ef7c6e66c7e&"
	    			+ "redirect_uri=http%3a%2f%2fwww.xywxfw.com%2fxinyu%2fmobileCusLogin&response_type=code&scope=snsapi_base&state=1#wechat_redirect"); 
	  
	        CommonButton btn22 = new CommonButton();  
	        btn22.setName("联系方式");  
	        btn22.setType("click");  
	        btn22.setKey("22");  
	        
	        ComplexButton mainBtn1 = new ComplexButton();  
	        mainBtn1.setName("电脑维护");  
	        mainBtn1.setSub_button(new Button[] {btn11,btn12,btn13});
	        
	        ComplexButton mainBtn2 = new ComplexButton();  
	        mainBtn2.setName("会员服务");  
	        mainBtn2.setSub_button(new Button[] {btn21,btn22});  
	  
	        CommonButton mainBtn3 = new CommonButton();  
	        mainBtn3.setName("商城"); 
	        mainBtn3.setType("click");
	        mainBtn3.setKey("31");
	        Menu menu = new Menu();  
	        menu.setButton(new Button[] {mainBtn1, mainBtn2,mainBtn3});  
	  
	        return menu;  
	    }  
	    
	    public static void setIndustry() {
	    	WeiXinUtil.getAccessToken();
	    	Map<String,String> map = new HashMap<String,String>();
	    	map.put("industry_id1", "2");
	    	map.put("industry_id2", "3");
	    	String jsonMenu = JSONObject.fromObject(map).toString();
	    	String url = setIndustry_url.replace("ACCESS_TOKEN", accessToken.getToken());
			JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);
			System.out.println(jsonObject.toString());
	    }
	    
	    public static String getTemplate() {
	    	WeiXinUtil.getAccessToken();
			String url = getTemplateId_url.replace("ACCESS_TOKEN", accessToken.getToken());
			JSONObject jsonObject = httpRequest(url, "GET", null);
			System.out.println(jsonObject.toString());
			return null;
	    }
	    
	    public static Boolean sendTemplate(String openId,String tourl,Map<String,String> msgdata,OrderStatus type,Boolean isUrgent) {
	    	WeiXinUtil.getAccessToken();
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("touser", openId);   // openid
	    	String color = isUrgent?"#EC0000":"#173177";
	    	JSONObject data = new JSONObject();
	    	JSONObject first = new JSONObject();
	    	first.put("value", msgdata.get("orderNo"));
	    	first.put("color", color);
	    	data.put("orderNo",first);
	    	String template_id = "";
	    	JSONObject keyword1 = new JSONObject();
	    	JSONObject keyword2 = new JSONObject();
	    	JSONObject keyword3 = new JSONObject();
	    	JSONObject keyword4 = new JSONObject();
	    	JSONObject remark = new JSONObject();
	    	keyword1.put("color", color);
	    	keyword2.put("color", color);
	    	keyword3.put("color", color);
	    	keyword4.put("color", color);
	    	remark.put("color", color);
	    	if(type.equals(OrderStatus.Dispatched)) {
	    		template_id = templateId_depath;
	    		keyword1.put("value", msgdata.get("unit"));
		    	keyword2.put("value", msgdata.get("contact"));
		    	keyword3.put("value", msgdata.get("phone"));
		    	keyword4.put("value", msgdata.get("address"));
		    	remark.put("value", msgdata.get("description"));
	    		data.put("unit",keyword1);
		    	data.put("contact",keyword2);
		    	data.put("phone",keyword3);
		    	data.put("address",keyword4);
		    	data.put("description",remark);
	    	}else if(type.equals(OrderStatus.MaintenanceFeedback)){
	    		template_id = templateId_feedback;
	    		keyword1.put("value", msgdata.get("report"));
		    	keyword2.put("value", msgdata.get("feedbackTime"));
		    	keyword3.put("value", msgdata.get("userName"));
		    	remark.put("value", msgdata.get("phone"));
	    		data.put("report",keyword1);
		    	data.put("feedbackTime",keyword2);
		    	data.put("userName",keyword3);
		    	data.put("phone",remark);
	    	}else if(type.equals(OrderStatus.OrderConfirmed)) {
	    		template_id = templateId_confirm;
		    	keyword1.put("value", msgdata.get("userName"));
		    	keyword2.put("value", msgdata.get("confirmTime"));
		    	data.put("userName",keyword1);
		    	data.put("confirmTime",keyword2);
	    	}else if(type.equals(OrderStatus.Complete)) {
	    		template_id = templateId_complete;
	    		keyword1.put("value", msgdata.get("technician"));
		    	keyword2.put("value", msgdata.get("depathUser"));
		    	keyword3.put("value", msgdata.get("phone"));
		    	keyword4.put("value", msgdata.get("status"));
		    	remark.put("value", msgdata.get("completeDate"));
	    		data.put("technician",keyword1);
		    	data.put("depathUser",keyword2);
		    	data.put("phone",keyword3);
		    	data.put("status",keyword4);
		    	data.put("completeDate",remark);
	    	}else if(type.equals(OrderStatus.QuotedPrice)) {
	    		template_id = templateId_price;
	    		keyword1.put("value", msgdata.get("deviceName"));
		    	keyword2.put("value", msgdata.get("description"));
		    	keyword3.put("value", msgdata.get("report"));
		    	keyword4.put("value", msgdata.get("total"));
		    	remark.put("value", msgdata.get("phone"));
	    		data.put("deviceName",keyword1);
		    	data.put("description",keyword2);
		    	data.put("report",keyword3);
		    	data.put("total",keyword4);
		    	data.put("phone",remark);
	    	}
	    	jsonObject.put("template_id", template_id);
	    	jsonObject.put("url", tourl);
	    	
	    	jsonObject.put("data", data);
	    	String jsonData = JSONObject.fromObject(jsonObject).toString();
	    	String url = sendTemplateMsg_url.replace("ACCESS_TOKEN", accessToken.getToken());
	    	JSONObject jsonResult = httpRequest(url, "POST", jsonData);
	    	if(jsonResult == null) return Boolean.FALSE; 
	    	int errcode = jsonResult.getInt("errcode");
	    	if(errcode == 0){
	    		return Boolean.TRUE;
	    	} else {
	    		return Boolean.FALSE;
	    	}
			
	    }
     
}
