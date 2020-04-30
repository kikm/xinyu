package com.xinyu.model;

public class OrderPart extends Part{
	
	private Long opid;
	private String orderNo;
	private Long orderId;
	private Float offer;
	private Integer num = 1;
	
	
	public Long getOpid() {
		return opid;
	}
	public void setOpid(Long opid) {
		this.opid = opid;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Float getOffer() {
		return offer;
	}
	public void setOffer(Float offer) {
		this.offer = offer;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	

}
