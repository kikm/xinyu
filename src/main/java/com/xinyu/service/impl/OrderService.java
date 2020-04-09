package com.xinyu.service.impl;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.xinyu.bean.AccessToken;
import com.xinyu.bean.Layui;
import com.xinyu.bean.OrderBean;
import com.xinyu.bean.OrderStatus;
import com.xinyu.bean.PageBean;
import com.xinyu.dao.DeviceDao;
import com.xinyu.dao.OrderDao;
import com.xinyu.dao.PartDao;
import com.xinyu.dao.UnitDao;
import com.xinyu.dao.UserDao;
import com.xinyu.model.Device;
import com.xinyu.model.Order;
import com.xinyu.model.OrderPart;
import com.xinyu.model.Part;
import com.xinyu.model.Unit;
import com.xinyu.model.User;
import com.xinyu.service.IOrderService;
import com.xinyu.util.FileUpDownLoadUtils;
import com.xinyu.util.WeiXinUtil;

@Service
public class OrderService implements IOrderService {
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DeviceDao deviceDao;
	
	@Autowired
	private PartDao partDao;
	
	@Autowired
	private UnitDao unitDao;
	
	private Logger logger = LoggerFactory
			.getLogger(OrderService.class);
	
	public Layui getOrderData(PageBean pageBean,Order order) {
		
		int count = orderDao.getCountOrderCriteriaQuery(order);
		pageBean.setCount(count);
		List<Order> orderList = orderDao.getOrderCriteriaQuery(order, pageBean);
		List<OrderBean> data = this.OrderToOrderBean(orderList);
		
		return Layui.data("",0,count, data);
	}
	
	@Override
	public List<OrderBean> getOrderList(PageBean pageBean,Order order) {
		
		List<Order> orderList = orderDao.getOrderCriteriaQuery(order, pageBean);
		List<OrderBean> data = this.OrderToOrderBean(orderList);
		
		return data;
	}
	
	@Override
	public List<OrderBean> getOrderListByStatusList(PageBean pageBean,Order order,List<OrderStatus> statusList) {
		
		List<Order> orderList = orderDao.getOrderCriteriaQueryByStatusList(order, pageBean,statusList);
		List<OrderBean> data = this.OrderToOrderBean(orderList);
		
		return data;
	}
	
	@Override
	public Order getOrderById(Long id) {
		
		Order orderResult = orderDao.getOrderById(id);
		
		return orderResult;
	}
	
	@Override
	public OrderBean getOrderBeanById(Long id) {
		
		Order orderResult = orderDao.getOrderById(id);
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(orderResult);
		OrderBean oBean = this.OrderToOrderBean(orderList).get(0);
		User u = userDao.findByUserId(orderResult.getDepathUser());
		oBean.setPhone(u.getTelephone());
		return oBean;
	}
	
	private List<OrderBean> OrderToOrderBean(List<Order> orderList) {
		List<OrderBean> result = new ArrayList<OrderBean>();
		
		for(Order o : orderList) {
			OrderBean bean = new OrderBean();
			bean.setDeviceName(o.getDevice()==null?"":o.getDevice().getName());
			bean.setUnitName(o.getUnit()==null?"":o.getUnit().getName());
			bean.setStatusName(o.getStatus()==null?"":o.getStatus().getCnName());
			bean.setStatus(o.getStatus());
			bean.setContact(o.getContact());
			bean.setPhone(o.getPhone());
			bean.setFacility(o.getFacility());
			bean.setPartList(o.getPartList());
			bean.setPartListSize(o.getPartList()==null?0:o.getPartList().size());
			bean.setId(o.getId());
			bean.setModel(o.getModel());
			bean.setOrderNo(o.getOrderNo());
			bean.setUpkeep(o.getUpkeep());
			bean.setSn(o.getSn());
			result.add(bean);
			
			if(o.getUpkeep() != null) {
				float total = o.getUpkeep();
				for(OrderPart op : o.getPartList()) {
					total +=op.getOffer();
				}
				bean.setTotal(total);
			}
				
		}
		
		return result;
	}
	
	@Override
	public List<Device> getDeviceList() {
		List<Device> deviceList = deviceDao.getAllDevice();
		
		return deviceList;
		
	}
	
