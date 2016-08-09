package com.rxoa.zlpay.vo;

public class SmsCodeReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String reqId;
	private String reqType;
	private String phoneNo;
	
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
}
