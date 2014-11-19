package com.weiproduct.zenlead.helper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiproduct.zenlead.R;
import com.weiproduct.zenlead.model.Task;

public class TaskListAdapter extends ArrayAdapter {
	private int resourceId; 	
	
	private TextView txtTaskName;
	private TextView txtTaskTime;
	private TextView txtTaskCount;
	
	public TaskListAdapter(Context context, int resource, List<Task> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub		
		this.resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		Task task = (Task) getItem(position);
		
		LinearLayout taskListItem = new LinearLayout(getContext());  
		String inflater = Context.LAYOUT_INFLATER_SERVICE;   
		LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);   
		vi.inflate(resourceId, taskListItem, true);  
		
		txtTaskName = (TextView)taskListItem.findViewById(R.id.txtTaskName);
		txtTaskTime = (TextView)taskListItem.findViewById(R.id.txtTaskTime);
		txtTaskCount = (TextView)taskListItem.findViewById(R.id.txtTaskCount);
		
		txtTaskName.setText(task.getTaskName());
		txtTaskTime.setText(task.getTime());
		txtTaskCount.setText(String.valueOf(task.getTaskCount()));
		
		return taskListItem;		
	}
	
	
	
}
