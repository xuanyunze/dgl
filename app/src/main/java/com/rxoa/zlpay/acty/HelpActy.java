package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.heip.Help1Activity;
import com.rxoa.zlpay.heip.Help2Activity;
import com.rxoa.zlpay.heip.Help3Activity;

public class HelpActy extends BaseUIActivity implements OnClickListener{
	private LinearLayout help1;
	private LinearLayout help2;
	private LinearLayout help3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_help);
		setTitleText(R.string.title_help);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		help1 = (LinearLayout) findViewById(R.id.more_bar_notify);
		help2 = (LinearLayout) findViewById(R.id.more_bar_guide);
		help3 = (LinearLayout) findViewById(R.id.more_bar_help1);
		help1.setOnClickListener(this);
		help2.setOnClickListener(this);
		help3.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
		else 
		if(id==R.id.more_bar_notify){
			Intent intent = new Intent();
			intent.setClass(HelpActy.this,Help1Activity.class);
			startActivity(intent);
		}else if(id==R.id.more_bar_guide){
			Intent intent = new Intent();
			intent.setClass(HelpActy.this,Help2Activity.class);
		    startActivity(intent);
		}else if(id==R.id.more_bar_help1){
			Intent intent = new Intent();
			intent.setClass(HelpActy.this,Help3Activity.class);
		    startActivity(intent);
	}}
}
