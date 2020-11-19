package com.xinyu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baidu.aip.ocr.AipOcr;
import com.xinyu.Constance;
import com.xinyu.bean.City;
import com.xinyu.bean.Layui;
import com.xinyu.bean.OrderBean;
import com.xinyu.bean.OrderStatus;
import com.xinyu.model.Order;
import com.xinyu.model.Role;
import com.xinyu.model.Unit;
import com.xinyu.model.User;
import com.xinyu.service.IOrderService;
import com.xinyu.service.IUserService;
import com.xinyu.service.IWxService;
import com.xinyu.util.WeiXinUtil;

@RestController
@RequestMapping("/wx")
public class WeiXinController {
	
	@Autowired
	private IWxService wxService;
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IUserService userService;
	
	
	/**
	 * 
	 * @param signature  微信加密签名  
	 * @param timestamp  时间戳  
	 * @param nonce      随机数
	 * @param echostr    随机字符串  
	 * @return
	 */
	@RequestMapping(value = "/requestAuth", method = RequestMethod.GET)
    public String requestAuth(String signature ,String timestamp,String nonce,String echostr) {  
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败 
        if (WeiXinUtil.checkSignature(signature, timestamp, nonce)) {  
        	return echostr;
        } 
        return "";
    }
	
	//处理微信服务器发来的请求
	@RequestMapping(value = "/requestAuth", method = RequestMethod.POST)
	public String acceptWXMsg(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException
	{
	    String respMessage = wxService.processRequest(req);  
	          
	    return respMessage;
			
	}
	
	
	@RequestMapping("/maintenanceFeedback")
    public Layui maintenanceFeedback(HttpServletRequest request,Order order,String orderId,String technicianOpenId,String report) {
		order.setId(Long.valueOf(orderId));
		order.setReport(report);
		MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
		Layui result = orderService.maintenanceFeedback(params,order,technicianOpenId);
		
        return result;
    }
	
	@RequestMapping(value = "/preFeedback", method = RequestMethod.GET)
    public ModelAndView mobileLoginAuthentication(String technicianOpenId,String depathUserOpenId,String orderId) {
		ModelAndView mov = new ModelAndView("/mobile/maintenanceFeedback");//未绑定账号
		mov.addObject("technicianOpenId", technicianOpenId);
		mov.addObject("depathUserOpenId", depathUserOpenId);
		Order o = orderService.getSingelOrder(Long.valueOf(orderId));
		if(o == null) {
			mov = new ModelAndView("/mobile/error");
			mov.addObject("msg", "工单不存在，请联系客服");
			return mov;
		}
		String image  = o.getImageUrls();
		Boolean canRepath = userService.checkCanRepath(depathUserOpenId,orderId);
		if(canRepath) {
			List<User> orderDepathList = orderService.getOrgDepathList(technicianOpenId);
			mov.addObject("depathList", orderDepathList);
		}
		if(StringUtils.isNotBlank(image)){
			image = image.split(";")[0];
		}
		mov.addObject("orderId", orderId);
		mov.addObject("image", image);
		mov.addObject("canRepath", canRepath);
		
		return mov;
	}
	
	@RequestMapping(value = "/preConfirm", method = RequestMethod.GET)
    public ModelAndView preConfrim(String orderId,String cusopenId) {
		ModelAndView mov = new ModelAndView("/mobile/confirmOrComplete");//未绑定账号
		OrderBean order = orderService.getOrderBeanById(Long.valueOf(orderId));
		if(order == null) {
			mov = new ModelAndView("/mobile/error");
			mov.addObject("msg", "工单不存在，请联系客服");
		}
		mov.addObject("openId", cusopenId);
		mov.addObject("orderId", orderId);
		mov.addObject("type", "confirm");
		mov.addObject("orderBean", order);
		return mov;
    }
	
	@RequestMapping(value = "/cusConfirm")
    public Layui cusConfirm(String openId,String orderId,String confirmOpinion) {
		Layui result = orderService.orderConfirm(orderId,openId,confirmOpinion);
		
		return result;
	}
	
	@RequestMapping(value = "/preComplete" , method = RequestMethod.GET)
    public ModelAndView preComplete(String orderId,String technicianOpenId) {
		ModelAndView mov = new ModelAndView("/mobile/confirmOrComplete");//未绑定账号
		OrderBean order = orderService.getOrderBeanById(Long.valueOf(orderId));
		mov.addObject("openId", technicianOpenId);
		mov.addObject("orderId", orderId);
		mov.addObject("orderBean", order);
		mov.addObject("type", "complete");
		
        return mov;
    }
	
	@RequestMapping(value = "/getconfirmOrCompleteInfo" , method = RequestMethod.GET)
    public OrderBean getconfirmOrCompleteInfo(String orderId) {
		OrderBean order = orderService.getOrderBeanById(Long.valueOf(orderId));
		
        return order;
    }
	
	@RequestMapping(value = "/complete")
    public Layui complete(String openId,String orderId) {
		Layui result = orderService.techComplete(orderId);
		return result;
	}
	
	@RequestMapping("/getMobileData")
    public List<OrderBean> getModelData(String openId, String status,String type) {
		Order order = new Order();
		List<OrderBean> total = null;
		User loginUser = userService.getUserByOpenId(openId);
		boolean iscs = false;
		List<OrderStatus> list = new ArrayList<OrderStatus>();
		list.add(OrderStatus.OrderConfirmed);
		list.add(OrderStatus.Dispatched);
		for(Role r : loginUser.getRoles()) {
			if(r.getName().equals("customerService")) iscs = true;//账号有客服角色，展示所有
		}
		if("cus".equals(type)) {
			if(!iscs) {order.setUnit(loginUser.getUnit());}
			list.add(OrderStatus.QuotedPrice);
			total = orderService.getOrderListByStatusList(order, list);
		}else {
			total = new ArrayList<OrderBean>();
			if(!iscs) {order.setTechnician(loginUser.getId());}
			total = orderService.getOrderListByStatusList(order,list);
		}
		
        return total;
    }
	
	@RequestMapping("/getHistorybytext")
    public List<OrderBean> getHistorybytext(String openId, String searchText) {
		Order order = new Order();
		List<OrderBean> total = null;
		User loginUser = userService.getUserByOpenId(openId);
		boolean iscs = false;
		for(Role r : loginUser.getRoles()) {
			if(r.getName().equals("customerService")) iscs = true;//账号有客服角色，展示所有
		}
		if(!iscs) {order.setUnit(loginUser.getUnit());}
		total = orderService.getOrderByOneText(order, searchText);
		
        return total;
    }
	
	@RequestMapping(value = "/startOrder", method = RequestMethod.GET)
    public ModelAndView startOrder(String code) {
		ModelAndView mov = new ModelAndView("/mobile/bandCount"); //未绑定账号\客户页面
		mov.addObject("msg", "没有操作权限，请联系管理员");
		String openID = null; 
		net.sf.json.JSONObject jsonOpenID = null; 
		if(code != null){
			jsonOpenID = WeiXinUtil.getOpenID(code);
			openID = (String)jsonOpenID.get("openid");//"onIRYuB19EGw1E9ojhwSJZe6Wxuo";
			User u = userService.getUserByOpenId(openID);
			if(u == null) {
				return mov;
			}else {
				mov = new ModelAndView("/mobile/startOrder");
				mov.addObject("openId", openID); 
				String orderNo = orderService.getNewOrderNo();
				for(Role r : u.getRoles()) {
					if(r.getName().equals("customer")) {
						mov.addObject("unitname", u.getUnit().getName());
						mov.addObject("unitid", u.getUnit().getId());
						orderNo += 'C'+orderNo;
						mov.addObject("iscus", Boolean.TRUE);
						break;
					}else {
						List<Unit> unitList = orderService.getUnit();
						mov.addObject("units", unitList);
					}
				}
				List<User> orderDepathList = orderService.getDepathList();
				mov.addObject("depathList", orderDepathList);
				mov.addObject("orderNo", orderNo);
			}
		}
		return mov;
		
	}
	
	@RequestMapping(value = "/depath", method = RequestMethod.GET)
	public ModelAndView depath(String orderId,String openId) {
		ModelAndView mov = new ModelAndView("/mobile/depathOrder"); //未绑定账号\客户页面
		
		Order o = orderService.getSingelOrder(Long.valueOf(orderId));
		if(StringUtils.isBlank(o.getTechnician())) {
			mov = new ModelAndView("/mobile/error"); 
			mov.addObject("msg", "该工单已派发");
			return mov;
		}
		mov.addObject("openId", openId); 
		mov.addObject("orderId", orderId); 
		List<User> orderDepathList = orderService.getDepathList();
		mov.addObject("depathList", orderDepathList);
		//mov.addObject("openId", "oCnlEuFjrHbyecP-JwXMeT0Jcoh8");
		return mov;
	}
	
	@RequestMapping("/saveOrUpdateOrder")
	public Layui saveOrUpdateOrder(HttpServletRequest request, Order order, String deleteFile, String deleteOrderPart,String openId) {
		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		User u = userService.getUserByOpenId(openId);
		order.setCity(City.valueOf(u.getCity()));
		Boolean issz = City.SZ.equals(order.getCity())?Boolean.TRUE:Boolean.FALSE;
		Layui result = orderService.saveOrUpdateOrder(params, order, deleteFile, deleteOrderPart);
		if((int)result.get("code") == 0 ) {
			Boolean iscs = false;
			for(Role r : u.getRoles()) {
				if(r.getName().equals("customer")) {
					iscs = true;
					result.put("depathJump", Boolean.FALSE);
					break;
				}
			}
			if(iscs&&issz) {//是深圳客户账号直接派单
				String tenUserId = orderService.getLeastOrderTen(order.getCity().getName());//取当前身上最少单的技术员派单
				result = orderService.depathOrder(String.valueOf(order.getId()), tenUserId, openId);
			}else if(iscs){
				orderService.noticeCustomerService(order,u);
			}else {
				result.put("depathJump", Boolean.TRUE);
			}
		}
		return result;
	}
	
	@RequestMapping("/depathOrders")
	public Layui depathOrder(String ids, String technicianId,String openId) {
		Layui result = orderService.depathOrder(ids, technicianId, openId);

		return result;
	}
	
	@RequestMapping("/redepathOrders")
	@ResponseBody
	public Layui redepathOrder(String ids, String technicianId,String depathUserId) {
		Layui result = orderService.redepathOrder(ids, technicianId, depathUserId);

		return result;
	}
	
	@RequestMapping("/baiduOCR")
    public Order baiduOCR(HttpServletRequest request) {
		// 初始化一个AipOcr
	    AipOcr client = new AipOcr(Constance.BAIDUOCR_APP_ID, Constance.BAIDUOCR_API_KEY, Constance.BAIDUOCR_SECRET_KEY);
	
	    // 可选：设置网络连接参数
	    client.setConnectionTimeoutInMillis(2000);
	    client.setSocketTimeoutInMillis(60000);
	
	    // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
	    //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
	    //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
	
	    // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
	    // 也可以直接通过jvm启动参数设置此环境变量
	    //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
	    MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
	    List<MultipartFile> files = params.getFiles("file[]");
	    if(files == null || files.size() < 1) {
	    	files = params.getFiles("file");
	    }
        MultipartFile file = null; 
        file = files.get(0);
        byte[] bytes = null;
        if (!file.isEmpty()) {    
        	try {    
        		bytes = file.getBytes();    
            } catch (Exception e) {
                	e.printStackTrace();
                    return null;  
            }    
        }
	    // 调用接口
	    JSONObject res = client.basicGeneral(bytes, new HashMap<String, String>());
	    JSONArray words_result = (JSONArray)res.get("words_result");
	    System.out.println(words_result.toString());
	    Order order = new Order();
	    String phone = "";
	    String contact = "";
	    String description = "";
	    String address = "";
	    String facility = "";
	    for(int i = 0;i<words_result.length();i++) {
	    	Pattern digit = Pattern.compile("^-?\\d+(\\.\\d+)?$");
	    	Pattern name = Pattern.compile("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*");
	    	String words = words_result.getJSONObject(i).get("words").toString();
	        if (digit.matcher(words).matches()) {
	        	phone = words;
	        }
	        if (words.indexOf("棱")!=-1||words.indexOf("公司")!=-1||words.indexOf("楼")!=-1||words.indexOf("院")!=-1||words.indexOf("房")!=-1||words.indexOf("堂")!=-1||words.indexOf("厦")!=-1) {
	        	if(words.indexOf("棱")!=-1||words.indexOf("空")!=-1||words.indexOf("德")!=-1) {
	        		words = words.replaceAll("棱", "楼");
	        		words = words.replaceAll("空", "室");
	        		words = words.replaceAll("德", "物");
	        	}
	        	address = words;
	        }
	        if (name.matcher(words).matches()&&words.length()<4&&words.indexOf("修人")==-1) {
	        	contact = words;
	        }
	        if (words.indexOf("描")!=-1||words.indexOf("述")!=-1) {
	        	description = words_result.getJSONObject(i+1).get("words").toString();
	        }
	        if (words.indexOf("名称")!=-1) {
	        	facility = words_result.getJSONObject(i+1).get("words").toString();
	        }
	    }
	    order.setContact(contact);
	    order.setPhone(phone);
	    order.setDescription(description);
	    order.setAddress(address);
	    order.setFacility(facility);
	    return order;
	}
	
	@RequestMapping(value = "/getTimeLine")
    public Layui cusConfirm(String orderId) {
		Layui result = orderService.getTimeLine(orderId);
		
		return result;
	}

}
