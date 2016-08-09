package com.rxoa.zlpay.entity;

import com.rxoa.zlpay.base.BaseEntity;

public class OrderTransAccount extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String receiveAccName;
	private String rceceiveAccNo;
	private String bankName;
	private String bankDistr;
	private String bankBranch;
	private String transValue;
	private String noticePhone;
	
	public String getReceiveAccName() {
		return receiveAccName;
	}
	public void setReceiveAccName(String receiveAccName) {
		this.receiveAccName = receiveAccName;
	}
	public String getRceceiveAccNo() {
		return rceceiveAccNo;
	}
	public void setRceceiveAccNo(String rceceiveAccNo) {
		this.rceceiveAccNo = rceceiveAccNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankDistr() {
		return bankDistr;
	}
	public void setBankDistr(String bankDistr) {
		this.bankDistr = bankDistr;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public String getTransValue() {
		return transValue;
	}
	public void setTransValue(String transValue) {
		this.transValue = transValue;
	}
	public String getNoticePhone() {
		return noticePhone;
	}
	public void setNoticePhone(String noticePhone) {
		this.noticePhone = noticePhone;
	}
}
