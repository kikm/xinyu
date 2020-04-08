package com.xinyu.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinyu.bean.Layui;
import com.xinyu.bean.PageBean;
import com.xinyu.dao.DeviceDao;
import com.xinyu.dao.PartDao;
import com.xinyu.model.Device;
import com.xinyu.model.Order;
import com.xinyu.model.OrderPart;
import com.xinyu.model.Part;
import com.xinyu.service.IDeviceService;
import com.xinyu.util.FileUpDownLoadUtils;

@Service
public class DeviceServiceImpl implements IDeviceService {

	@Autowired
	private DeviceDao deviceDao;
	
	@Autowired
	private PartDao partDao;
	
	@Override
	public Layui getDeviceList(PageBean pagebean, Device d){
		int count = deviceDao.getCountDeviceCriteriaQuery(d);
		pagebean.setCount(count);
		List<Device> data = deviceDao.getDeviceList(pagebean,d);
		for(Device temp : data) {
			String name = "";
			for(Part p : temp.getPartList()) {
				name += p.getName()+" , ";
			}
			if(StringUtils.isNotBlank(name)) {
				name = name.substring(0, name.length()-2);
			}
			temp.setPartListName(name);
		}
		return Layui.data("查询成功", 0, count, data);
	}

	@Override
	public Device getDeviceById(Long id) {
		Device d = deviceDao.getDeviceById(id);
		
		return d;
	}

	@Override
	@Transactional
	public Layui deleteDevice(String id) {
		if(StringUtils.isNotBlank(id)) {
			Long did = Long.valueOf(id);
			Device d = deviceDao.getDeviceById(did);
			List<Long> idList = new ArrayList<Long>();
			for(Part op : d.getPartList()) {
				idList.add(op.getId());
			}
			deviceDao.deleteDeviceById(Long.valueOf(id));
			if(idList.size() > 0) {
				partDao.deletePart(idList);
			}
 		}
		return Layui.data("删除成功", 0, 0, null);
	}

	@Override
	@Transactional
	public Layui saveOrUpdateDevice(Device d,String deletePart) {
		synchronized(d) {
        	try {
        		Boolean isNew = d.getId() == null?true:false;
        		deviceDao.saveOrUpdateDevice(d);
        		for(Part p : d.getPartList()) {
        			p.setDeviceId(d.getId());
        		}
        		if(isNew) {
             		partDao.savePart(d.getPartList());
             	}else {
             		List<Part> saveList = new ArrayList<Part>();
             		List<Part> updateList = new ArrayList<Part>();
             		for(Part op : d.getPartList()) {
             			if(op.getId() == null) {
             				saveList.add(op);
             			}else {
             				updateList.add(op);
             			}
             		}
             		if(saveList.size()>0) partDao.savePart(saveList);
             		if(updateList.size()>0) partDao.updatePart(updateList);
             		if(StringUtils.isNotBlank(deletePart)) {
             			List<Long> idList = new ArrayList<Long>();
             			List<String> list = Arrays.asList(deletePart.split(","));
             			for(String sid : list){
             				idList.add(Long.valueOf(sid));
             			}
             			partDao.deletePart(idList);
             		}
             	}
             } catch (Exception e) {
            	 e.printStackTrace();
            	 return Layui.data("保存设备错误",1, 0, null);
             }    
        	
		}
		
		
		
		return Layui.data("保存成功",0, 0, null);
	}
}
