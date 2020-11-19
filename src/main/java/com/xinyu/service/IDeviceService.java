package com.xinyu.service;

import com.xinyu.bean.Layui;
import com.xinyu.bean.PageBean;
import com.xinyu.model.Device;

public interface IDeviceService {

	public Layui getDeviceList(PageBean pagebean,Device d);

	public Device getDeviceById(Long id);

	public Layui deleteDevice(String id);

	public Layui saveOrUpdateDevice(Device d, String deletePart);

}
