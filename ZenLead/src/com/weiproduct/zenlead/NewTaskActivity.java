package com.weiproduct.zenlead;

import java.io.Serializable;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weiproduct.zenlead.common.Utility;
import com.weiproduct.zenlead.model.Task;

public class NewTaskActivity extends Activity {

	private EditText editTxtTaskName;
	private TextView txtCreateTime;
	
	private String currentTime;
	private Task newTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task_2);
		
	    ActionBar actionBar = getActionBar();    
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    newTask = new Task();
	    
	    editTxtTaskName = (EditText)this.findViewById(R.id.editTxtTaskName);
	    txtCreateTime = (TextView)this.findViewById(R.id.txtCreateTime);
	    
	    currentTime = Utility.getCurrentDateAndTime();
	    txtCreateTime.setText(currentTime);
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
		return true;
	}
	
	public void btnStartTask(View view) {
		if(editTxtTaskName.getText().toString().equals("")) {
			Utility.showToast(this, this.getString(R.string.namerequired));
			return;
		}
				
		Intent intent = new Intent(this, CaptureActivity.class);		
		Bundle bundle = new Bundle();

		newTask.setTaskName(editTxtTaskName.getText().toString());
		bundle.putSerializable("newTask", (Serializable) newTask);

		intent.putExtras(bundle);
		startActivity(intent);
	}

	public Task getNewTask() {
		return newTask;
	}

	public void setNewTask(Task newTask) {
		this.newTask = newTask;
	}
	
}
