package com.xinyu.bean;

public enum City {
	
	SZ,HZ,ZJ,HN;

	public String getName(){

		return name();
	}
	
	public String getCnName() {
		switch (this) {
		case SZ:
			return "深圳";//-->待派
		case HZ:
			return "惠州";//--待维修确认
		case ZJ:
			return "湛江";//-->待报价
		case HN:
			return "海南";//-->待发送客户
		default:
			return "湛江";
		}
	}

}
