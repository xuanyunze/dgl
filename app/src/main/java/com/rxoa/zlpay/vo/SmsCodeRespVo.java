package com.rxoa.zlpay.vo;

import org.json.JSONObject;

public class SmsCodeRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	private String reqId;
	private String verCode;
	
	public static SmsCodeRespVo getInstance(){
		return new SmsCodeRespVo();
	}
	public SmsCodeRespVo initFromJson(String json){
		try{System.out.println(json);
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.reqId = obj.getString("reqId");
				this.verCode = obj.getString("verCode");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}

	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public String getVerCode() {
		return verCode;
	}
	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}
}
