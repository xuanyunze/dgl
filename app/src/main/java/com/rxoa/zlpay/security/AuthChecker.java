package com.rxoa.zlpay.security;

import java.util.Date;

import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.acty.UserLoginActy;
import com.rxoa.zlpay.base.util.DateUtil;
import com.rxoa.zlpay.util.DialogUtil;

import android.app.Activity;
import android.content.Intent;

public class AuthChecker {
	public static enum AuthResult{
		SUCCESS,
		CHECK_SESSION_NEEDLOGIN,
		CHECK_SESSION_TIMEOUT,
		CEHCK_SESSION_USERLOCKED,
		CHECK_AUTH_FAIL,
		CHECK_SESSION_ERROR
	}
	public static int checkLoginStat(){
		if(Config._isLogin==false||Config.Uid==null){
			return 0;
		}else if(DateUtil.differenceBySec(DateUtil.parseFromFullDateTime(Config.LastActTime), new Date())>Config.SessionTimeOut){
			return 2;
		}
		return 1;
	}
	public static boolean checkLoginWithToast(Activity acty){
		int iRes = checkLoginStat();
		if(iRes==0){
			DialogUtil.showToast(acty,acty.getApplicationContext().getResources().getString(R.string.toast_login));
			Intent intent = new Intent(acty,UserLoginActy.class);
			acty.startActivity(intent);
			return false;
		}else if(iRes==2){
			DialogUtil.showToast(acty,acty.getApplicationContext().getResources().getString(R.string.toast_login_timeout));
			Intent intent = new Intent(acty,UserLoginActy.class);
			acty.startActivity(intent);
			return false;
		}
		return true;
	}
}

