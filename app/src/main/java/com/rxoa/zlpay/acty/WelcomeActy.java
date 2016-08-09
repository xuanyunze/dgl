package com.rxoa.zlpay.acty;

import java.util.Timer;
import java.util.TimerTask;

import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.BaseActivity;
import com.rxoa.zlpay.util.VersionMgr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class WelcomeActy extends BaseActivity implements OnClickListener{
	  TimerTask task = new TimerTask() {
		  @Override
		  public void run() {
			  Intent intent = new Intent(WelcomeActy.this, MainHomeActy.class);
			  startActivity(intent);
			  finish();
		  }
	  };
	  
	public static void launch(Intent intent){
		
	}
	
	private void initView(){
		setContentView(R.layout.acty_welcome);
		findViewById(R.id.imageView1).setOnClickListener(this);
		new Timer().schedule(task,1000 * 2);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initView();
	}
	
	protected void onPause(){
		super.onPause();
	}
	
	protected void onResume(){
		super.onResume();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(WelcomeActy.this,MainHomeActy.class);
		this.startActivity(intent);
		this.finish();
	}

}
