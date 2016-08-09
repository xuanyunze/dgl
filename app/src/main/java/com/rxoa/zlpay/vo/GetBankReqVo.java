package com.rxoa.zlpay.vo;

public class GetBankReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String reqtype;//MCARD_BANKS,MCARD_BRANCHES;
	private String bankName;
	private String province;
	private String city;
	private String area;
	
	public String getReqtype() {
		return reqtype;
	}
	public void setReqtype(String reqtype) {
		this.reqtype = reqtype;
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}
