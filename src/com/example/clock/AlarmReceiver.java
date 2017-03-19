package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("执行了闹钟");
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		//取消掉当前执行的闹钟
		am.cancel(PendingIntent.getBroadcast(context, getResultCode(), new Intent(context, AlarmReceiver.class), 0));
		
		//启动Activity
		Intent i = new Intent(context, PlayAlarmAty.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	
	}

}
