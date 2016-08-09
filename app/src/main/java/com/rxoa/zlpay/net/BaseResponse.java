package com.rxoa.zlpay.net;

import com.rxoa.zlpay.base.util.Base64Util;
import com.rxoa.zlpay.base.util.DES3Util;
import com.rxoa.zlpay.security.DataManager;

public class BaseResponse {
	protected int respCode = 0;
	protected String respMessage = "";
	protected String respString = "";
	protected Object respObject = null;

	public Object decryptData(String data,boolean isDecrypt){
		try{
			if(isDecrypt){
				respString = new String(DES3Util.decrypt(DataManager.getWorkKey(), Base64Util.decrypt(data)));
			}else{
				respString = new String(Base64Util.decrypt(data));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	public int getRespCode() {
		return respCode;
	}
	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public String getRespString() {
		return respString;
	}
	public void setRespString(String respString) {
		this.respString = respString;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	public Object getRespObject() {
		return respObject;
	}
	public void setRespObject(Object respObject) {
		this.respObject = respObject;
	}
}
