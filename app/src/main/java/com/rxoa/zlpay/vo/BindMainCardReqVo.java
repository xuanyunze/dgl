package com.rxoa.zlpay.vo;

public class BindMainCardReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String province;
	private String city;
	private String accCate;
	private String accName;
	private String accNo;
	private String bankName;
	private String bankDistr;
	private String bankBranch;
	private String bankCode;
	private String posImgStr;
	private String nageImgStr;
	
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
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAccCate() {
		return accCate;
	}
	public void setAccCate(String accCate) {
		this.accCate = accCate;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
}
