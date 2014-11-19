package com.weiproduct.zenlead.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.weiproduct.zenlead.model.MySQLiteHelper;
import com.weiproduct.zenlead.model.TaskDetail;

public class TaskDetailDao extends BaseDataSource{
	
	private String[] allColumns = {
			MySQLiteHelper.TASK_DETAIL_COLUMN_TASKID,
			MySQLiteHelper.TASK_DETAIL_COLUMN_ORDERNUM,
			MySQLiteHelper.TASK_DETAIL_COLUMN_TRACKINGNUM
	};
	
	public TaskDetailDao(Context context) {
		super(context);
	}
	
	private TaskDetail cursorToTaskDetail(Cursor cursor) {
		TaskDetail taskDetail = new TaskDetail();
		
		taskDetail.setTaskId(cursor.getInt(0));
		taskDetail.setOrderNum(cursor.getString(1));
		taskDetail.setTrackingNum(cursor.getString(2));
		
		return taskDetail;		
	}
	
	public void createTaskDetail(TaskDetail taskDetail) {
		ContentValues values = new ContentValues();
		
		values.put(MySQLiteHelper.TASK_DETAIL_COLUMN_TASKID, taskDetail.getTaskId());
		values.put(MySQLiteHelper.TASK_DETAIL_COLUMN_ORDERNUM, taskDetail.getOrderNum());
		values.put(MySQLiteHelper.TASK_DETAIL_COLUMN_TRACKINGNUM, taskDetail.getTrackingNum());
		
		long insertId = database.insert(MySQLiteHelper.TABLE_TASK_DETAIL, null, values);
	
	}	
	
	public void updateTaskDetail(TaskDetail taskDetail) {
		ContentValues values = new ContentValues();
		
		values.put(MySQLiteHelper.TASK_DETAIL_COLUMN_TASKID, taskDetail.getTaskId());
		values.put(MySQLiteHelper.TASK_DETAIL_COLUMN_ORDERNUM, taskDetail.getOrderNum());
		values.put(MySQLiteHelper.TASK_DETAIL_COLUMN_TRACKINGNUM, taskDetail.getTrackingNum());
		
		database.update(MySQLiteHelper.TABLE_TASK_DETAIL, values, null, null);
	}
	
	public void deleteTaskDetail(TaskDetail taskDetail) {
		int id = taskDetail.getTaskId();
		database.delete(MySQLiteHelper.TABLE_TASK_DETAIL, MySQLiteHelper.TASK_DETAIL_COLUMN_TASKID + "=" + id, null);
	}
	
	public List<TaskDetail> getAllTaskDetail(TaskDetail taskDetail) {
		List<TaskDetail> taskDetails = new ArrayList<TaskDetail>();
		
		int id = taskDetail.getTaskId();		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TASK_DETAIL, allColumns, MySQLiteHelper.TASK_DETAIL_COLUMN_TASKID + "=" + id, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TaskDetail tempTaskDetail = cursorToTaskDetail(cursor);
			taskDetails.add(tempTaskDetail);
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return taskDetails;
		
	}
	
}
