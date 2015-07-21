package org.example.myapp.client.view;

import org.example.myapp.R;
import org.example.myapp.client.model.ArchivesBean;
import org.example.myapp.common.AppContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentHealthData extends Fragment {

	private View view;
	private Bundle bundle;
	private ArchivesBean archivesBean;

	private RelativeLayout baseinfo;
	private boolean isShow = true;
	private TextView tv_install_disease_type;
	private TextView tv_health_checkout;
	private TextView tv_healeh_medical1;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_archives, null);
		// 接受从FragmentActivity传过来的ArchivesBean数据
		bundle = this.getArguments();
		if (bundle != null) {
			archivesBean = (ArchivesBean) bundle.getSerializable("info");
		} else {
			archivesBean = AppContext.getInstance().getBean();
		}
		isShow = bundle.getBoolean("isShow");
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		StringBuffer sickStr = new StringBuffer();
		StringBuffer medicalStr = new StringBuffer();
		StringBuffer fzjcStr = new StringBuffer();

		baseinfo = (RelativeLayout) view.findViewById(R.id.baseinfo);
		baseinfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(view.getContext()
						.getApplicationContext(), HealthDataBaseInfo.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("info", archivesBean);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		tv_install_disease_type = (TextView) view
				.findViewById(R.id.tv_healeh_chuyuan_item1);
		tv_healeh_medical1 = (TextView) view
				.findViewById(R.id.tv_healeh_medical1);
		tv_health_checkout = (TextView) view
				.findViewById(R.id.et_healeh_checkout2);
		// 因为对象archivesBean中的sick和medical是数组，这里需要再次解析一次。
		try {
			JSONArray sickArray = new JSONArray(archivesBean.getSick());
			for (int i = 0; i < sickArray.length(); i++) {
				sickStr.append(sickArray.get(i) + "\n");
			}
			JSONArray MedicalArray = new JSONArray(archivesBean.getMedicine());
			for (int i = 0; i < MedicalArray.length(); i++) {
				medicalStr.append(MedicalArray.get(i) + "\n");
			}
			JSONObject FzjcJson = new JSONObject(archivesBean.getFzjc());
			if (FzjcJson.toString().length() > 2) {
				fzjcStr.append("血压:	" + FzjcJson.getString("血压") + "\n");
				fzjcStr.append("心率:	" + FzjcJson.getString("心率") + "\n");
				fzjcStr.append("血红蛋白:	" + FzjcJson.getString("血红蛋白") + "\n");
				fzjcStr.append("空腹血糖:	" + FzjcJson.getString("空腹血糖") + "\n");
				fzjcStr.append("甘油三酯:	" + FzjcJson.getString("甘油三酯") + "\n");
				fzjcStr.append("总胆固醇:	" + FzjcJson.getString("总胆固醇") + "\n");
				fzjcStr.append("低密度脂蛋白:	" + FzjcJson.getString("低密度脂蛋白") + "\n");
				fzjcStr.append("高密度脂蛋白血压:	" + FzjcJson.getString("高密度脂蛋白")
						+ "\n");
				fzjcStr.append("NT-βoBNP:	" + FzjcJson.getString("NT-βoBNP")
						+ "\n");
				fzjcStr.append("谷丙转氨酶:	" + FzjcJson.getString("谷丙转氨酶") + "\n");
				fzjcStr.append("肌酐:	" + FzjcJson.getString("肌酐") + "\n");
				fzjcStr.append("心脏彩超射血分值:	" + FzjcJson.getString("心脏彩超射血分值")
						+ "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		tv_health_checkout.setText(fzjcStr);
		tv_install_disease_type.setText(sickStr);
		tv_healeh_medical1.setText(medicalStr);
	}

}
