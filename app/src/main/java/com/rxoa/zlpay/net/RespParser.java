package com.rxoa.zlpay.net;

import org.json.JSONObject;

import android.content.Context;

import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.acty.UserLoginActy;
import com.rxoa.zlpay.base.BaseEntity;
import com.rxoa.zlpay.base.util.Base64Util;
import com.rxoa.zlpay.base.util.DES3Util;
import com.rxoa.zlpay.security.AuthChecker;
import com.rxoa.zlpay.security.AuthChecker.AuthResult;

public class RespParser extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	protected int respCode = 0;
	protected String respMessage = "";
	protected String respString = "";
	protected Object respObject = null;
	
	public static RespParser getInstance(){
		return new RespParser();
	}
	public RespParser parse(Context context,String data,boolean isDecrypt){
		try{
			JSONObject resObj = new JSONObject(data);
			this.setRespCode(Integer.parseInt(resObj.getString("respCode")));
			this.setRespMessage(resObj.getString("respMsg"));
			this.setRespString(resObj.getString("respContent"));
			
			if(checkAuthData(context).equals(AuthResult.SUCCESS)&&respString!=null&&!respString.equals("null")&&!respString.equals("")){
				if(isDecrypt){
					respString = new String(DES3Util.decrypt(Base64Util.decrypt(Config.WorkKey), Base64Util.decrypt(respString)));
				}else{
					respString = new String(Base64Util.decrypt(respString));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public AuthResult checkAuthData(Context context){
		if(respCode!=0){
			if(respMessage.equals(AuthChecker.AuthResult.CHECK_SESSION_NEEDLOGIN.name())){
				UserLoginActy.launch(context, null);
				return AuthResult.CHECK_SESSION_NEEDLOGIN;
			}
		}
		return AuthResult.SUCCESS;
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
