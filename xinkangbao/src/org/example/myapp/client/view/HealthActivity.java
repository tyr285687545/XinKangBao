package org.example.myapp.client.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.example.myapp.R;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.client.model.HealthStat;
import org.example.myapp.client.model.HeartRate;
import org.example.myapp.client.model.HeartRateList;
import org.example.myapp.client.view.MainActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HealthActivity extends Activity {

	private TextView meanTextview;
	private TextView sdnnTextview;
	private TextView sdannTextview;
	private TextView sdnniTextview;
	private TextView rmssdTextview;
	private static DecimalFormat df = new DecimalFormat("######0.00");
	private LinearLayout health_linelayout;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.health_manager);

		health_linelayout = (LinearLayout) findViewById(R.id.health_linelayout);
		meanTextview = (TextView) findViewById(R.id.id_mean);
		sdnnTextview = (TextView) findViewById(R.id.id_sdnn);
		sdannTextview = (TextView) findViewById(R.id.id_sdann);

		sdnniTextview = (TextView) findViewById(R.id.id_sdnni);
		rmssdTextview = (TextView) findViewById(R.id.id_r_mssd);

		ManageActivity.addActiviy("healthActivity", HealthActivity.this);
		updateInfo();
	}
	
	public void updateInfo() {

		if (LoginActivity.ret.getRet_code() == 0) {
			HealthStat healthStat = HealthStat.jiexi_by_str(LoginActivity.ret.getOrg_str());
			if (healthStat != null) {
				meanTextview.setText(df.format(healthStat.getMean()));
				sdnnTextview.setText(df.format(healthStat.getSdnn()));
				sdannTextview.setText(df.format(healthStat.getSdann()));
				sdnniTextview.setText(df.format(healthStat.getSdnni()));
				rmssdTextview.setText(df.format(healthStat.getR_mssd()));
			}
		} else {
			Toast.makeText(HealthActivity.this, "��ȡ������Ϣʧ�ܣ�  " + LoginActivity.ret.getMsg(),
					Toast.LENGTH_SHORT).show();
		}
		addView();
	}

	private void addView()
	{
		if (LoginActivity.user_ret.getRet_code() == 0) {
			HeartRateList heartList = HeartRateList.parse(LoginActivity.user_ret.getOrg_str());
			List<HeartRate> list = heartList.getHeartRateList();
			int heartlistsize = list.size();
			
			if (heartlistsize != 0) 
			{
				// ��״ͼ���������е�����
				health_linelayout.addView(xychar(list, heartlistsize));
				
			}
		} else {
			Toast.makeText(HealthActivity.this, "��ȡ������Ϣʧ�ܣ�  " + LoginActivity.user_ret.getMsg(),
					Toast.LENGTH_SHORT).show();
		}

	}
	public GraphicalView xychar(List<HeartRate> list, int heartlistsize) {
		int i = 0;
		String[] titles = new String[] { "����" };
		List<Date[]> dates = new ArrayList<Date[]>();
		List<double[]> values = new ArrayList<double[]>();
		dates.add(new Date[heartlistsize]);
		values.add(new double[heartlistsize]);

		double ymin = 200;
		double ymax = 0;

		while (i < heartlistsize) {
			dates.get(0)[i] = list.get(heartlistsize - i - 1).getDate();
			values.get(0)[i] = list.get(heartlistsize - i - 1).getHeartrate();
			if (values.get(0)[i] < ymin) {
				ymin = values.get(0)[i];
			}
			if (values.get(0)[i] > ymax) {
				ymax = values.get(0)[i];
			}
			i = i + 1;
		}
		ymin = 40;
		ymax = 120;
		int[] colors = new int[] { Color.BLUE };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT };
		// �����Ⱦ
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		// ������е����ݼ�, //�������ݼ��Լ���Ⱦ
		XYMultipleSeriesDataset dataset = buildDateDataset(titles, dates,
				values);

		setChartSettings(renderer, "����ͼ", "Date", "����",
				dates.get(0)[0].getTime(),
				dates.get(0)[heartlistsize - 1].getTime(), ymin,
				ymax, Color.BLACK, Color.LTGRAY);

		int length = renderer.getSeriesRendererCount();
		/***************/
		for (i = 0; i < length; i++) {
			SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
			ssr.setChartValuesTextAlign(Align.RIGHT);
			ssr.setChartValuesTextSize(12);
			ssr.setDisplayChartValues(true);
		}
		// ����x���ǩ��
		renderer.setXLabels(heartlistsize / 8);
		// ����Y���ǩ��
		renderer.setYLabels(heartlistsize / 6);
		renderer.setMarginsColor(0x00888888);
		
		renderer.setPanLimits(new double[] { dates.get(0)[0].getTime() - 100,
				dates.get(0)[heartlistsize - 1].getTime() + 100, ymin - 10,
				ymax + 10 });
	

		GraphicalView mChartView = ChartFactory.getTimeChartView(
				getApplicationContext(), dataset, renderer, "dd��HH��");
		return mChartView;
	}

	public void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);

		// ����x���y��ı�ǩ���뷽ʽ
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.CENTER);

		// ������ʵ����
		renderer.setShowGrid(true);

		renderer.setShowAxes(true);
		// ��������ͼ֮��ľ���
		// renderer.setBarSpacing(0.2);
		renderer.setPanEnabled(true, false); // ���û���,����Ǻ�����Ի���,���򲻿ɻ���

		renderer.setClickEnabled(false);
		// ����x���y���ǩ����ɫ
		renderer.setXLabelsColor(Color.RED);
		renderer.setYLabelsColor(0, Color.RED);

		// ����ͼ���������С
		renderer.setLegendTextSize(12);
	}

	protected void onResume() {

		updateInfo();
		super.onResume();
	}

	public XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			//r.setPointStyle(PointStyle.TRIANGLE);				
			
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	public XYMultipleSeriesDataset buildDateDataset(String[] titles,
			List<Date[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}
}
