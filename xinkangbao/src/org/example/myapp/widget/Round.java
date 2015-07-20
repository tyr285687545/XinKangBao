package org.example.myapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
/**
 * 红点
 * @author rcq
 *
 */
public class Round extends View{
	 private Paint paint;
	 private int size=20;//设置红点数量
	 private int windX;
	 private int windY;
	   @Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		windX=right-left;
		windY=bottom-top;
		
	}
	public Round(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		paint=new Paint();
	}

	public Round(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public Round(Context context) {
		this(context,null);
	}

	/**重写该方法，实现画红点*/
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float roundx,roundy,radius;// 圆控件的长和高以及半径
		float textx,texty;
		roundx=windX/2;
		roundy=windY/2;
		radius=windX>windY?windY/2:windX/2;
		
		if(size>9&&size<100) textx=roundx-radius*2/3.5f+0.5f;//两位数
		else if(size>99) 
			{
			   textx=roundx-radius*3 /3.5f+0.5f;//三位数
			}
		else textx=roundx-radius/3.5f+0.5f;
		texty=roundy+radius/2.5f+0.5f;
		paint.setColor(Color.RED);// 设置画笔颜色
		paint.setStyle(Style.FILL);// 设置画笔填充
		canvas.drawCircle(roundx, roundy, radius, paint);// 用画笔在画布上添加一个圆，不只可以添加圆，还可以添加矩形等！
		paint.setColor(Color.WHITE);// 设置画笔颜色
		paint.setTextSize(radius);
		canvas.drawText(size+"", textx, texty, paint);// 用画笔在画布上添加文字，中间两个参数对应的是坐标。
	}
	
	//设置消息数量
	public void setSize(int size)
	{
		this.size=size;
		invalidate();
	}
}
