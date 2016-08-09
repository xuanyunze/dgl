package com.rxoa.zlpay.heip;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.R.layout;
import com.rxoa.zlpay.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class Help2Activity extends BaseUIActivity implements OnClickListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help2);
		initView();
	}

	private void initView() {
		setTitleText(R.string.title_help);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help2, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
	}

}
