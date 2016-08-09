package com.rxoa.zlpay.entity;

import com.rxoa.zlpay.base.BaseEntity;

public class OrderChargeAccount extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String orderValue;
	private String payCardNo;
	private String payAccName;
	
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
}
