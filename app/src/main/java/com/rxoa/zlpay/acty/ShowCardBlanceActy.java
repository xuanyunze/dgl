package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;

public class ShowCardBlanceActy extends BaseUIActivity implements OnClickListener{
	private String accno;
	private String acctype;
	private String blance;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		accno = intent.getStringExtra("accno");
		acctype = intent.getStringExtra("acctype");
		blance = intent.getStringExtra("blance");
		
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_showcardblance);
		setTitleText(R.string.title_showblance);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		((TextView)findViewById(R.id.showcardblance_acc_value)).setText(accno);
		((TextView)findViewById(R.id.showcardblance_type_value)).setText(acctype);
		((TextView)findViewById(R.id.showcardblance_blance_value)).setText(blance);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
	}
}
