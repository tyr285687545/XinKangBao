package org.example.myapp.client.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.example.myapp.R;
import org.example.myapp.client.network.YQClient;
import org.example.myapp.common.MessageOne;
import org.example.myapp.common.MyTime;
import org.example.myapp.common.ReturnObj;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.imageloader.SelectPhoto;

public class ChatActivity extends Activity {
	EditText et_input;

	private String chatContent;// 消息内容
	ListView chatListView;

	public static ChatAdapter chat_adap;// 好友列表的adapter
	private static HashMap chatEntityList = new HashMap<Long, ArrayList<MessageOne>>();
	ArrayList<MessageOne> curr_char_list;
	private static ChatActivity entity;
	String function[] = new String[] { "拍照", "从相册选择" };
	private static Long chatAccount;
	private static String charName;
	private ImageView iv_show;
	public static final int TAKE_PHOTO = 1;
	private String filename;
	private Uri imageUri;
	private static YQClient new_http_client = null;
	BroadcastMainChat broadcastMain;
	private Bitmap bitmap;

	public static int[] avatar = new int[] { R.drawable.avatar_default,
			R.drawable.h001, R.drawable.h002, R.drawable.h003, R.drawable.h004,
			R.drawable.h005, R.drawable.h006, R.drawable.group_avatar };

