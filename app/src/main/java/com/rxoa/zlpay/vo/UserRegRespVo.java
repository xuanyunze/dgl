package com.rxoa.zlpay.vo;

import org.json.JSONObject;

public class UserRegRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	private int respCode;
	private String respMessage;
	
	public static UserRegRespVo getInstance(){
		return new UserRegRespVo();
	}
	public UserRegRespVo initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				respCode = obj.getInt("respCode");
				respMessage = obj.getString("respMessage");
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
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}	
}
