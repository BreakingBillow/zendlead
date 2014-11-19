package com.weiproduct.zenlead;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.weiproduct.zenlead.camera.CameraManager;
import com.weiproduct.zenlead.common.Utility;
import com.weiproduct.zenlead.decode.CaptureActivityHandler;
import com.weiproduct.zenlead.decode.InactivityTimer;
import com.weiproduct.zenlead.helper.TaskDetailListAdapter;
import com.weiproduct.zenlead.model.Task;
import com.weiproduct.zenlead.model.TaskDetail;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 
 * 时间: 2014年5月9日 下午12:25:31
 * 
 * 版本: V_1.0.0
 * 
 * 描述: 扫描界面
 */
public class CaptureActivity extends Activity implements Callback,
		View.OnClickListener {

	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private boolean isNeedCapture = false;

	// Added for modification
	private Task newTask;
	private List newTaskDetailList;
	private TaskDetail newTaskDetail;
	
	private String temp = "";

	private String dialogPackageCount;

	private final String generalOrderNumberRegEx = "[0-9]{14}";
	private final String chinaAirPostMailTrackingNumberRegEx = "R[A-Z][0-9]{9}CN";


	private TaskDetailListAdapter taskDetailListAdapter;

	//

	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qr_scan);

		newTaskDetailList = new ArrayList();
		newTaskDetail = new TaskDetail();

		Intent intent = this.getIntent();
		newTask = (Task) intent.getSerializableExtra("newTask");
		newTask.setTaskDetailList(newTaskDetailList);

		ListView taskListView = (ListView) this
				.findViewById(R.id.taskDetailList);
		taskDetailListAdapter = new TaskDetailListAdapter(this,
				R.layout.task_detail_list_item, newTask.getTaskDetailList());
		taskListView.setAdapter(taskDetailListAdapter);

		// 监听图片识别按钮
		findViewById(R.id.capture_scan_photo).setOnClickListener(this);
		findViewById(R.id.capture_flashlight).setOnClickListener(this);
		findViewById(R.id.capture_button_finish).setOnClickListener(this);

		// 初始化 CameraManager
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);

		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE,
				0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(final String result) {

		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		
		temp = result;

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		dialog.setMessage(result + Utility.judgeBarcodeType(result, CaptureActivity.this));

		if (Pattern.compile(generalOrderNumberRegEx).matcher(result).find()) {
			if (newTaskDetail.getOrderNum() == null) {

				newTaskDetail = Utility.barcodeFilterInTaskDetail(
						newTaskDetail, result);

				dialog.setNegativeButton(this.getString(R.string.getTrackingNo),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								handler.sendEmptyMessage(R.id.restart_preview);
							}
						});

			} else {

				if (newTaskDetail.getTrackingNum() != null) {
					newTaskDetailList.add(newTaskDetail);
					newTaskDetail = new TaskDetail();

					newTaskDetail = Utility.barcodeFilterInTaskDetail(
							newTaskDetail, result);

					dialog.setNegativeButton(this.getString(R.string.getTrackingNo),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									handler.sendEmptyMessage(R.id.restart_preview);
								}
							});

				} else {
					dialog.setNegativeButton(this.getString(R.string.ignoreRepeat),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									handler.sendEmptyMessage(R.id.restart_preview);
								}
							});
				}

			}

		} else if (Pattern.compile(chinaAirPostMailTrackingNumberRegEx)
				.matcher(result).find()) {
			if (newTaskDetail.getTrackingNum() == null) {

				newTaskDetail = Utility.barcodeFilterInTaskDetail(
						newTaskDetail, result);

				dialog.setNegativeButton(this.getString(R.string.getOrderNo),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								handler.sendEmptyMessage(R.id.restart_preview);

							}
						});

			} else {

				if (newTaskDetail.getOrderNum() != null) {
					newTaskDetailList.add(newTaskDetail);
					newTaskDetail = new TaskDetail();

					newTaskDetail = Utility.barcodeFilterInTaskDetail(
							newTaskDetail, result);

					dialog.setNegativeButton(this.getString(R.string.getOrderNo),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									handler.sendEmptyMessage(R.id.restart_preview);

								}
							});
				} else {
					dialog.setNegativeButton(this.getString(R.string.ignoreRepeat),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									handler.sendEmptyMessage(R.id.restart_preview);
								}
							});
				}
			}

		}

		dialog.setPositiveButton(this.getString(R.string.wrong),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// finish();
						dialog.dismiss();
						handler.sendEmptyMessage(R.id.restart_preview);

					}
				});

		dialogPackageCount = this.getString(R.string.txtPackage) + (newTaskDetailList.size() + 1);
		dialog.setTitle(dialogPackageCount);

		dialog.create().show();
		// Toast.makeText(getApplicationContext(), result,
		// Toast.LENGTH_SHORT).show();

		// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
		// handler.sendEmptyMessage(R.id.restart_preview);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width
					/ mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height
					/ mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);

		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(CaptureActivity.this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	public void taskDetailPage() {

		newTask.setTaskDetailList(newTaskDetailList);

		Intent intent = new Intent(this, SaveTaskDetailActivity.class);
		Bundle bundle = new Bundle();

		bundle.putSerializable("newTask", (Serializable) newTask);

		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.capture_scan_photo: // 图片识别
			// 打开手机中的相册
			// Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //
			// "android.intent.action.GET_CONTENT"
			// innerIntent.setType("image/*");
			// Intent wrapperIntent = Intent.createChooser(innerIntent,
			// "选择二维码图片");
			// this.startActivityForResult(wrapperIntent, REQUEST_CODE);
			break;

		case R.id.capture_flashlight:
			if (flag) {
				CameraManager.get().offLight(); // 关闭闪光灯
				flag = false;
			} else {
				CameraManager.get().openLight(); // 打开闪光灯
				flag = true;
			}
			break;

		case R.id.capture_button_finish:

			newTaskDetail = Utility.barcodeFilterInTaskDetail(newTaskDetail, temp);	
			newTaskDetailList.add(newTaskDetail);
			
			newTask.setTaskDetailList(newTaskDetailList);

			// Go to saving page
			taskDetailPage();

			break;
		default:
			break;
		}
	}
}