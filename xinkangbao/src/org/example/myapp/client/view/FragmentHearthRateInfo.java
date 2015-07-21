package org.example.myapp.client.view;

import org.example.myapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 心率信息
 * */
public class FragmentHearthRateInfo extends Fragment
{
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_heart_rata_info, null);
		Log.v("sky", "LoginActivity.user_ret.getMsg() = "+LoginActivity.user_ret.getMsg());
		Log.v("sky", "LoginActivity.user_ret.getMsg() = "+LoginActivity.user_ret.getOrg_str());
		return view;
	}
}
