package com.xinyu.bean;

public class PageBean {

	private String order;//排序方式：desc、asc
	private String sortColumn;//用来排序字段名
	private int page;//当前页
	private int limit;//每页记录数
	private int count;//总记录数
	
	public PageBean(){
		order = "desc";
		sortColumn = "id";
		page = 1;
		limit = 10;
		count = 0;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int pageSize) {
		this.limit = pageSize;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public int getStart() {
		return (limit * (page - 1));
	}

	public int getEnd() {
		return limit * page;
	}


}
