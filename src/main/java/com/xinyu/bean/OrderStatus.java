package com.xinyu.bean;

public enum OrderStatus {
	
	preOrder,Dispatched,MaintenanceFeedback,QuotedPrice,Sent,OrderConfirmed,Complete,Finish;

	public String getName(){

		return name();
	}
	
	public String getCnName() {
		switch (this) {
		case preOrder:
			return "预建工单";//-->待派
		case Dispatched:
			return "已派";//--待维修确认
		case MaintenanceFeedback:
			return "维修反馈";//-->待报价
		case QuotedPrice:
			return "报价";//-->待发送客户
		case Sent:
			return "已发送";//-->待客户确认
		case OrderConfirmed:
			return "确认工单";//-->待完结
		case Complete:
			return "维修完工";
		case Finish:
			return "已完结";
		default:
			return "无状态";
		}
	}

}
