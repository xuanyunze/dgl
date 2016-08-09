package com.rxoa.zlpay.vo;

public class BlanceTakemoneyReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String arriveType;
	private String takeValue;
	private String takePwd;
	private String arriveValue;
	private String benefitValue;
	private String arriveDate;
	
	public String getArriveType() {
		return arriveType;
	}
	public void setArriveType(String arriveType) {
		this.arriveType = arriveType;
	}
	public String getTakeValue() {
		return takeValue;
	}
	public void setTakeValue(String takeValue) {
		this.takeValue = takeValue;
	}
	public String getTakePwd() {
		return takePwd;
	}
	public void setTakePwd(String takePwd) {
		this.takePwd = takePwd;
	}
	public String getArriveValue() {
		return arriveValue;
	}
	public void setArriveValue(String arriveValue) {
		this.arriveValue = arriveValue;
	}
	public String getBenefitValue() {
		return benefitValue;
	}
	public void setBenefitValue(String benefitValue) {
		this.benefitValue = benefitValue;
	}
	public String getArriveDate() {
		return arriveDate;
	}
	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}
}
