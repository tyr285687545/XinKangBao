package org.example.myapp.client.view;

import java.util.ArrayList;
import java.util.List;

import org.example.myapp.R;
import org.example.myapp.adapter.DoctorServcieAdapter;
import org.example.myapp.adapter.Hospital_Adapter;
import org.example.myapp.adapter.SortAdapter;
import org.example.myapp.client.model.Doctor;
import org.example.myapp.common.Hospital;
import org.example.myapp.common.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DoctorService extends Activity {

	ListView doctor_service_lv;
	DoctorServcieAdapter lv_adapter;
	AutoCompleteTextView autoCompleteTextView;
	ArrayAdapter<String> autoadapter;
	List<String> name_list;
	String[] name;
	String JobArray [] =  {"院长","副院长","科主任","主任医师","医师副主任","主治医师"};
	List<Doctor> search_list;
	List<Hospital> hospital_list;
	// List<String> diseasetype;
	List<String> education;
	Doctor doctor = new Doctor();
	TextView hospital_tv;
	TextView education_tv;
	TextView disease_tv;
	TextView job_tv;
	TextView tv_dialog_title;
	ListView lv;
	ArrayList<Doctor> doList;
	private ImageView iv;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.doctores_service);
		lv_adapter = new DoctorServcieAdapter(this,
				LoginActivity.buddyEntityList);
		new getList().execute("");
		initView();
	}

	private void initView() {
		// 显示个人中心按钮
		iv = (ImageView) findViewById(R.id.doctor_service_head_iv);
		iv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DoctorService.this, Myself.class);
				startActivity(intent);
			}
		});
		doctor_service_lv = (ListView) findViewById(R.id.doctor_service_lv);
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.tv_search);
		hospital_tv = (TextView) findViewById(R.id.service_hosipal);
		education_tv = (TextView) findViewById(R.id.service_education);
		job_tv = (TextView) findViewById(R.id.doctor_service_job);
		// disease_tv = (TextView) findViewById(R.id.service_hosipal);
		initParam();
	}

	private void initParam() {
		job_tv.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final List<Hospital> jobPosition =new ArrayList<Hospital>();
				Hospital doctorJob;
				for (int i = 0; i < 5; i++) 
				{
					doctorJob = new Hospital();
					doctorJob.setName(JobArray[i]);
					jobPosition.add(doctorJob);
				}
				final Dialog dialog = new Dialog(DoctorService.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_hospital);
				ListView lv_job = (ListView) dialog.findViewById(R.id.lv_hospital);
				// 设置适配器
				final Hospital_Adapter adapter = new Hospital_Adapter(
						DoctorService.this, jobPosition);
				lv_job.setAdapter(adapter);
				// listView 的点击事件
				lv_job.setOnItemClickListener(new OnItemClickListener()
				{
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) 
					{
						doList = sortList(
								jobPosition.get(position).getName(), 2);
						lv_adapter = new DoctorServcieAdapter(
								DoctorService.this, doList);
						doctor_service_lv.setAdapter(lv_adapter);
						// 更新适配器内容
						adapter.notifyDataSetChanged();
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});

		education_tv.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				final Dialog dialog = new Dialog(DoctorService.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_hospital);
				ListView lv_education = (ListView) dialog
						.findViewById(R.id.lv_hospital);
				tv_dialog_title = (TextView) dialog
						.findViewById(R.id.dialog_tv_title);
				SortAdapter adapter = new SortAdapter(DoctorService.this,
						education);
				lv_education.setAdapter(adapter);
				tv_dialog_title.setText("选择-学历");
				lv_education.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						doList = sortList(education.get(position), 1);
						lv_adapter = new DoctorServcieAdapter(
								DoctorService.this, doList);
						doctor_service_lv.setAdapter(lv_adapter);
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
		
		// 医院筛选对话框
		hospital_tv.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				final Dialog dialog = new Dialog(DoctorService.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_hospital);
				lv = (ListView) dialog.findViewById(R.id.lv_hospital);
				// 设置适配器
				final Hospital_Adapter adapter = new Hospital_Adapter(
						DoctorService.this, hospital_list);
				lv.setAdapter(adapter);
				// listView 的点击事件
				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (Hospital_Adapter.getIsSelected().get(position)) {
							// 如果为选中状态设置非选中
							Hospital_Adapter.getIsSelected().put(position,
									false);
						} else {
							// 如果为非选中状态设置选中
							Hospital_Adapter.getIsSelected()
									.put(position, true);
							// 将其他设置为非选中
							for (int i = 0; i < hospital_list.size(); i++) {
								// 排除需要选中的checkBox
								if (i != position) {
									Hospital_Adapter.getIsSelected().put(i,
											false);
								}
							}
						}
						// 用户选择了医院后进行筛选
						doList = sortList(
								hospital_list.get(position).getName(), 0);
						lv_adapter = new DoctorServcieAdapter(
								DoctorService.this, doList);
						doctor_service_lv.setAdapter(lv_adapter);

						// 更新适配器内容
						adapter.notifyDataSetChanged();
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
		doctor_service_lv.setAdapter(lv_adapter);

		name = new String[LoginActivity.buddyEntityList.size()];

		for (int i = 0; i < LoginActivity.buddyEntityList.size(); i++) {
			LoginActivity.buddyEntityList.get(i).getName();

			name[i] = LoginActivity.buddyEntityList.get(i).getName();
		}
		autoadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, name);
		autoCompleteTextView.setAdapter(autoadapter);
		initOnClick();
	}

	private void initOnClick() {

		doctor_service_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent toDocDetail = new Intent(DoctorService.this,
						DoctorDetail.class);
				if (search_list != null && search_list.size() == 1) {
					toDocDetail.putExtra("tel", search_list.get(0).getDoc_id());
					toDocDetail.putExtra("name", search_list.get(0).getName());
					toDocDetail.putExtra("isinline", search_list.get(0)
							.getIsOnline());
					toDocDetail.putExtra("hospital", search_list.get(0)
							.getHospital());
					toDocDetail.putExtra("person", search_list.get(0)
							.getPerson());
					toDocDetail.putExtra("department", search_list.get(0)
							.getDepartment());
					toDocDetail
							.putExtra("major", search_list.get(0).getMajor());
					toDocDetail.putExtra("job", search_list.get(0).getJob());
					toDocDetail.putExtra("thumb_src", search_list.get(0)
							.getThumb_url());
				} else if (doList != null && doList.size() > 0) {
					toDocDetail.putExtra("tel", doList.get(position)
							.getDoc_id());
					if (!StringUtils.isEmpty(doList.get(position)
							.getThumb_url())) {
						toDocDetail.putExtra("thumb_src", doList.get(position)
								.getThumb_url());
					}
					toDocDetail
							.putExtra("name", doList.get(position).getName());
					toDocDetail.putExtra("isinline", doList.get(position)
							.getIsOnline());
					toDocDetail.putExtra("hospital", doList.get(position)
							.getHospital());
					toDocDetail.putExtra("person", doList.get(position)
							.getPerson());
					toDocDetail.putExtra("department", doList.get(position)
							.getDepartment());
					toDocDetail.putExtra("major", doList.get(position)
							.getMajor());
					toDocDetail.putExtra("job", doList.get(position).getJob());
				} else {
					toDocDetail.putExtra("tel", LoginActivity.buddyEntityList
							.get(position).getDoc_id());
					if (!StringUtils.isEmpty(LoginActivity.buddyEntityList.get(
							position).getThumb_url())) {
						toDocDetail.putExtra("thumb_src",
								LoginActivity.buddyEntityList.get(position)
										.getThumb_url());
					}
					toDocDetail.putExtra("name", LoginActivity.buddyEntityList
							.get(position).getName());
					toDocDetail.putExtra("isinline",
							LoginActivity.buddyEntityList.get(position)
									.getIsOnline());
					toDocDetail.putExtra("hospital",
							LoginActivity.buddyEntityList.get(position)
									.getHospital());
					toDocDetail.putExtra("person",
							LoginActivity.buddyEntityList.get(position)
									.getPerson());
					toDocDetail.putExtra("department",
							LoginActivity.buddyEntityList.get(position)
									.getDepartment());
					toDocDetail.putExtra("major", LoginActivity.buddyEntityList
							.get(position).getMajor());
					toDocDetail.putExtra("job", LoginActivity.buddyEntityList
							.get(position).getJob());
				}
				startActivity(toDocDetail);
			}
		});
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int Tag = -1;
				String choosename = autoCompleteTextView.getText().toString();
				for (int i = 0; i < LoginActivity.buddyEntityList.size(); i++) {
					if (LoginActivity.buddyEntityList.get(i).getName()
							.equals(choosename)) {
						Tag = i;
						break;
					}
				}
				if (Tag != -1) {
					search_list = new ArrayList<Doctor>();
					doctor = LoginActivity.buddyEntityList.get(Tag);
					search_list.add(doctor);
					lv_adapter = new DoctorServcieAdapter(DoctorService.this,
							search_list);
					doctor_service_lv.setAdapter(lv_adapter);
					lv_adapter.notifyDataSetChanged();
				}
			}
		});
		autoCompleteTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				switch (count) {
				case 0:
					search_list.clear();
					lv_adapter = new DoctorServcieAdapter(DoctorService.this,
							LoginActivity.buddyEntityList);
					doctor_service_lv.setAdapter(lv_adapter);
					lv_adapter.notifyDataSetChanged();
					break;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}
	/**
	 * 排序
	 * */
	private ArrayList<Doctor> sortList(String s, int Flag) {
		ArrayList<Doctor> doctors = new ArrayList<Doctor>();
		for (int i = 0; i < LoginActivity.buddyEntityList.size(); i++) {
			if (Flag == 0) {
				// 医院排序
				if (LoginActivity.buddyEntityList.get(i).getHospital()
						.equals(s)) {
					doctors.add(LoginActivity.buddyEntityList.get(i));
				}
			} else if (Flag == 1) {
				// 学历排序
				if (LoginActivity.buddyEntityList.get(i).getJob().contains(s)) {
					doctors.add(LoginActivity.buddyEntityList.get(i));
				}
			}else if (Flag == 2) {
				Log.d("sky", "getJob = "+LoginActivity.buddyEntityList.get(i).getJob());
				//职务排序
				if (LoginActivity.buddyEntityList.get(i).getJob().contains(s)) {
					doctors.add(LoginActivity.buddyEntityList.get(i));
				}
			}

		}
		return doctors;
	}

	class getList extends AsyncTask {

		ProgressDialog dialog = new ProgressDialog(DoctorService.this);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("请稍后,正在获取筛选列表.");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			hospital_list = LoginActivity.new_http_client.getHospital();
			education = LoginActivity.new_http_client.getEducation();
			// diseasetype = LoginActivity.new_http_client.getDiseaseType();
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			dialog.dismiss();
		}
	}
}
