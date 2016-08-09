package com.rxoa.zlpay.vo;

import org.json.JSONObject;

import com.rxoa.zlpay.base.util.JsonUtil;

public class OrderPayRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	private String payType;
	private String cardAccno;
	private String cardAcctype;
	private String cardAccblance;
	private String orderId;
	private String orderType;
	private String orderValue;
	private String payResult;
	
	public static OrderPayRespVo getInstance(){
		return new OrderPayRespVo();
	}
	
	public OrderPayRespVo initFromJson(String json){
		try{
			if(!JsonUtil.isJsonNull(json)){
				JSONObject obj = new JSONObject(json);
				this.payType = obj.getString("payType");
				this.cardAccno = obj.getString("cardAccno");
				this.cardAcctype = obj.getString("cardAcctype");
				this.cardAccblance = obj.getString("cardAccblance");
				this.orderId = obj.getString("orderId");
				this.orderType = obj.getString("orderType");
				this.orderValue = obj.getString("orderValue");
				this.payResult = obj.getString("payResult");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}

	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getCardAccno() {
		return cardAccno;
	}
	public void setCardAccno(String cardAccno) {
		this.cardAccno = cardAccno;
	}
	public String getCardAcctype() {
		return cardAcctype;
	}
	public void setCardAcctype(String cardAcctype) {
		this.cardAcctype = cardAcctype;
	}
	public String getCardAccblance() {
		return cardAccblance;
	}
	public void setCardAccblance(String cardAccblance) {
		this.cardAccblance = cardAccblance;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}
	public String getPayResult() {
		return payResult;
	}
	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}
}
