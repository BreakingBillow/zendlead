package com.weiproduct.zenlead.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.weiproduct.zenlead.model.MySQLiteHelper;


public class BaseDataSource {

	public SQLiteDatabase database;
	public MySQLiteHelper dbHelper;
	
	public BaseDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
}
