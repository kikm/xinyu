package com.xinyu.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xinyu.bean.Layui;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Device;
import com.xinyu.service.IDeviceService;

@Controller
@RequestMapping("/device")
public class DeviceController {
	
	@Autowired
	private IDeviceService deviceService;
	
	
	@RequestMapping("/deviceList")
    public ModelAndView getOrderList() {
		ModelAndView mov = new ModelAndView();
		
		mov.setViewName("deviceList");
		
        return mov;
    }
	
	@RequestMapping("/deviceTableData")
	@ResponseBody
    public Layui getDeviceData(PageBean pageBean,String deviceName) {
		Device d = new Device();
		if(StringUtils.isNotEmpty(deviceName)){
			d.setName(deviceName);
		}
		Layui result = deviceService.getDeviceList(pageBean, d);
		
        return result;
    }
	
	
	
	@RequestMapping("/saveOrUpdateDevice")
	@ResponseBody
    public Layui saveOrUpdateDevice(Device d,String deletePart) {
		Layui result = deviceService.saveOrUpdateDevice(d, deletePart);
		
        return result;
    }
	
	@RequestMapping("/getDeviceInfo")
	@ResponseBody
    public Device getDeviceInfo(Long id) {
		Device result = deviceService.getDeviceById(id);
		
        return result;
    }
	
	@RequestMapping("/deleteDevice")
	@ResponseBody
    public Layui deleteDevice(String id) {
		Layui result = deviceService.deleteDevice(id);
		
        return result;
    }
	

}
