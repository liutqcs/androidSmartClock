package com.example.clock;

import java.util.Timer;
import java.util.TimerTask;

import com.example.clock.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/*
 * 创建计时器类
 */
public class TimerView extends LinearLayout {

	
	/* 这个构造方法由xml文件使用
	 * 
	 */
	 
	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	 /* 这个构造方法由程序使用
	  * 
	  */
	public TimerView(Context context) {
		super(context);
	}

	
	/* 初始化完成
	 * 当View中所有的子控件 均被映射成xml后触发
	 */
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();

		btnStart = (Button) findViewById(R.id.btnStart);
		btnPause = (Button) findViewById(R.id.btnPause);
		btnResume = (Button) findViewById(R.id.btnResume);
		btnReset = (Button) findViewById(R.id.btnReset);
		

		// btnStart添加监听器
		btnStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startTime();
				 /* 开始启动时候要调整按钮
				  * 
				  */
				btnStart.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.VISIBLE);
				
			}
		});
		
		//btnPause添加监听事件
		btnPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				stopTimer();
				
				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);
			}
		});
		
		btnResume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startTime();
				btnResume.setVisibility(GONE);
				btnPause.setVisibility(VISIBLE);
			}
		});
		
		//btnReset监听器--匿名内部类实现
		btnReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//停掉时间
				stopTimer();
				//全部清零
				etHour.setText("00");
				etMin.setText("00");
				etSec.setText("00");
				
				//只有btnStart按钮可见
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
				
			}
		});

		etHour = (EditText) findViewById(R.id.etHour);
		etMin = (EditText) findViewById(R.id.etMin);
		etSec = (EditText) findViewById(R.id.etSec);

		// 初始化
		etHour.setText("00");
		// 添加事件监听器
		etHour.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				//避免文本框内容是空的时候出现错误
				if(!TextUtils.isEmpty(s)){
					int value = Integer.parseInt(s.toString());
					
					if (value > 59) {
						etHour.setText("59");
					} else if (value < 0) {
						etHour.setText("0");
					}
					
				}
				//如果里面文字发生改变都执行这个方法
				checkToEnableBtnStart();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		etMin.setText("00");
		etMin.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int acount) {
				//避免文本框内容是空的时候出现错误
				if(!TextUtils.isEmpty(s)){
					int value = Integer.parseInt(s.toString());
					
					if (value > 59) {
						etMin.setText("59");
					} else if (value < 0) {
						etMin.setText("0");
					}
					
				}
				//如果里面文字发生改变都执行这个方法
				checkToEnableBtnStart();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		etSec.setText("00");
		etSec.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//避免文本框内容是空的时候出现错误
				if(!TextUtils.isEmpty(s)){
					int value = Integer.parseInt(s.toString());
					
					if (value > 59) {
						etSec.setText("59");
					} else if (value < 0) {
						etSec.setText("0");
					}
					
				}
				//如果里面文字发生改变都执行这个方法
				checkToEnableBtnStart();
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});

		btnStart.setVisibility(View.VISIBLE);// 设置可见
		btnStart.setEnabled(false);// 没设置时间之前不可点击
		btnPause.setVisibility(View.GONE);
		btnResume.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
	}
	
	 /* 如果里面的文字不为0的话就让按钮启动可见，如果为0的话就禁止掉按钮
	  * 
	  */
	 
	private void checkToEnableBtnStart(){
		//文件不为空并且要大于0
		btnStart.setEnabled(
				(!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0) ||
				(!TextUtils.isEmpty(etMin.getText()) && Integer.parseInt(etMin.getText().toString()) > 0 )||
				(!TextUtils.isEmpty(etSec.getText()) && Integer.parseInt(etSec.getText().toString()) > 0)
				);
		
	}

	
	 /* startTime方法 其监听方法中用到
	  * 
	  */
	 
	private void startTime(){
		if(timerTask == null){
			//计算一共要执行多少次
			allTimerCount = Integer.parseInt(etHour.getText().toString())*60*60 + Integer.parseInt(etMin.getText().toString())*60 +Integer.parseInt(etSec.getText().toString());
			timerTask = new TimerTask() {
				
				//内部的run方法被timer执行 1秒执行一次
				@Override
				public void run() {
					allTimerCount--;
					
					//allTimerCount类成员 不用传过去参数 直接使用
					//每走一秒钟时间就刷新 并且要呈现的文本框里面
					handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);
					//allTimerCount<=0就停掉时间
					if(allTimerCount <= 0){
						
						//弹出对话框
						handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
						stopTimer();
					}
				}
			};
			//执行一个timeTask 每隔1秒钟执行
			timer.schedule(timerTask, 1000, 1000);// 1秒后启动任务,以后每隔1秒执行一次线程
		}
	}
	
	private void stopTimer(){
		if(timerTask != null){
			timerTask.cancel();
			timerTask = null;
		}
		
	}
	
	/*private Handler handler = new Handler(){
		public void  handMessage(android.os.Message msg){
			switch (msg.what) {
			case MSG_WHAT_TIME_TICK:
				
				int hour = allTimerCount/60/60;//得到当前的小时数
				int min =(allTimerCount/60)%60;//得到当前的分钟数
				int sec = allTimerCount%60;//取得秒数
				
				//单时钟在继续时候每隔一秒钟都设置时钟文本变化
				etHour.setText(hour + ""); 
				etMin.setText(min + ""); 
				etSec.setText(sec + ""); 
				
				break;
			case MSG_WHAT_TIME_IS_UP:
				new AlertDialog.Builder(getContext()).setTitle("Time is up").setMessage("Time is up").setNegativeButton("Cancel", null).show();
				

				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
				
				break;


			default:
				break;
			}
		};
	};*/
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_TIME_TICK:

				int hour = allTimerCount / 60 / 60;// 得到当前的小时数
				int min = (allTimerCount / 60) % 60;// 得到当前的分钟数
				int sec = allTimerCount % 60;// 取得秒数
				
				//单时钟在继续时候每隔一秒钟都设置时钟文本变化
				etHour.setText(hour + "");
				etMin.setText(min + "");
				etSec.setText(sec + "");

				break;
			case MSG_WHAT_TIME_IS_UP:
				new AlertDialog.Builder(getContext()).setTitle("Time is up").setMessage("Time is up").setNegativeButton("Cancel", null).show();

				//其他按钮设置只呈现开始按钮
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);

				break;
			default:
				break;
			}
		};
	};
	
	private static final int MSG_WHAT_TIME_IS_UP = 1;
	private static final int MSG_WHAT_TIME_TICK = 2;//时钟一格一格往下走
	
	private int allTimerCount = 0;//总计时间
	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	
	private Button btnStart, btnPause, btnResume, btnReset;
	private EditText etHour, etMin, etSec;
}


