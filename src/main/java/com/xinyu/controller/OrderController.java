package com.xinyu.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xinyu.Constance;
import com.xinyu.bean.Layui;
import com.xinyu.bean.OrderStatus;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Device;
import com.xinyu.model.Order;
import com.xinyu.model.OrderPart;
import com.xinyu.model.Unit;
import com.xinyu.model.User;
import com.xinyu.service.IOrderService;
import com.xinyu.util.FileUpDownLoadUtils;
import com.xinyu.util.WeiXinUtil;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private IOrderService orderService;

	@RequestMapping("/orderList")
	public ModelAndView getOrderList() {
		ModelAndView mov = new ModelAndView("orderList");
		List<Unit> unitList = orderService.getUnit();
		List<Device> dList = orderService.getDeviceList();
		List<User> orderDepathList = orderService.getDepathList();
		Map<String,Integer> depathCount = orderService.getDepathCount();
		mov.setViewName("orderList");
		mov.addObject("statusList", OrderStatus.values());
		mov.addObject("depathCount",depathCount);
		mov.addObject("orderDepathList", orderDepathList);
		mov.addObject("unitList", unitList);
		mov.addObject("dList", dList);
		mov.addObject("pageBean", new PageBean());
		return mov;
	}

	@RequestMapping("/orderTableData")
	@ResponseBody
	public Layui getOrderData(PageBean pageBean, String orderNo, Long unitId, String status) {
		Order order = new Order();
		Unit u = new Unit();
		order.setUnit(u);
		if (StringUtils.isNotEmpty(orderNo)) {
			order.setOrderNo(orderNo);
		}
		if (unitId != null) {
			u.setId(unitId);
		}
		if (StringUtils.isNotEmpty(status)) {
			order.setStatus(OrderStatus.valueOf(status));
		}
		Layui result = orderService.getOrderData(pageBean, order);

		return result;
	}

	@RequestMapping("/getNewOrderNo")
	@ResponseBody
	public Layui getInitData() {

		Layui result = orderService.getNewOrderNo();

		return result;
	}

	@RequestMapping("/getPartDataByDevice")
	@ResponseBody
	public Layui getPartDataByDevice(Integer deviceId) {

		Layui result = orderService.getPartDataByDevice(deviceId);

		return result;
	}

	@RequestMapping("/saveOrUpdateOrder")
	@ResponseBody
	public Layui saveOrUpdateOrder(HttpServletRequest request,@SessionAttribute(Constance.USER_ID)String depathUserId, Order order, String deleteFile, String deleteOrderPart) {
		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		if(order.getDepathUser() == null&&order.getUpkeep()!= null) {
			order.setDepathUser(depathUserId);
		}
		Layui result = orderService.saveOrUpdateOrder(params, order, deleteFile, deleteOrderPart);

		return result;
	}

	@RequestMapping("/getOrderInfo")
	@ResponseBody
	public Order getOrderInfo(Long id) {
		Order result = orderService.getOrderById(id);

		return result;
	}
	
	@RequestMapping("/getOrderPartInfo")
	@ResponseBody
	public Layui getOrderPartInfo(String ids) {
		Layui result = orderService.getOrderPartByIds(ids);

		return result;
	}

	@RequestMapping("/deleteOrder")
	@ResponseBody
	public Layui deleteOrder(String id) {
		Layui result = orderService.deleteOrder(id);

		return result;
	}

	@RequestMapping("/depathOrders")
	@ResponseBody
	public Layui depathOrder(String ids, String technicianId,
			@SessionAttribute(Constance.USER_ID) String depathUserId) {
		Layui result = orderService.depathOrder(ids, technicianId, depathUserId);

		return result;
	}
	
	@RequestMapping("/redepathOrders")
	@ResponseBody
	public Layui redepathOrder(String ids, String technicianId,
			@SessionAttribute(Constance.USER_ID) String depathUserId) {
		Layui result = orderService.redepathOrder(ids, technicianId, depathUserId);

		return result;
	}
	
	@RequestMapping("/arrivalNotice")
	@ResponseBody
	public Layui arrivalNotice(String ids) {
		Layui result = orderService.arrivalNotice(ids);

		return result;
	}

	@RequestMapping("/finishOrders")
	@ResponseBody
	public Layui finishOrder(String ids, String userId) {
		Layui result = orderService.finishOrder(ids, userId);
		return result;
	}
	
	@RequestMapping("/refreshCount")
	@ResponseBody
	public Map<String,Integer> refreshCount() {
		return orderService.getDepathCount();
	}

	@RequestMapping("/download")
	public void download(String path, HttpServletResponse response) {
		
		FileUpDownLoadUtils.download(path, response);
	}
	
	@RequestMapping("/print")
    public ModelAndView print(String orderId) {
		Order order = orderService.getOrderById(Long.valueOf(orderId));
		String view = "printwuzi-servicenoprice";
		if(order.getUnit().getName().indexOf("设计") != -1){
			view = "printsheji-servicewithprice";
		}
		ModelAndView mov = new ModelAndView(view);
		Calendar calendar = Calendar.getInstance(); 
		mov.addObject("orderNo", order.getOrderNo());
		mov.addObject("year", calendar.get(Calendar.YEAR));
		mov.addObject("mon", calendar.get(Calendar.MONTH)+1);
		mov.addObject("day", calendar.get(Calendar.DATE));
		mov.addObject("cusName", order.getAddress());
		mov.addObject("description", order.getDescription());
		Float total = 0f;
		for(OrderPart p : order.getPartList()) {
			int i = 1;
			mov.addObject("part"+i, p.getName());
			mov.addObject("part"+i+"_number", p.getNum());
			mov.addObject("part"+i+"_single", p.getPartCost());
			mov.addObject("part"+i+"_total", p.getPartCost()*p.getNum());
			total += p.getPartCost()*p.getNum();
		}
		mov.addObject("total", total);
		mov.addObject("chitotal", WeiXinUtil.numberTOChiString(total));
		return mov;
	}
	
	@RequestMapping("/getTechOrder")
	@ResponseBody
	public Layui getTechOrder(String tech) {
		
		Layui result = orderService.getTechOrder(tech);
		return result;
	}
	
	@RequestMapping("/china.json")
	@ResponseBody
	public ModelAndView mapJson() {
		return new ModelAndView("440800.geoJson");
	}
	
	

}
