package com.rxoa.zlpay.vo;

public class OrderSignatureReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String orderType;
	private String imgStr;
	
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
	public String getImgStr() {
		return imgStr;
	}
	public void setImgStr(String imgStr) {
		this.imgStr = imgStr;
	}
}
