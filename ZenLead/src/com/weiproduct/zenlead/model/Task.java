package com.weiproduct.zenlead.model;

import java.io.Serializable;
import java.util.List;

public class Task implements Serializable {

	private int taskId;
	private String taskName;
	private int taskCount;
	private String time;
	
	private List<TaskDetail> taskDetailList;
		
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<TaskDetail> getTaskDetailList() {
		return taskDetailList;
	}
	public void setTaskDetailList(List<TaskDetail> taskDetailList) {
		this.taskDetailList = taskDetailList;
	}

	
}
