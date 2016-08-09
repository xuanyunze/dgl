package com.rxoa.zlpay.vo;

import org.json.JSONObject;

public class BlanceTakemoneyRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	private String orderType;
	private String orderId;
	
	public static BlanceTakemoneyRespVo getInstance(){
		return new BlanceTakemoneyRespVo();
	}
	
	
	public BlanceTakemoneyRespVo initFromJson(String json){
		try{System.out.println(json);
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.setRespCode(obj.getInt("respCode"));
				this.setRespMessage(obj.getString("respMessage"));
				this.setOrderId(obj.getString("orderId"));
				this.setOrderType(obj.getString("orderType"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
