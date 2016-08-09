package com.rxoa.zlpay.avator;

public class AvPayType{
	public static enum code{
		Account,Fast,Device
	}
	public static String value2text(String value) {
		if(value.equals("1"))
			return "账户余额";
		if(value.equals("2"))
			return "快捷支付";
		if(value.equals("3"))
			return "刷卡支付";
		return "未知";
	}
	public static String text2value(String text) {
		if(text.equals("账户余额"))
			return "1";
		if(text.equals("快捷支付"))
			return "2";
		if(text.equals("刷卡支付"))
			return "3";
		return "0";
	}
	public static String value2code(String value) {
		if(value.equals("1"))
			return code.Account.name();
		if(value.equals("2"))
			return code.Fast.name();
		if(value.equals("3"))
			return code.Device.name();
		return value;
	}
	public static String code2value(String vcode) {
		if(vcode.equals(code.Account.name()))
			return "1";
		if(vcode.equals(code.Fast.name()))
			return "2";
		if(vcode.equals(code.Device.name()))
			return "3";
		return "0";
	}
	
	public static String code2text(String code) {
		return code;
	}
	public static String text2code(String text) {
		return text;
	}
}
