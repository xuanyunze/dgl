package com.rxoa.zlpay.vo;

import org.json.JSONObject;

import com.rxoa.zlpay.base.util.JsonUtil;

public class OrderTakemoneyDetailRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String orderType;
	private String orderTime;
	private String orderValue;
	private String orderStat;
	private String takeValue;
	private String arriveType;
	private String arriveDate;
	private String arriveValue;
	private String benefitValue;
	private String arriveAccno;
	private String arriveAccname;
	
	public static OrderTakemoneyDetailRespVo getInstance(){
		return new OrderTakemoneyDetailRespVo();
	}
	
	public OrderTakemoneyDetailRespVo initFromJson(String json){
		try{
			if(!JsonUtil.isJsonNull(json)){
				JSONObject obj = new JSONObject(json);
				this.setOrderId(obj.getString("orderId"));
				this.setOrderType(obj.getString("orderType"));
				this.setOrderTime(obj.getString("orderTime"));
				this.setOrderValue(obj.getString("orderValue"));
				this.setOrderStat(obj.getString("orderStat"));
				this.setTakeValue(obj.getString("takeValue"));
				this.setArriveType(obj.getString("arriveType"));
				this.setArriveDate(obj.getString("arriveDate"));
				this.setArriveValue(obj.getString("arriveValue"));
				this.setBenefitValue(obj.getString("benefitValue"));
				this.setArriveAccno(obj.getString("arriveAccno"));
				this.setArriveAccname(obj.getString("arriveAccname"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
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

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}

	public String getTakeValue() {
		return takeValue;
	}

	public void setTakeValue(String takeValue) {
		this.takeValue = takeValue;
	}

	public String getArriveType() {
		return arriveType;
	}

	public void setArriveType(String arriveType) {
		this.arriveType = arriveType;
	}

	public String getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getArriveValue() {
		return arriveValue;
	}

	public void setArriveValue(String arriveValue) {
		this.arriveValue = arriveValue;
	}

	public String getBenefitValue() {
		return benefitValue;
	}

	public void setBenefitValue(String benefitValue) {
		this.benefitValue = benefitValue;
	}

	public String getArriveAccno() {
		return arriveAccno;
	}

	public void setArriveAccno(String arriveAccno) {
		this.arriveAccno = arriveAccno;
	}

	public String getOrderStat() {
		return orderStat;
	}

	public void setOrderStat(String orderStat) {
		this.orderStat = orderStat;
	}

	public String getArriveAccname() {
		return arriveAccname;
	}

	public void setArriveAccname(String arriveAccname) {
		this.arriveAccname = arriveAccname;
	}
}
