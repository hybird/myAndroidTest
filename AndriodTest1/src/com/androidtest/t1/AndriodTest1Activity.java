package com.androidtest.t1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AndriodTest1Activity extends Activity
{

	private Button button;

	private ProgressBar progressBar;

	TextView tv_1;

	private MyHandler myHandler;

	private MyThread m = new MyThread();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tv_1 = (TextView) findViewById(R.id.tv_1);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		progressBar.setMax(100);
	}

	

	public void tt(View view)
	{
		tv_1.setText("aaa");
		myHandler = new MyHandler();

		new Thread(m).start();

		Log.d(getLocalClassName(), "onCreate--The Thread is: "
				+ Thread.currentThread().getId());

	}

	// 在对UI进行更新时，执行时所在的线程为主UI线程

	class MyHandler extends Handler
	{
		// 继承Handler类时，必须重写handleMessage方法

		public MyHandler() {

		}

		public MyHandler(Looper l) {
			super(l);
		}

		@Override
		public void handleMessage(Message msg)
		{
			// 执行接收到的通知，此时执行的顺序是按照队列进行，即先进先出

			super.handleMessage(msg);

			Bundle b = msg.getData();

			String textStr0 = tv_1.getText().toString();

			String textStr1 = b.getString("textStr");

			tv_1.setText(textStr0 + " " + textStr1);

			int barValue = b.getInt("barValue");
			progressBar.setProgress(barValue);

		}
	}

	class MyThread implements Runnable
	{

		int i = 1;

		public void run()
		{
			while (i < 11)
			{
				Log.i("", "Thread--The ThreadId is: "
						+ Thread.currentThread().getId());

				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("textStr", "textStrThread" + i);
				bundle.putInt("barValue", i * 10);
				i++;
				msg.setData(bundle);
				myHandler.sendMessage(msg);
				if (i == 5)
				{
					myHandler.postDelayed(new MyThread2(), 2000L);
				}
			}

		}

	}

	class MyThread2 implements Runnable
	{

		public void run()
		{
			tv_1.setText("");

		}

	}

	public void toMove(View view)
	{
		Intent intent = new Intent();
		intent.setClass(this, MoveActivity.class);
		startActivity(intent);
	}
	
	public void toLoc(View view)
	{
		Intent intent = new Intent();
		intent.setClass(this, LocationActivity.class);
		startActivity(intent);
	}

}