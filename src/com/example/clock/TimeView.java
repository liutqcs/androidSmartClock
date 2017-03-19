package com.example.clock;


import java.util.Calendar;

import android.R.string;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * 创建显示时间类TimeView
 */
public class TimeView extends LinearLayout {

	// 在初始化的同时指定style
	public TimeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	// 这个构造方法被xml初始化器来使用
	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	// 这个构造方法用来被代码调用
	public TimeView(Context context) {
		super(context);
		
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvTime.setText("hello");
		
		timeHandler.sendEmptyMessage(0);
		
	}
	
	//可见属性改变时候
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		// TODO Auto-generated method stub
		super.onVisibilityChanged(changedView, visibility);
		//可见的话把消息呈现出来，否则移除掉消息
		if(visibility == View.VISIBLE){
			timeHandler.sendEmptyMessage(0);
		}else{
			timeHandler.removeMessages(0);
		}
	}
	
	//刷新重新取得当前时间
	private void refreshTime(){
		Calendar c = Calendar.getInstance();
		//获取时间
		tvTime.setText(String.format(
				"%d时%d分%d秒\n%s年%s月%s日",
				c.get(Calendar.HOUR_OF_DAY),//获取小时
				c.get(Calendar.MINUTE),//获取分钟
				c.get(Calendar.SECOND),//获取秒数
				c.get(Calendar.YEAR),//获取年份
				c.get(Calendar.MONTH)+1,//获取月份
				c.get(Calendar.DAY_OF_MONTH)//获取本月的第n天
				));
	}
	
	//呈现时间每秒一次
	private Handler timeHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			refreshTime();
			//当前的view处于可见时候刷新 不可见时候没必要刷新
			if(getVisibility() == View.VISIBLE){
				timeHandler.sendEmptyMessageDelayed(0, 1000);//1000毫秒重新执行sendEmptyMessage
			}	
		};
	};
	private TextView tvTime;

}
