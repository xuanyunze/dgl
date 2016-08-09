package com.rxoa.zlpay.avator;

public class AvOrderStat{
	public static enum code{
		UNPAY,PAYFAIL,UNSIG,DEALING,SUCCESS,FAIL
	}
	public static String value2text(String value) {
		if(value.equals("1"))
			return "未支付";
		if(value.equals("2"))
			return "支付失败";
		if(value.equals("3"))
			return "未签名";
		if(value.equals("4"))
			return "正在处理";
		if(value.equals("5"))
			return "交易成功";
		if(value.equals("6"))
			return "交易失败";
		if(value.equals("7"))
			return "已提交清算";
		if(value.equals("8"))
			return "交易完成";
		return "未知";
	}
	public static String text2value(String text) {
		if(text.equals("未支付"))
			return "1";
		if(text.equals("支付失败"))
			return "2";
		if(text.equals("未签名"))
			return "3";
		if(text.equals("正在处理"))
			return "4";
		if(text.equals("交易成功"))
			return "5";
		if(text.equals("交易失败"))
			return "6";
		if(text.equals("已提交清算"))
			return "7";
		if(text.equals("交易完成"))
			return "8";
		return "0";
	}
	public static String value2code(String value) {
		if(value.equals("1"))
			return code.UNPAY.name();
		if(value.equals("2"))
			return code.PAYFAIL.name();
		if(value.equals("3"))
			return code.UNSIG.name();
		if(value.equals("4"))
			return code.DEALING.name();
		if(value.equals("5"))
			return code.SUCCESS.name();
		if(value.equals("6"))
			return code.FAIL.name();
		return value;
	}
	public static String code2value(String vcode) {
		if(vcode.equals(code.UNPAY.name()))
			return "1";
		if(vcode.equals(code.PAYFAIL.name()))
			return "2";
		if(vcode.equals(code.UNSIG.name()))
			return "3";
		if(vcode.equals(code.DEALING.name()))
			return "4";
		if(vcode.equals(code.SUCCESS.name()))
			return "5";
		if(vcode.equals(code.FAIL.name()))
			return "6";
		return "0";
	}
	
	public static String code2text(String code) {
		return code;
	}
	public static String text2code(String text) {
		return text;
	}
}
