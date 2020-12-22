package com.xinyu.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.xinyu.bean.City;
import com.xinyu.bean.OrderStatus;

public class Order implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String orderNo;
	private Boolean isUrgent = false;
	private Boolean isRedepath = false;
	private Unit unit;
	private String contact;
	private String address;
	private String phone;
	private String email;
	private String facility;
	private String model;
	private String sn;
	private String description;
	private OrderStatus status;
	private Device device;
	private String report;
	private Float upkeep;
	private Date completeDate;
	private List<OrderPart> partList;
	private String imageUrls;
	private String depathUser;
	private String technician;
	private Date depathDate;
	private Date feedbackDate;
	private String confirmUser;
	private String confirmDate;
	private Date createDate;
	private Date arrivalDate;
	private Date dtdDate;
	private String techName;
	private String confirmOpinion;
	private City city;
	private String assistTechs;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public Float getUpkeep() {
		return upkeep;
	}
	public void setUpkeep(Float upkeep) {
		this.upkeep = upkeep;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public List<OrderPart> getPartList() {
		return partList;
	}
	public void setPartList(List<OrderPart> partList) {
		this.partList = partList;
	}
	public String getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}
	public String getDepathUser() {
		return depathUser;
	}
	public void setDepathUser(String depathUser) {
		this.depathUser = depathUser;
	}
	public String getTechnician() {
		return technician;
	}
	public void setTechnician(String technician) {
		this.technician = technician;
	}
	public Date getDepathDate() {
		return depathDate;
	}
	public void setDepathDate(Date depathDate) {
		this.depathDate = depathDate;
	}
	public Date getFeedbackDate() {
		return feedbackDate;
	}
	public void setFeedbackDate(Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}
	public Boolean getIsUrgent() {
		return isUrgent;
	}
	public void setIsUrgent(Boolean isUrgent) {
		this.isUrgent = isUrgent;
	}
	public String getConfirmUser() {
		return confirmUser;
	}
	public void setConfirmUser(String confirmUser) {
		this.confirmUser = confirmUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Boolean getIsRedepath() {
		return isRedepath;
	}
	public void setIsRedepath(Boolean isRedepath) {
		this.isRedepath = isRedepath;
	}
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public String getTechName() {
		return techName;
	}
	public void setTechName(String techName) {
		this.techName = techName;
	}
	public Date getDtdDate() {
		return dtdDate;
	}
	public void setDtdDate(Date dtdDate) {
		this.dtdDate = dtdDate;
	}
	public String getConfirmOpinion() {
		return confirmOpinion;
	}
	public void setConfirmOpinion(String confirmOpinion) {
		this.confirmOpinion = confirmOpinion;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public String getAssistTechs() {
		return assistTechs;
	}
	public void setAssistTechs(String assistTechs) {
		this.assistTechs = assistTechs;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
