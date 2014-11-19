package com.weiproduct.zenlead;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.weiproduct.zenlead.dao.TaskDao;
import com.weiproduct.zenlead.helper.TaskListAdapter;
import com.weiproduct.zenlead.model.Task;

public class MainActivity extends Activity {

	private TaskDao taskDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView taskListView = (ListView) this.findViewById(R.id.taskList);

		// Connection
		taskDao = new TaskDao(this);
		taskDao.open();

		List<Task> tasks = taskDao.getAllTask();
		TaskListAdapter taskListAdapter = new TaskListAdapter(this,
				R.layout.task_list_item, tasks);
		taskListView.setAdapter(taskListAdapter);

		taskListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ListView listView = (ListView) parent;

				Task taskInList = (Task) listView.getItemAtPosition(position);

				// Intent intent = new Intent(this, TaskDetailActivity.class);
				taskDetailPage(taskInList);

			}

		});

		Log.d("SDcard·��", Environment.getExternalStorageDirectory()
				.getAbsolutePath());

	}

	public void taskDetailPage(Task task) {
		Intent intent = new Intent(this, TaskDetailActivity.class);

		Bundle bundle = new Bundle();
		bundle.putSerializable("newTask", (Serializable) task);

		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);

		//Associate searchable configuration with the SearchView 
//		SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE); 
//		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//		searchView.setSearchableInfo(
//		searchManager.getSearchableInfo(getComponentName()));

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_new:
			// do something here
			Log.d("Main Activity", "New task item clicked");
			createNewTask();
			return true;

		case R.id.action_search:
			Log.d("Main Activity", "Search item clicked");

			// jumpToSearch();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void createNewTask() {
		Intent intent = new Intent(this, NewTaskActivity.class);
		startActivity(intent);
	}

	public void jumpToSearch() {
		Intent intent = new Intent(this, NewTaskActivity.class);
		startActivity(intent);
	}

}
