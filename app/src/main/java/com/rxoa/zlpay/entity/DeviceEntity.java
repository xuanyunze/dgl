package com.rxoa.zlpay.entity;

import org.json.JSONObject;

import com.rxoa.zlpay.base.BaseEntity;
import com.rxoa.zlpay.base.util.JsonUtil;

public class DeviceEntity extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String deviceSn;
	private String deviceType;
	private String deviceCate;
	private String deviceStat;
	private String bindDate;
	
	public DeviceEntity initFromJson(String json){
		try{
			if(!JsonUtil.isJsonNull(json)){
				JSONObject obj = new JSONObject(json);
				this.deviceSn = obj.getString("deviceSn");
				this.deviceType = obj.getString("deviceType");
				this.deviceCate = obj.getString("deviceCate");
				this.deviceStat = obj.getString("deviceStat");
				this.bindDate = obj.getString("bindDate");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceCate() {
		return deviceCate;
	}
	public void setDeviceCate(String deviceCate) {
		this.deviceCate = deviceCate;
	}
	public String getDeviceStat() {
		return deviceStat;
	}
	public void setDeviceStat(String deviceStat) {
		this.deviceStat = deviceStat;
	}
	public String getBindDate() {
		return bindDate;
	}
	public void setBindDate(String bindDate) {
		this.bindDate = bindDate;
	}
}
