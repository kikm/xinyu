package com.xinyu.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.xinyu.bean.Layui;
import com.xinyu.bean.OrderBean;
import com.xinyu.bean.OrderStatus;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Device;
import com.xinyu.model.Order;
import com.xinyu.model.Unit;
import com.xinyu.model.User;

public interface IOrderService {

	public Layui getOrderData(PageBean pageBean,Order order, String startDate, String endDate);
	
	public List<Device> getDeviceList();
	
	public String getNewOrderNo();

	public Layui getPartDataByDevice(Integer deviceId);

	public List<Unit> getUnit();

	public Layui saveOrUpdateOrder(MultipartHttpServletRequest params, Order order, String deleteFile,String deleteOrderPart);

	public Order getOrderById(Long order);

	public Layui deleteOrder(String deleteOrder);

	public Layui depathOrder(String ids,String userId,String depathUserId);

	public List<User> getDepathList();

	public Layui orderConfirm(String orderId, String openId,String confirmOpinion);

	public Layui finishOrder(String ids, String userId);

	public Layui maintenanceFeedback(MultipartHttpServletRequest params, Order order, String tenchOpenId);

	public Layui techComplete(String orderId);

	public OrderBean getOrderBeanById(Long id);

	public List<OrderBean> getOrderListByStatusList(Order order, List<OrderStatus> statusList);

	public Map<String, Integer> getDepathCount();

	public String getOrderImage(String orderId);

	public Layui redepathOrder(String ids, String technicianId, String depathUserId);

	public Layui arrivalNotice(String ids);

	public String getOrderPartByIds(String ids);

	public Layui getTechOrder(String tech);

	public Layui getTimeLine(String orderId);

	public List<OrderBean> getOrderByOneText(Order order,String text);
	
	public Order getSingelOrder(Long orderId);

	public List<User> getOrgDepathList(String depathOpendID);

	public String getLeastOrderTen(String city);

	public String getOrderPartDesById(Long id);

	public void noticeCustomerService(Order order, User u);
	
}
