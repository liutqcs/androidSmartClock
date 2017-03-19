package com.example.clock;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.os.Build;

public class MainActivity extends Activity {
	private TabHost tabHost;// 实例化

	private StopWatchView stopWatchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * 实现TabHost有两种方式： 1.不继承TabActivity 在布局文件中定义TabHost 2.继承TabActivity
		 * 用getTabhost()方法获取TabHost 各tab内容在布局文件中定义
		 */
		// 获取tabHost
		// 选项卡其实就是一个tabSpec，获取一个新的TabHost.TabSpec，并关联到当前tabHost
		// TabSpec：理解tabSpec，它就相当于一个tab选项卡，我们要给选项卡设置标签、添加图片和文字就用setIndicator(...)和setContent(...)。需要几个选项卡就创建几个tabSpec。而tabHost就是一个盛装选项卡的容器，所以选项卡设置好后要把他们一
		// 一添加到容器内，即tabHost.addTab(tabSpec);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();// 初始化,实例化了tabWidget和tabContent
		tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("时钟")
				.setContent(R.id.tabTime));// 指定内容
		tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("闹钟")
				.setContent(R.id.tabAlarm));
		tabHost.addTab(tabHost.newTabSpec("tabTimer").setIndicator("计时器")
				.setContent(R.id.tabTimer));
		tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("秒表")
				.setContent(R.id.tabStopWatch));
		stopWatchView = (StopWatchView) findViewById(R.id.tabStopWatch);
	}

	/*
	 * 重写onDestroy()方法来侦听MainActivity方法
	 */
	@Override
	protected void onDestroy() {
		stopWatchView.onDestory();
		super.onDestroy();

	}

}





