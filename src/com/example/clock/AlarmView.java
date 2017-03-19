package com.example.clock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.TimedText;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

/*
 * 创建闹钟类AlarmView
 */
public class AlarmView extends LinearLayout {

	// public AlarmView(Context context, AttributeSet attrs, int defStyle) {
	// super(context, attrs, defStyle);
	// // TODO Auto-generated constructor stub
	// }

	public AlarmView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AlarmView(Context context) {
		super(context);
		init();
	}

	// 初始化 调用系统时间
	private void init() {
		alarmManager = (AlarmManager) getContext().getSystemService(
				Context.ALARM_SERVICE);// 使用系统闹钟服务
	}

	// 重写onFinishInflate()方法
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
		// 闹钟列表
		lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);

		adapter = new ArrayAdapter<AlarmView.AlarmData>(getContext(),
				android.R.layout.simple_list_item_activated_1);// 使用系统的简单资源
		lvAlarmList.setAdapter(adapter);// 呈现数据
		// 设置好adapter之后读取
		readSavedAlarmList();

		// 添加监听器
		btnAddAlarm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addAlarm();
			}
		});

		lvAlarmList
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {
						// 弹出一个对话框
						new AlertDialog.Builder(getContext())
								.setTitle("操作选项")
								.setItems(new CharSequence[] { "删除" },
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												switch (which) {
												case 0:
													deleteAlarm(position);
													break;
												default:
													break;
												}

											}
										}).setNegativeButton("取消", null).show();// 取消时候不用监听器

						return true;
					}
				});
	}

	// 删除闹钟
	private void deleteAlarm(int position) {
		// 从adapter中移除 列表中移除
		AlarmData ad = adapter.getItem(position);
		adapter.remove(ad);
		// 重新保存
		saveAlarmList();

		// 移除闹钟
		alarmManager.cancel(PendingIntent.getBroadcast(getContext(),
				ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
		// alarmManager.cancel(PendingIntent.getBroadcast(getContext(),
		// ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
	}

	// 添加闹钟方法
	private void addAlarm() {
		// TODO

		Calendar c = Calendar.getInstance();

		// 弹出时间选择对话框
		new TimePickerDialog(getContext(),
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {

						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						calendar.set(Calendar.SECOND, 0);// 时间清零 到指定的分钟就会执行闹钟
						calendar.set(Calendar.MILLISECOND, 0);// 秒数清零
						// 获取当前时间
						Calendar currentTime = Calendar.getInstance();
						// 和当前时间比较 如果小于等于当前时间 就往后推一天
						if (calendar.getTimeInMillis() <= currentTime
								.getTimeInMillis()) {
							calendar.setTimeInMillis(calendar.getTimeInMillis()
									+ 24 * 60 * 60 * 1000);
						}
						AlarmData ad = new AlarmData(calendar.getTimeInMillis());
						adapter.add(ad);
						/*
						 * 1.RTC_WAKEUP手机休应用程序不会停掉 操作系统时间为准 2.triggerAtMillis
						 * 什么时间启动 3.intervalMillis 在启动之后每隔多长时间可以继续启动一次
						 * 4.operation 挂起 不是立即执行以后某个时间点执行
						 */
						//定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
						alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
								calendar.getTimeInMillis(), 5 * 60 * 1000,
								PendingIntent.getBroadcast(getContext(), ad
										.getId(), new Intent(getContext(),
										AlarmReceiver.class), 0));
						// 第一次启动时间
						// 循环启动-5分钟
						// 挂起的PendingIntent未来某时间执行
					}

				}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true) {
			@Override
			protected void onStop() {
				// TODO Auto-generated method stub
				// super.onStop();//注释掉就可以
			}
		}.show();
		// 需要重写TimePickerDialog中的onStop方法
	}

	// 存储闹钟数据
	private void saveAlarmList() {
		Editor editor = getContext().getSharedPreferences(
				AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

		// 用StringBuffer来存储
		StringBuffer sb = new StringBuffer();
		// 循环遍历adapter 如果有多个数据 中间用逗号隔开
		for (int i = 0; i < adapter.getCount(); i++) {
			sb.append(adapter.getItem(i).getTime()).append(",");
		}
		// 最后去掉，号
		if (sb.length() > 1) {
			String content = sb.toString().substring(0, sb.length() - 1);
			editor.putString(KEY_ALARM_LIST, content);
			System.out.println(content);
		} else {
			// 提交
			editor.putString(KEY_ALARM_LIST, null);
		}
		// 提交
		editor.commit();
	}

	// 读取已存储的闹钟数据
	private void readSavedAlarmList() {
		SharedPreferences sp = getContext().getSharedPreferences(
				AlarmView.class.getName(), Context.MODE_PRIVATE);
		// 获取数据内容
		String content = sp.getString(KEY_ALARM_LIST, null);
		// 如果content不为空截取用，号分开
		if (content != null) {
			String[] timeStrings = content.split(",");
			// foreach()对数组遍历
			for (String string : timeStrings) {
				adapter.add(new AlarmData(Long.parseLong(string)));
			}
		}
	}

	private Button btnAddAlarm;
	private ListView lvAlarmList;
	private static final String KEY_ALARM_LIST = "alarmList";
	private ArrayAdapter<AlarmData> adapter;
	private AlarmManager alarmManager;// 使用系统闹钟

	private TimePickerDialog tpd = null;
	
	int am_pm;

	// 闹钟所要想起的时间
	private static class AlarmData {
		public AlarmData(long time) {
			this.time = time;

			// 使用那time来创建时间
			date = Calendar.getInstance();
			date.setTimeInMillis(time);
			
			// 月份返回值从0开始 要+1
			timeLabel = String.format("%d年%d月%d日   %d:%d %s",
					date.get(Calendar.YEAR),
					date.get(Calendar.MONTH) + 1,
					date.get(Calendar.DAY_OF_MONTH),
					date.get(Calendar.HOUR_OF_DAY), 
					date.get(Calendar.MINUTE),
					date.get(Calendar.AM_PM)==0?"am":"pm"//结果为“0”是上午 结果为“1”是下午
					);
		}

		public long getTime() {
			return time;
		}

		public String getTimeLabel() {
			return timeLabel;
		}

		// 为了能呈现到列表中 重写toString()方法
		@Override
		public String toString() {
			return getTimeLabel();
		}

		// 使用设置闹钟时间作为请求码 处理long类型到int类型越界
		public int getId() {
			return (int) (getTime() / 1000 / 60);
		}

		private String timeLabel = "";
		private long time = 0;
		private Calendar date;// 指明一个date变量
	}

}
