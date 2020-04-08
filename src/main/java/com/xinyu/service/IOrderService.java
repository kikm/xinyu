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

	public Layui getOrderData(PageBean pageBean,Order order);
	
	public List<OrderBean> getOrderList(PageBean pageBean,Order order);

	public List<Device> getDeviceList();
	
	public Layui getNewOrderNo();

	public Layui getPartDataByDevice(Integer deviceId);

	public List<Unit> getUnit();

	public Layui saveOrUpdateOrder(MultipartHttpServletRequest params, Order order, String deleteFile,String deleteOrderPart);

	public Order getOrderById(Long order);

	public Layui deleteOrder(String deleteOrder);

	public Layui depathOrder(String ids,String userId,String depathUserId);

	public List<User> getDepathList();

	public Layui orderConfirm(String orderId, String openId);

	public Layui finishOrder(String ids, String userId);

	public Layui maintenanceFeedback(MultipartHttpServletRequest params, Order order, String tenchOpenId);

	public Layui techComplete(String orderId);

	public OrderBean getOrderBeanById(Long id);

	public List<OrderBean> getOrderListByStatusList(PageBean pageBean, Order order, List<OrderStatus> statusList);
	
}
