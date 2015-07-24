package org.example.myapp.client.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.example.myapp.client.model.ArchivesBean;
import org.example.myapp.client.model.EducationArticle;
import org.example.myapp.client.model.User;
import org.example.myapp.client.model.VersionInfo;
import org.example.myapp.client.view.MainActivity;
import org.example.myapp.common.Hospital;
import org.example.myapp.common.MessageOne;
import org.example.myapp.common.MyAppConfig;
import org.example.myapp.common.ReturnObj;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.example.myapp.utils.SaxService;

public class YQClient {

	@SuppressWarnings("unused")
	private Context context;
	private static HttpParams params = null;
	private static SchemeRegistry schemeRegistry = null;

	private HttpClient httpClient = null;
	@SuppressWarnings("unused")
	private String result;
	private List<VersionInfo> infos;

	public static DefaultHttpClient getThreadSafeClient() {
		if (YQClient.params == null || schemeRegistry == null) {
			DefaultHttpClient client = new DefaultHttpClient();
			params = client.getParams();
			ClientConnectionManager mgr = client.getConnectionManager();
			YQClient.schemeRegistry = mgr.getSchemeRegistry();
		}

		DefaultHttpClient client = new DefaultHttpClient(
				new ThreadSafeClientConnManager(params, schemeRegistry), params);
		return client;
	}

