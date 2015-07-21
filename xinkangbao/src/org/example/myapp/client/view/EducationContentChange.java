package org.example.myapp.client.view;

import java.util.ArrayList;
import java.util.List;

import org.example.myapp.R;
import org.example.myapp.adapter.EducationAdapter;
import org.example.myapp.adapter.SortAdapter;
import org.example.myapp.client.model.EducationArticle;
import org.example.myapp.common.AppContext;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class EducationContentChange extends Activity implements OnClickListener {
	private ProgressDialog dialog;
	private ImageView iv;
	private RadioButton radio_1;
	private RadioButton radio_2;
	private RadioButton radio_3;
	private RadioButton radio_4;
	private RadioButton radio_5;
	private RadioButton radio_6;
	private TextView tv_disease_type;
	private List<String> diseasetype;

	/**
	 * 存放所有教育文章的list
	 * */
	public static List<EducationArticle> articles = new ArrayList<EducationArticle>();
	/**
	 * 满足一级筛选的list
	 * */
	public static List<EducationArticle> articles_type = new ArrayList<EducationArticle>();
	/**
	 * 满足二级筛选的list
	 * */
	public List<EducationArticle> articles_sort = new ArrayList<EducationArticle>();

	private ListView lv_list;
	private TextView tv_content;
	private Bundle bundle = new Bundle();
	private String content;

	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.educationcontengchange);
		initView();
		new AsyncGetArticle().execute("");
	}

	private void setList(List<EducationArticle> list) {
		EducationAdapter adapter = new EducationAdapter(
				EducationContentChange.this, list);
		lv_list.setAdapter(adapter);
	}

	private void setText(String text) {
		tv_content.setText(text);
	}

	private void initView() {

		lv_list = (ListView) findViewById(R.id.education_list);

		lv_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(EducationContentChange.this,
						EducationDeatil.class);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		tv_content = (TextView) findViewById(R.id.education_text);

		// 点击疾病类型
		tv_disease_type = (TextView) findViewById(R.id.disease_type_tv);
		tv_disease_type.setOnClickListener(new OnClickListener() {
			private SortAdapter sortAdapter;

			public void onClick(View v) {
				final Dialog diseaseTypeDialog = new Dialog(
						EducationContentChange.this);
				diseaseTypeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				diseaseTypeDialog.setContentView(R.layout.dialog_hospital);
				ListView lvDiseaseType = (ListView) diseaseTypeDialog
						.findViewById(R.id.lv_hospital);
				TextView tv_dialog_title = (TextView) diseaseTypeDialog
						.findViewById(R.id.dialog_tv_title);
				tv_dialog_title.setText("选择-疾病类型");
				sortAdapter = new SortAdapter(EducationContentChange.this,
						diseasetype);
				lvDiseaseType.setAdapter(sortAdapter);
				lvDiseaseType.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						tv_disease_type.setText(diseasetype.get(position));
						articles_type.clear();
						// 筛选出满足一级条件的文章
						for (int i = 0; i < articles.size(); i++) {
							for (int j = 0; j < articles.get(i)
									.getKeywordArray().length; j++) {
								if (articles.get(i).getKeywordArray()[j]
										.equals(diseasetype.get(position))) {
									articles_type.add(articles.get(i));
								}
							}
						}
						// 刷新Adapter 用户每次设置类型后显示所有该类型的文章
						radio_1.setChecked(false);
						radio_2.setChecked(false);
						radio_3.setChecked(false);
						radio_4.setChecked(false);
						radio_5.setChecked(false);
						radio_6.setChecked(false);
						lv_list.setVisibility(View.VISIBLE);
						tv_content.setVisibility(View.GONE);
						articles_sort.clear();
						setList(articles_type);
						AppContext.getInstance().setArticles(articles_type);
						diseaseTypeDialog.dismiss();
					}
				});
				diseaseTypeDialog.show();
			}
		});

		radio_1 = (RadioButton) findViewById(R.id.radiobtn_1);
		radio_2 = (RadioButton) findViewById(R.id.radiobtn_2);
		radio_3 = (RadioButton) findViewById(R.id.radiobtn_3);
		radio_4 = (RadioButton) findViewById(R.id.radiobtn_4);
		radio_5 = (RadioButton) findViewById(R.id.radiobtn_5);
		radio_6 = (RadioButton) findViewById(R.id.radiobtn_6);

		radio_1.setOnClickListener(this);
		radio_2.setOnClickListener(this);
		radio_3.setOnClickListener(this);
		radio_4.setOnClickListener(this);
		radio_5.setOnClickListener(this);
		radio_6.setOnClickListener(this);

		iv = (ImageView) findViewById(R.id.education_head_iv);
		iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(EducationContentChange.this,
						Myself.class);
				startActivity(intent);
			}
		});

	}

	// 获取文章
	@SuppressWarnings("rawtypes")
	class AsyncGetArticle extends AsyncTask {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(EducationContentChange.this);
			dialog.setMessage("正在获取文章列表..");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			articles = LoginActivity.new_http_client.getArticle();
			// 文章类型
			for (int i = 0; i < articles.size(); i++) {
				articles.get(i).setKeywordArray(
						articles.get(i).getKeywords().split(","));
				articles.get(i).setSortArray(
						articles.get(i).getSort().split(","));
				articles.get(i).setSort(
						(articles.get(i).getSort().split(","))[0]);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			dialog.dismiss();

			setList(articles);
			AppContext.getInstance().setArticles(articles);
			new getList().execute("");
		}
	}

	class getList extends AsyncTask {
		ProgressDialog dialog = new ProgressDialog(EducationContentChange.this);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("请稍后,正在获取筛选列表.");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Object doInBackground(Object... params) {
			diseasetype = LoginActivity.new_http_client.getDiseaseType();
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			dialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radiobtn_1:// 推荐
			String user_choose_type_1 = (String) radio_1.getText();
			articles_sort.clear();

			// 进行二级筛选
			for (int i = 0; i < articles_type.size(); i++) {
				if (articles_type.get(i).getSort().equals(user_choose_type_1)) {
					articles_sort.add(articles_type.get(i));
				}
			}
			if (articles_sort.size() == 0) {

				lv_list.setVisibility(View.GONE);

				tv_content.setVisibility(View.VISIBLE);

				tv_content.setText("抱歉,暂无推荐文章!");
			} else {
				tv_content.setVisibility(View.GONE);

				lv_list.setVisibility(View.VISIBLE);
				setList(articles_sort);
				AppContext.getInstance().setArticles(articles_sort);
			}
			break;
		case R.id.radiobtn_2:// 治疗
			String user_choose_type_2 = (String) radio_2.getText();
			articles_sort.clear();
			content = null;
			// 进行二级筛选
			// 进行二级筛选
			for (int i = 0; i < articles_type.size(); i++) {
				if (articles_type.get(i).getSort().equals(user_choose_type_2)) {
					articles_sort.add(articles_type.get(i));
				}
			}
			if (articles_sort.size() == 0) {

				lv_list.setVisibility(View.GONE);

				tv_content.setVisibility(View.VISIBLE);

				tv_content.setText("抱歉,暂无预防文章!");
			} else {
				tv_content.setVisibility(View.GONE);

				lv_list.setVisibility(View.VISIBLE);
				setList(articles_sort);
				AppContext.getInstance().setArticles(articles_sort);
			}
			break;
		case R.id.radiobtn_3:// 预防
			String user_choose_type_3 = (String) radio_3.getText();
			if (articles_sort.size() > 0) {
				articles_sort.clear();
			}
			// 进行二级筛选
			for (int i = 0; i < articles_type.size(); i++) {
				if (articles_type.get(i).getSort().equals(user_choose_type_3)) {
					articles_sort.add(articles_type.get(i));
				}
			}
			if (articles_sort.size() == 0) {

				lv_list.setVisibility(View.GONE);

				tv_content.setVisibility(View.VISIBLE);
				tv_content.setText("抱歉,暂无治疗文章!");
			} else {
				tv_content.setVisibility(View.GONE);

				lv_list.setVisibility(View.VISIBLE);
				setList(articles_sort);
				AppContext.getInstance().setArticles(articles_sort);
			}
			break;
		case R.id.radiobtn_4:// 定义
			String user_choose_type_4 = (String) radio_4.getText();
			articles_sort.clear();
			// 进行二级筛选
			if (articles_type.size() > 0) {
				for (int i = 0; i < articles_type.size(); i++) {
					if (articles_type.get(i).getSort()
							.equals(user_choose_type_4)) {
						articles_sort.add(articles_type.get(i));
						content = articles_type.get(i).getContent();
						bundle.putString("text", content);
					}
				}
				lv_list.setVisibility(View.GONE);
				tv_content.setVisibility(View.VISIBLE);
				if (content == null) {
					setText("抱歉暂无相关文章!");
					tv_content.setGravity(Gravity.CENTER);
				} else {
					tv_content.setGravity(Gravity.LEFT);
					setText(content);
				}
			}
			break;
		case R.id.radiobtn_5:// 症状
			String user_choose_type_5 = (String) radio_5.getText();
			articles_sort.clear();
			content = null;
			// 进行二级筛选
			if (articles_type.size() > 0) {
				Log.d("sky", "articles_type.size() > 0");
				for (int i = 0; i < articles_type.size(); i++) {
					if (articles_type.get(i).getSort()
							.equals(user_choose_type_5)) {
						articles_sort.add(articles_type.get(i));
						content = articles_type.get(i).getContent();
						bundle.putString("text", content);
					}
				}
				lv_list.setVisibility(View.GONE);

				tv_content.setVisibility(View.VISIBLE);
				if (content == null) {
					setText("抱歉暂无相关文章!");
					tv_content.setGravity(Gravity.CENTER);
				} else {
					tv_content.setGravity(Gravity.LEFT);
					setText(content);
				}
			}
			break;
		case R.id.radiobtn_6:// 诊断
			String user_choose_type_6 = (String) radio_6.getText();
			articles_sort.clear();
			content = null;
			// 进行二级筛选
			if (articles_type.size() > 0) {
				for (int i = 0; i < articles_type.size(); i++) {
					if (articles_type.get(i).getSort()
							.equals(user_choose_type_6)) {
						articles_sort.add(articles_type.get(i));

						bundle.putString("text", articles_type.get(i)
								.getContent());
						content = articles_type.get(i).getContent();
					}
				}
				lv_list.setVisibility(View.GONE);

				tv_content.setVisibility(View.VISIBLE);
				if (content == null) {
					setText("抱歉暂无相关文章!");
				} else {
					setText(content);
				}
			}
			break;
		}
	}
}
