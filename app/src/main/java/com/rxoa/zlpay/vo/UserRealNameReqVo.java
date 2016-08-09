package com.rxoa.zlpay.vo;

public class UserRealNameReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String realName;
	private String idType;
	private String idNo;
	private String posImgStr;
	private String nageImgStr;
	private String selfImgStr;
	
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getPosImgStr() {
		return posImgStr;
	}
	public void setPosImgStr(String posImgStr) {
		this.posImgStr = posImgStr;
	}
	public String getNageImgStr() {
		return nageImgStr;
	}
	public void setNageImgStr(String nageImgStr) {
		this.nageImgStr = nageImgStr;
	}
	public String getSelfImgStr() {
		return selfImgStr;
	}
	public void setSelfImgStr(String selfImgStr) {
		this.selfImgStr = selfImgStr;
	}
}