	public YQClient() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().penaltyDeath()
				.build());
		httpClient = getThreadSafeClient();
	}

	public YQClient(boolean is_not_null) {

	}

	/**
	 * @brief： 登陆
	 * 
	 */
	public ReturnObj sendLoginInfo(User user) {
		ReturnObj obj = new ReturnObj();
		try {
			NameValuePair pair_1 = new BasicNameValuePair("tel",
					Long.toString(user.getId()));
			NameValuePair pair_2 = new BasicNameValuePair("password",
					user.getPassword());
			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(pair_1);
			pairList.add(pair_2);
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
					HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(MyAppConfig.USER_LOGIN_URL);
			httpPost.setEntity(requestHttpEntity);
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			// 鍙戦�佽姹�
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 8000);
			HttpResponse response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;
			}
			// MainActivity.myInfo=ms.getContent();

			String str_user_info = getUserInfo(user.getId());

			MainActivity.myInfo = str_user_info;
			if (0 != user.paser_str(str_user_info)) {
				obj.setMsg("登陆失败!");
				obj.setRet_code(-1);
				return obj;
			}
			MainActivity.myUser = user;
			obj.paser_return_code(result);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() == null) {
				Log.e("sky", "e.getMessage()" + e.getMessage());
				obj.setMsg("服务器未响应!");
			}
			obj.setRet_code(1);

		}
		return obj;
	}

	/**
	 * @brief: 注册用户
	 */
	public ReturnObj sendRegisterInfo(User user) {

		ReturnObj obj = new ReturnObj();
		try {
			NameValuePair pair_1 = new BasicNameValuePair("name",
					user.getName());
			NameValuePair pair_2 = new BasicNameValuePair("sex", user.getSex());
			NameValuePair pair_3 = new BasicNameValuePair("age",
					Integer.toString(user.getAge()));
			NameValuePair pair_4 = new BasicNameValuePair("tel",
					Long.toString(user.getId()));
			NameValuePair pair_5 = new BasicNameValuePair("address",
					user.getAddress());
			NameValuePair pair_6 = new BasicNameValuePair("password",
					user.getPassword());
			NameValuePair pair_7 = new BasicNameValuePair("email",
					user.getMail());

			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(pair_1);
			pairList.add(pair_2);
			pairList.add(pair_3);
			pairList.add(pair_4);
			pairList.add(pair_5);
			pairList.add(pair_6);
			pairList.add(pair_7);

			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
					HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(MyAppConfig.USER_REGISTER_URL);
			httpPost.setEntity(requestHttpEntity);

			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			// 鍙戦�佽姹�

			HttpResponse response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;
			}

			obj.paser_return_code(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(1);

		}
		return obj;
	}

	/**
	 * @brief 更新用户信息
	 */
	@SuppressWarnings("unused")
	public ReturnObj sendUpdateInfo(User user) {

		ReturnObj obj = new ReturnObj();
		try {

			NameValuePair pair_1 = new BasicNameValuePair("name",
					user.getName());
			NameValuePair pair_2 = new BasicNameValuePair("sex", user.getSex());
			NameValuePair pair_3 = new BasicNameValuePair("age",
					Integer.toString(user.getAge()));
			NameValuePair pair_4 = new BasicNameValuePair("tel",
					Long.toString(user.getId()));
			NameValuePair pair_5 = new BasicNameValuePair("address",
					user.getAddress());
			NameValuePair pair_6 = new BasicNameValuePair("password",
					user.getPassword());
			NameValuePair pair_7 = new BasicNameValuePair("email",
					user.getMail());

			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(pair_1);
			pairList.add(pair_2);
			pairList.add(pair_3);
			pairList.add(pair_4);
			pairList.add(pair_5);
			pairList.add(pair_6);
			pairList.add(pair_7);

			pairList.add(new BasicNameValuePair("id", Integer.toString(user
					.getId_real())));

			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
					HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(MyAppConfig.USER_UPDATE_INFO_URL);
			httpPost.setEntity(requestHttpEntity);

			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");

			HttpResponse response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;
			}

			String place = "["
					+ Thread.currentThread().getStackTrace()[2].getFileName()
					+ " "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ ":"
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "] ";
			obj.paser_return_code(result);

		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(1);

		}

		String place = "["
				+ Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ ":"
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "] ";

		return obj;
	}

	/**
	 * @brief: 获取用户信息
	 */
	@SuppressWarnings("unused")
	public String getUserInfo(Long id) {
		String str = "";
		ReturnObj obj = new ReturnObj();
		try {
			HttpGet httpGet = new HttpGet(MyAppConfig.USER_INFO_URL
					+ Long.toString(id));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = "";
			while (null != (line = reader.readLine())) {
				str += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * @brief: 病人添加好友
	 */
	public ReturnObj add_user_doc(Long id, Long doc_id) {

		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(MyAppConfig.USER_ADD_DOC_URL
					+ Long.toString(id) + "/" + Long.toString(doc_id));

			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}

		return obj;
	}

	/**
	 * @brief: 删除好友
	 */
	public ReturnObj del_user_doc(Long id, Long doc_id) {

		ReturnObj obj = new ReturnObj();
		try {

			// todo 闇�瑕佷慨鏀硅繖涓猧d
			HttpGet httpGet = new HttpGet(MyAppConfig.USER_DEL_DOC_URL
					+ Long.toString(id) + "/" + Long.toString(doc_id));

			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}

			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}

		return obj;
	}

	/**
	 * @brief 获取医生信息
	 */
	public String get_doc_info(Long id) {

		String str_all = "";
		try {

			// todo 闇�瑕佷慨鏀硅繖涓猧d
			HttpGet httpGet = new HttpGet(MyAppConfig.DOC_INFO_URL
					+ Long.toString(id));

			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));

			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}

		} catch (Exception e) {
			e.printStackTrace();
			str_all = "";
		}

		return str_all;
	}

	/**
	 * @brief 获取医生列表
	 */
	public ReturnObj get_user_doc_list(Long id) {

		ReturnObj obj = new ReturnObj();
		try {
			HttpGet httpGet = new HttpGet(MyAppConfig.USER_DOC_LIST_URL
					+ Long.toString(id));

			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}

			obj.paser_return_code(str_all);

		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}

		return obj;
	}

	/**
	 * @brief 获取医生列表
	 */
	public ReturnObj get_all_doc_list(int pageindex) {

		ReturnObj obj = new ReturnObj();
		try {
			HttpGet httpGet = new HttpGet(MyAppConfig.DOC_ALL_URL
					+ Integer.toString(pageindex));
			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);

		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}

		return obj;
	}

	/**
	 * @brief 获取未读信息
	 */
	public ReturnObj get_user_doc_msg_toread_list(Long id) {

		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(MyAppConfig.USER_MSG_TO_READ_URL
					+ Long.toString(id));

			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}

			obj.paser_return_code(str_all);

		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}

		return obj;
	}

	/**
	 * @brief 发送信息
	 */
	public ReturnObj sendOneMessageToDoc(MessageOne one_mes) {

		ReturnObj obj = new ReturnObj();
		try {

			NameValuePair pair_1 = new BasicNameValuePair("sendertel",
					Long.toString(one_mes.getSender()));
			NameValuePair pair_2 = new BasicNameValuePair("senderrole",
					Integer.toString(one_mes.getSender_role()));
			NameValuePair pair_3 = new BasicNameValuePair("receivertel",
					Long.toString(one_mes.getReceiver()));
			NameValuePair pair_4 = new BasicNameValuePair("receiverrole",
					Integer.toString(one_mes.getReceiver_role()));
			NameValuePair pair_5 = new BasicNameValuePair("content",
					one_mes.getContent());

			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(pair_1);
			pairList.add(pair_2);
			pairList.add(pair_3);
			pairList.add(pair_4);
			pairList.add(pair_5);

			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
					HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(
					MyAppConfig.USER_SEND_MSG_TO_DOC_URL);
			httpPost.setEntity(requestHttpEntity);

			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			// 鍙戦�佽姹�
			HttpResponse response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;
			}

			obj.paser_return_code(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(1);

		}
		return obj;
	}

	/**
	 * @brief 获取未读信息
	 */
	public ReturnObj get_user_doc_msg_toread_detail_list(Long id, Long doc_id) {

		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(MyAppConfig.USER_DOC_CHAT_MSG_URL
					+ Long.toString(doc_id) + "/" + Long.toString(id));
			HttpResponse httpResponse = YQClient.getThreadSafeClient().execute(
					httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}

			obj.paser_return_code(str_all);

		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}

		return obj;
	}

	/**
	 * @brief 新增预约
	 */
	public ReturnObj add_user_oder(Long patient_tel, Long doctor_tel,
			String appdatetime) {

		ReturnObj obj = new ReturnObj();
		try {

			NameValuePair pair_1 = new BasicNameValuePair("patient_tel",
					Long.toString(patient_tel));
			NameValuePair pair_2 = new BasicNameValuePair("doctor_tel",
					Long.toString(doctor_tel));
			NameValuePair pair_3 = new BasicNameValuePair("appdatetime",
					appdatetime);

			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(pair_1);
			pairList.add(pair_2);
			pairList.add(pair_3);

			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
					HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(MyAppConfig.PATIENT_ADD_ORDER);
			httpPost.setEntity(requestHttpEntity);

			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			// 鍙戦�佽姹�
			HttpResponse response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;
			}
			obj.paser_return_code(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(1);
		}
		return obj;
	}

	/**
	 * @brief: 查询预约
	 * @param id
	 * @return
	 */
	public ReturnObj get_user_order_list(long id) {
		ReturnObj obj = new ReturnObj();
		try {
			// todo 闇�瑕佷慨鏀硅繖涓猧d
			HttpGet httpGet = new HttpGet(MyAppConfig.PATIENT_GET_ORDER_LIST
					+ Long.toString(id));

			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}

			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	/**
	 * @brief: 删除预约
	 * 
	 */
	public ReturnObj del_user_order(int id) {
		ReturnObj obj = new ReturnObj();
		try {
			// todo 闇�瑕佷慨鏀硅繖涓猧d
			HttpGet httpGet = new HttpGet(MyAppConfig.PATIENT_DEL_ORDER
					+ Integer.toString(id));

			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}

			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	public ReturnObj get_post_list(long id) {
		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(MyAppConfig.PATIENT_GET_POST_LIST
					+ Long.toString(id));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	/**
	 * @brief 发帖
	 */
	public ReturnObj add_user_post(Long patient_tel, String title,
			String content, int node_id) {

		ReturnObj obj = new ReturnObj();
		try {

			NameValuePair pair_1 = new BasicNameValuePair("patient_tel",
					Long.toString(patient_tel));
			NameValuePair pair_2 = new BasicNameValuePair("title", title);
			NameValuePair pair_3 = new BasicNameValuePair("content", content);
			NameValuePair pair_4 = new BasicNameValuePair("node_id",
					Integer.toString(node_id));

			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(pair_1);
			pairList.add(pair_2);
			pairList.add(pair_3);
			pairList.add(pair_4);

			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
					HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(MyAppConfig.PATIENT_ADD_POST);
			httpPost.setEntity(requestHttpEntity);

			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			// 鍙戦�佽姹�
			HttpResponse response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;
			}

			obj.paser_return_code(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(1);
		}
		return obj;
	}

	/**
	 * 获取帖子内容
	 * 
	 * @param id
	 * @return
	 */
	public ReturnObj get_post_detail_by_id(int id) {
		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(MyAppConfig.PATIENT_GET_POST_DETAIL
					+ Integer.toString(id));

			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	/**
	 * 获取帖子内容
	 * 
	 * @param id
	 *            帖子ID
	 * @return
	 */
	public ReturnObj get_post_comment_list(int id) {
		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(
					MyAppConfig.PATIENT_GET_POST_COMMENT_LIST
							+ Integer.toString(id));

			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	/**
	 * @brief 发帖
	 * 
	 */
	public ReturnObj add_post_comment(Long patient_tel, String comment,
			int topic_id) {

		ReturnObj obj = new ReturnObj();
		try {

			NameValuePair pair_1 = new BasicNameValuePair("patient_tel",
					Long.toString(patient_tel));
			NameValuePair pair_2 = new BasicNameValuePair("comment", comment);
			NameValuePair pair_3 = new BasicNameValuePair("topic_id",
					Integer.toString(topic_id));

			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(pair_1);
			pairList.add(pair_2);
			pairList.add(pair_3);

			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
					HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(
					MyAppConfig.PATIENT_ADD_POST_COMMENT);
			httpPost.setEntity(requestHttpEntity);

			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");

			HttpResponse response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;
			}

			obj.paser_return_code(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(1);
		}
		return obj;
	}

	/**
	 * 获取健康信息
	 * 
	 * @param id
	 *            当前用户ID
	 * @return
	 */
	public ReturnObj get_patient_health_stat(long id) {
		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(MyAppConfig.GET_PATIENT_HEALTH_STAT
					+ Long.toString(id));

			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	/**
	 * 获取心跳信息
	 * 
	 * @param id
	 *            当前用户ID
	 * @return
	 */
	public ReturnObj get_patient_heartrate_list(long id) {
		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(
					MyAppConfig.GET_PATIENT_HEARTRATE_LIST + Long.toString(id)
							+ "/50");

			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	/**
	 * 获取心跳信息
	 * 
	 * @param id
	 *            当前用户ID
	 * @return
	 */
	public ReturnObj get_patient_device(long id) {
		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(MyAppConfig.GET_PATIENT_DEVICE
					+ Long.toString(id));

			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	/**
	 * 获取心跳信息
	 * 
	 * @param id
	 *            当前用户ID
	 * @return
	 */
	public ReturnObj get_patient_pos(String dev) {
		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(MyAppConfig.GET_PATIENT_LOC + dev);
			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	/**
	 * 获取最新版本
	 * */
	public List<VersionInfo> getNewestVersion() {
		HttpEntity httpEntity = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(
					"http://rj17701.sinaapp.com/index.php/app/checkversion");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				httpEntity = httpResponse.getEntity();
				result = httpEntity.toString();
			}
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				SaxService sax = new SaxService();
				infos = sax.getVersion(inputStream);
			}
			return infos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	/**
	 * 修改密码
	 * */
	public ReturnObj change_psw(String patient_tel, Long oldpsw, Long newpsw) {

		ReturnObj obj = new ReturnObj();
		try {

			HttpGet httpGet = new HttpGet(MyAppConfig.PASSWROD_CHANGE_URL
					+ patient_tel + "/" + oldpsw + "/" + newpsw);
			HttpResponse httpResponse = getThreadSafeClient().execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			obj.paser_return_code(str_all);
			JSONObject jsonObject = new JSONObject(str_all);

			String status = jsonObject.getString("status");
			String msg = jsonObject.getString("msg");
			String content = jsonObject.getString("content");
			obj.setRet_code(Integer.valueOf(status));
			obj.setMsg(msg);
			obj.setOrg_str(content);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setMsg(e.getMessage());
			obj.setRet_code(-1);
		}
		return obj;
	}

	/**
	 * 获取文章
	 * */
	@SuppressLint("NewApi")
	public ArrayList<EducationArticle> getArticle() {

		ArrayList<EducationArticle> list_article = new ArrayList<EducationArticle>();
		EducationArticle article;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(
					"http://1.rj17701.sinaapp.com/index.php/topic/showarticles");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			JSONArray array = new JSONArray(str_all);
			for (int i = 0; i < array.length(); i++) {
				article = new EducationArticle();
				String content = array.getJSONObject(i).getString("content");
				String title = array.getJSONObject(i).getString("title");
				String addtime = array.getJSONObject(i).getString("addtime");
				String name = array.getJSONObject(i).getString("name");
				String keyWord = array.getJSONObject(i).getString("keywords");
				String sort = array.getJSONObject(i).getString("sort");
				article.setContent(content);
				article.setTitle(title);
				article.setAddtime(addtime);
				article.setName(name);
				article.setKeywords(keyWord);
				article.setSort(sort);
				list_article.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list_article;
	}

	/**
	 * 获取学历列表
	 * */
	public List<String> getEducation() {
		String str = "";
		List<String> list = new ArrayList<String>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(MyAppConfig.GET_EDUCATION);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = "";
			while (null != (line = reader.readLine())) {
				str += line;
			}
			JSONArray array = new JSONArray(str);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = new JSONObject(array.getString(i));
				String string = jsonObject.getString("edu");
				list.add(string);
				Log.i("sky", "" + string);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取医院列表
	 * */
	public List<Hospital> getHospital() {
		String str = "";
		List<Hospital> hospitals = new ArrayList<Hospital>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(MyAppConfig.GET_HOSPITAL_NAME);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = "";
			while (null != (line = reader.readLine())) {
				str += line;
			}
			JSONArray array = new JSONArray(str);
			for (int i = 0; i < array.length(); i++) {
				Hospital hospital = new Hospital();
				JSONObject jsonObject = new JSONObject(array.getString(i));
				String string = jsonObject.getString("hos_name");
				hospital.setName(string);
				hospitals.add(hospital);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hospitals;
	}

	/**
	 * 获取疾病类型
	 * */
	public List<String> getDiseaseType() {
		String str = "";
		List<String> list = new ArrayList<String>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(MyAppConfig.GET_DISEASE_TYPE);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = "";
			while (null != (line = reader.readLine())) {
				str += line;
			}
			JSONArray array = new JSONArray(str);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = new JSONObject(array.getString(i));
				list.add(jsonObject.getString("disease_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取档案信息
	 * */
	public ArchivesBean getArchives(Long id) {
		ArchivesBean archives = new ArchivesBean();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(MyAppConfig.GET_ARCHIVES + id);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String str_all = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				str_all += line;
			}
			JSONObject allInfo = new JSONObject(str_all);
			String fzjcJson = allInfo.getString("fzjc");
			String patientJson = allInfo.getString("patient");

			JSONObject jsonObject = new JSONObject(patientJson);
			String name = jsonObject.getString("name");
			archives.setName(name);
			archives.setSex(jsonObject.getString("sex"));
			archives.setAge(jsonObject.getString("age"));
			archives.setLife(jsonObject.getString("life"));
			archives.setAddress(jsonObject.getString("address"));
			archives.setCustomhospital(jsonObject.getString("customhospital"));
			archives.setDepartment(jsonObject.getString("department"));
			archives.setPersonaldoctor(jsonObject.getString("personaldoctor"));
			archives.setTime(jsonObject.getString("time"));
			archives.setSick(jsonObject.getString("sickhistory"));
			archives.setMedicine(jsonObject.getString("mainmedicine"));
			archives.setGuardian(jsonObject.getString("guardian"));
			archives.setGuardian_tel(jsonObject.getString("guardian_tel"));
			archives.setFzjc(fzjcJson);
			String sickJson = allInfo.getString("sick");
			String medicineJson = allInfo.getString("medicine");
			archives.setPatient(patientJson);
			archives.setSick(sickJson);
			archives.setMedicine(medicineJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return archives;
	}
	
	public List<String> getHeartBeat(long id)
	{
		String str = "";
		List<String> list = new ArrayList<String>();
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(MyAppConfig.GET_ONE_MINUTE_HEARTBEAT+"/"+id+"/50");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = "";
			while (null != (line = reader.readLine())) {
				str += line;
			}
			JSONObject jsonObject = new JSONObject(str);
			String item1 = jsonObject.getString("list_1");
			String item2 = jsonObject.getString("list_2");
			String item3 = jsonObject.getString("list_3");
			String item4 = jsonObject.getString("list_4");
			String item5 = jsonObject.getString("list_5");
			list.add(item1);
			list.add(item2);
			list.add(item3);
			list.add(item4);
			list.add(item5);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return list;
	
	}
}
