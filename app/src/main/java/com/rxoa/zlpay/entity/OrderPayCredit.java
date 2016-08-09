package com.rxoa.zlpay.entity;

import com.rxoa.zlpay.base.BaseEntity;

public class OrderPayCredit extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String accName;
	private String accNo;
	private String accBank;
	private String accValue;
	
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAccBank() {
		return accBank;
	}
	public void setAccBank(String accBank) {
		this.accBank = accBank;
	}
	public String getAccValue() {
		return accValue;
	}
	public void setAccValue(String accValue) {
		this.accValue = accValue;
	}
}
