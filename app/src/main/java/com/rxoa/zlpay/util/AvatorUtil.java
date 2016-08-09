package com.rxoa.zlpay.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvatorUtil {
	public static String dealNull(String src){
		return AvatorUtil.dealNull(src,"");
	}
	public static String dealNull(String src,String rep){
		if(src==null||src.equals("null")){
			return rep;
		}
		return src;
	}
	public static String ustatV2T(String stat){
		if(stat.equals("0")){return "未激活";}
		else if(stat.equals("1")){return "已激活";}
		return "未知";
	}
	public static String idCardTypeV2T(String type){
		if(type.equals("0")){return "身份证";}
		else if(type.equals("1")){return "护照";}
		return "未知";
	}
	public static String isFastPayV2T(String v){
		if(v.equals("0")){
			return "未开通";
		}else if(v.equals("1")){
			return "已开通";
		}
		return "未知";
	}
	public static String isFastPayT2V(String v){
		if(v.equals("未开通")||v.equals("不开通")){
			return "0";
		}else if(v.equals("开通")||v.equals("已开通")){
			return "1";
		}
		return "0";
	}
	public static String isMainCardV2T(String v){
		if(v.equals("0")){
			return "否";
		}else if(v.equals("1")){
			return "是";
		}
		return "未知";
	}
	public static String bankCardTypeV2T(String v){
		if(v.equals("0")){
			return "借记卡";
		}else if(v.equals("1")){
			return "信用卡";
		}
		return "未知";
	}	
	public static String bankCardTypeT2V(String v){
		if(v.equals("借记卡")){
			return "0";
		}else if(v.equals("信用卡")||v.equals("贷记卡")){
			return "1";
		}
		return "0";
	}
	public static String deviceTypeV2T(String v){
		if(v.equals("0")){
			return "耳机接口设备";
		}else if(v.equals("1")){
			return "蓝牙设备";
		}
		return "未知";
	}
	public static String deviceTypeT2V(String v){
		if(v.equals("耳机接口设备")){
			return "0";
		}else if(v.equals("蓝牙设备")){
			return "1";
		}
		return "未知";
	}
	public static String deviceStatV2T(String v){
		if(v.equals("0")){
			return "未分配";
		}else if(v.equals("2")){
			return "已绑定";
		}
		return "未知";
	}
	public static String feeCode(String type,String in){
		List<String> t = new ArrayList<String>(Arrays.asList("民生服务","餐饮娱乐","大宗批发"));
		List<String> v = new ArrayList<String>(Arrays.asList("1001","1002","1003"));
		return avator(v,t,in,type);
	}
	public static String settleCode(String type,String in){
		List<String> t = new ArrayList<String>(Arrays.asList("当日到账","次日到账","七日到账"));
		List<String> v = new ArrayList<String>(Arrays.asList("1001","1002","1003"));
		return avator(v,t,in,type);
	}
	private static String avator(List<String> v,List<String> t,String in,String type){
		try{
			if(type.equals("v2t")){
				return t.get(v.indexOf(in));
			}else if(type.equals("t2v")){
				return v.get(t.indexOf(in));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return in;
	}
}
