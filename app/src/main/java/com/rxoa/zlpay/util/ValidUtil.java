package com.rxoa.zlpay.util;

import android.app.Activity;

public class ValidUtil {
	public static boolean isEmpty(Activity acty,String value,String toast){
		if(value.equals("")){
			DialogUtil.showToast(acty, toast+"不能为空");
			return false;
		}
		return true;
	}
	public static boolean isPhoneNumber(Activity acty,String value){
		if(value.equals("")){
			DialogUtil.showToast(acty, "手机号格式不正确");
			return false;
		}
		return true;
	}
	public static boolean isEqual(Activity acty,String valueA,String valueB,String toastA,String toastB){
		if(!valueA.equals(valueB)){
			DialogUtil.showToast(acty, toastA+"和"+toastB+"不一致");
			return false;
		}
		return true;
	}
	public static boolean isPwdStr(Activity acty,String value){
		if(value.length()<6){
			DialogUtil.showToast(acty, "密码太简单了");
			return false;
		}else if(value.length()>16){
			DialogUtil.showToast(acty, "密码太长了");
			return false;
		}
		return true;
	}
	
}
