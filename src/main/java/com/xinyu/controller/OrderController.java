package com.xinyu.controller;

import java.util.ArrayList;
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

import com.xinyu.Constance;
import com.xinyu.bean.City;
import com.xinyu.bean.Layui;
import com.xinyu.bean.OrderStatus;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Device;
import com.xinyu.model.Order;
import com.xinyu.model.OrderPart;
import com.xinyu.model.Unit;
import com.xinyu.model.User;
import com.xinyu.service.IOrderService;
import com.xinyu.service.IUserService;
import com.xinyu.util.FileUpDownLoadUtils;
import com.xinyu.util.WeiXinUtil;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IUserService userService;

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
	public Layui getOrderData(PageBean pageBean, String startDate,String endDate, Long unitId, String status) {
		Order order = new Order();
		Unit u = new Unit();
		order.setUnit(u);
		if (unitId != null) {
			u.setId(unitId);
		}
		if (StringUtils.isNotEmpty(status)) {
			order.setStatus(OrderStatus.valueOf(status));
		}
		Layui result = orderService.getOrderData(pageBean, order,startDate,endDate);

		return result;
	}
	
	@RequestMapping("/finishOrderList")
	public ModelAndView getFinishOrderList() {
		ModelAndView mov = new ModelAndView("finishOrderList");
		List<Unit> unitList = orderService.getUnit();
		mov.addObject("unitList", unitList);
		mov.addObject("pageBean", new PageBean());
		return mov;
	}

	@RequestMapping("/finishOrderTableData")
	@ResponseBody
	public Layui getFinishOrderData(PageBean pageBean, String startDate,String endDate, Long unitId) {
		Order order = new Order();
		Unit u = new Unit();
		order.setUnit(u);
		if (unitId != null) {
			u.setId(unitId);
		}
		order.setStatus(OrderStatus.Finish);
		Layui result = orderService.getOrderData(pageBean, order,startDate,endDate);

		return result;
	}
	
	@RequestMapping("/getDepathCount")
	@ResponseBody
	public Layui getDepathCount() {
		Map<String ,Integer> count = orderService.getDepathCount();
		List<Map<String ,Integer>> list = new ArrayList<Map<String ,Integer>>();
		list.add(count);
		return Layui.data(null, 0, 0, list);
	}
	

	@RequestMapping("/getNewOrderNo")
	@ResponseBody
	public String getInitData() {

		String result = orderService.getNewOrderNo();

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
		User u = userService.getUserById(depathUserId);
		order.setCity(City.valueOf(u.getCity()));
		Layui result = orderService.saveOrUpdateOrder(params, order, deleteFile, deleteOrderPart);
		if((int)result.get("code") == 0) {
			orderService.quitePriceNoteic(order);
		}
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
		String msg = orderService.getOrderPartByIds(ids);

		return Layui.data(msg, 0, 0, null);
	}

	@RequestMapping("/deleteOrder")
	@ResponseBody
	public Layui deleteOrder(String id) {
		Layui result = orderService.deleteOrder(id);

		return result;
	}

	@RequestMapping("/depathOrders")
	@ResponseBody
	public Layui depathOrder(String ids, String technicianId,String assistTechs,
			@SessionAttribute(Constance.USER_ID) String depathUserId) {
		Layui result = orderService.depathOrder(ids, technicianId, depathUserId,assistTechs);
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
		if(order.getPartList().size()>0) {
			for(OrderPart p : order.getPartList()) {
				int i = 1;
				mov.addObject("part"+i, p.getName());
				mov.addObject("part"+i+"_number", p.getNum());
				mov.addObject("part"+i+"_single", p.getPartCost());
				mov.addObject("part"+i+"_total", p.getPartCost()*p.getNum());
				total += p.getPartCost()*p.getNum();
			}
		}else {
			mov.addObject("part1", order.getDevice().getName());
			mov.addObject("part1_number", 1);
			mov.addObject("part1_single", order.getUpkeep());
			mov.addObject("part1_total", order.getUpkeep());
			total += order.getUpkeep();
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
	
	@RequestMapping("/statistics")
	public ModelAndView statistics() {
		ModelAndView mov = new ModelAndView("statistics");
		return mov;
	}
	
	@RequestMapping("/china.json")
	@ResponseBody
	public ModelAndView mapJson() {
		return new ModelAndView("440800.geoJson");
	}
	
	

}
