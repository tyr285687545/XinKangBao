package org.example.myapp.client.view;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class DemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// ��ʹ�� SDK �����֮ǰ��ʼ�� context ��Ϣ������ ApplicationContext
		SDKInitializer.initialize(this);
	}

}