package com.xinyu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.xinyu.bean.Layui;
import com.xinyu.bean.OrderBean;
import com.xinyu.bean.OrderStatus;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Order;
import com.xinyu.model.Role;
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
    public Layui saveOrUpdateOrder(HttpServletRequest request,Order order,String orderId,String technicianOpenId,String report) {
		order.setId(Long.valueOf(orderId));
		order.setReport(report);
		MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
		//Layui result = orderService.maintenanceFeedback(params,order,technicianOpenId);
		
        return Layui.data("反馈成功",0, 0, null);
    }
	
	@RequestMapping(value = "/preFeedback", method = RequestMethod.GET)
    public ModelAndView mobileLoginAuthentication(String technicianOpenId,String depathUserOpenId,String orderId) {
		ModelAndView mov = new ModelAndView("/mobile/maintenanceFeedback");//未绑定账号
		mov.addObject("technicianOpenId", technicianOpenId);
		mov.addObject("depathUserOpenId", depathUserOpenId);
		mov.addObject("orderId", orderId);

		return mov;
	}
	
	@RequestMapping(value = "/preConfirm", method = RequestMethod.GET)
    public ModelAndView preConfrim(String orderId,String cusopenId) {
		ModelAndView mov = new ModelAndView("/mobile/confirmOrComplete");//未绑定账号
		OrderBean order = orderService.getOrderBeanById(Long.valueOf(orderId));
		mov.addObject("openId", cusopenId);
		mov.addObject("orderId", orderId);
		mov.addObject("type", "confirm");
		mov.addObject("orderBean", order);
		return mov;
    }
	
	@RequestMapping(value = "/cusConfirm")
    public Layui cusConfirm(String openId,String orderId) {
		Layui result = orderService.orderConfirm(orderId,openId);
		
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

}
