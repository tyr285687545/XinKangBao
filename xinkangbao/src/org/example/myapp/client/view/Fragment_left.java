package org.example.myapp.client.view;

import org.example.myapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Fragment_left extends Fragment implements OnClickListener
{
	private static final int VIDEO_CONTENT_DESC_MAX_LINE = 4;// 默认展示最大行数3行
	private static final int SHRINK_UP_STATE = 1;// 收起状态
	private static final int SPREAD_STATE = 2;// 展开状态
	private static int mState_PERSON = SHRINK_UP_STATE;//默认收起状态
	private static int mState_READ = SHRINK_UP_STATE;//默认收起状态
	private static int mState_EXPLAIN = SHRINK_UP_STATE;//默认收起状态
	//点击展开图片
	private TextView tv_person_more;
	private TextView tv_read_more;
	private TextView tv_explain_more;
	private RelativeLayout rela_moreperson;
	private RelativeLayout rela_more_read;
	private RelativeLayout rela_more_explain;
	
	//展开的textView
	private TextView tv_person_text;
	private TextView tv_read_detail;
	private TextView tv_explain_detail;
	
	//点击收起的图片
	private TextView tv_spread;
	private TextView tv_spread_read;
	private TextView tv_spread_explain;

	private TextView tv_hospital;//毕业学校
	private TextView tv_hospital_;//毕业学校
	private TextView tv_goodat_detail;//擅长详细tv
	private View view;
	private Bundle bundle;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		view = inflater.inflate(R.layout.detail_fragment, null);
		bundle = this.getArguments(); 
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		//展开按钮
		tv_person_more = (TextView)view.findViewById(R.id.tv_more);
		tv_read_more = (TextView)view.findViewById(R.id.tv_more_read);
		tv_explain_more = (TextView)view.findViewById(R.id.tv_more_explain);
		rela_moreperson = (RelativeLayout)view.findViewById(R.id.rela_more);
		rela_more_read = (RelativeLayout)view.findViewById(R.id.rela_more_read);
		rela_more_explain = (RelativeLayout)view.findViewById(R.id.rela_more_explain);
		
		//擅长
		tv_goodat_detail = (TextView)view.findViewById(R.id.tv_goodat_detail);
		
		//需要展开收起的TextView
		tv_person_text = (TextView)view.findViewById(R.id.tv_person_detail);
		tv_read_detail = (TextView)view.findViewById(R.id.tv_read_detail);
		tv_explain_detail = (TextView)view.findViewById(R.id.tv_explain_detail);
		
		//收起按钮
		tv_spread = (TextView)view.findViewById(R.id.tv_spread);
		tv_spread_read = (TextView)view.findViewById(R.id.tv_spread_read);
		tv_spread_explain = (TextView)view.findViewById(R.id.tv_spread_explain);
		
		tv_hospital= (TextView)view.findViewById(R.id.tv_hospital);
		tv_hospital_= (TextView)view.findViewById(R.id.tv_detail_);
		
		//设置简历,擅长 内容
		if (bundle!=null) 
		{
			tv_person_text.setText(bundle.getString("person"));
			tv_goodat_detail.setText(bundle.getString("major"));
			tv_hospital.setText(bundle.getString("job"));
			tv_hospital_.setText(bundle.getString("department"));
		}
		rela_moreperson.setOnClickListener(this);
		rela_more_read.setOnClickListener(this);
		rela_more_explain.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rela_more:
			if (mState_PERSON == SPREAD_STATE) 
			{
				tv_person_text.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
				tv_person_text.requestLayout();
				tv_spread.setVisibility(View.GONE);
				tv_person_more.setVisibility(View.VISIBLE);
				mState_PERSON = SHRINK_UP_STATE;
			} else if (mState_PERSON == SHRINK_UP_STATE) 
			{
				tv_person_text.setMaxLines(Integer.MAX_VALUE);
				tv_person_text.requestLayout();
				tv_spread.setVisibility(View.VISIBLE);
				tv_person_more.setVisibility(View.GONE);
				mState_PERSON = SPREAD_STATE;
			}
			break;
		case R.id.rela_more_read:
			if (mState_READ == SPREAD_STATE) 
			{
				tv_read_detail.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
				tv_read_detail.requestLayout();
				tv_read_more.setVisibility(View.VISIBLE);
				tv_spread_read.setVisibility(View.GONE);
				mState_READ = SHRINK_UP_STATE;
			} else if (mState_READ == SHRINK_UP_STATE) {
				tv_read_detail.setMaxLines(Integer.MAX_VALUE);
				tv_read_detail.requestLayout();
				tv_read_more.setVisibility(View.GONE);
				tv_spread_read.setVisibility(View.VISIBLE);
				mState_READ = SPREAD_STATE;
			}
			break;
		case R.id.rela_more_explain:
			if (mState_EXPLAIN == SPREAD_STATE) 
			{
				tv_explain_detail.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
				tv_explain_detail.requestLayout();
				tv_explain_more.setVisibility(View.VISIBLE);
				tv_spread_explain.setVisibility(View.GONE);
				mState_EXPLAIN = SHRINK_UP_STATE;
			} else if (mState_EXPLAIN == SHRINK_UP_STATE) 
			{
				tv_explain_detail.setMaxLines(Integer.MAX_VALUE);
				tv_explain_detail.requestLayout();
				tv_explain_more.setVisibility(View.GONE);
				tv_spread_explain.setVisibility(View.VISIBLE);
				mState_EXPLAIN = SPREAD_STATE;
			}
			break;
		}
	}
}
