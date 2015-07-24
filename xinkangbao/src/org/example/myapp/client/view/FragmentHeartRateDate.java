package org.example.myapp.client.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.example.myapp.R;
import org.example.myapp.client.model.HealthStat;
import org.example.myapp.client.model.HeartRate;
import org.example.myapp.client.model.HeartRateList;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * */
public class FragmentHeartRateDate extends Fragment {
	private View view;
	private DecimalFormat df = new DecimalFormat("######0.00");
	private RelativeLayout rela_history_rate;
	private TextView tv_param_mean;
	private TextView tv_param_sdnn;
	private TextView tv_param_sdann;
	private TextView tv_param_sdnni;
	private TextView tv_param_rmssd;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.heart_rate_data, null);

		rela_history_rate =(RelativeLayout)view.findViewById(R.id.table);
		tv_param_mean = (TextView) view.findViewById(R.id.tv_param_mean);
		tv_param_sdnn = (TextView) view.findViewById(R.id.tv_param_sdnn);
		tv_param_sdann = (TextView) view.findViewById(R.id.tv_param_sdann);
		tv_param_sdnni = (TextView) view.findViewById(R.id.tv_param_sdnni);
		tv_param_rmssd = (TextView) view.findViewById(R.id.tv_param_rmssd);

		if (LoginActivity.ret.getRet_code() == 0) 
		{
			HealthStat healthStat = HealthStat.jiexi_by_str(LoginActivity.ret
					.getOrg_str());
			if (healthStat != null) 
			{
				tv_param_mean.setText(df.format(healthStat.getMean()));
				tv_param_sdnn.setText(df.format(healthStat.getSdnn()));
				tv_param_sdann.setText(df.format(healthStat.getSdann()));
				tv_param_sdnni.setText(df.format(healthStat.getSdnni()));
				tv_param_rmssd.setText(df.format(healthStat.getR_mssd()));
			}else {
				tv_param_mean.setText("0.00");
				tv_param_sdnn.setText("0.00");
				tv_param_sdann.setText("0.00");
				tv_param_sdnni.setText("0.00");
				tv_param_rmssd.setText("0.00");
				
			}
		} else {
			Toast.makeText(view.getContext().getApplicationContext(),
					"��ȡ������Ϣʧ�ܣ�  " + LoginActivity.ret.getMsg(),
					Toast.LENGTH_SHORT).show();
		}
		HeartRateList heartList = HeartRateList.parse(LoginActivity.user_ret.getOrg_str());
		
//		List<HeartRate> list = heartList.getHeartRateList();
		
//		rela_history_rate.addView(xychar(list, list.size()));
		return view;
	}
	
	public GraphicalView xychar(List<HeartRate> list, int heartlistsize) 
	{
		int i = 0;
		String[] titles = new String[] { "����" };
		List<Date[]> dates = new ArrayList<Date[]>();
		List<double[]> values = new ArrayList<double[]>();
		dates.add(new Date[heartlistsize]);
		values.add(new double[heartlistsize]);

		double ymin = 200;
		double ymax = 0;

		while (i < heartlistsize) 
		{
			dates.get(0)[i] = list.get(heartlistsize - i - 1).getDate();
			values.get(0)[i] = list.get(heartlistsize - i - 1).getHeartrate();
			if (values.get(0)[i] < ymin) 
			{
				ymin = values.get(0)[i];
			}
			if (values.get(0)[i] > ymax) 
			{
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
	

		Log.d("sky", "here");
		GraphicalView mChartView = ChartFactory.getTimeChartView(
				view.getContext().getApplicationContext(), dataset, renderer, "dd��HH��");
		Log.d("sky", "here1");
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

//	protected void onResume() {
//
//		updateInfo();
//		super.onResume();
//	}

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
