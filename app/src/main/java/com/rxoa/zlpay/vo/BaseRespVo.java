package com.rxoa.zlpay.vo;

import com.rxoa.zlpay.base.BaseEntity;

public class BaseRespVo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	protected int respCode;
	protected String respMessage;
	
	public int getRespCode() {
		return respCode;
	}
	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
}
