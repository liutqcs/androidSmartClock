package com.example.clock;

import java.util.Timer;
import java.util.TimerTask;

import com.example.clock.R;

import android.R.string;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;





public class StopWatchView extends LinearLayout {

	public StopWatchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		tvHour = (TextView) findViewById(R.id.timeHour);
		tvHour.setText("00");
		tvMin = (TextView) findViewById(R.id.timeMin);
		tvMin.setText("00");
		tvSec = (TextView) findViewById(R.id.timeSec);
		tvSec.setText("00");
		tvMSec = (TextView) findViewById(R.id.timeMsec);
		tvMSec.setText("00");
		
		btnLap = (Button) findViewById(R.id.btnSWLap);
		btnLap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//计时
				adapter.insert(String.format("%d时%d分%d秒%d", tenMSecs/100/60/60,tenMSecs/100/60%60,tenMSecs/100%60,tenMSecs%100), 0);
			}
		});
		btnPause = (Button) findViewById(R.id.btnSWPause);
		btnPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				stopTimer();
				
				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);
				btnLap.setVisibility(View.GONE);
				btnReset.setVisibility(View.VISIBLE);
			}
		});
		btnReset = (Button) findViewById(R.id.btnSWReset);
		btnReset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				stopTimer();
				tenMSecs = 0;
				//清掉
				adapter.clear();
				
				btnLap.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
			}
		});
		btnResume = (Button) findViewById(R.id.btnSWResume);
		btnResume.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startTimer();
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.GONE);
				btnLap.setVisibility(View.VISIBLE);
			}
		});
		btnStart = (Button) findViewById(R.id.btnSWStart);
		btnStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startTimer();
				
				btnStart.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnLap.setVisibility(View.VISIBLE);
			}
		});
		btnLap.setVisibility(View.GONE);
		btnPause.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
		btnResume.setVisibility(View.GONE);
		
		lvTimeList=(ListView) findViewById(R.id.lvWatchTimeList);
		adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
		lvTimeList.setAdapter(adapter);
		
		showTimeTask = new TimerTask() {
			
			@Override
			public void run() {
				hander.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
			}
		};
		timer.schedule(showTimeTask, 200, 200);//一秒刷新5次
	}
	
	private void startTimer(){
		if (timerTask==null) {
			timerTask = new TimerTask() {
				
				@Override
				public void run() {
					tenMSecs++;
				}
			};
			timer.schedule(timerTask, 10, 10);//每隔10毫秒执行run方法以后每隔10秒执行一次线程
	          
		}
	}
	
	private void stopTimer(){
		if (timerTask!=null) {
			timerTask.cancel();
			timerTask=null;
		}
	}
	
	private int tenMSecs = 0;
	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	private TimerTask showTimeTask = null;

	private TextView tvHour,tvMin,tvSec,tvMSec;
	private Button btnStart,btnResume,btnReset,btnPause,btnLap;
	private ListView lvTimeList;
	private ArrayAdapter<String> adapter;
	
	private Handler hander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_SHOW_TIME:
				tvHour.setText(tenMSecs/100/60/60+"");//得到小时
				tvMin.setText(tenMSecs/100/60%60+"");
				tvSec.setText(tenMSecs/100%60+"");
				tvMSec.setText(tenMSecs%100+"");
				break;
			default:
				break;
			}
		};
	};
	
	private static final int MSG_WHAT_SHOW_TIME = 1;

	public void onDestory() {
		timer.cancel();
	}
}





