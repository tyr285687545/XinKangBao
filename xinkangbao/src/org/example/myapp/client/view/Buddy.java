package org.example.myapp.client.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.example.myapp.R;
import org.example.myapp.client.model.ArchivesBean;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.client.model.VersionInfo;
import org.example.myapp.client.network.YQClient;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.common.StringUtils;
import org.example.myapp.widget.BannerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Buddy extends FragmentActivity {

	// public static YQClient new_http_client = null;
	//���ݵ����������е�bean
	private ArchivesBean archives;
	
	private boolean interceptFlag = false;

	private Thread downLoadThread;

	private ListView lv_all_doctor;

	private ImageView iv;

	private TextView iv_health_rate;//��������

	private BannerLayout bannerLayout;

	private TextView btn_my_doctor;

	private int progress;


	private Doctor temp;

	// public static List<Doctor> buddyEntityList = new ArrayList<Doctor>();//
	// �����б�

	public static BuddyAdapter ba = null;// �����б��adapter

	public final static int CATALOG_ALL = 1;

	public final static int CATALOG_MY = 2;

	private int curDocCatalog = CATALOG_MY;

	private String new_code;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private String new_name;

	private List<VersionInfo> versionInfos;

	private String curVersionCode;

	private String curVersionName;

	AlertDialog.Builder builder;

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
	/* ��������֪ͨuiˢ�µ�handler��msg���� */
	private ProgressBar mProgress;

	/* ���ذ���װ·�� */
	private static final String savePath = "/sdcard/xinkangbao/";

	private static final String saveFileName = savePath + "xinkangbao.apk";

	private Dialog downloadDialog;

	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.function);

		ManageActivity.addActiviy("buddyActivity", Buddy.this);
		bannerLayout = (BannerLayout) findViewById(R.id.function_banner);
		
		new AsyncCheckVersion().execute("");
		
		initView();
		
		initFrameListView();
		
		LoadDocList(curDocCatalog);
