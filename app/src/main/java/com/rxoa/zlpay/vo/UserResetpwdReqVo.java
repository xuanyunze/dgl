package com.rxoa.zlpay.vo;

public class UserResetpwdReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String pwdType;
	private String hisPwd;
	private String newPwd;
	
	public String getPwdType() {
		return pwdType;
	}
	public void setPwdType(String pwdType) {
		this.pwdType = pwdType;
	}
	public String getHisPwd() {
		return hisPwd;
	}
	public void setHisPwd(String hisPwd) {
		this.hisPwd = hisPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
}