	@Override
	public Layui getNewOrderNo() {
		String number = "0001";
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateNo = dateFormat.format(new Date());
		String orderNo = orderDao.getNewOrderNo(dateFormat.format(new Date()));
		String result = "";
		if(StringUtils.isNotBlank(orderNo)) {
			String subOrderNo = orderNo.substring(orderNo.lastIndexOf("0"), orderNo.length());
			int  subNum  = Integer.valueOf(subOrderNo);
			result = dateNo+String.format("%04d", subNum+1);
		}else {
			result = dateNo+number;
		}
		
		return Layui.data(result, 0, 0, null);
	}

	@Override
	public Layui getPartDataByDevice(Integer deviceId) {
		List<Part> partList = partDao.getPartByDevice(deviceId);
		return Layui.data("",0, 0, partList);
	}
	
	@Override
	public List<Unit> getUnit() {
		List<Unit> unitList = unitDao.getUnit();
		return unitList;
	}
	
	@Override
	@Transactional
	public Layui deleteOrder(String deleteOrder) {
		if(StringUtils.isNotBlank(deleteOrder)) {
			Long id = Long.valueOf(deleteOrder);
			Order oriOrder = orderDao.getOrderById(id);
			List<Long> idList = new ArrayList<Long>();
			for(OrderPart op : oriOrder.getPartList()) {
				idList.add(op.getOpid());
			}
			orderDao.deleteOrder(id);
 			if(idList.size() > 0) {
 				orderDao.deleteOrderPart(idList);
 			}
 			if(StringUtils.isNotBlank(oriOrder.getImageUrls())) {
 				FileUpDownLoadUtils.deleteFile(oriOrder.getImageUrls());
 			}
 			
 		}
		return Layui.data("删除成功", 0, 0, null);
	}
	
	@Override
	@Transactional
	public Layui saveOrUpdateOrder(MultipartHttpServletRequest params,Order order,String deleteFile,String deleteOrderPart) {
		
		
        List<MultipartFile> files = params.getFiles("file");   
        MultipartFile file = null; 
        String imageurls = "";
        for (int i = 0; i < files.size(); ++i) {    
            file = files.get(i);    
            if (!file.isEmpty()) {    
                try {    
                    byte[] bytes = file.getBytes();    
                    String imageUrl = FileUpDownLoadUtils.uploadFile(bytes,file.getOriginalFilename());
                    imageurls += imageUrl+";";
                } catch (Exception e) {
                	e.printStackTrace();
                    return Layui.data("保存图片错误",1, 0, null);  
                }    
            }
        }  
        order.setImageUrls(imageurls.length()>1?imageurls.substring(0,imageurls.length()-1):null);
        if(order.getUpkeep() == null) {
			order.setStatus(OrderStatus.preOrder);
		}else {
			order.setStatus(OrderStatus.QuotedPrice);
		}
        synchronized(order) {
        	try {
        		Boolean isNew = order.getId() == null?true:false;
        		if(isNew) {
        			order.setCreateDate(new Date());
        		}
        		orderDao.saveOrUpdateOrder(order);
        		for(OrderPart p : order.getPartList()) {
        			p.setOrderNo(order.getOrderNo());
        			p.setOrderId(order.getId());
        		}
        		if(order.getUpkeep() != null) {
        			if(isNew) {
        				orderDao.saveOrderPart(order.getPartList());
        			}else {
        				List<OrderPart> saveList = new ArrayList<OrderPart>();
        				List<OrderPart> updateList = new ArrayList<OrderPart>();
        				List<OrderPart> orderPartList = order.getPartList();
        				for(OrderPart op : orderPartList) {
        					if(op.getOpid() == null) {
        						saveList.add(op);
        					}else {
        						updateList.add(op);
        					}
        				}
        				if(saveList.size()>0) orderDao.saveOrderPart(saveList);
        				if(updateList.size()>0) orderDao.updateOrderPart(updateList);
        				if(StringUtils.isNotBlank(deleteOrderPart)) {
        					List<Long> idList = new ArrayList<Long>();
        					List<String> list = Arrays.asList(deleteOrderPart.split(","));
        					for(String sid : list){
        						idList.add(Long.valueOf(sid));
        					}
        					orderDao.deleteOrderPart(idList);
        				}
        			}
        		}
        		if(OrderStatus.QuotedPrice.equals(order.getStatus())) {
        			List<User> userList = userDao.getUserListByUnit(order.getUnit().getId());
        			if(userList != null&&userList.size()>0) {
        				Order temp = orderDao.getDepathAndTechUser(order.getId());
        				User duser = userDao.findByUserId(temp.getDepathUser());
        				Map<String,String> data = new HashMap<String,String>();
        				data.put("orderNo", order.getOrderNo());
        				data.put("deviceName", temp.getFacility());
        				data.put("description", order.getDescription());
        				data.put("report", order.getReport());
        				data.put("partCost", order.getOrderNo());
        				data.put("upkeep",String.valueOf(order.getUpkeep()));
        				float total = order.getUpkeep();
        				for(OrderPart op : order.getPartList()) {
        					total +=op.getOffer();
        				}
        				data.put("total", String.valueOf(total));
        				data.put("phone", duser.getTelephone());
        				//http://localhost:8080/xinyu/wx/preConfirm?confirmUser=oCnlEuFjrHbyecP-JwXMeT0Jcoh8&orderId=61
        				String url = "http://xywxfw.com/xinyu/wx/preConfirm?cusopenId="+userList.get(0).getOpenID()+"&orderId="+order.getId();
        				Boolean isSend = WeiXinUtil.sendTemplate(userList.get(0).getOpenID(),url,data,OrderStatus.QuotedPrice,order.getIsUrgent());
        			}
        		}
             } catch (Exception e) {
            	 e.printStackTrace();
            	 FileUpDownLoadUtils.deleteFile(imageurls);
            	 return Layui.data("保存工单错误",1, 0, null);
             }    
        	
		}
        
        if(StringUtils.isNotBlank(deleteFile)) {
			FileUpDownLoadUtils.deleteFile(deleteFile);
		}
        
		return Layui.data("保存成功",0, 0, null);
	}

