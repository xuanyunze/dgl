package com.rxoa.zlpay.avator;

public class AvatorUtil {
	public String arriveType2Text(int value){
		if(value==0){
			return "已受理";
		}else if(value==1){
			return "正在审核";
		}else if(value==2){
			return "正在结算";
		}else if(value==3){
			return "已完成";
		}else if(value==4){
			return "申请被拒绝";
		}
		return "未知";
	}
	public int arriveType2Value(String text){
		if(text.equals("已受理")){
			return 0;
		}else if(text.equals("正在审核")){
			return 1;
		}else if(text.equals("正在结算")){
			return 2;
		}else if(text.equals("已完成")){
			return 3;
		}else if(text.equals("申请被拒绝")){
			return 4;
		}
		return 99;
	}
}
