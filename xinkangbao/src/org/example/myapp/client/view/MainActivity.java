package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.client.model.User;
import org.example.myapp.client.network.YQClient;
import org.example.myapp.common.MyAppConfig;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	public static String myInfo;
	public static User myUser;
	private TabHost tabHost;
	private RadioGroup radioGroup;
	private static final String BUDDY = "好友";
	private static final String BBS = "论坛";
	private static final String HEALTH = "健康";
	private static final String MORE = "教育";
	public Intent buddyIntent;
	public Intent bbsIntent;
	public Intent moreIntent;
	public Intent doctorDetailIntent;
	public Intent healthIntent;
	public static ProgressDialog progressDialog;

	public static YQClient client_in_strict_mode;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabhost);
		tabHost = this.getTabHost();
		setupIntent();

		radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				switch (checkedId) {
				/*
				 * case R.id.tab_recent: tabHost.setCurrentTabByTag(RECENT);
				 * break;
				 */
				// 设置当前标签的标签
				case R.id.tab_buddy:
					tabHost.setCurrentTabByTag(BUDDY);
					break;
				case R.id.tab_health:
					tabHost.setCurrentTabByTag(HEALTH);
					break;
				case R.id.main_footbar_question:
					tabHost.setCurrentTabByTag(BBS);
					break;
				case R.id.tab_more:
					tabHost.setCurrentTabByTag(MORE);
					break;
				case R.id.main_doctor_service:
					tabHost.setCurrentTabByTag("doctor");
					break;
				}
			}
		});
	}

	public void initProgressDialog(Context mContext)
	{
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("正在加载中...");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 按下HOME键
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			// 显示Notification
			Toast.makeText(getApplicationContext(), "按下HOME键",
					Toast.LENGTH_LONG).show();
			showNotification();
			moveTaskToBack(true);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/*
	 * @Override protected void onStop() { showNotification();//显示通知栏图标
	 * super.onStop(); }
	 */

	@Override
	protected void onResume() {
		// 取消通知栏图标
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_login_out:
			Toast.makeText(this, "你选择了：" + item.getTitle(), Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.menu_about:
			Dialog alertDialog = new AlertDialog.Builder(this)
					.setTitle("关于心康宝")
					.setMessage("三石摇篮出品\n version: " + MyAppConfig.APP_VERSION)
					.create();
			alertDialog.show();
			break;
		case R.id.menu_setting:
			Toast.makeText(this, "你选择了：" + item.getTitle(), Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.menu_close:
			closeAllActivity();
			break;
		}
		return true;
	}

	// 退出所有activity
	public void closeAllActivity() {
		if (ManageActivity.getActivity("loginActivity") != null) {
			ManageActivity.getActivity("loginActivity").finish();
		}

		if (ManageActivity.getActivity("buddyActivity") != null) {
			ManageActivity.getActivity("buddyActivity").finish();
		}
		this.finish();
		System.exit(0);
	}

	// 按下返回键后不应该去LoginActivity，应该使其直接回到桌面，
	// 次方法只适用于 2.0 以上版本，
	// 低于2.0 使用public boolean onKeyDown(int keyCode, KeyEvent event)
	public void onBackPressed() {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
		super.onBackPressed();
	}

	private void setupIntent() {

		buddyIntent = new Intent(this, Buddy.class);
		bbsIntent = new Intent(this, Activity_question.class);
//		bbsIntent = new Intent(this, QuestionNewActivity.class);
		bbsIntent.putExtra("post_tag", "问答");
		moreIntent = new Intent(this, EducationContentChange.class);
		doctorDetailIntent = new Intent(this, DoctorService.class);
		healthIntent = new Intent(this, ActivityHealthData.class);

		TabSpec tabSpec1 = tabHost.newTabSpec(BUDDY).setIndicator(BUDDY)
				.setContent(buddyIntent);
		tabHost.addTab(tabSpec1);
		TabSpec tabSpec2 = tabHost.newTabSpec(BBS).setIndicator(BBS)
				.setContent(bbsIntent);
		tabHost.addTab(tabSpec2);
		TabSpec tabSpec3 = tabHost.newTabSpec(HEALTH).setIndicator(HEALTH)
				.setContent(healthIntent);
		tabHost.addTab(tabSpec3);
		TabSpec tabSpec4 = tabHost.newTabSpec(MORE).setIndicator(MORE)
				.setContent(moreIntent);
		tabHost.addTab(tabSpec4);
		TabSpec tabSpec5 = tabHost.newTabSpec("doctor").setIndicator("doctor")
				.setContent(doctorDetailIntent);
		tabHost.addTab(tabSpec5);
	}

	// 显示通知栏图标
	private void showNotification() {
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		// 定义Notification的各种属性
		Notification notification = new Notification(R.drawable.logo,
				"心康宝正在运行", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT; // 加入通知栏的"Ongoing"中
		notification.flags |= Notification.FLAG_NO_CLEAR; // 点击了通知栏中的"清除通知"后不清除
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults = Notification.DEFAULT_LIGHTS;
		// 设置通知的事件消息
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentItent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(this, "心康宝", // 通知栏标题
				"三石摇篮", // 通知栏内容
				contentItent); // 点击该通知后要跳转的Activity
		// 把Notification传递给NotificationManager，id为0
		notificationManager.notify(0, notification);
	}
}
