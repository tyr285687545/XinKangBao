package org.example.myapp.client.view;
import org.example.myapp.R;
import org.example.myapp.client.model.Position;
import org.example.myapp.common.ReturnObj;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * 此demo用来展示如何在地图上用GraphicsOverlay添加点、线、多边形、圆 同时展示如何在地图上用TextOverlay添加文字
 * 
 */
public class PosServiceActivity extends Activity {
	
	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	// UI相关
	Button resetBtn;
	Button clearBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_geometry);
		
//		SDKInitializer.initialize(getApplicationContext());
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		
		mBaiduMap = mMapView.getMap();
		Position pos = get_pat_pos();
		if (pos == null) {
			Toast.makeText(PosServiceActivity.this, "获取位置信息失败", Toast.LENGTH_SHORT).show();
		} else {
			// 界面加载时添加绘制图层 
			addCustomElementsDemo(pos.getL1(), pos.getL2());
			LatLng cenpt = new LatLng(pos.getL1(), pos.getL2());
			// 定义地图状态
			MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).build();
			// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
					.newMapStatus(mMapStatus);
			// 改变地图状态
			mBaiduMap.setMapStatus(mMapStatusUpdate);
		}
	}

	
	 // 添加点、线、多边形、圆、文字
	public void addCustomElementsDemo(double l1, double l2) {
		LatLng llDot = new LatLng(l1, l2);
//		OverlayOptions ooDot = new DotOptions().center(llDot).radius(10)
//				.color(0xAAFF0000);
//		mBaiduMap.addOverlay(ooDot);
		BitmapDescriptor bitmap = BitmapDescriptorFactory    
				.fromResource(R.drawable.local_position);    
		//构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions()    
		.position(llDot)    
		.icon(bitmap);    
		mBaiduMap.addOverlay(option);
		// 添加文字
//		LatLng llText = new LatLng(l1-0.005, l2);
//		OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
//				.fontSize(24).fontColor(0xFFFF00FF).text("当前位置")
//				.position(llText);
//		mBaiduMap.addOverlay(ooText);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}
	
	public Position get_pat_pos() {
		
		ReturnObj ret = MainActivity.client_in_strict_mode.get_patient_device(MainActivity.myUser.getId());
		if (ret.getRet_code() == 0) 
		{
			String device = Position.get_device_id(ret.getOrg_str());
			if (device.length() != 0) 
			{
				ret = MainActivity.client_in_strict_mode.get_patient_pos(device);
				if (ret.getRet_code() == 0)
				{
					return Position.paser_str(ret.getOrg_str());
				}
			}
		}
		return null;
	}
}