	@Override
	public Layui depathOrder(String ids,String technicianId,String depathUserId) {
		//WeiXinUtil.deleteMenu();
		//WeiXinUtil.createMenu(WeiXinUtil.getMenu());
		//WeiXinUtil.setIndustry();
		String[] idarr = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		for(String id : idarr) {
			if(StringUtils.isNotBlank(id)) {
				idList.add(Long.valueOf(id));
			}
		}
		if(StringUtils.isBlank(technicianId)) {
			return Layui.data("未选择派单人员", 1, 0, null);
		}
		User user = userDao.findByUserId(technicianId);
		if(StringUtils.isBlank(user.getOpenID())) {
			return Layui.data("技术人员未绑定账号，无法派单", 1, 0, null);
		}
		User depathuser = userDao.findByUserId(depathUserId);
		List<Order> orderList = orderDao.getOrderByIds(idList);
		Map<String,String> data = new HashMap<String,String>();
		String msg = "";
		for(Order order : orderList) {
			if(order.getStatus() != OrderStatus.preOrder) {
				continue;
			}
			data.clear();
			data.put("orderNo", order.getOrderNo());
			data.put("address", order.getAddress());
			data.put("contact", order.getContact());
			data.put("phone", order.getPhone());
			data.put("unit", order.getUnit().getName());
			data.put("description", order.getDescription());
			String url = "http://xywxfw.com/xinyu/wx/preFeedback?technicianOpenId="+user.getOpenID()+"&orderId="+order.getId()+"&depathUserOpenId="+depathuser.getOpenID();
			Boolean isSend = WeiXinUtil.sendTemplate(user.getOpenID(),url,data,OrderStatus.Dispatched,order.getIsUrgent());
			Order orderTemp = new Order();
			orderTemp.setId(order.getId());
			orderTemp.setStatus(OrderStatus.Dispatched);
			orderTemp.setDepathDate(new Date());
			orderTemp.setDepathUser(depathUserId);
			orderTemp.setTechnician(technicianId);
			if(isSend) {
				orderDao.updateOrderStatus(orderTemp);
				msg += orderTemp.getOrderNo()+":"+"派送成功";
			}else {
				msg += orderTemp.getOrderNo()+":"+"派送失败";
			}
		}
		return Layui.data(msg, 0, 0, null);
	}

	@Override
	public List<User> getDepathList() {
		List<User> userList = userDao.getUserListByRole("technician");
		
		return userList;
	}
	
