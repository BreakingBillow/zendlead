package com.weiproduct.zenlead.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.weiproduct.zenlead.R;
import com.weiproduct.zenlead.model.TaskDetail;

public class Utility {

	public static int findInArray(String[] array, String element) {
		if (array == null) {
			return -1;
		}

		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(element)) {
				return i;
			}
		}

		return -1;
	}

	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
	
	/* Get folder path */	
	public static String getExternalStorageFolder() {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		path += Constant.EXTERNALSTORAGEFOLDER;
		
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		return path;
	}
	

	public static boolean checkNetworkConnection(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public static void hideKeyboard(Activity activity) {
		InputMethodManager inputManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

	}

	public static void showToastAtTop(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
		toast.show();
	}

	public static void showToast(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, -200);
		toast.show();
	}

	public static void showToastLong(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, -200);
		toast.show();
	}

	public static void dateAddByDay(Date date, int days) {
		date.setTime(date.getTime() + (long) days * 24l * 60l * 60l * 1000l);
	}

	public static String getCurrentDateAndTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());

	}

	public static String judgeBarcodeType(String inputValue, Context context ) {

		String generalOrderNumberRegEx = "[0-9]{14}";
		String chinaAirPostMailTrackingNumberRegEx = "R[A-Z][0-9]{9}CN";

		if (Pattern.compile(generalOrderNumberRegEx).matcher(inputValue).find()) {
			return context.getString(R.string.trackingNumber);
		} else if (Pattern.compile(chinaAirPostMailTrackingNumberRegEx)
				.matcher(inputValue).find()) {
			return context.getString(R.string.orderNumber);
		}

		return context.getString(R.string.nofound);
	}

	public static TaskDetail barcodeFilterInTaskDetail(TaskDetail taskDetail,
			String barcode) {
		String generalOrderNumberRegEx = "[0-9]{14}";
		String chinaAirPostMailTrackingNumberRegEx = "R[A-Z][0-9]{9}CN";

		if (Pattern.compile(generalOrderNumberRegEx).matcher(barcode).find()) {
			taskDetail.setOrderNum(barcode);
		} else if (Pattern.compile(chinaAirPostMailTrackingNumberRegEx)
				.matcher(barcode).find()) {
			taskDetail.setTrackingNum(barcode);
		}

		return taskDetail;
	}

}
