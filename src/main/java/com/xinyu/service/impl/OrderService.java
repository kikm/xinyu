package com.xinyu.service.impl;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import com.xinyu.model.Organization;
import com.xinyu.model.Part;
import com.xinyu.model.Unit;
import com.xinyu.model.User;
import com.xinyu.service.IOrderService;
import com.xinyu.service.IOrgService;
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
	
	@Autowired
	private IOrgService orgService;
	
	private Logger logger = LoggerFactory
			.getLogger(OrderService.class);
	
	public Layui getOrderData(PageBean pageBean,Order order,String startDate,String endDate) {
		
		int count = orderDao.getCountOrderCriteriaQuery(order,startDate,endDate);
		pageBean.setCount(count);
		List<Order> orderList = orderDao.getOrderCriteriaQuery(order, pageBean,startDate,endDate);
		List<OrderBean> data = this.OrderToOrderBean(orderList);
		
		return Layui.data("",0,count, data);
	}
	
	
	@Override
	public List<OrderBean> getOrderListByStatusList(Order order,List<OrderStatus> statusList) {
		
		List<Order> orderList = orderDao.getOrderCriteriaQueryByStatusList(order,statusList);
		List<OrderBean> data = this.OrderToOrderBean(orderList);
		
		return data;
	}
	
	@Override
	public List<OrderBean> getOrderByOneText(Order order,String type,String startDate,String endDate) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		if(StringUtils.isBlank(startDate)) {
			int year = cal.get(Calendar.YEAR);
			startDate = year+"-01-01";
			endDate = f.format(new Date());
		}
		List<Order> orderList = orderDao.getOrderByOneText(order,type,startDate,endDate);
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
		if(orderResult == null) {
			return null;
		}
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(orderResult);
		OrderBean oBean = this.OrderToOrderBean(orderList).get(0);
		User u = userDao.findByUserId(orderResult.getDepathUser());
		oBean.setPhone(u.getTelephone());
		if(StringUtils.isNotBlank(oBean.getImageUrls())) {
			String firstImage = oBean.getImageUrls().split(";")[0];
			oBean.setImageUrls(firstImage);
		}
		return oBean;
	}
	
	private List<OrderBean> OrderToOrderBean(List<Order> orderList) {
		List<OrderBean> result = new ArrayList<OrderBean>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(Order o : orderList) {
			OrderBean bean = new OrderBean();
			bean.setDeviceName(o.getDevice()==null?"":o.getDevice().getName());
			bean.setUnitName(o.getUnit()==null?"":o.getUnit().getName());
			bean.setStatusName(o.getStatus()==null?"":o.getStatus().getCnName());
			bean.setStatus(o.getStatus());
			bean.setContact(o.getContact());
			bean.setPhone(o.getPhone());
			bean.setDescription(o.getDescription());
			bean.setFacility(o.getFacility());
			bean.setAddress(o.getAddress());
			bean.setReport(o.getReport());
			bean.setPartList(o.getPartList());
			bean.setPartListSize(o.getPartList()==null?0:o.getPartList().size());
			String des = this.getOrderPartDesById(o.getId());
			bean.setPartListDes(des);
			bean.setId(o.getId());
			bean.setModel(o.getModel());
			bean.setOrderNo(o.getOrderNo());
			bean.setUpkeep(o.getUpkeep());
			bean.setSn(o.getSn());
			bean.setImageUrls(o.getImageUrls());
			bean.setTechName(o.getTechName());
			bean.setCreateTime(sdf.format(o.getCreateDate()));
			result.add(bean);
			
			if(o.getUpkeep() != null) {
				float total = o.getUpkeep();
				if(o.getPartList()!= null && o.getPartList().size()>0) {
					for(OrderPart op : o.getPartList()) {
						total +=(op.getOffer()==null?0:op.getOffer())*(op.getNum()==null?0:op.getNum());
					}
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
	public String getNewOrderNo() {
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
		
		return result;
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
			if(OrderStatus.preOrder.equals(oriOrder.getStatus())||OrderStatus.Finish.equals(oriOrder.getStatus())) {
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
			}else {
				return Layui.data("该工单已派发，无法删除，或结单后删除", 0, 0, null);
			}
 			
 		}
		return Layui.data("删除成功", 0, 0, null);
	}
	
	@Override
	@Transactional
	public Layui saveOrUpdateOrder(MultipartHttpServletRequest params,Order order,String deleteFile,String deleteOrderPart) {
		
		
		Boolean isNew = order.getId() == null?true:false;
        List<MultipartFile> files = params.getFiles("file[]");
        String oldOI = "";
        String keepImg = "";
        if(!isNew) {
        	oldOI = orderDao.getOrderImages(String.valueOf(order.getId()));
        	if(StringUtils.isNotBlank(oldOI)) {
        		String[] fileArr = oldOI.split(";");
        		for(String f : fileArr) {
        			keepImg += deleteFile.indexOf(f)!=-1?"":f+";";
        		}
        		
        	}
        }
        MultipartFile file = null; 
        String imageurls = "";
        for (int i = 0; i < files.size(); ++i) {    
            file = files.get(i);    
            if (!file.isEmpty()) {    
                try {    
                    byte[] bytes = file.getBytes();    
                    String imageUrl = FileUpDownLoadUtils.uploadFile(bytes,file.getOriginalFilename());
                    imageurls += imageUrl+";";
                    keepImg += imageUrl+";";
                } catch (Exception e) {
                	e.printStackTrace();
                    return Layui.data("保存图片错误",1, 0, null);  
                }    
            }
        }  
        order.setImageUrls(keepImg.length()>1?keepImg:null);
        if(order.getUpkeep() == null) {
			order.setStatus(OrderStatus.preOrder);
		}else {
			order.setStatus(OrderStatus.QuotedPrice);
		}
        synchronized(order) {
        	try {
        		
        		if(isNew) {
        			order.setCreateDate(new Date());
        		}
        		orderDao.saveOrUpdateOrder(order);
        		if(order.getPartList() != null) {
        			for(OrderPart p : order.getPartList()) {
        				p.setOrderNo(order.getOrderNo());
        				p.setOrderId(order.getId());
        			}
        		}
        		if(order.getUpkeep() != null) {
        			if(isNew) {
        				deviceDao.addDeviceFrequentCount(order.getDevice().getId());
        				if(order.getPartList()!= null && order.getPartList().size()>0) {
        					orderDao.saveOrderPart(order.getPartList());
        				}
        			}else {
        				List<OrderPart> saveList = new ArrayList<OrderPart>();
        				List<OrderPart> updateList = new ArrayList<OrderPart>();
        				List<OrderPart> orderPartList = order.getPartList();
        				if(order.getPartList()!= null && order.getPartList().size()>0) {
        					for(OrderPart op : orderPartList) {
        						if(op.getOpid() == null) {
        							saveList.add(op);
        						}else {
        							updateList.add(op);
        						}
        					}
        					if(saveList.size()>0) orderDao.saveOrderPart(saveList);
        					if(updateList.size()>0) orderDao.updateOrderPart(updateList);
        				}
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
        		
             } catch (Exception e) {
            	 e.printStackTrace();
            	 FileUpDownLoadUtils.deleteFile(imageurls);
            	 return Layui.data("保存工单错误",1, 0, null);
             }    
        	
		}
        
        if(StringUtils.isNotBlank(deleteFile)) {
			FileUpDownLoadUtils.deleteFile(deleteFile);
		}
        
		return Layui.data("保存成功",0, order.getId().intValue(), null);
	}
	
	@Override
	public void quitePriceNoteic(Order order) {
		if(OrderStatus.QuotedPrice.equals(order.getStatus())&&order.getPartList()!=null &&order.getPartList().size()>0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<User> userList = userDao.getUserListByUnit(order.getUnit().getId());
			if(userList != null&&userList.size()>0) {
				Order temp = orderDao.getDepathAndTechUser(order.getId());
				User duser = userDao.findByUserId(temp.getDepathUser() == null?order.getDepathUser():temp.getDepathUser());
				Map<String,String> data = new HashMap<String,String>();
				data.put("orderNo", order.getOrderNo());
				data.put("facility", temp.getFacility());
				data.put("description", order.getDescription());
				data.put("report", order.getReport());
				data.put("address", order.getAddress());
				data.put("cusInfo", order.getContact());
				data.put("partCost", order.getOrderNo());
				data.put("priceDate", sdf.format(new Date()));
				data.put("upkeep",String.valueOf(order.getUpkeep()));
				float total = order.getUpkeep();
				for(OrderPart op : order.getPartList()) {
					if(op != null && op.getOffer()!= null) total +=op.getOffer();
				}
				data.put("total", String.valueOf(total));
				data.put("phone", duser.getTelephone());
				//http://localhost:8080/xinyu/wx/preConfirm?confirmUser=oCnlEuFjrHbyecP-JwXMeT0Jcoh8&orderId=61
				String url = "http://xywxfw.com/xinyu/wx/preConfirm?cusopenId="+userList.get(0).getOpenID()+"&orderId="+order.getId();
				WeiXinUtil.sendTemplate(userList.get(0).getOpenID(),url,data,OrderStatus.QuotedPrice,order.getIsUrgent());
			}
		}
	}

	@Override
	public Layui depathOrder(String ids,String technicianId,String depathUserId,String assistTechs) {
		//WeiXinUtil.deleteMenu();
		//WeiXinUtil.createMenu(WeiXinUtil.getMenu());
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
//		if(StringUtils.isBlank(user.getOpenID())) {
//			return Layui.data("技术人员未绑定账号，无法派单", 1, 0, null);
//		}
		User depathuser = userDao.findByUserId(depathUserId);
		if(depathuser == null) depathuser = userDao.findByOpenId(depathUserId);
		List<String> assitUserList = new ArrayList<String>();
		if(StringUtils.isNotBlank(assistTechs)) {
			String[] assitArr = assistTechs.split(",");
			List<String> assitList = new ArrayList<String>();
			for(String id : assitArr) {
				if(StringUtils.isNotBlank(id)) {
					assitList.add(id);
				}
			}
			assitUserList = userDao.getUserListOpenid(assitList);
			
		}
		List<Order> orderList = orderDao.getOrderByIds(idList);
		Map<String,String> data = new HashMap<String,String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		data.put("depathUser", depathuser.getName());
		String msg = "派送结果";
		for(Order order : orderList) {
			if(order.getStatus() != OrderStatus.preOrder&&order.getStatus() != OrderStatus.QuotedPrice) {
				continue;
			}
			data.put("date", sdf.format(order.getCreateDate()));
			data.put("orderNo", order.getOrderNo());
			data.put("address", order.getAddress());
			data.put("contact", order.getContact());
			data.put("phone", order.getPhone());
			data.put("unit", order.getUnit().getName());
			data.put("description", order.getDescription());
			data.put("dtdDate", sdf.format(order.getDtdDate()));
			String url = "http://xywxfw.com/xinyu/wx/preFeedback?technicianOpenId="+user.getOpenID()+"&orderId="+order.getId()+"&depathUserOpenId="+depathuser.getOpenID();
			Order orderTemp = new Order();
			orderTemp.setId(order.getId());
			if(order.getStatus() == OrderStatus.preOrder)
				orderTemp.setStatus(OrderStatus.Dispatched);
			else {
				orderTemp.setStatus(order.getStatus());
				url = "http://xywxfw.com/xinyu/wx/preComplete?technicianOpenId="+user.getOpenID()+"&orderId="+order.getId()+"&depathUserOpenId="+depathuser.getOpenID();
			}
			Boolean isSend = WeiXinUtil.sendTemplate(user.getOpenID(),url,data,OrderStatus.Dispatched,order.getIsUrgent());
			data.put("orderNo", order.getOrderNo());
			data.put("address", order.getAddress());
			data.put("tech", user.getName());
			data.put("techphone", user.getTelephone());
			List<User> userList = userDao.getUserListByUnit(order.getUnit().getId());
			if(userList != null&&userList.size()>0&&userList.get(0).getOpenID() != null) {
				WeiXinUtil.sendTemplate(userList.get(0).getOpenID(),null,data,OrderStatus.Dispatched,order.getIsUrgent());
				
			}
			for(String assit : assitUserList) {
				WeiXinUtil.sendTemplate(assit,null,data,OrderStatus.Dispatched,order.getIsUrgent());
			}
			orderTemp.setDepathDate(new Date());
			orderTemp.setDepathUser(depathuser.getId());
			orderTemp.setTechnician(technicianId);
			orderTemp.setAssistTechs(assistTechs);
			if(isSend) {
				orderDao.updateOrderStatus(orderTemp);
				msg += order.getOrderNo()+":"+"派送成功";
			}else {
				msg += order.getOrderNo()+":"+"派送失败";
			}
		}
		Map<String ,Integer> count = this.getDepathCount();
		List<Map<String ,Integer>> list = new ArrayList<Map<String ,Integer>>();
		list.add(count);
		return Layui.data(msg, 0, 0, list);
	}
	
	@Override
	public Layui redepathOrder(String ids,String technicianId,String depathUserId) {
		String[] idarr = ids.split(",");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		if(depathuser == null) depathuser = userDao.findByOpenId(depathUserId);
		List<Order> orderList = orderDao.getOrderByIds(idList);
		Map<String,String> data = new HashMap<String,String>();
		String msg = "转派成功";
		for(Order order : orderList) {
			User beforeUser = userDao.findByUserId(order.getTechnician());
			if(order.getStatus() == OrderStatus.Dispatched&&order.getIsRedepath()) {
				msg += order.getOrderNo()+":"+"已转派一次，不能多次转派";
				continue;
			}
			if(technicianId.equals(order.getTechnician())) {
				msg += order.getOrderNo()+":"+"转派人与原技术人一致";
				continue;
			}
			data.clear();
			data.put("orderNo", order.getOrderNo());
			data.put("address", order.getAddress());
			data.put("contact", order.getContact());
			data.put("phone", order.getPhone());
			data.put("unit", order.getUnit().getName());
			data.put("description", order.getDescription());
			data.put("redepathUser", user.getName());
			data.put("redepathUserPhone", user.getTelephone());
			data.put("dtdDate", sdf.format(order.getDtdDate()));
			data.put("date", sdf.format(order.getCreateDate()));
			data.put("beforeUser", beforeUser.getName());
			data.put("tenUser","tenUser");
			String url = "http://xywxfw.com/xinyu/wx/preFeedback?technicianOpenId="+user.getOpenID()+"&orderId="+order.getId()+"&depathUserOpenId="+depathuser.getOpenID();
			Order orderTemp = new Order();
			Boolean isSend = WeiXinUtil.sendTemplate(user.getOpenID(),url,data,OrderStatus.Dispatched,order.getIsUrgent());
			if(isSend) {//转派成功通知
				data.remove("tenUser");
				WeiXinUtil.sendTemplate(beforeUser.getOpenID(),url,data,OrderStatus.Dispatched,order.getIsUrgent());
			}
			orderTemp.setId(order.getId());
			orderTemp.setStatus(OrderStatus.Dispatched);
			orderTemp.setIsRedepath(true);
			orderTemp.setDepathDate(new Date());
			orderTemp.setDepathUser(depathUserId);
			orderTemp.setTechnician(technicianId);
			if(isSend) {
				orderDao.updateOrderStatus(orderTemp);
				msg += order.getOrderNo()+":"+"转派成功";
			}else {
				msg += order.getOrderNo()+":"+"转派失败";
			}
		}
		return Layui.data(msg, 0, 0, null);
	}
	
	@Override
	public Layui arrivalNotice(String ids) {
		String[] idarr = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String id : idarr) {
			if(StringUtils.isNotBlank(id)) {
				idList.add(Long.valueOf(id));
			}
		}
		List<Order> orderList = orderDao.getOrderByIds(idList);
		Map<String,String> data = new HashMap<String,String>();
		String msg = "";
		for(Order order : orderList) {
			if(StringUtils.isBlank(order.getTechnician())) {
				msg += order.getOrderNo()+":"+"工单未派单";
				continue;
			}
			if(order.getArrivalDate() != null) {
				msg += order.getOrderNo()+":"+"到货通知已发送";
				continue;
			}
			User user = userDao.findByUserId(order.getTechnician());
			if(order.getStatus() != OrderStatus.preOrder&&order.getStatus() != OrderStatus.OrderConfirmed) {
				msg = order.getOrderNo()+":工单状态已过期";
				continue;
			}
			data.clear();
			data.put("orderNo", order.getOrderNo());
			data.put("description", order.getDescription());
			data.put("report", order.getReport());
			String partList = "";
			Integer number = 0;
			if(order.getPartList() != null) {
				for(OrderPart op : order.getPartList()) {
					partList = partList+op.getName()+"/";
					number = number+op.getNum();	
				}
			}
			data.put("partList", partList.length()>1?partList.substring(0,partList.length()-1):order.getDevice().getName());
			data.put("number", String.valueOf(number));
			data.put("arrTime", sdf.format(new Date()));
			order.setArrivalDate(new Date());
			Boolean isSend = WeiXinUtil.sendTemplate(user.getOpenID(),null,data,OrderStatus.Finish,order.getIsUrgent());
			if(isSend) {
				orderDao.updateArrivalTime(order);
				msg += order.getOrderNo()+":"+"通知成功";
			}else {
				msg += order.getOrderNo()+":"+"通知失败";
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
	public List<User> getOrgDepathList(String depathOpenID) {
		User user = userDao.getUserByOpenId(depathOpenID);
		List<Organization> orgList = orgService.getChildrens(user.getOrgId());
		List<Long> orgIdList = new ArrayList<Long>();
		for(Organization o : orgList) {
			orgIdList.add(o.getId());
		}
		List<User> result = new ArrayList<User>();
		List<User> userList = userDao.getUserListByRole("technician");
		for(User u : userList) {
			if(orgIdList.contains(u.getOrgId())) {
				result.add(u);
			}
		}
		
		return result;
	}
	
	@Override
	public Layui maintenanceFeedback(MultipartHttpServletRequest params,Order order,String technicianOpenId) {
		
		List<MultipartFile> files = params.getFiles("file");   
        MultipartFile file = null; 
        String imageurls = "";
        String old = orderDao.getOrderImages(String.valueOf(order.getId()));
        if(StringUtils.isNotBlank(old)) {
        	imageurls = old;
        }
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
		data.put("model", temp.getSn());
		data.put("facility", temp.getFacility());
		data.put("description", temp.getDescription());
		data.put("feedbackTime",sdf.format(new Date()));
		data.put("userName", user.getName());
		data.put("phone", user.getTelephone());
		WeiXinUtil.sendTemplate(duser.getOpenID(),null,data,OrderStatus.MaintenanceFeedback,temp.getIsUrgent());
		return Layui.data("反馈成功",0, 0, null);  
	}

	@Override
	public Layui orderConfirm(String orderId,String openId,String confirmOpinion) {
		User confirmUser = userDao.findByOpenId(openId);
		Order order = new Order();
		order.setId(Long.valueOf(orderId));
		order.setStatus(OrderStatus.OrderConfirmed);
		order.setConfirmUser(confirmUser.getId());
		order.setConfirmOpinion(confirmOpinion);
		orderDao.updateOrderStatus(order);
		Order otemp = orderDao.getDepathAndTechUser(Long.valueOf(orderId));
		OrderBean obean = this.getOrderBeanById(Long.valueOf(orderId));
		User duser = userDao.findByUserId(otemp.getDepathUser());
		User tuser = userDao.findByUserId(otemp.getTechnician());
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		Map<String,String> data = new HashMap<String,String>();
		data.put("orderNo", otemp.getOrderNo());
		data.put("confirmUser", confirmUser.getName());
		data.put("facility", obean.getFacility());
		data.put("confirmTime",sdf.format(new Date()));
		data.put("total",String.valueOf(obean.getTotal()));
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
		data.put("facility", otemp.getFacility());
		data.put("description", otemp.getDescription());
		data.put("report", otemp.getReport());
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

	@Override
	public Map<String, Integer> getDepathCount() {
		Map<String, Integer> baseMap = new HashMap<String, Integer>();
	    List<Map<String, Object>> baseList = orderDao.getDepathCount();
	    for (Map<String, Object> map:baseList) {
	        String base = null;
	        Integer fare = null;
	        for (Map.Entry<String,Object>  entry:map.entrySet()) {
	            if ("idname".equals(entry.getKey())) {
	                base =  String.valueOf(entry.getValue());
	            }else if ("count".equals(entry.getKey())) {
	                fare = new Integer(String.valueOf(entry.getValue()));
	            }
	            baseMap.put(base,fare);
	        }
	    }
	    return baseMap;
	}

	@Override
	public String getOrderImage(String orderId) {
		String images = orderDao.getOrderImages(orderId);
		return images;
	}

	@Override
	public String getOrderPartByIds(String ids) {
		String[] idarr = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		for(String id : idarr) {
			if(StringUtils.isNotBlank(id)) {
				idList.add(Long.valueOf(id));
			}
		}
		List<OrderPart> opList = orderDao.getOrderPartByIds(idList);
		String msg = "到货配件确认：<br/>";
		Map<String,String> map = new HashMap<String,String>();
		for(OrderPart op : opList) {
			if(map.get(op.getOrderNo()) == null)
				map.put(op.getOrderNo(), op.getName()+"/"+op.getNum()+"\r\n");
			else {
				String value = map.get(op.getOrderNo());
				value = value + op.getName()+"/"+op.getNum()+"\r\n";
				map.put(op.getOrderNo(), value);
			}
		}
		for(String key : map.keySet()) {
			msg = msg+key+":"+map.get(key)+"<br/>";
		}
		return msg;
	}
	
	@Override
	public String getOrderPartDesById(Long id) {
		List<Long> idList = new ArrayList<Long>();
		idList.add(id);
		List<OrderPart> opList = orderDao.getOrderPartByIds(idList);
		String msg = "";
		for(OrderPart op : opList) {
			msg = msg + op.getName()+"("+op.getNum()+")\r\n";
		}
		return msg;
	}

	@Override
	public Layui getTechOrder(String tech) {
		List<Order> list = orderDao.getTechOrder(tech);
		return Layui.data("", 0, 0, list);
	}

	@Override
	public Layui getTimeLine(String orderId) {
		Order o = orderDao.getOrderById(Long.valueOf(orderId));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,String>> timeLine = new ArrayList<Map<String,String>>();
		Map<String,String> itemcreate = new HashMap<String,String>();
		Map<String,String> itemdepath = new HashMap<String,String>();
		Map<String,String> itemfeedback = new HashMap<String,String>();
		Map<String,String> itemconfirm = new HashMap<String,String>();
		Map<String,String> itemarrival = new HashMap<String,String>();
		Map<String,String> itemcomplete = new HashMap<String,String>();
		User tuser = null;
		String content = "由"+o.getUnit().getName()+"("+o.getAddress()+")"+"-"+o.getContact()+"("+o.getPhone()+")发起工单申请,故障描述："+o.getDescription();
		itemcreate.put(sdf.format(o.getCreateDate()), content);
		timeLine.add(itemcreate);
		if(o.getDepathDate() != null) {
			tuser = userDao.findByUserId(o.getTechnician());
			content = "工单派发成功，技术员："+tuser.getName()+"("+tuser.getTelephone()+")"+"于"+sdf.format(o.getDtdDate())+"前上门检修";
			itemdepath.put(sdf.format(o.getDepathDate()), content);
			timeLine.add(itemdepath);
		}
		if(o.getFeedbackDate() != null) {
			content = "检修完成，技术员："+tuser.getName()+"("+tuser.getTelephone()+")"+"检修完成，检修结果："+o.getReport();
			itemdepath.put(sdf.format(o.getFeedbackDate()), content);
			timeLine.add(itemfeedback);
		}
		if(o.getConfirmDate() != null) {
			User cuser = userDao.findByUserId(o.getConfirmUser());
			content = "报价确认完成，确认账号："+cuser.getName()+"("+cuser.getTelephone()+")"+"开始维修";
			itemdepath.put(sdf.format(o.getConfirmDate()), content);
			timeLine.add(itemconfirm);
		}
		if(o.getArrivalDate() != null&&o.getPartList()!=null) {
			String partList = "";
			Integer number = 0;
			for(OrderPart op : o.getPartList()) {
				partList = partList+op.getName()+"/";
				number = number+op.getNum();	
			}
			partList = partList.length()>1?partList.substring(0,partList.length()-1):o.getDevice().getName();
			
			content = "采购配件已全部到货，配件总数共"+String.valueOf(number)+"+,包括："+partList;
			itemarrival.put(sdf.format(o.getArrivalDate()), content);
			timeLine.add(itemarrival);
		}
		if(o.getCompleteDate() != null) {
			content = "工单维修完成，技术员："+tuser.getName()+"("+tuser.getTelephone()+")"+"维修完成";
			itemcomplete.put(sdf.format(o.getCompleteDate()), content);
			timeLine.add(itemcomplete);
		}
		
		return Layui.data("", 0, 0, timeLine);
	}

	@Override
	public Order getSingelOrder(Long orderId) {
		Order order = orderDao.getOrderSingleById(orderId);
		return order;
	}

	@Override
	public String getLeastOrderTen(String city) {
		String tenUserId = orderDao.getLeastOrderTen(city);
		return tenUserId;
	}

	@Override
	public void noticeCustomerService(Order order, User u) {
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        List<User> userList = userDao.getUserListByRole("customerService");
		Map<String,String> data = new HashMap<String,String>();
		data.put("orderNo", order.getOrderNo());
		data.put("user", u.getName());
		data.put("unit", u.getUnit().getName());
		data.put("facility", order.getFacility());
		data.put("phone", u.getTelephone());
		data.put("description", order.getDescription());
		data.put("createTime", sdf.format(order.getCreateDate()));
		for(User user : userList) {
			if(StringUtils.isNotBlank(user.getOpenID())) {
				String url = "http://xywxfw.com/xinyu/wx/depath?technicianOpenId="+u.getOpenID()+"&orderId="+order.getId();
				WeiXinUtil.sendTemplate(user.getOpenID(),url,data,OrderStatus.preOrder,order.getIsUrgent());
			}
		}
		
	}

}
