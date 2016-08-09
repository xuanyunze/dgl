package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;

public class AccountSecurityActy extends BaseUIActivity implements OnClickListener{
	private LinearLayout barLogin;;
	private LinearLayout barPay;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_accoutsecurity);
		setTitleText(R.string.title_accsecurity);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
		barLogin = (LinearLayout) findViewById(R.id.accsafe_bar_logincode);
		barPay = (LinearLayout) findViewById(R.id.accsafe_bar_paycode);
		barLogin.setOnClickListener(this);
		barPay.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.accsafe_bar_logincode){
			Intent intent = new Intent(AccountSecurityActy.this,UserResetpwdActy.class);
			intent.putExtra("type", "login");
			startActivity(intent);
		}else if(id==R.id.accsafe_bar_paycode){
			Intent intent = new Intent(AccountSecurityActy.this,UserResetpwdActy.class);
			intent.putExtra("type", "pay");
			startActivity(intent);
		}
	}
}
