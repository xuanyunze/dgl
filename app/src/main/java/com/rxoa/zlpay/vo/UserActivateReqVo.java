package com.rxoa.zlpay.vo;

public class UserActivateReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;

	private String actiType;
	private String actiCode;
	private String actiSn;
	private String payPwd;
	
	public String getActiType() {
		return actiType;
	}
	public void setActiType(String actiType) {
		this.actiType = actiType;
	}
	public String getActiCode() {
		return actiCode;
	}
	public void setActiCode(String actiCode) {
		this.actiCode = actiCode;
	}
	public String getActiSn() {
		return actiSn;
	}
	public void setActiSn(String actiSn) {
		this.actiSn = actiSn;
	}
	public String getPayPwd() {
		return payPwd;
	}
	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
}