//public class TimerView extends LinearLayout {
//
//	/*
//	 * 这个构造方法由xml文件使用
//	 */
//	public TimerView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	/*
//	 * 这个构造方法由程序使用
//	 */
//	public TimerView(Context context) {
//		super(context);
//	}
//
//	/*
//	 * 初始化完成
//	 */
//	@Override
//	protected void onFinishInflate() {
//		super.onFinishInflate();
//
//		btnStart = (Button) findViewById(R.id.btnStart);
//		btnPause = (Button) findViewById(R.id.btnPause);
//		btnReset = (Button) findViewById(R.id.btnReset);
//		btnResume = (Button) findViewById(R.id.btnResume);
//
//		// btnStart添加监听器
//		btnStart.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				startTimer();
//
//				// 开始启动时候要调整按钮状态
//				btnStart.setVisibility(View.GONE);
//				btnPause.setVisibility(View.VISIBLE);
//				btnReset.setVisibility(View.VISIBLE);
//			}
//		});
//
//		// btnPause添加监听事件
//		btnPause.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				stopTimer();
//
//				btnPause.setVisibility(View.GONE);
//				btnResume.setVisibility(View.VISIBLE);
//			}
//		});
//
//		// btnPesume添加监听事件
//		btnResume.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				startTimer();
//
//				btnResume.setVisibility(View.GONE);
//				btnPause.setVisibility(View.VISIBLE);
//			}
//		});
//
//		btnReset.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 停掉时间
//				stopTimer();
//
//				// 清零
//				etHour.setText("00");
//				etMin.setText("00");
//				etSec.setText("00");
//
//				// 只有btnStart按钮可见
//				btnReset.setVisibility(View.GONE);
//				btnResume.setVisibility(View.GONE);
//				btnPause.setVisibility(View.GONE);
//				btnStart.setVisibility(View.VISIBLE);
//			}
//		});
//
//		etHour = (EditText) findViewById(R.id.etHour);
//		etMin = (EditText) findViewById(R.id.etMin);
//		etSec = (EditText) findViewById(R.id.etSec);
//
//		// 初始化
//		etHour.setText("00");
//		// 添加事件监听器
//		etHour.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// 避免文本框内容是空的时候出现错误
//				if (!TextUtils.isEmpty(s)) {
//					int value = Integer.parseInt(s.toString());
//
//					// 设置的数值有问题时候置为0
//					if (value > 59) {
//						etHour.setText("59");
//					} else if (value < 0) {
//						etHour.setText("0");
//					}
//				}
//				// 如果里面文字发生改变都执行这个方法
//				checkToEnableBtnStart();
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//		});
//		etMin.setText("00");
//		etMin.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				if (!TextUtils.isEmpty(s)) {
//					int value = Integer.parseInt(s.toString());
//
//					if (value > 59) {
//						etMin.setText("59");
//					} else if (value < 0) {
//						etMin.setText("0");
//					}
//				}
//				checkToEnableBtnStart();
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//		});
//		etSec.setText("00");
//		etSec.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				if (!TextUtils.isEmpty(s)) {
//					int value = Integer.parseInt(s.toString());
//
//					if (value > 59) {
//						etSec.setText("59");
//					} else if (value < 0) {
//						etSec.setText("0");
//					}
//				}
//				checkToEnableBtnStart();
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//			}
//		});
//
//		btnStart.setVisibility(View.VISIBLE);// 设置可见
//		btnStart.setEnabled(false);// 没设置时间之前不可点击
//		btnPause.setVisibility(View.GONE);
//		btnResume.setVisibility(View.GONE);
//		btnReset.setVisibility(View.GONE);
//	}
//
//	/*
//	 * 如果里面的文字不为0的话就让按钮启动可见，如果为0的话就禁止掉按钮
//	 */
//	private void checkToEnableBtnStart() {
//		btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()) && Integer
//				.parseInt(etHour.getText().toString()) > 0)
//				|| (!TextUtils.isEmpty(etMin.getText()) && Integer
//						.parseInt(etMin.getText().toString()) > 0)
//				|| (!TextUtils.isEmpty(etSec.getText()) && Integer
//						.parseInt(etSec.getText().toString()) > 0));
//	}
//
//	/*
//	 * startTime()方法 其监听方法中用到
//	 */
//	private void startTimer() {
//		if (timerTask == null) {
//			// 计算一共要执行多少次
//			allTimerCount = Integer.parseInt(etHour.getText().toString()) * 60
//					* 60 + Integer.parseInt(etMin.getText().toString()) * 60
//					+ Integer.parseInt(etSec.getText().toString());
//			timerTask = new TimerTask() {
//
//				// 内部的run方法被timer执行 1秒执行一次
//				@Override
//				public void run() {
//					allTimerCount--;
//
//					// allTimerCount类成员 不用传过去参数 直接使用
//					// 每走一秒钟时间就刷新 并且要呈现的文本框里面
//					handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);
//					// allTimerCount<=0就停掉时间
//					if (allTimerCount <= 0) {
//						handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
//						stopTimer();
//					}
//				}
//			};
//			// 执行一个timeTask 每隔1秒钟执行
//			timer.schedule(timerTask, 1000, 1000);
//		}
//	}
//
//	private void stopTimer() {
//		if (timerTask != null) {
//			timerTask.cancel();
//			timerTask = null;
//		}
//	}
//
//	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case MSG_WHAT_TIME_TICK:
//
//				int hour = allTimerCount / 60 / 60;// 得到当前的小时数
//				int min = (allTimerCount / 60) % 60;// 得到当前的分钟数
//				int sec = allTimerCount % 60;// 取得秒数
//				
//				//单时钟在继续时候每隔一秒钟都设置时钟文本变化
//				etHour.setText(hour + "");
//				etMin.setText(min + "");
//				etSec.setText(sec + "");
//
//				break;
//			case MSG_WHAT_TIME_IS_UP:
//				new AlertDialog.Builder(getContext()).setTitle("Time is up")
//						.setMessage("Time is up")
//						.setNegativeButton("Cancel", null).show();
//
//				btnReset.setVisibility(View.GONE);
//				btnResume.setVisibility(View.GONE);
//				btnPause.setVisibility(View.GONE);
//				btnStart.setVisibility(View.VISIBLE);
//
//				break;
//			default:
//				break;
//			}
//		};
//	};
//
//	private static final int MSG_WHAT_TIME_IS_UP = 1;//时钟一格一格往下走
//	private static final int MSG_WHAT_TIME_TICK = 2;
//
//	private int allTimerCount = 0;//总计时间
//	private Timer timer = new Timer();
//	private TimerTask timerTask = null;
//	private Button btnStart, btnPause, btnResume, btnReset;
//	private EditText etHour, etMin, etSec;
//
//}




