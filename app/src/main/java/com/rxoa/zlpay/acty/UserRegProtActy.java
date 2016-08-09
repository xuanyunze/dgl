package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.CheckBox;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;

public class UserRegProtActy extends BaseUIActivity implements OnClickListener{
	private WebView webView;
	private CheckBox checkBox;
	
	public static void launch(Intent intent){
		
	}
	
	private void initView(){
		setContentView(R.layout.acty_reg_prot);
		setTitleText(R.string.title_reg_agree);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		this.webView = ((WebView)findViewById(R.id.reg_prot_wvw));
		this.webView.setBackgroundColor(0);
		this.webView.loadUrl("file:///android_asset/pact.html");
		this.checkBox = ((CheckBox)findViewById(R.id.reg_prot_chx));
		this.checkBox.setOnClickListener(this);
		findViewById(R.id.reg_prot_btnagree).setOnClickListener(this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initView();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(R.id.reg_prot_chx==id){
			if(this.checkBox.isChecked()){
				showToast("您已同意协议，可以点击“下一步”!");
			}else{
				showToast("您需要同意协议才能继续注册!");
			}
		}else if(R.id.reg_prot_btnagree==id){
			if(this.checkBox.isChecked()){
				Intent intent = new Intent(UserRegProtActy.this,UserRegActy.class);
				this.startActivity(intent);
			}else{
				showToast("您需要同意协议才能进行注册!");
			}
		}
	}
}
