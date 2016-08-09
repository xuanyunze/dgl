package com.rxoa.zlpay.entity;

import org.json.JSONObject;

import com.rxoa.zlpay.base.BaseEntity;
import com.rxoa.zlpay.base.util.JsonUtil;

public class BankCardEntity extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String accName;
	private String accNo;
	private String cardType;
	private String accBank;
	private String accDistr;
	private String accBankBranch;
	private String bankNo;
	private String isFast;
	private String isMainCard;
	private String bindTime;
	
	public BankCardEntity initFromJson(String json){
		try{
			if(!JsonUtil.isJsonNull(json)){
				JSONObject obj = new JSONObject(json);
				this.accName = obj.getString("accName");
				this.accNo = obj.getString("accNo");
				this.cardType = obj.getString("cardType");
				this.accBank = obj.getString("accBank");
				this.accDistr = obj.getString("accDistr");
				this.accBankBranch = obj.getString("accBankBranch");
				this.bankNo = obj.getString("bankNo");
				this.isFast = obj.getString("isFast");
				this.isMainCard = obj.getString("isMainCard");
				this.bindTime = obj.getString("bindTime");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getAccBank() {
		return accBank;
	}
	public void setAccBank(String accBank) {
		this.accBank = accBank;
	}
	public String getAccDistr() {
		return accDistr;
	}
	public void setAccDistr(String accDistr) {
		this.accDistr = accDistr;
	}
	public String getAccBankBranch() {
		return accBankBranch;
	}
	public void setAccBankBranch(String accBankBranch) {
		this.accBankBranch = accBankBranch;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getIsFast() {
		return isFast;
	}
	public void setIsFast(String isFast) {
		this.isFast = isFast;
	}
	public String getIsMainCard() {
		return isMainCard;
	}
	public void setIsMainCard(String isMainCard) {
		this.isMainCard = isMainCard;
	}
	public String getBindTime() {
		return bindTime;
	}
	public void setBindTime(String bindTime) {
		this.bindTime = bindTime;
	}
}
