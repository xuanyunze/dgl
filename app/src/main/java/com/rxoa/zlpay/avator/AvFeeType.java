package com.rxoa.zlpay.avator;

public class AvFeeType {
	public static String value2text(String value) {
		if(value.equals("1001"))
			return "民生服务";
		if(value.equals("1002"))
			return "餐饮娱乐";
		if(value.equals("1003"))
			return "大宗批发";
		return "未知";
	}
	public static String text2value(String text) {
		if(text.equals("民生服务"))
			return "1001";
		if(text.equals("餐饮娱乐"))
			return "1002";
		if(text.equals("大宗批发"))
			return "10033";
		return "99";
	}
}
