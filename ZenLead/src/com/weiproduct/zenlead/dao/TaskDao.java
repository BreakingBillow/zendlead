package com.weiproduct.zenlead.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.weiproduct.zenlead.model.MySQLiteHelper;
import com.weiproduct.zenlead.model.Task;

public class TaskDao extends BaseDataSource {

	private String[] allColumns = {
			MySQLiteHelper.TASK_COLUMN_ID,
			MySQLiteHelper.TASK_COLUMN_NAME,
			MySQLiteHelper.TASK_COLUMN_COUNT,
			MySQLiteHelper.TASK_COLUMN_TIME
	};
	
	public TaskDao(Context context) {
		super(context);
	}
	
	
	private Task cursorToTask(Cursor cursor) {
		Task task = new Task();
		
		task.setTaskId(cursor.getInt(0));
		task.setTaskName(cursor.getString(1));
		task.setTaskCount(cursor.getInt(2));
		task.setTime(cursor.getString(3));
		
		return task;
	}
		
	public Task createTask(Task task) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.TASK_COLUMN_NAME, task.getTaskName());
		values.put(MySQLiteHelper.TASK_COLUMN_COUNT, task.getTaskCount());
		values.put(MySQLiteHelper.TASK_COLUMN_TIME, task.getTime());
		
		long insertId = database.insert(MySQLiteHelper.TABLE_TASK, null, values);
		
		Cursor cursor = database.query( MySQLiteHelper.TABLE_TASK, allColumns, MySQLiteHelper.TASK_COLUMN_ID + "=" + insertId, null,null, null, null);
		cursor.moveToFirst();
		Task newTask = cursorToTask(cursor);
		cursor.close();
		
		return newTask;		
	}
	
	public void updateTask(Task task) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.TASK_COLUMN_NAME, task.getTaskName());
		values.put(MySQLiteHelper.TASK_COLUMN_COUNT, task.getTaskCount());
		values.put(MySQLiteHelper.TASK_COLUMN_TIME, task.getTime());	
		
		database.update(MySQLiteHelper.TABLE_TASK, values, null, null);
	}
	
	
	public void deleteTask(Task task) {
		int id = task.getTaskId();		
		database.delete(MySQLiteHelper.TABLE_TASK, MySQLiteHelper.TASK_COLUMN_ID + "=" + id, null);
	}
	
	public List<Task> getAllTask() {
		List<Task> tasks = new ArrayList<Task>();	
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TASK, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task task = cursorToTask(cursor);
			tasks.add(task);
			cursor.moveToNext();
		}
		
		cursor.close();		
		return tasks;		
	}
	
}