	@Override
	public Layui maintenanceFeedback(MultipartHttpServletRequest params,Order order,String technicianOpenId) {
		
		List<MultipartFile> files = params.getFiles("file");   
        MultipartFile file = null; 
        String imageurls = "";
        for (int i = 0; i < files.size(); ++i) {    
            file = files.get(i);    
            if (!file.isEmpty()) {    
                try {    
                    byte[] bytes = file.getBytes();    
                    String imageUrl = FileUpDownLoadUtils.uploadFile(bytes,file.getOriginalFilename());
                    imageurls += imageUrl+";";
                } catch (Exception e) {
                	e.printStackTrace();
                    return Layui.data("保存图片错误",1, 0, null);  
                }    
            }
        }  
        User user = userDao.findByOpenId(technicianOpenId);
        order.setImageUrls(imageurls.length()>1?imageurls.substring(0,imageurls.length()-1):null);
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        order.setStatus(OrderStatus.MaintenanceFeedback);
        order.setFeedbackDate(new Date());
		orderDao.maintenanceFeedback(order);
		Order temp = orderDao.getDepathAndTechUser(order.getId());
		User duser = userDao.findByUserId(temp.getDepathUser());
		Map<String,String> data = new HashMap<String,String>();
		data.put("orderNo", temp.getOrderNo());
		data.put("report", order.getReport());
		data.put("feedbackTime",sdf.format(new Date()));
		data.put("userName", user.getName());
		WeiXinUtil.sendTemplate(duser.getOpenID(),null,data,OrderStatus.MaintenanceFeedback,temp.getIsUrgent());
		return Layui.data("反馈成功",0, 0, null);  
	}

	@Override
	public Layui orderConfirm(String orderId,String openId) {
		User confirmUser = userDao.findByOpenId(openId);
		Order order = new Order();
		order.setId(Long.valueOf(orderId));
		order.setStatus(OrderStatus.OrderConfirmed);
		order.setConfirmUser(confirmUser.getId());
		orderDao.updateOrderStatus(order);
		Order otemp = orderDao.getDepathAndTechUser(Long.valueOf(orderId));
		User duser = userDao.findByUserId(otemp.getDepathUser());
		User tuser = userDao.findByUserId(otemp.getTechnician());
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		Map<String,String> data = new HashMap<String,String>();
		data.put("orderNo", otemp.getOrderNo());
		data.put("confirmTime",sdf.format(new Date()));
		data.put("userName", duser.getName());
		String url = "http://xywxfw.com/xinyu/wx/preComplete?technicianOpenId="+tuser.getOpenID()+"&orderId="+order.getId()+"&depathUserOpenId="+duser.getOpenID();
		//通知客服和技术人员
		WeiXinUtil.sendTemplate(duser.getOpenID(),url,data,OrderStatus.OrderConfirmed,otemp.getIsUrgent());
		WeiXinUtil.sendTemplate(tuser.getOpenID(),url,data,OrderStatus.OrderConfirmed,otemp.getIsUrgent());
		return Layui.data("确认成功，开始采购维修", 0, 0, null);
	}
	
	@Override
	public Layui techComplete(String orderId) {
		Order order = new Order();
		order.setId(Long.valueOf(orderId));
		order.setStatus(OrderStatus.Complete);
		order.setCompleteDate(new Date());
		orderDao.updateOrderStatus(order);
		Order otemp = orderDao.getDepathAndTechUser(Long.valueOf(orderId));
		User tuser = userDao.findByUserId(otemp.getTechnician());
		User duser = userDao.findByUserId(otemp.getDepathUser());
		User cuser = userDao.findByUserId(otemp.getConfirmUser());
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		Map<String,String> data = new HashMap<String,String>();
		data.put("orderNo", otemp.getOrderNo());
		data.put("technician",tuser.getName());
		data.put("depathUser", duser.getName());
		data.put("phone", duser.getTelephone());
		data.put("status", "维修完成");
		data.put("completeDate", sdf.format(new Date()));
		WeiXinUtil.sendTemplate(duser.getOpenID(),null,data,OrderStatus.Complete,otemp.getIsUrgent());
		if(!duser.getOpenID().equals(cuser.getOpenID())) {
			WeiXinUtil.sendTemplate(cuser.getOpenID(),null,data,OrderStatus.Complete,otemp.getIsUrgent());
		}
		return Layui.data("确认成功", 0, 0, null);
	}

	@Override
	public Layui finishOrder(String ids, String userId) {
		String[] idarr = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		for(String id : idarr) {
			if(StringUtils.isNotBlank(id)) {
				idList.add(Long.valueOf(id));
			}
		}
		List<Order> orderList = orderDao.getOrderByIds(idList);
		for(Order order : orderList) {
			Order orderTemp = new Order();
			orderTemp.setId(order.getId());
			orderTemp.setStatus(OrderStatus.Finish);
			orderDao.updateOrderStatus(orderTemp);
		}
		
		return Layui.data("结单成功", 0, 0, null);
	}

}
