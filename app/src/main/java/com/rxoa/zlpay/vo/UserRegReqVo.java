package com.rxoa.zlpay.vo;

import com.rxoa.zlpay.base.BaseEntity;

public class UserRegReqVo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String reqid;
	private String uphone;
	private String upwd;
	private String ucode;
	
	public String getReqid() {
		return reqid;
	}
	public void setReqid(String reqid) {
		this.reqid = reqid;
	}
	public String getUphone() {
		return uphone;
	}
	public void setUphone(String uphone) {
		this.uphone = uphone;
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
