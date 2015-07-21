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
		// ���ܴ�FragmentActivity��������ArchivesBean����
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
		// ��Ϊ����archivesBean�е�sick��medical�����飬������Ҫ�ٴν���һ�Ρ�
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
				fzjcStr.append("Ѫѹ:	" + FzjcJson.getString("Ѫѹ") + "\n");
				fzjcStr.append("����:	" + FzjcJson.getString("����") + "\n");
				fzjcStr.append("Ѫ�쵰��:	" + FzjcJson.getString("Ѫ�쵰��") + "\n");
				fzjcStr.append("�ո�Ѫ��:	" + FzjcJson.getString("�ո�Ѫ��") + "\n");
				fzjcStr.append("��������:	" + FzjcJson.getString("��������") + "\n");
				fzjcStr.append("�ܵ��̴�:	" + FzjcJson.getString("�ܵ��̴�") + "\n");
				fzjcStr.append("���ܶ�֬����:	" + FzjcJson.getString("���ܶ�֬����") + "\n");
				fzjcStr.append("���ܶ�֬����Ѫѹ:	" + FzjcJson.getString("���ܶ�֬����")
						+ "\n");
				fzjcStr.append("NT-��oBNP:	" + FzjcJson.getString("NT-��oBNP")
						+ "\n");
				fzjcStr.append("�ȱ�ת��ø:	" + FzjcJson.getString("�ȱ�ת��ø") + "\n");
				fzjcStr.append("����:	" + FzjcJson.getString("����") + "\n");
				fzjcStr.append("����ʳ���Ѫ��ֵ:	" + FzjcJson.getString("����ʳ���Ѫ��ֵ")
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
