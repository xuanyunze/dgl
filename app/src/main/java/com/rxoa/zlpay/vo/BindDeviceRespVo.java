package com.rxoa.zlpay.vo;

import org.json.JSONObject;

public class BindDeviceRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	private String resKey;
	
	public static BindDeviceRespVo getInstance(){
		return new BindDeviceRespVo();
	}
	public BindDeviceRespVo initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.respCode = obj.getInt("respCode");
				this.resKey = obj.getString("resKey");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public String getResKey() {
		return resKey;
	}
	public void setResKey(String resKey) {
		this.resKey = resKey;
	}
}
