package com.rxoa.zlpay.vo;

import org.json.JSONObject;

import com.rxoa.zlpay.base.util.JsonUtil;

public class SysUpdateRespVo {
	private String versionName;
	private String downloadUrl;
	private String description;
	
	public SysUpdateRespVo initFromJson(String json){
		try{
			if(!JsonUtil.isJsonNull(json)){
				JSONObject obj = new JSONObject(json);
				this.versionName = obj.getString("versionName");
				this.downloadUrl = obj.getString("downloadUrl");
				this.description = obj.getString("description");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public static SysUpdateRespVo getInstance(){
		return new SysUpdateRespVo();
	}
	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
