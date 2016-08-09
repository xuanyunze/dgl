package com.rxoa.zlpay.vo;

public class OrderTakemoneyDetailReqVo extends BaseReqVo{
	private static final long serialVersionUID = 1L;

	private String orderType;
	private String orderFlowid;
	
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderFlowid() {
		return orderFlowid;
	}
	public void setOrderFlowid(String orderFlowid) {
		this.orderFlowid = orderFlowid;
	}
}
