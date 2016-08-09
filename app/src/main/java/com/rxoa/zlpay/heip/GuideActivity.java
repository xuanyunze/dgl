package com.rxoa.zlpay.heip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.R.id;
import com.rxoa.zlpay.R.layout;
import com.rxoa.zlpay.R.menu;
import com.rxoa.zlpay.R.string;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class GuideActivity extends BaseUIActivity implements OnClickListener {
	private WebView webView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initView();
		 this.webView = ((WebView)findViewById(R.id.wb));
			this.webView.setBackgroundColor(0);
			this.webView.loadUrl("file:///android_asset/xinshouzhiyin.html");
	}

	
	



	private void initView() {
		setTitleText(R.string.title_guide);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help3, menu);
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
