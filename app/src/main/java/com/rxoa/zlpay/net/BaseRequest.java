package com.rxoa.zlpay.net;

import android.content.Context;

import com.rxoa.zlpay.Config;

public class BaseRequest {
	protected Context context = null;
	protected boolean USE_LOCAL_DATA = Config.USE_LOCAL_DATA;
	protected String BASE_URL = Config.BASE_URL;
	
	public boolean getUSE_LOCAL_DATA() {
		return USE_LOCAL_DATA;
	}
	public void setUSE_LOCAL_DATA(boolean uSE_LOCAL_DATA) {
		USE_LOCAL_DATA = uSE_LOCAL_DATA;
	}
	
	public String getRandom(){
		try{
			return Math.round(Math.random()*100000000)+"";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "10000000";
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
}
