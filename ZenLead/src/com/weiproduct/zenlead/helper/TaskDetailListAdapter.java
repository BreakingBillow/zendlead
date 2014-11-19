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
import com.weiproduct.zenlead.model.TaskDetail;

public class TaskDetailListAdapter extends ArrayAdapter {
	private int resourceId; 
	
	private TextView txtOrderNumber;
	private TextView txtTrackingNumber;
	
	public TaskDetailListAdapter(Context context, int resource, List<TaskDetail> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.resourceId = resource;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
		TaskDetail taskDetail = (TaskDetail)getItem(position);
		
		LinearLayout taskDetailListItem = new LinearLayout(getContext()); 
		String inflater = Context.LAYOUT_INFLATER_SERVICE;   
		LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);   
		vi.inflate(resourceId, taskDetailListItem, true);  
		
		
		txtOrderNumber = (TextView)taskDetailListItem.findViewById(R.id.txtOrderNumber);
		txtTrackingNumber = (TextView)taskDetailListItem.findViewById(R.id.txtTrackingNumber);
		
		txtOrderNumber.setText(taskDetail.getOrderNum());
		txtTrackingNumber.setText(taskDetail.getTrackingNum());
		
		return taskDetailListItem;
	}

}
