package com.rxoa.zlpay.avator;

public class AvArriveStat{
	public static enum code{
		Today,Tomorrow
	}
	public static String value2text(String value) {
		if(value.equals("0"))
			return "已受理";
		if(value.equals("1"))
			return "正在审核";
		if(value.equals("2"))
			return "已提交结算";
		if(value.equals("3"))
			return "已完成";
		if(value.equals("4"))
			return "申请被拒绝";
		return "未知";
	}
	public static String text2value(String text) {
		if(text.equals("已受理"))
			return "0";
		if(text.equals("正在审核"))
			return "1";
		if(text.equals("已提交结算"))
			return "2";
		if(text.equals("已完成"))
			return "3";
		if(text.equals("申请被拒绝"))
			return "4";
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
