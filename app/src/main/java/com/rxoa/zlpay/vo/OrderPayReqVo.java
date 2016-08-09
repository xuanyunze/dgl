package com.rxoa.zlpay.vo;

public class OrderPayReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String orderType;
	private String orderDate;
	private String orderValue;
	private String orderStat;
	
	private String payType;
	private String payAccName;
	private String payAccNo;
	private String payBankName;
	private String payPwd;
	private String payFeeCode;
	private String paySettleCode;
	private String payAreaCode;
	private String payDeviceSn;
	
	private String receAccName;
	private String receAccNo;
	private String receBankName;
	private String receBankDistr;
	private String receBankBranch;
	private String chargePhoneNo;
	private String notifyPhoneNo;
	private String takeAccount;
	private String takeType;
	
	private String isEntrack;
	private String trackTwo;
	private String trackThree;
	private String track55;
	private String cardSequence;
	private String cardExternal;
	private String sigImageStr;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}
	public String getOrderStat() {
		return orderStat;
	}
	public void setOrderStat(String orderStat) {
		this.orderStat = orderStat;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayAccName() {
		return payAccName;
	}
	public void setPayAccName(String payAccName) {
		this.payAccName = payAccName;
	}
	public String getPayAccNo() {
		return payAccNo;
	}
	public void setPayAccNo(String payAccNo) {
		this.payAccNo = payAccNo;
	}
	public String getPayBankName() {
		return payBankName;
	}
	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}
	public String getPayPwd() {
		return payPwd;
	}
	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
	public String getReceAccName() {
		return receAccName;
	}
	public void setReceAccName(String receAccName) {
		this.receAccName = receAccName;
	}
	public String getReceAccNo() {
		return receAccNo;
	}
	public void setReceAccNo(String receAccNo) {
		this.receAccNo = receAccNo;
	}
	public String getReceBankName() {
		return receBankName;
	}
	public void setReceBankName(String receBankName) {
		this.receBankName = receBankName;
	}
	public String getReceBankDistr() {
		return receBankDistr;
	}
	public void setReceBankDistr(String receBankDistr) {
		this.receBankDistr = receBankDistr;
	}
	public String getReceBankBranch() {
		return receBankBranch;
	}
	public void setReceBankBranch(String receBankBranch) {
		this.receBankBranch = receBankBranch;
	}
	public String getChargePhoneNo() {
		return chargePhoneNo;
	}
	public void setChargePhoneNo(String chargePhoneNo) {
		this.chargePhoneNo = chargePhoneNo;
	}
	public String getNotifyPhoneNo() {
		return notifyPhoneNo;
	}
	public void setNotifyPhoneNo(String notifyPhoneNo) {
		this.notifyPhoneNo = notifyPhoneNo;
	}
	public String getTakeAccount() {
		return takeAccount;
	}
	public void setTakeAccount(String takeAccount) {
		this.takeAccount = takeAccount;
	}
	public String getTakeType() {
		return takeType;
	}
	public void setTakeType(String takeType) {
		this.takeType = takeType;
	}
	public String getTrackTwo() {
		return trackTwo;
	}
	public void setTrackTwo(String trackTwo) {
		this.trackTwo = trackTwo;
	}
	public String getTrackThree() {
		return trackThree;
	}
	public void setTrackThree(String trackThree) {
		this.trackThree = trackThree;
	}
	public String getCardSequence() {
		return cardSequence;
	}
	public void setCardSequence(String cardSequence) {
		this.cardSequence = cardSequence;
	}
	public String getCardExternal() {
		return cardExternal;
	}
	public void setCardExternal(String cardExternal) {
		this.cardExternal = cardExternal;
	}
	public String getPayFeeCode() {
		return payFeeCode;
	}
	public void setPayFeeCode(String payFeeCode) {
		this.payFeeCode = payFeeCode;
	}
	public String getPaySettleCode() {
		return paySettleCode;
	}
	public void setPaySettleCode(String paySettleCode) {
		this.paySettleCode = paySettleCode;
	}
	public String getPayAreaCode() {
		return payAreaCode;
	}
	public void setPayAreaCode(String payAreaCode) {
		this.payAreaCode = payAreaCode;
	}
	public String getPayDeviceSn() {
		return payDeviceSn;
	}
	public void setPayDeviceSn(String payDeviceSn) {
		this.payDeviceSn = payDeviceSn;
	}
	public String getTrack55() {
		return track55;
	}
	public void setTrack55(String track55) {
		this.track55 = track55;
	}
	public String getIsEntrack() {
		return isEntrack;
	}
	public void setIsEntrack(String isEntrack) {
		this.isEntrack = isEntrack;
	}
	public String getSigImageStr() {
		return sigImageStr;
	}
	public void setSigImageStr(String sigImageStr) {
		this.sigImageStr = sigImageStr;
	}
	
}
