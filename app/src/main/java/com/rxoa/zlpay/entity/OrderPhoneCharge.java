package com.rxoa.zlpay.entity;

import org.json.JSONObject;

import com.rxoa.zlpay.base.BaseEntity;

public class OrderPhoneCharge extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String phoneNumber;
	private String chargeValue;
	
	public static OrderPhoneCharge getInstance(){
		return new OrderPhoneCharge();
	}
	public OrderPhoneCharge initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.phoneNumber = obj.getString("phoneNumber");
				this.chargeValue = obj.getString("chargeValue");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getChargeValue() {
		return chargeValue;
	}
	public void setChargeValue(String chargeValue) {
		this.chargeValue = chargeValue;
	}
}
