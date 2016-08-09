package com.rxoa.zlpay.vo;

public class UserFindpwdReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String userId;
	private String userPwd;
	private String reqId;
	private String vrCode;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getVrCode() {
		return vrCode;
	}
	public void setVrCode(String vrCode) {
		this.vrCode = vrCode;
	}
}
