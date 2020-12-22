package com.xinyu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xinyu.bean.PageBean;
import com.xinyu.model.Device;

@Mapper
public interface DeviceDao {

	public List<Device> getAllDevice();
	
	public List<Device> getDeviceList(@Param("page")PageBean pagebean, @Param("device")Device d);
	
	public Integer getCountDeviceCriteriaQuery(@Param("device")Device dev);

	public void saveOrUpdateDevice(@Param("device")Device d);

	public void deleteDeviceById(Long valueOf);

	public Device getDeviceById(Long id);
	
	public void addDeviceFrequentCount(Long id);
}
