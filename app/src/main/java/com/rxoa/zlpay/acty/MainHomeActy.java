package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;

import com.rxoa.zlpay.R;
import com.rxoa.zlpay.adapter.MainHomeFmtAdapter;
import com.rxoa.zlpay.base.BaseFmtActivity;
import com.rxoa.zlpay.base.BaseViewPager;
import com.rxoa.zlpay.security.AuthChecker;

public class MainHomeActy extends BaseFmtActivity implements OnClickListener{
	
	public static final int TAB_APP = 0;
	public static final int TAB_ORDER = 1;
	public static final int TAB_ACC = 2;
	public static final int TAB_MORE = 3;
	
	private BaseViewPager viewPager;
	private RadioButton main_tab_app, main_tab_order,
			main_tab_acc, main_tab_more;
	private MainHomeFmtAdapter adapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//BaseApplication.getInstance();
		setContentView(R.layout.acty_home);
		setConfigInfo();
		initView();
		addListener();
	}
	private void setConfigInfo(){
		Intent intent = getIntent();
		//Config.USER_ID = intent.getStringExtra("userid");
		//Config.PID_FT = intent.getStringExtra("pid");
		//Config.ISDEBUG = intent.hasExtra("isdebug")?intent.getBooleanExtra("isdebug", true):Config.ISDEBUG;
		//Config.ISPUBLISH = intent.hasExtra("ispub")?intent.getBooleanExtra("ispub", true):Config.ISPUBLISH;
		//Config.DEV_SERVERIP = intent.hasExtra("devip")?intent.getStringExtra("devip"):Config.DEV_SERVERIP;
		//Config.PUB_SERVERIP = intent.hasExtra("pubip")?intent.getStringExtra("pubip"):Config.PUB_SERVERIP;
		
		//DisplayMetrics dm = getResources().getDisplayMetrics();
		//Config._ScreenWidth = dm.widthPixels;
		//Config._ScreenHeight = dm.heightPixels;
	}
	private void initView() {
		viewPager = (BaseViewPager) findViewById(R.id.viewpager);
		viewPager.setScanScroll(false);
		main_tab_app = (RadioButton) findViewById(R.id.main_tab_app);
		main_tab_order = (RadioButton) findViewById(R.id.main_tab_order);
		main_tab_acc = (RadioButton) findViewById(R.id.main_tab_acc);
		main_tab_more = (RadioButton) findViewById(R.id.main_tab_more);
		main_tab_app.setOnClickListener(this);
		main_tab_order.setOnClickListener(this);
		main_tab_acc.setOnClickListener(this);
		main_tab_more.setOnClickListener(this);
		
		adapter = new MainHomeFmtAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		
		int vcur = getIntent().getIntExtra("curFrag", 0);
		setCurPage(vcur);
	}

	private void addListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			//在选定的页面上
			public void onPageSelected(int id) {
				switch (id) {
				case TAB_APP:
					main_tab_app.setChecked(true);
					break;
				case TAB_ORDER:
					main_tab_order.setChecked(true);
					break;
				case TAB_ACC:
					main_tab_acc.setChecked(true);
					break;
				case TAB_MORE:
					main_tab_more.setChecked(true);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		if(vid==R.id.main_tab_app){
			viewPager.setCurrentItem(TAB_APP);
		}else if(vid==R.id.main_tab_order){
			if(AuthChecker.checkLoginWithToast(MainHomeActy.this)){
				viewPager.setCurrentItem(TAB_ORDER);
			}else{
				main_tab_app.setChecked(true);
			}
		}else if(vid==R.id.main_tab_acc){
			if(AuthChecker.checkLoginWithToast(MainHomeActy.this)){
				viewPager.setCurrentItem(TAB_ACC);
			}else{
				main_tab_app.setChecked(true);
			}
		}else if(vid==R.id.main_tab_more){
			if(AuthChecker.checkLoginWithToast(MainHomeActy.this)){
				viewPager.setCurrentItem(TAB_MORE);
			}else{
				main_tab_app.setChecked(true);
			}
		}
	}
	
	public void setCurPage(int i){
		switch(i){
		case 0:
			main_tab_app.setChecked(true);
			viewPager.setCurrentItem(i);
			break;
		case 1:
			main_tab_order.setChecked(true);
			viewPager.setCurrentItem(i);
			break;
		case 2:
			main_tab_acc.setChecked(true);
			viewPager.setCurrentItem(i);
			break;
		case 3:
			main_tab_more.setChecked(true);
			viewPager.setCurrentItem(i);
			break;
		default:
			break;
		}
	}
}
