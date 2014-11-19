package com.weiproduct.zenlead;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.weiproduct.zenlead.common.Utility;
import com.weiproduct.zenlead.dao.TaskDao;
import com.weiproduct.zenlead.dao.TaskDetailDao;
import com.weiproduct.zenlead.helper.TaskDetailListAdapter;
import com.weiproduct.zenlead.model.Task;
import com.weiproduct.zenlead.model.TaskDetail;

public class SaveTaskDetailActivity extends Activity {

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
		setContentView(R.layout.activity_task_detail);

		Intent intent = this.getIntent();
		tempTask = (Task) intent.getSerializableExtra("newTask");

		lbTaskName = (TextView) this.findViewById(R.id.lbTaskName);
		lbTaskTime = (TextView) this.findViewById(R.id.lbCreateTime);
		lbTotalPackage = (TextView) this.findViewById(R.id.lbTotalPackage);
		

		
		taskDetailListView = (ListView) this.findViewById(R.id.taskDetailList);

		lbTaskName.setText(tempTask.getTaskName());
		lbTaskTime.setText(Utility.getCurrentDateAndTime());
		lbTotalPackage.setText(String.valueOf(tempTask.getTaskDetailList()
				.size()));

		TaskDetailListAdapter taskDetailListAdapter = new TaskDetailListAdapter(
				this, R.layout.task_detail_list_item,
				tempTask.getTaskDetailList());
		taskDetailListView.setAdapter(taskDetailListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_detail, menu);
		return true;
	}

	public void btnStartTask(View view) {

		Task createdTask = new Task();

		// task Connection
		taskDao = new TaskDao(this);
		taskDao.open();

		// task detail Connetcion
		taskDetailDao = new TaskDetailDao(this);
		taskDetailDao.open();

		tempTask.setTime(Utility.getCurrentDateAndTime());
		tempTask.setTaskCount(tempTask.getTaskDetailList().size());
		createdTask = taskDao.createTask(tempTask);

		for (TaskDetail td : tempTask.getTaskDetailList()) {
			td.setTaskId(createdTask.getTaskId());

			taskDetailDao.createTaskDetail(td);
		}

		// FileInputStream templateInputStream = new
		// FileInputStream("batchsendgoodstempalte.xls");
		try {

			// //AssetFileDescriptor templateFile =
			// getResources().openRawResourceFd(R.raw.batchsendgoodstempalte);
			// //FileInputStream templateInputStream = (FileInputStream)
			// getResources().openRawResource(R.raw.batchsendgoodstempalte);
			//
			// FileInputStream templateInputStream = new
			// FileInputStream("batchsendgoodstempalte.xls");
			// HSSFWorkbook hssfWrokBookTemplateFile = new
			// HSSFWorkbook(templateInputStream);
			//
			// HSSFSheet hssfSheetTempalteFile =
			// hssfWrokBookTemplateFile.getSheetAt(0); // First Sheet
			//
			// int fristRow = 1;
			// for(TaskDetail td : tempTask.getTaskDetailList()) {
			// HSSFRow hssfRowTemplateFile =
			// hssfSheetTempalteFile.createRow(fristRow++);
			//
			// hssfRowTemplateFile.createCell(0).setCellValue(td.getOrderNum());
			// hssfRowTemplateFile.createCell(3).setCellValue(td.getTrackingNum());
			//
			// }
			//
			// FileOutputStream fos = new FileOutputStream(new
			// File(Utility.getExternalStorageFolder(), "test.xls"));
			// hssfWrokBookTemplateFile.write(fos);

			// InputStream is =
			// getResources().getAssets().open("batchsendgoodstemplate.xls");

			// Workbook wb = Workbook.getWorkbook(new
			// File("batchsendgoodstempalte.xls"));

			// Workbook wb = Workbook.getWorkbook(is);
			// WritableWorkbook book = Workbook.createWorkbook( new File(
			// Utility.getExternalStorageFolder() + "/test.xls"), wb);
			WritableWorkbook book = Workbook.createWorkbook(new File(Utility
					.getExternalStorageFolder()
					+ "/"
					+ tempTask.getTaskName()
					+ ".xls"));

			WritableSheet sheet = book.createSheet("Sheet1", 0);

			int fristRow = 0;
			for (TaskDetail td : tempTask.getTaskDetailList()) {

				if (fristRow == 0) {
					sheet.addCell(new Label(0, fristRow, "Order Number"));
					sheet.addCell(new Label(1, fristRow, "Delivery Status"));
					sheet.addCell(new Label(2, fristRow, "Logistics Company"));
					sheet.addCell(new Label(3, fristRow, "Tracking Number"));
					sheet.addCell(new Label(4, fristRow, "Remark"));

				}
				
				sheet.addCell(new Label(0, fristRow, td.getOrderNum()));
				sheet.addCell(new Label(1, fristRow, "Full Shipment"));
				sheet.addCell(new Label(2, fristRow, "China Post Air Mail"));
				sheet.addCell(new Label(3, fristRow, td.getTrackingNum()));
				sheet.addCell(new Label(4, fristRow, "All items shipped out."));

			}

			book.write();
			book.close();

		} catch (Exception e) {
			e.printStackTrace();

		}

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
