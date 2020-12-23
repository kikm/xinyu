package com.xinyu.bean;

import com.xinyu.model.Order;

public class OrderBean extends Order {

	public String statusName;
	public String deviceName;
	public String unitName;
	public Float total;
	public Integer partListSize;
	public String partListDes;
	public String phone;
	public String createTime;
	
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public Integer getPartListSize() {
		return partListSize;
	}
	public void setPartListSize(Integer partListSize) {
		this.partListSize = partListSize;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPartListDes() {
		return partListDes;
	}
	public void setPartListDes(String partListDes) {
		this.partListDes = partListDes;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	
	
	
	
	
	
}
