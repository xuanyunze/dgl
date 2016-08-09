package com.rxoa.zlpay.entity;

import org.json.JSONObject;

import com.rxoa.zlpay.base.BaseEntity;
import com.rxoa.zlpay.base.util.JsonUtil;

public class UserAccInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String uid;
	private String realName;
	private String idType;
	private String idNo;
	private String regTime;
	private String authTime;
	private String ustat;
	
	public UserAccInfo initFromJson(String json){
		try{
			if(!JsonUtil.isJsonNull(json)){
				JSONObject obj = new JSONObject(json);
				this.uid = obj.getString("uid");
				this.realName = obj.getString("realName");
				this.idType = obj.getString("idType");
				this.idNo = obj.getString("idNo");
				this.ustat = obj.getString("ustat");
				this.authTime = obj.getString("authTime");
				this.regTime = obj.getString("regTime");
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
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getUstat() {
		return ustat;
	}
	public void setUstat(String ustat) {
		this.ustat = ustat;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getAuthTime() {
		return authTime;
	}
	public void setAuthTime(String authTime) {
		this.authTime = authTime;
	}
}
