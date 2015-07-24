package org.example.myapp.client.view;

import java.text.DecimalFormat;
import org.example.myapp.R;
import org.example.myapp.client.model.HealthStat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 健康管理:心率信息
 * */
public class FragmentHearthRateInfo extends Fragment {
	private View view;
	private boolean isShow = true;
	private DecimalFormat df = new DecimalFormat("######0.00");
	private RelativeLayout layout;

	private TextView tv_param_1_content;
	private TextView tv_param_2_content;
	private TextView tv_param_3_content;
	private TextView tv_param_4_content;
	private TextView tv_param_5_content;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.fragment_heart_rata_info, null);

		Bundle bundle = this.getArguments();

		isShow = bundle.getBoolean("isShow");

		layout = (RelativeLayout) view.findViewById(R.id.rela_rate_fenxi);

		tv_param_1_content = (TextView) view
				.findViewById(R.id.tv_param_1_content);
		tv_param_2_content = (TextView) view
				.findViewById(R.id.tv_param_2_content);
		tv_param_3_content = (TextView) view
				.findViewById(R.id.tv_param_3_content);
		tv_param_4_content = (TextView) view
				.findViewById(R.id.tv_param_4_content);
		tv_param_5_content = (TextView) view
				.findViewById(R.id.tv_param_5_content);
		if (isShow) {
			layout.setVisibility(View.VISIBLE);
			if (LoginActivity.ret.getRet_code() == 0) 
			{
				HealthStat healthStat = HealthStat
						.jiexi_by_str(LoginActivity.ret.getOrg_str());
				if (healthStat != null) {
					tv_param_1_content.setText(df.format(healthStat.getMean()));
					tv_param_2_content.setText(df.format(healthStat.getSdnn()));
					tv_param_3_content
							.setText(df.format(healthStat.getSdann()));
					tv_param_4_content
							.setText(df.format(healthStat.getSdnni()));
					tv_param_5_content
							.setText(df.format(healthStat.getR_mssd()));
				}
			} else {
				Toast.makeText(view.getContext().getApplicationContext(),
						"获取健康信息失败：  " + LoginActivity.ret.getMsg(),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			layout.setVisibility(View.GONE);
		}

		return view;
	}
}
