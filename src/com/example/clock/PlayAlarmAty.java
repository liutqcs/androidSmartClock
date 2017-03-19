package com.example.clock;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

/*
 * 创建播放闹钟的界面
 */
public class PlayAlarmAty extends Activity{
	
	private MediaPlayer mp;// 定义多媒体对象  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置一个Activity的显示界面播放音乐界面 在AndroidManifest中注册
		setContentView(R.layout.alarm_player_aty);
		//通过music来创建
		mp = MediaPlayer.create(this, R.raw.music);
		mp.start();//开始播放
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mp.stop();
		mp.release();//释放
	}
}
