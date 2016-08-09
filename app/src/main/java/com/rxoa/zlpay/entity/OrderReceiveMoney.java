package com.rxoa.zlpay.entity;

import org.json.JSONObject;

import com.rxoa.zlpay.base.BaseEntity;

public class OrderReceiveMoney extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String orderValue;
	private String payCardNo;
	private String payAccName;
	private String payFeeCode;
	private String paySettleCode;
	private String payAreaCode;
	
	public static OrderReceiveMoney getInstance(){
		return new OrderReceiveMoney();
	}
	public OrderReceiveMoney initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.orderValue = obj.getString("orderValue");
				this.payCardNo = obj.getString("payCardNo");
				this.payAccName = obj.getString("payAccName");
				this.payFeeCode = obj.getString("payFeeCode");
				this.paySettleCode = obj.getString("paySettleCode");
				this.payAreaCode = obj.getString("payAreaCode");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public String getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}
	public String getPayCardNo() {
		return payCardNo;
	}
	public void setPayCardNo(String payCardNo) {
		this.payCardNo = payCardNo;
	}
	public String getPayAccName() {
		return payAccName;
	}
	public void setPayAccName(String payAccName) {
		this.payAccName = payAccName;
	}
	public String getPaySettleCode() {
		return paySettleCode;
	}
	public void setPaySettleCode(String paySettleCode) {
		this.paySettleCode = paySettleCode;
	}
	public String getPayFeeCode() {
		return payFeeCode;
	}
	public void setPayFeeCode(String payFeeCode) {
		this.payFeeCode = payFeeCode;
	}
	public String getPayAreaCode() {
		return payAreaCode;
	}
	public void setPayAreaCode(String payAreaCode) {
		this.payAreaCode = payAreaCode;
	}
}
