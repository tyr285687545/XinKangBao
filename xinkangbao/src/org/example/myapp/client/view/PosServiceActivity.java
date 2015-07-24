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
 * ��demo����չʾ����ڵ�ͼ����GraphicsOverlay��ӵ㡢�ߡ�����Ρ�Բ ͬʱչʾ����ڵ�ͼ����TextOverlay�������
 * 
 */
public class PosServiceActivity extends Activity {
	
	// ��ͼ���
	MapView mMapView;
	BaiduMap mBaiduMap;
	// UI���
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
			Toast.makeText(PosServiceActivity.this, "��ȡλ����Ϣʧ��", Toast.LENGTH_SHORT).show();
		} else {
			// �������ʱ��ӻ���ͼ�� 
			addCustomElementsDemo(pos.getL1(), pos.getL2());
			LatLng cenpt = new LatLng(pos.getL1(), pos.getL2());
			// �����ͼ״̬
			MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).build();
			// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
			MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
					.newMapStatus(mMapStatus);
			// �ı��ͼ״̬
			mBaiduMap.setMapStatus(mMapStatusUpdate);
		}
	}

	
	 // ��ӵ㡢�ߡ�����Ρ�Բ������
	public void addCustomElementsDemo(double l1, double l2) {
		LatLng llDot = new LatLng(l1, l2);
//		OverlayOptions ooDot = new DotOptions().center(llDot).radius(10)
//				.color(0xAAFF0000);
//		mBaiduMap.addOverlay(ooDot);
		BitmapDescriptor bitmap = BitmapDescriptorFactory    
				.fromResource(R.drawable.local_position);    
		//����MarkerOption�������ڵ�ͼ�����Marker
		OverlayOptions option = new MarkerOptions()    
		.position(llDot)    
		.icon(bitmap);    
		mBaiduMap.addOverlay(option);
		// �������
//		LatLng llText = new LatLng(l1-0.005, l2);
//		OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
//				.fontSize(24).fontColor(0xFFFF00FF).text("��ǰλ��")
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
