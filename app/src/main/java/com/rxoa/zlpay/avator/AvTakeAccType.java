package com.rxoa.zlpay.avator;

public class AvTakeAccType{
	public static enum code{
		Today,Tomorrow
	}
	public static String value2text(String value) {
		if(value.equals("1"))
			return "收款余额";
		if(value.equals("2"))
			return "收款冻结";
		if(value.equals("3"))
			return "充值余额";
		return "未知";
	}
	public static String text2value(String text) {
		if(text.equals("收款余额"))
			return "1";
		if(text.equals("收款冻结"))
			return "2";
		if(text.equals("充值余额"))
			return "3";
		return "0";
	}
	public static String value2code(String value) {
		if(value.equals("1"))
			return code.Today.name();
		if(value.equals("2"))
			return code.Tomorrow.name();
		return value;
	}
	public static String code2value(String vcode) {
		if(vcode.equals(code.Today.name()))
			return "1";
		if(vcode.equals(code.Tomorrow.name()))
			return "2";
		return "0";
	}
	
	public static String code2text(String code) {
		return code;
	}
	public static String text2code(String text) {
		return text;
	}
}
