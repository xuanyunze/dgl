
package com.rxoa.zlpay.vo;

public class BindOtherCardReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String accName;
	private String accNo;
	private String bankName;
	private String bankDistr;
	private String bankBranch;
	private String cardType;
	private String isFast;
	private String cardValidDate;
	private String cardCvv;
	private String posImgStr;
	
	public String getPosImgStr() {
		return posImgStr;
	}
	public void setPosImgStr(String posImgStr) {
		this.posImgStr = posImgStr;
	}
	

	public String getCardValidDate() {
		return cardValidDate;
	}
	public void setCardValidDate(String cardValidDate) {
		this.cardValidDate = cardValidDate;
	}
	public String getCardCvv() {
		return cardCvv;
	}
	public void setCardCvv(String cardCvv) {
		this.cardCvv = cardCvv;
	}
	

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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getIsFast() {
		return isFast;
	}
	public void setIsFast(String isFast) {
		this.isFast = isFast;
	}
}
