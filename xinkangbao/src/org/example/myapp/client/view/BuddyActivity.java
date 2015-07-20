package org.example.myapp.client.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.example.myapp.R;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.client.model.VersionInfo;
import org.example.myapp.client.network.YQClient;
import org.example.myapp.common.MyTime;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.common.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BuddyActivity extends Activity {

	BroadcastMain broadcastMain;

	ReturnObj ret_obj = null;

	private ListView listview_my_docs = null;

	private ListView listview_all_docs = null;

	private Button frame_btn_my_docs = null;

	private Button frame_btn_all_docs = null;

	private String new_code;

	private String new_name;

	public String buddyStr = "";

	AlertDialog.Builder builder;
	
	public static List<Doctor> buddyEntityList = new ArrayList<Doctor>();// 好友列表

	public static BuddyAdapter ba = null;// 好友列表的adapter

	public static YQClient new_http_client = null;

	public final static int CATALOG_ALL = 1;

	public final static int CATALOG_MY = 2;

	private int curDocCatalog = CATALOG_MY;
	
	private String curVersionCode;

	private String curVersionName;

	private List<VersionInfo> versionInfos;

	private Dialog downloadDialog;
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/xinkangbao/";

	private static final String saveFileName = savePath
			+ "xinkangbao.apk";

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};
	Doctor temp;

	public void paser_doc_list(final int catalog) {
		if (new_http_client == null) {
			return;
		}

		if (catalog == CATALOG_ALL) {
			new Runnable() {
				public void run() {
					ret_obj = new_http_client.get_all_doc_list(0);
				}
			}.run();
		} else {
			new Runnable() {
				public void run() {
					ret_obj = new_http_client
							.get_user_doc_list(MainActivity.myUser.getId());
				}
			}.run();
		}
		if (null != ret_obj && ret_obj.getRet_code() == 0) {
			buddyStr = ret_obj.getOrg_str();
			buddyEntityList = Doctor.paser_str_to_objlist(buddyStr);
			if (MainActivity.progressDialog != null
					&& MainActivity.progressDialog.isShowing()) {
				MainActivity.progressDialog.dismiss();
				MainActivity.progressDialog = null;
			}
		} else {
			buddyStr = "";
			if (MainActivity.progressDialog.isShowing()
					&& MainActivity.progressDialog != null) {
				MainActivity.progressDialog.dismiss();
				MainActivity.progressDialog = null;
			}
		}
	}

	public static void get_user_doc_msg_toread_read() {

		ReturnObj ret_obj = new_http_client
				.get_user_doc_msg_toread_list(MainActivity.myUser.getId());

		if (ret_obj.getRet_code() == 0) {
			for (int idx = 0; idx < buddyEntityList.size(); idx++) {
				Doctor doc = buddyEntityList.get(idx);
				doc.setMes_to_read(0);
				buddyEntityList.set(idx, doc);
			}

			try {
				JSONTokener jsonParser = new JSONTokener(ret_obj.getOrg_str());
				JSONObject ret = (JSONObject) (jsonParser.nextValue());
				JSONArray doc_list = ret.getJSONArray("list");
				int length = doc_list.length();

				for (int i = 0; i < length; i++) {
					JSONObject oj_tmp = doc_list.getJSONObject(i);

					int count = Integer.parseInt(oj_tmp.getString("unreadcnt"));
					Long tel = Long.parseLong(oj_tmp.getString("sendertel"));

					String place = "["
							+ Thread.currentThread().getStackTrace()[2]
									.getFileName()
							+ " "
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName()
							+ ":"
							+ Thread.currentThread().getStackTrace()[2]
									.getLineNumber() + "] ";

					for (int idx = 0; idx < buddyEntityList.size(); idx++) {
						if ((buddyEntityList.get(idx).getDoc_id()).equals(tel) == true) {
							Doctor doc = buddyEntityList.get(idx);
							doc.setMes_to_read(count);
							buddyEntityList.set(idx, doc);
						}
					}
				}
				// 冒泡排序。选择消息最多的放前面；
				for (int i = 0; i < buddyEntityList.size() - 1; i++) {
					int max = buddyEntityList.get(i).getMes_to_read();
					int max_idx = i;
					for (int j = i + 1; j < buddyEntityList.size(); j++) {
						int cur = buddyEntityList.get(j).getMes_to_read();
						if (cur > max) {
							max = cur;
							max_idx = j;
						}

					}

					if (max == 0) {
						break;
					}
					if (max_idx != i) {
						Doctor doc_tmp = buddyEntityList.get(max_idx);
						buddyEntityList.set(max_idx, buddyEntityList.get(i));
						buddyEntityList.set(i, doc_tmp);
					}
				}
			} catch (Exception e) {
				// Toast.makeText(BuddyActivity.this, "更新未读消息失败! " +
				// e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		} else {
			// Toast.makeText(BuddyActivity.this, "获取未读未读消息失败! " +
			// ret_obj.getMsg(), Toast.LENGTH_SHORT).show();
		}
	}

	public void updateInfo() {
		TextView tel = (TextView) findViewById(R.id.buddy_top_tel);
		TextView name = (TextView) findViewById(R.id.buddy_top_name);

		name.setText(MainActivity.myUser.getName());
		tel.setText(Long.toString(MainActivity.myUser.getId()));
	}

	/*
	 * protected void onResume() { updateInfo(); LoadDocList(curDocCatalog); if
	 * (new_http_client != null) { get_user_doc_msg_toread_read(); }
	 * 
	 * String place = "[" +
	 * Thread.currentThread().getStackTrace()[2].getFileName() + " " +
	 * Thread.currentThread().getStackTrace()[2].getMethodName() + ":" +
	 * Thread.currentThread().getStackTrace()[2].getLineNumber()+"] ";
	 * Log.v("mark", place + "  onresume time:" + MyTime.geTime());
	 * 
	 * Intent intent = new Intent(); intent.setClass(this, BuddyService.class);
	 * startService(intent);
	 * 
	 * broadcastMain = new BroadcastMain(); IntentFilter filter = new
	 * IntentFilter(); filter.addAction( BuddyService.BROADCASTACTION );
	 * registerReceiver( broadcastMain, filter); ba.notifyDataSetChanged();
	 * super.onResume(); } protected void onPause() { String place = "[" +
	 * Thread.currentThread().getStackTrace()[2].getFileName() + " " +
	 * Thread.currentThread().getStackTrace()[2].getMethodName() + ":" +
	 * Thread.currentThread().getStackTrace()[2].getLineNumber()+"] ";
	 * Log.i("wangbo debug", place + "  onpause time:" + MyTime.geTime());
	 * 
	 * Intent intent = new Intent(); intent.setClass(this, BuddyService.class);
	 * stopService(intent); unregisterReceiver(broadcastMain); super.onPause();
	 * } protected void onStop() {
	 * 
	 * super.onStop(); }
	 */
	ListView listView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_buddy);
		ManageActivity.addActiviy("buddyActivity", BuddyActivity.this);
		if (new_http_client == null) {
			new_http_client = new YQClient(true);
		}
		new AsyncCheckVersion().execute("");
		initFrameButton();
		initFrameListView();
		LoadDocList(curDocCatalog);
	}

	private void LoadDocList(int catalog) {
		paser_doc_list(catalog);
		ListView curlv = CATALOG_ALL == curDocCatalog ? listview_all_docs
				: listview_my_docs;
		ba = new BuddyAdapter(this, buddyEntityList);
		curlv.setAdapter(ba);
	}

	private void initFrameButton() {
		frame_btn_my_docs = (Button) findViewById(R.id.frame_btn_my_docs);
		frame_btn_all_docs = (Button) findViewById(R.id.frame_btn_all_docs);

		// 设置首选择项
		frame_btn_my_docs.setEnabled(false);
		frame_btn_my_docs.setOnClickListener(frameDocBtnClick(
				frame_btn_my_docs, CATALOG_MY));
		frame_btn_all_docs.setOnClickListener(frameDocBtnClick(
				frame_btn_all_docs, CATALOG_ALL));

	}

	private void initFrameListView() {
		listview_all_docs = (ListView) findViewById(R.id.listview_all_docs);
		setListViewListener(listview_all_docs);
		listview_my_docs = (ListView) findViewById(R.id.listview_my_docs);
		setListViewListener(listview_my_docs);
	}

	private View.OnClickListener frameDocBtnClick(final Button btn,
			final int catalog) {
		return new View.OnClickListener() {

			public void onClick(View v) {
				if (btn == frame_btn_my_docs) {
					frame_btn_my_docs.setEnabled(false);
					frame_btn_all_docs.setEnabled(true);
					listview_my_docs.setVisibility(View.VISIBLE);
					listview_all_docs.setVisibility(View.GONE);

				} else {
					frame_btn_my_docs.setEnabled(true);
					frame_btn_all_docs.setEnabled(false);
					listview_my_docs.setVisibility(View.GONE);
					listview_all_docs.setVisibility(View.VISIBLE);
					if (MainActivity.progressDialog != null) {
						MainActivity.progressDialog.show();
					} else {
						new MainActivity()
								.initProgressDialog(BuddyActivity.this);
						MainActivity.progressDialog.show();
					}
				}
				curDocCatalog = catalog;
				LoadDocList(curDocCatalog);
			}
		};
	}

	// listView的Item点击事件
	private void setListViewListener(final ListView listview) {
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long l) 
			{
				temp = (Doctor) listview.getItemAtPosition(position);
				// 打开聊天页面
				Intent intent = new Intent(BuddyActivity.this,
						DoctorInfoActivity.class);
				intent.putExtra("doccatalog", curDocCatalog);
				intent.putExtra("tel", temp.getDoc_id());
				intent.putExtra("name", temp.getName());
				intent.putExtra("major", temp.getMajor());
				intent.putExtra("isonline", temp.getIsOnline());
				startActivity(intent);
			}
		});
	}

	public boolean onContextItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case 0:
			// 打开聊天页面
			Intent intent = new Intent(BuddyActivity.this, ChatActivity.class);
			intent.putExtra("tel", temp.getDoc_id());
			intent.putExtra("name", temp.getName());
			intent.putExtra("major", temp.getMajor());
			intent.putExtra("isonline", temp.getIsOnline());
			startActivity(intent);
			break;
		case 1:
			// 向服务器发送一个删除好友的包
			ReturnObj obj = MainActivity.client_in_strict_mode.del_user_doc(
					MainActivity.myUser.getId(), temp.getDoc_id());
			if (obj.getRet_code() != 0) {
				Toast.makeText(BuddyActivity.this, "删除医生失败! " + obj.getMsg(),
						Toast.LENGTH_SHORT).show();
			}
			// 删除好友列表中的该好友
			for (int i = 0; i < buddyEntityList.size(); i++) {
				if ((buddyEntityList.get(i).getDoc_id()) == temp.getDoc_id()) {
					buddyEntityList.remove(i);
				}
			}
			listview_all_docs = (ListView) findViewById(R.id.listview);
			ba = new BuddyAdapter(this, buddyEntityList);
			listview_all_docs.setAdapter(ba);
			break;
		case 2:
			int ret_code = display_doc_info();
			if (ret_code != 0) {
				Toast.makeText(BuddyActivity.this, "获取医生详细信息失败",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case 3:
			// 新建一个页面
			Intent intent_yuyue = new Intent(BuddyActivity.this,
					DoctorOrderActivity.class);
			intent_yuyue.putExtra("tel", temp.getDoc_id());
			intent_yuyue.putExtra("name", temp.getName());
			intent_yuyue.putExtra("major", temp.getMajor());
			intent_yuyue.putExtra("isonline", temp.getIsOnline());
			startActivity(intent_yuyue);

			break;
		}
		return super.onContextItemSelected(item);
	}

	private int display_doc_info() {

		final EditText et = new EditText(BuddyActivity.this);

		Doctor new_temp = Doctor
				.paser_str_to_obj(MainActivity.client_in_strict_mode
						.get_doc_info(temp.getDoc_id()));
		if (new_temp != null) {
			et.setKeyListener(null);
			et.setText("电话 : " + Long.toString(new_temp.getDoc_id()) + "\n医院: "
					+ new_temp.getHospital() + "\n科室: "
					+ new_temp.getDepartment() + "\n职务: " + new_temp.getJob()
					+ "\n主治: " + new_temp.getMajor() + "\n性别: "
					+ new_temp.getSex() + "\n年龄: " + new_temp.getAge()
					+ "\nemail: " + new_temp.getMail());
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					BuddyActivity.this);
			dialog.setTitle("医生" + new_temp.getName() + "详细信息");
			dialog.setView(et);
			dialog.setNegativeButton("关闭", null);
			dialog.show();
		} else {
			return -1;

		}
		return 0;
	}

	private List<Doctor> jieXi(String str) {
		buddyEntityList.clear();
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONObject ret = (JSONObject) (jsonParser.nextValue());
			JSONArray doc_list = ret.getJSONArray("list");
			int length = doc_list.length();
			for (int i = 0; i < length; i++) {
				JSONObject oj_tmp = doc_list.getJSONObject(i);
				Doctor doc_tmp = new Doctor();
				doc_tmp.setName(oj_tmp.getString("name"));
				Log.i("mark", "current_item: " + Integer.toString(i) + " name:"
						+ oj_tmp.getString("name"));
				doc_tmp.setSex(oj_tmp.getString("sex"));
				doc_tmp.setDoc_id((Long.parseLong(oj_tmp.getString("tel"))));
				doc_tmp.setHospital(oj_tmp.getString("hospital"));
				doc_tmp.setDepartment(oj_tmp.getString("department"));
				doc_tmp.setJob(oj_tmp.getString("job"));
				doc_tmp.setMajor(oj_tmp.getString("attending"));
				doc_tmp.setMail(oj_tmp.getString("email"));
				buddyEntityList.add(doc_tmp);
			}

		} catch (JSONException e) {
			buddyEntityList.clear();
		}
		return buddyEntityList;
	}

	public class BroadcastMain extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("mark", "handleMessage time:" + MyTime.geTime());
			ba.notifyDataSetChanged();
		}
	}

	/**
	 * 异步：获取最新版本
	 * */
	class AsyncCheckVersion extends AsyncTask {
		@Override
		protected Object doInBackground(Object... arg0) {
			versionInfos = new YQClient().getNewestVersion();
			if (versionInfos.size()> 0)
			{
				for (int i = 0; i < versionInfos.size(); i++)
				{
					new_code= versionInfos.get(i).getVersionCode();
					new_name = versionInfos.get(i).getVersionName();
					versionInfos.get(i).getUpdateLog();
					versionInfos.get(i).getDownloadUrl();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			/**
			 * 获取版本号
			 */
			try {
				PackageInfo pack_info = getApplication().getPackageManager()
						.getPackageInfo(getApplication().getPackageName(), 0);
				curVersionName = pack_info.versionName;
				curVersionCode = String.valueOf(pack_info.versionCode);
			} catch (Exception e) {

			}
			// 版本对比
			if (!curVersionCode.equals(new_code)
					&& !curVersionName.equals(new_name)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						BuddyActivity.this);
				builder.setTitle("提示:");
				builder.setMessage("发现新版本,安装新版本可以享受最新的功能哦~");
				builder.setNegativeButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						showDownloadDialog();
					}
				});
				builder.setPositiveButton("下次再说", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
					}
				});
				builder.show();
			}
		}
	}

	private void showDownloadDialog() {
		builder = new Builder(BuddyActivity.this);
		builder.setCancelable(false);
		builder.setTitle("软件版本更新");
		final LayoutInflater inflater = LayoutInflater.from(BuddyActivity.this);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			if (versionInfos.size() > 0 && !StringUtils.isEmpty(versionInfos.get(0).getDownloadUrl()))
			{
				
			}
			try {
				URL url = new URL(versionInfos.get(0).getDownloadUrl());
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		BuddyActivity.this.startActivity(i);

	}
}
