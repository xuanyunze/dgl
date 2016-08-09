package com.rxoa.zlpay.entity;

import java.util.ArrayList;
import java.util.List;

import com.rxoa.zlpay.base.BaseEntity;

public class OrderWrapper extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String orderType;
	private String orderValue;
	private String payType;
	private List<String> payTypeAllow;
	private String fastAccNo;
	private String fastAccName;
	private String deviceSn;
	private String deviceAccNo;
	private String deviceAccName;
	private String deviceSerial;
	private String payPassword;
	private Object OrderItem;
	
	public static OrderWrapper getInstance(){
		return new OrderWrapper();
	}
	public static enum PayType{
		Account,Fast,Device
	}
	public OrderWrapper wrap(Object obj){
		setOrderItem(obj);
		return this;
	}
	public OrderWrapper addPayTypeAllow(String type){
		try{
			if(payTypeAllow==null){
				payTypeAllow = new ArrayList<String>();
			}
			payTypeAllow.add(type);
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}
	public String getFastAccNo() {
		return fastAccNo;
	}
	public void setFastAccNo(String fastAccNo) {
		this.fastAccNo = fastAccNo;
	}
	public String getFastAccName() {
		return fastAccName;
	}
	public void setFastAccName(String fastAccName) {
		this.fastAccName = fastAccName;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getDeviceAccNo() {
		return deviceAccNo;
	}
	public void setDeviceAccNo(String deviceAccNo) {
		this.deviceAccNo = deviceAccNo;
	}
	public String getDeviceAccName() {
		return deviceAccName;
	}
	public void setDeviceAccName(String deviceAccName) {
		this.deviceAccName = deviceAccName;
	}
	public String getDeviceSerial() {
		return deviceSerial;
	}
	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public Object getOrderItem() {
		return OrderItem;
	}
	public void setOrderItem(Object orderItem) {
		OrderItem = orderItem;
	}
	public List<String> getPayTypeAllow() {
		return payTypeAllow;
	}
	public void setPayTypeAllow(List<String> payTypeAllow) {
		this.payTypeAllow = payTypeAllow;
	}
}
