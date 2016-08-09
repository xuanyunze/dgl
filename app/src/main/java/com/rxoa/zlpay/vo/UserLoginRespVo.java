package com.rxoa.zlpay.vo;

import org.json.JSONObject;

public class UserLoginRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	private String uid;
	private String token;
	private String publicKey;
	private String privateKey;
	private String loginTime;
	
	public static UserLoginRespVo getInstance(){
		return new UserLoginRespVo();
	}
	public UserLoginRespVo initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				uid = obj.getString("uid");
				token = obj.getString("token");
				privateKey = obj.getString("privateKey");
				loginTime = obj.getString("loginTime");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
}
