package com.rxoa.zlpay.acty;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;

public class AboutUsActy extends BaseUIActivity implements OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_aboutus);
		setTitleText(R.string.title_aboutus);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
	}
}
