package com.rxoa.zlpay.entity;

import org.json.JSONObject;

import com.rxoa.zlpay.base.BaseEntity;

public class OrderTakeMoney extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String takeAccount;
	private String takeValue;
	private String takeType;
	
	public static OrderTakeMoney getInstance(){
		return new OrderTakeMoney();
	}
	public OrderTakeMoney initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.takeAccount = obj.getString("takeAccount");
				this.takeValue = obj.getString("takeValue");
				this.takeType = obj.getString("takeType");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	public String getTakeAccount() {
		return takeAccount;
	}
	public void setTakeAccount(String takeAccount) {
		this.takeAccount = takeAccount;
	}
	public String getTakeValue() {
		return takeValue;
	}
	public void setTakeValue(String takeValue) {
		this.takeValue = takeValue;
	}
	public String getTakeType() {
		return takeType;
	}
	public void setTakeType(String takeType) {
		this.takeType = takeType;
	}
}
