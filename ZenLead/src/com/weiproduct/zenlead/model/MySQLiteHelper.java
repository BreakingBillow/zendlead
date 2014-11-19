package com.weiproduct.zenlead.model;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "ZenLead.db";
	private static final int DATABASE_VERSION = 3;
	
	// table name
	public static final String TABLE_TASK = "task";
	public static final String TABLE_TASK_DETAIL = "task_detail";
	
	// Table task
	public static final String TASK_COLUMN_ID = "id";
	public static final String TASK_COLUMN_NAME = "name";
	public static final String TASK_COLUMN_COUNT = "count";
	public static final String TASK_COLUMN_TIME = "time";
	
	private static final String DATABASE_CREATE_TASK = "create table if not exists "
			+  TABLE_TASK 
			+ "("
			+ TASK_COLUMN_ID
			+ " integer primary key autoincrement, "
			+ TASK_COLUMN_NAME
			+ " text, "
			+ TASK_COLUMN_COUNT
			+ " int, "
			+ TASK_COLUMN_TIME
			+ " text"
			+ ")";
	
	//Table task detail 	
	public static final String TASK_DETAIL_COLUMN_TASKID= "taskId";
	public static final String TASK_DETAIL_COLUMN_ORDERNUM = "orderNum";
	public static final String TASK_DETAIL_COLUMN_TRACKINGNUM = "trackingNum";
	
	private static final String DATABASE_CREATE_TASK_DETAIL = "create table if not exists "
			+ TABLE_TASK_DETAIL
			+ "("
			+ TASK_DETAIL_COLUMN_TASKID
			+ " integer, "
			+ TASK_DETAIL_COLUMN_ORDERNUM
			+ " text, "
			+ TASK_DETAIL_COLUMN_TRACKINGNUM
			+ " text, primary key ( "
			+ TASK_DETAIL_COLUMN_TASKID
			+ ", "
			+ TASK_DETAIL_COLUMN_ORDERNUM
			+", "
			+ TASK_DETAIL_COLUMN_TRACKINGNUM
			+ ")"
			+ ")";
		
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE_TASK);
		db.execSQL(DATABASE_CREATE_TASK_DETAIL);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_DETAIL);
		onCreate(db);
	}

}
