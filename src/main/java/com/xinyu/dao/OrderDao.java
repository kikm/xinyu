package com.xinyu.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xinyu.bean.OrderStatus;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Order;
import com.xinyu.model.OrderPart;

@Mapper
public interface OrderDao {

	public List<Order> getOrderCriteriaQuery(@Param("order")Order order,@Param("page")PageBean page);

	public int getCountOrderCriteriaQuery(@Param("order")Order order);
	
	public String getNewOrderNo(String preOrderNo);

	public Long saveOrUpdateOrder(@Param("order")Order orderSql);
	
	public Integer saveOrderPart(List<OrderPart> list);

	public Integer updateOrderPart(List<OrderPart> partList);

	public Order getOrderById(Long id);
	
	public List<Order> getOrderByIds(List<Long> list);
	
	public void deleteOrderPart(List<Long> list);
	
	public void deleteOrder(Long id);
	
	public void updateOrderStatus(@Param("order")Order order);
	
	public void maintenanceFeedback(@Param("order")Order order);

	public Order getDepathAndTechUser(Long orderId);

	public List<Order> getOrderCriteriaQueryByStatusList(Order order, List<OrderStatus> list);
	
	public List<Map<String, Object>> getDepathCount();
}
