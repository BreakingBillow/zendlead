package com.weiproduct.zenlead.model;

import java.io.Serializable;

public class TaskDetail implements Serializable {

	private int taskId;
	private String orderNum;
	private String trackingNum;
		
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getTrackingNum() {
		return trackingNum;
	}
	public void setTrackingNum(String trackingNum) {
		this.trackingNum = trackingNum;
	}
	
	
	
	
}
