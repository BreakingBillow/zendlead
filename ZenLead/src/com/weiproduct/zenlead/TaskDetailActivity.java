package com.weiproduct.zenlead;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.weiproduct.zenlead.common.Utility;
import com.weiproduct.zenlead.dao.TaskDao;
import com.weiproduct.zenlead.dao.TaskDetailDao;
import com.weiproduct.zenlead.helper.TaskDetailListAdapter;
import com.weiproduct.zenlead.model.Task;
import com.weiproduct.zenlead.model.TaskDetail;

public class TaskDetailActivity extends Activity {
	
	private Task tempTask;

	private TextView lbTaskName;
	private TextView lbTaskTime;
	private TextView lbTotalPackage;

	private ListView taskDetailListView;

	private TaskDao taskDao;
	private TaskDetailDao taskDetailDao;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail_track);
		
	    ActionBar actionBar = getActionBar();    
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		// task Connection
		taskDao = new TaskDao(this);
		taskDao.open();

		// task detail Connetcion
		taskDetailDao = new TaskDetailDao(this);
		taskDetailDao.open();
		
		
		Intent intent = this.getIntent();
		tempTask = (Task) intent.getSerializableExtra("newTask");
		
		TaskDetail tempTaskDetail = new TaskDetail();
		tempTaskDetail.setTaskId(tempTask.getTaskId());
		
		lbTaskName = (TextView) this.findViewById(R.id.lbTaskName);
		lbTaskTime = (TextView) this.findViewById(R.id.lbCreateTime);
		lbTotalPackage = (TextView) this.findViewById(R.id.lbTotalPackage);

		taskDetailListView = (ListView) this.findViewById(R.id.taskDetailList);

		lbTaskName.setText(tempTask.getTaskName());
		lbTaskTime.setText(Utility.getCurrentDateAndTime());
		lbTotalPackage.setText(String.valueOf(taskDetailDao.getAllTaskDetail(tempTaskDetail).size()));

		TaskDetailListAdapter taskDetailListAdapter = new TaskDetailListAdapter(
				this, R.layout.task_detail_list_item,
				taskDetailDao.getAllTaskDetail(tempTaskDetail));
		taskDetailListView.setAdapter(taskDetailListAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_detail, menu);
		return true;
	}

}
