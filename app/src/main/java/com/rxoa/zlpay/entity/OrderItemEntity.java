package com.rxoa.zlpay.entity;

import org.json.JSONObject;

import com.rxoa.zlpay.base.BaseEntity;

public class OrderItemEntity extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String orderUuid;
	private String orderUid;
	private String orderType;
	private String orderDate;
	private String orderFlowid;
	private String orderValue;
	private String orderStat;
	private String orderSubtype;
	
	public OrderItemEntity initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.orderType = obj.getString("orderType");
				this.orderDate = obj.getString("orderDate");
				this.orderFlowid = obj.getString("orderFlowid");
				this.orderValue = obj.getString("orderValue");
				this.orderStat = obj.getString("orderStat");
				this.orderSubtype = obj.getString("orderSubtype");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public String getOrderUid() {
		return orderUid;
	}

	public void setOrderUid(String orderUid) {
		this.orderUid = orderUid;
	}

	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderFlowid() {
		return orderFlowid;
	}
	public void setOrderFlowid(String orderFlowid) {
		this.orderFlowid = orderFlowid;
	}
	public String getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}
	public String getOrderStat() {
		return orderStat;
	}
	public void setOrderStat(String orderStat) {
		this.orderStat = orderStat;
	}

	public String getOrderSubtype() {
		return orderSubtype;
	}

	public void setOrderSubtype(String orderSubtype) {
		this.orderSubtype = orderSubtype;
	}
}