	@SuppressWarnings("unchecked")
	public static void jiexi(String str) {
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONObject ret = (JSONObject) (jsonParser.nextValue());
			JSONArray doc_list = ret.getJSONArray("list");
			int length = doc_list.length();
			ArrayList<MessageOne> list_tmp = (ArrayList<MessageOne>) chatEntityList
					.get(chatAccount);
			for (int i = 0; i < length; i++) {
				JSONObject oj_tmp = doc_list.getJSONObject(i);
				MessageOne one_msg = new MessageOne();
				one_msg.setContent(oj_tmp.getString("content"));
				one_msg.setLeft(true);
				one_msg.setReceiver(MainActivity.myUser.getId());
				one_msg.setReceiver_name(MainActivity.myUser.getName());
				one_msg.setSender(chatAccount);
				one_msg.setSender_name(charName);
				one_msg.setReceiver_role(0);
				one_msg.setSender_role(1);
				one_msg.setTime(oj_tmp.getString("sendtime"));
				list_tmp.add(one_msg);
			}
			chatEntityList.put(chatAccount, list_tmp);
		} catch (JSONException e) {
			Log.i("exception wangbo", "获取医生列表失败! " + e.getMessage());
			// Toast.makeText(BuddyActivity.this, "获取医生列表失败! " + e.getMessage(),
			// Toast.LENGTH_SHORT).show();
		}
	}

	public static void updateCharList() {
		if (new_http_client != null) {
			ReturnObj obj = new_http_client
					.get_user_doc_msg_toread_detail_list(
							MainActivity.myUser.getId(), chatAccount);

			if (obj.getRet_code() == 0) {
				jiexi(obj.getOrg_str());
			}
		}
	}

	protected void onStop() {

		super.onStop();
	}

	protected void onResume() {

		// Intent intent = new Intent();
		// intent.setClass(this, ChatService.class);
		// startService(intent);
		//
		// broadcastMain = new BroadcastMainChat();
		// IntentFilter filter = new IntentFilter();
		// filter.addAction(ChatService.BROADCASTACTION);
		// registerReceiver(broadcastMain, filter);
		// chat_adap.notifyDataSetChanged();
		super.onResume();
	}

	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("debug wangbo begin", "onCreate  " + MyTime.geTime());
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);

		if (new_http_client == null) {
			new_http_client = new YQClient(true);
		}
		// 设置top面板信息
		chatAccount = getIntent().getLongExtra("tel", 0);
		charName = getIntent().getStringExtra("name");
		getIntent().getIntExtra("isonline", 0);
		getIntent().getStringExtra("major");

		TextView name_tv = (TextView) findViewById(R.id.chat_top_name);
		name_tv.setText(charName);

		// TextView tel_tv=(TextView) findViewById(R.id.chat_top_tel);
		// tel_tv.setText(Long.toString(chatAccount));

		// TextView isOnline_tv = (TextView)
		// findViewById(R.id.chat_top_isonline);
		// if (charOnline == 1) {
		// isOnline_tv.setText("在线");
		// } else {
		// isOnline_tv.setText("离线");
		// }

		chatListView = (ListView) findViewById(R.id.lv_chat);
		iv_show = (ImageView) findViewById(R.id.show_whole_pic);
		// 设置自动滑动到最新的消息
		chatListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		curr_char_list = (ArrayList<MessageOne>) chatEntityList
				.get(chatAccount);
		if (curr_char_list == null) {
			curr_char_list = new ArrayList<MessageOne>();
			chatEntityList.put(chatAccount, curr_char_list);
		}
		updateCharList();
		curr_char_list = (ArrayList<MessageOne>) chatEntityList
				.get(chatAccount);
		chat_adap = new ChatAdapter(this, curr_char_list);

		chatListView.setAdapter(chat_adap);
		entity = this;

		findViewById(R.id.btn_send_pic).setOnClickListener(
				new OnClickListener() {
					public void onClick(View view) {
						if (bitmap != null) {
							bitmap.recycle();
						}
						final Dialog dialog = new Dialog(ChatActivity.this);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.dialog_img);
						RelativeLayout re_take_photo = (RelativeLayout) dialog
								.findViewById(R.id.take_photo);
						RelativeLayout re_select_photo = (RelativeLayout) dialog
								.findViewById(R.id.select_photo);
						TextView tv_take_photo = (TextView) dialog
								.findViewById(R.id.tv_take_photo);
						TextView tv_select_photo = (TextView) dialog
								.findViewById(R.id.tv_selectphoto);

						tv_take_photo.setText(function[0]);
						tv_select_photo.setText(function[1]);
						re_take_photo.setOnClickListener(new OnClickListener()
						{
							public void onClick(View arg0) 
							{
								/**
								 * 拍照
								 * */
								SimpleDateFormat format = new SimpleDateFormat(
										"yyyyMMddHHmmss");
								Date date = new Date(System.currentTimeMillis());
								filename = format.format(date);
								// 创建File对象用于存储拍照的图片 SD卡根目录
								// File outputImage = new
								// File(Environment.getExternalStorageDirectory(),"test.jpg");
								// 存储至DCIM文件夹
								File path = Environment
										.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
								File outputImage = new File(path, filename
										+ ".jpg");
								try {
									if (outputImage.exists()) {
										outputImage.delete();
									}
									outputImage.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
								}
								// 将File对象转换为Uri并启动照相程序

								imageUri = Uri.fromFile(outputImage);
								Intent intent = new Intent(
										"android.media.action.IMAGE_CAPTURE"); // 照相
								intent.putExtra(MediaStore.EXTRA_OUTPUT,
										imageUri); // 指定图片输出地址
								Log.d("sky", "imageUri = " + imageUri);
								dialog.dismiss();
								startActivityForResult(intent, TAKE_PHOTO); // 启动照相
								// 拍完照startActivityForResult()
								// 结果返回onActivityResult()函数
							}
						});
						/**
						 * 从相册选
						 * */
						re_select_photo
								.setOnClickListener(new OnClickListener() {
									public void onClick(View arg0) {
										Intent toPhoto = new Intent(
												ChatActivity.this,
												SelectPhoto.class);
										dialog.dismiss();
										startActivity(toPhoto);
									}
								});
						dialog.show();
					}
				});

		// 发送按钮
		findViewById(R.id.ib_send).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 得到输入的数据，并清空EditText
				et_input = (EditText) findViewById(R.id.et_input);
				chatContent = et_input.getText().toString();
				et_input.setText("");

				final MessageOne one_mes = new MessageOne();
				one_mes.setContent(chatContent);
				one_mes.setLeft(false);

				one_mes.setReceiver(chatAccount);
				one_mes.setReceiver_name(charName);

				one_mes.setSender(MainActivity.myUser.getId());
				one_mes.setSender_name(MainActivity.myUser.getName());
				one_mes.setReceiver_role(1);
				one_mes.setSender_role(0);
				one_mes.setTime(MyTime.geTime());
				// 更新聊天内容
				updateChatView(one_mes);
				new AsyncTask() 
				{
					ReturnObj obj;

					protected Object doInBackground(Object... arg0) 
					{
						obj = MainActivity.client_in_strict_mode
								.sendOneMessageToDoc(one_mes);
						return null;
					}

					protected void onPostExecute(Object result) 
					{
						super.onPostExecute(result);
						if (obj != null && obj.getRet_code() != 0) 
						{
							Toast.makeText(ChatActivity.this,
									"发送消息失败 " + obj.getMsg(),
									Toast.LENGTH_SHORT).show();
						}
					}
				}.execute("");
			}
		});
	}

	// protected void onPause() {
	// String place = "["
	// + Thread.currentThread().getStackTrace()[2].getFileName() + " "
	// + Thread.currentThread().getStackTrace()[2].getMethodName()
	// + ":"
	// + Thread.currentThread().getStackTrace()[2].getLineNumber()
	// + "] ";
	// Log.i("wangbo debug", place + "  onpause time:" + MyTime.geTime());
	// Intent intent = new Intent();
	// intent.setClass(this, ChatService.class);
	// stopService(intent);
	// unregisterReceiver(broadcastMain);
	// super.onStop();
	// }

	public void updateChatView(MessageOne mes) {
		curr_char_list.add(mes);
		chat_adap.notifyDataSetChanged();
	}

	public class BroadcastMainChat extends BroadcastReceiver 
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			curr_char_list = (ArrayList<MessageOne>) chatEntityList
					.get(chatAccount);
			chat_adap.notifyDataSetChanged();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			Toast.makeText(ChatActivity.this,
					"ActivityResult resultCode error", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (requestCode == 1) {
			// 广播刷新相册
			Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			intentBc.setData(imageUri);
			this.sendBroadcast(intentBc);
			try {
				// 图片解析成Bitmap对象
				Options options = new Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(imageUri.toString(), options);
				int rawWidth = options.outWidth;
				int width = 50;
				if (rawWidth > width) {
					int s = rawWidth / width;
					options.inSampleSize = s;
				}
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(imageUri));
				iv_show.setVisibility(View.VISIBLE);
//				iv_show.setImageBitmap(bitmap);
				MessageOne messageOne = new MessageOne();
				messageOne.setBitmap(bitmap);
				messageOne.setPath(imageUri.toString());
				messageOne.setLeft(false);
				messageOne.setTime(MyTime.geTime());
				messageOne.setSender(MainActivity.myUser.getId());
				messageOne.setSender_name(MainActivity.myUser.getName());
				messageOne.setReceiver(chatAccount);
				messageOne.setReceiver_name(charName);
				updateChatView(messageOne);
				Toast.makeText(ChatActivity.this, imageUri.toString(),
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
}
