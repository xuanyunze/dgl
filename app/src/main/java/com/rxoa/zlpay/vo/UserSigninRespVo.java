package com.rxoa.zlpay.vo;

import org.json.JSONObject;

public class UserSigninRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	private String workKey;
	private String signTime;
	
	public static UserSigninRespVo getInstance(){
		return new UserSigninRespVo();
	}
	public UserSigninRespVo initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.workKey = obj.getString("workKey");
				this.signTime = obj.getString("signTime");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	public String getWorkKey() {
		return workKey;
	}
	public void setWorkKey(String workKey) {
		this.workKey = workKey;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
}
