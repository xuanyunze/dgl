package com.rxoa.zlpay.security;

import android.app.Activity;
import com.rxoa.zlpay.util.DialogUtil;

public class DataValidator {
	public static boolean isNumberic(String num){
		try{
			if(num==null||num.equals("")){return false;}
			java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后一位的数字的正则表达式
	        java.util.regex.Matcher match=pattern.matcher(num); 
	        if(!match.matches()){ 
	           return false; 
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	public static boolean isNumbericWithToast(Activity acty,String num,int id){
		if(!isNumberic(num)){
				DialogUtil.showToast(acty,acty.getApplicationContext().getResources().getString(id));
				return false;
			}
		return true;
	}
}