//		new getArchives().execute("");

	}

	private void initView() {
		iv_health_rate = (TextView) findViewById(R.id.ment_health_data_tv);
		iv_health_rate.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("unchecked")
			public void onClick(View v) {
				new Async1().execute("");
			}
		});
		// ��ʾ�������İ�ť
		iv = (ImageView) findViewById(R.id.function_head_menu);
		iv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Buddy.this, Myself.class);
				startActivity(intent);
			}
		});
		
		lv_all_doctor = (ListView) findViewById(R.id.function_lv);
		btn_my_doctor = (TextView) findViewById(R.id.ment_my_doctor_tv);
		btn_my_doctor.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent toMyDoc = new Intent(Buddy.this, MyDoctor.class);
				startActivity(toMyDoc);
			}
		});

		/**
		 * ����������
		 * */
		ba = new BuddyAdapter(Buddy.this, LoginActivity.buddyEntityList);
		ba.notifyDataSetChanged();
		lv_all_doctor.setAdapter(ba);
		setListViewListener(lv_all_doctor);
	}

	// listView��Item����¼�
	private void setListViewListener(final ListView listview) {
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long l) {
				temp = (Doctor) listview.getItemAtPosition(position);
				// ������ҳ��
				Intent intent = new Intent(Buddy.this, DoctorInfoActivity.class);
				intent.putExtra("doccatalog", curDocCatalog);
				intent.putExtra("tel", temp.getDoc_id());
				intent.putExtra("name", temp.getName());
				intent.putExtra("major", temp.getMajor());
				intent.putExtra("isonline", temp.getIsOnline());
				startActivity(intent);
			}
		});
	}

	private void initFrameListView() {

	}

	private void LoadDocList(int curDocCatalog2) {

	}

	private void showDownloadDialog() {
		builder = new Builder(Buddy.this);
		builder.setCancelable(false);
		builder.setTitle("����汾����");
		final LayoutInflater inflater = LayoutInflater.from(Buddy.this);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);

		builder.setView(v);
		builder.setNegativeButton("ȡ��", new OnClickListener() {
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

	/**
	 * ��ȡ������Ϣ
	 * */
	@SuppressWarnings("rawtypes")
	class getArchives extends AsyncTask {
		ProgressDialog dialog = new ProgressDialog(Buddy.this);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("���Ժ�,���ڻ�ȡ����.");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			LoginActivity.new_http_client.getArchives(Long
					.parseLong(LoginActivity.mySharedPreferences.getString(
							"current_login_tel", "")));
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
		}
	}

	/**
	 * �첽����ȡ���°汾
	 * */
	@SuppressWarnings("rawtypes")
	class AsyncCheckVersion extends AsyncTask {
		@Override
		protected Object doInBackground(Object... arg0) {
			versionInfos = new YQClient().getNewestVersion();
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			if (versionInfos != null && versionInfos.size() > 0) {
				for (int i = 0; i < versionInfos.size(); i++) {
					new_code = versionInfos.get(i).getVersionCode();
					new_name = versionInfos.get(i).getVersionName();
					versionInfos.get(i).getUpdateLog();
					versionInfos.get(i).getDownloadUrl();
				}
			}
			/**
			 * ��ȡ�汾��
			 */
			try {
				PackageInfo pack_info = getApplication().getPackageManager()
						.getPackageInfo(getApplication().getPackageName(), 0);
				curVersionName = pack_info.versionName;
				curVersionCode = String.valueOf(pack_info.versionCode);
			} catch (Exception e) {

			}
			// �汾�Ա�
			if ((new_code != null && !curVersionCode.equals(new_code))
					&& (new_name != null && !curVersionName.equals(new_name))) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						Buddy.this);
				builder.setTitle("��ʾ:");
				builder.setMessage("�����°汾,��װ�°汾�����������µĹ���Ŷ~");
				builder.setNegativeButton("ȷ��", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						showDownloadDialog();
					}
				});
				builder.setPositiveButton("�´���˵", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});
				builder.show();
			}
		}
	}

	/**
	 * ����apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			if (versionInfos != null
					&& versionInfos.size() > 0
					&& !StringUtils.isEmpty(versionInfos.get(0)
							.getDownloadUrl())) {

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
						// ���½���
						mHandler.sendEmptyMessage(DOWN_UPDATE);
						if (numread <= 0) {
							// �������֪ͨ��װ
							mHandler.sendEmptyMessage(DOWN_OVER);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!interceptFlag);// ���ȡ����ֹͣ����.
					fos.close();
					is.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {

				// Toast.makeText(Buddy.this, "����ʧ��!",
				// Toast.LENGTH_LONG).show();
			}
		}
	};

	/**
	 * ��װapk
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
		Buddy.this.startActivity(i);
	}

	@Override
	protected void onResume() {
		if (!bannerLayout.isScrolling())
			bannerLayout.startScroll();
		super.onResume();
	}
	/**
	 * �첽��ȡ��ȡ��������
	 * ͨ��bundle����ArchivesBean����
	 * */
	@SuppressWarnings("rawtypes")
	class Async extends AsyncTask 
	{
		ProgressDialog getArchivesDataDialog = new ProgressDialog(Buddy.this);
		Bundle bundle = new Bundle();
		private Intent intent;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			getArchivesDataDialog.setMessage("���Ժ�,���ڻ�ȡ������Ϣ.");
			getArchivesDataDialog.setCancelable(false);
			getArchivesDataDialog.show();
			intent = new Intent(Buddy.this, ActivityHealthData.class);
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			archives = LoginActivity.new_http_client.getArchives(Long.parseLong(LoginActivity.mySharedPreferences.getString("current_login_tel", "")));
			bundle.putSerializable("patient", archives);
			intent.putExtras(bundle);
			return null;
		}
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
//			Log.v("sky", "archives.getPatient() = "+archives.getPatient());
			getArchivesDataDialog.dismiss();
			startActivity(intent);
		}
	}
	/**
	 * �첽��ȡ��ȡ��������
	 * ͨ��bundle����ArchivesBean����
	 * */
	@SuppressWarnings("rawtypes")
	class Async1 extends AsyncTask 
	{
		ProgressDialog getArchivesDataDialog = new ProgressDialog(Buddy.this);
		Bundle bundle = new Bundle();
		private Intent intent;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			getArchivesDataDialog.setMessage("���Ժ�,���ڻ�ȡ������Ϣ.");
			getArchivesDataDialog.setCancelable(false);
			getArchivesDataDialog.show();
			intent = new Intent(Buddy.this, ActivityHealthData.class);
			intent.putExtra("isBack", true);
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			archives = LoginActivity.new_http_client.getArchives(Long.parseLong(LoginActivity.mySharedPreferences.getString("current_login_tel", "")));
			bundle.putSerializable("patient", archives);
			bundle.putBoolean("isShow", true);
			intent.putExtras(bundle);
			return null;
		}
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			getArchivesDataDialog.dismiss();
			startActivity(intent);
		}
	}
}