//public class StopWatchView extends LinearLayout {
//
//	// 使用xml资源来初始化这个构造方法
//	public StopWatchView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	protected void onFinishInflate() {
//		super.onFinishInflate();
//
//		tvHour = (TextView) findViewById(R.id.timeHour);
//		tvMin = (TextView) findViewById(R.id.timeMin);
//		tvSec = (TextView) findViewById(R.id.timeSec);
//		tvMsec = (TextView) findViewById(R.id.timeMsec);
//
//		// 初始值设为0
//		tvHour.setText("0");
//		tvMin.setText("0");
//		tvSec.setText("0");
//		tvMsec.setText("0");
//
//		btnLap = (Button) findViewById(R.id.btnSWLap);
//		// 计时按钮Lap监听器
//		btnLap.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				/*
//				 * 添加--每次都添加到第一位 时=tenMSecs/100/60/60 分=tenMSecs/100/60%60
//				 * 秒=tenMSecs/100&60 毫秒=tenMSecs%100
//				 */
//				adapter.insert(String.format("%d:%d:%d.%d",
//						tenMSecs / 100 / 60 / 60, tenMSecs / 100 / 60 % 60,
//						tenMSecs / 100 & 60, tenMSecs % 100), 0);
//			}
//		});
//		btnPause = (Button) findViewById(R.id.btnSWPause);
//		btnPause.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				stopTimer();
//
//				btnPause.setVisibility(View.GONE);
//				btnResume.setVisibility(View.VISIBLE);
//				btnLap.setVisibility(View.GONE);
//				btnReset.setVisibility(View.VISIBLE);
//			}
//		});
//
//		btnReset = (Button) findViewById(R.id.btnSWReset);
//		btnReset.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				stopTimer();
//				// 清零
//				tenMSecs = 0;
//				// 重置的时候列表清掉
//				adapter.clear();
//
//				btnLap.setVisibility(View.GONE);
//				btnPause.setVisibility(View.GONE);
//				btnReset.setVisibility(View.GONE);
//				btnResume.setVisibility(View.GONE);
//				btnStart.setVisibility(View.VISIBLE);
//			}
//		});
//
//		btnResume = (Button) findViewById(R.id.btnSWResume);
//		btnResume.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				startTimer();
//
//				btnResume.setVisibility(View.GONE);
//				btnPause.setVisibility(View.VISIBLE);
//				btnReset.setVisibility(View.GONE);
//				btnLap.setVisibility(View.VISIBLE);
//			}
//		});
//
//		btnStart = (Button) findViewById(R.id.btnSWStart);
//
//		// 为btnStart设置监听器
//		btnStart.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// 启动一个计时器
//				startTimer();
//
//				btnStart.setVisibility(View.GONE);
//				btnPause.setVisibility(View.VISIBLE);
//				btnLap.setVisibility(View.VISIBLE);
//			}
//		});
//
//		// 开始时候除了Start按钮可见 其他按钮都不可见的
//		btnLap.setVisibility(View.GONE);
//		btnPause.setVisibility(View.GONE);
//		btnReset.setVisibility(View.GONE);
//		btnResume.setVisibility(View.GONE);
//
//		lvTimeList = (ListView) findViewById(R.id.lvWatchTimeList);
//
//		// 初始化adapter 资源使用系统的
//		adapter = new ArrayAdapter<>(getContext(),
//				android.R.layout.simple_list_item_1);
//		lvTimeList.setAdapter(adapter);
//
//		// 启动showTimeTask
//		showTimeTask = new TimerTask() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
//			}
//		};
//		timer.schedule(showTimeTask, 200, 200);
//	}
//
//	// 开始计时的方法 用timer不断执行handler
//	private void startTimer() {
//		if (timerTask == null) {
//			timerTask = new TimerTask() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					tenMSecs++;
//				}
//			};
//			// 10ms刷新一次啊
//			timer.schedule(timerTask, 10, 10);
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
//	private int tenMSecs = 0;// 10ms的个数
//	private Timer timer = new Timer();
//	private TimerTask timerTask = null;
//	private TimerTask showTimeTask = null;// 呈现的
//
//	private TextView tvHour, tvMin, tvSec, tvMsec;
//	private Button btnStart, btnResume, btnReset, btnPause, btnLap;
//
//	private ListView lvTimeList;
//
//	private ArrayAdapter<String> adapter;
//
//	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			// 不断呈现当前真实时间
//			case MSG_WHAT_SHOW_TIME:
//				// 取得小时 tvHour=tenMSesc/100/60/60
//
//				// tvHour.setText(tenMSecs/100/60/60+"");
//				// tvMin.setText(tenMSecs/100/60%60+"");
//				// tvSec.setText(tenMSecs/100%60);
//				// tvMsec.setText(tenMSecs%100+"");
//
//				tvHour.setText(tenMSecs / 100 / 60 / 60 + "");
//				tvMin.setText(tenMSecs / 100 / 60 % 60 + "");
//				tvSec.setText(tenMSecs / 100 % 60 + "");
//				tvMsec.setText(tenMSecs % 100 + "");
//
//				break;
//
//			default:
//				break;
//			}
//		};
//	};
//
//	private static final int MSG_WHAT_SHOW_TIME = 1;
//
//	public void onDestory() {
//		// TODO Auto-generated method stub
//		// 访问到timer把所有的都cancel
//		timer.cancel();
//	}
//}