package com.rxoa.zlpay.vo;

public class BindDeviceReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String deviceType;
	private String deviceSn;
	private String keyMod;
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getKeyMod() {
		return keyMod;
	}
	public void setKeyMod(String keyMod) {
		this.keyMod = keyMod;
	}
}
