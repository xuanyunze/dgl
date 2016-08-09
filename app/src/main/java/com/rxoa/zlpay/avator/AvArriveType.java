package com.rxoa.zlpay.avator;

public class AvArriveType{
	public static enum code{
		Today,Tomorrow
	}
	public static String value2text(String value) {
		if(value.equals("T0"))
			return "当日到账[工作日]";
		if(value.equals("D0"))
			return "当日到账[周六日]";
		if(value.equals("T1"))
			return "次日到账[工作日]";
		if(value.equals("T7"))
			return "7日到账";
		if(value.equals("T15"))
			return "15日到账";
		if(value.equals("T30"))
			return "30日到账";
		return "未知";
	}
	public static String text2value(String text) {
		if(text.equals("当日到账[工作日]"))
			return "T0";
		if(text.equals("当日到账[周六日]"))
			return "D0";
		if(text.equals("次日到账[工作日]"))
			return "T1";
		if(text.equals("7日到账"))
			return "T7";
		if(text.equals("15日到账"))
			return "T15";
		if(text.equals("30日到账"))
			return "T30";
		return "99";
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
