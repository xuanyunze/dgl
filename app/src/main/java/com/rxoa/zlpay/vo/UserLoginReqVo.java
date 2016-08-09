package com.rxoa.zlpay.vo;

public class UserLoginReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	
	private String uid;
	private String upwd;
	private String ucode;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUpwd() {
		return upwd;
	}
	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}
	public String getUcode() {
		return ucode;
	}
	public void setUcode(String ucode) {
		this.ucode = ucode;
	}
}
