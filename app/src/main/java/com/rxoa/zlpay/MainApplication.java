package com.rxoa.zlpay;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;

import com.rxoa.zlpay.acty.MainHomeActy;
import com.rxoa.zlpay.base.BaseApplication;
import com.rxoa.zlpay.combx.LocationParser;

public class MainApplication extends BaseApplication{
	@SuppressWarnings("unused")
	private static final String mTAG = "MainApplication";
	public static MainApplication instance = null;
	public static boolean isFinish = false;
	private ArrayList<Activity> mActivities = new ArrayList<Activity>();
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	public MainHomeActy mainHomeActy;
	public String DeviceEntity;

	public String getDeviceEntity() {
		return DeviceEntity;
	}

	public void setDeviceEntity(String deviceEntity) {
		DeviceEntity = deviceEntity;
	}

	public static MainApplication getInstance() {
		if (instance == null) {
			throw new RuntimeException(
					"MainApplication has not initialed!!");
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			Config.VERSION = packageInfo.versionName;
		} catch (Exception e) {
			Config.VERSION = "0.0";
		}
		
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
        Config._ScreenWidth = dm.widthPixels;
        Config._ScreenHeight = dm.heightPixels;
        Config._Density = dm.density;
        updateUserInfo();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void exit() {
		for (Activity activity : mActivities) {
			if(!( activity instanceof MainHomeActy)){
				activity.finish();
			}
		}
		//System.exit(0);
	}

	public void addActivity(Activity activity) {
		if(activity instanceof MainHomeActy)return;
		if (!mActivities.contains(activity)) {
			mActivities.add(activity);
		}
	}

	public void removeActivity(Activity activity) {
		if (mActivities.contains(activity)) {
			mActivities.remove(activity);
		}
	}
	
	public void registerFragments(Fragment fragment){
		if(!fragments.contains(fragment)){
			fragments.add(fragment);
		}
	}
	public void removeFragments(Fragment fragment){
		fragments.remove(fragment);
	}

	/**
	 * 获取本机mac地址
	 * 
	 * @return
	 */
	public String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getInstance().getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public void updateUserInfo() {
		SharedPreferences preferences = getSharedPreferences(
				Config.PRE_SETTING, MODE_PRIVATE);
		Config._isLogin = preferences.getBoolean(Config.PRE_ISLOGIN, false);
		if (Config._isLogin) {
			 //String accountJson = preferences.getString(Config.PRE_USERINFO,
			 //"");
			 //Config.userInfo = new ResponseLogin();
			 //try {
			 //Config.userInfo.parseJson(accountJson);
			 //} catch (JSONException e) {
			 //Config.userInfo = null;
			 //}
		}
	}
	
}
