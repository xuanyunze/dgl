package com.rxoa.zlpay.vo;

import org.json.JSONObject;

import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.entity.OrderPhoneCharge;
import com.rxoa.zlpay.entity.OrderReceiveMoney;
import com.rxoa.zlpay.entity.OrderTakeMoney;

public class OrderDetailRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	private String orderFlowid;
	private String orderType;
	private String orderDate;
	private String orderValue;
	private String orderStat;
	private String payType;
	private String payAccName;
	private String payAccNo;
	private String payBankName;
	private String orderSigImgstr;
	private Object orderItem;
	
	public static OrderDetailRespVo getInstance(){
		return new OrderDetailRespVo();
	}
	public OrderDetailRespVo initFromJson(String json){
		try{System.out.println(json);
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.orderFlowid = obj.getString("orderFlowid");
				this.orderType = obj.getString("orderType");
				this.orderDate = obj.getString("orderDate");
				this.orderValue = obj.getString("orderValue");
				this.orderStat = obj.getString("orderStat");
				this.payType = obj.getString("payType");
				this.payAccName = obj.getString("payAccName");
				this.payAccNo = obj.getString("payAccNo");
				this.payBankName = obj.getString("payBankName");
				this.orderSigImgstr = obj.getString("orderSigImgstr");
				if(this.orderType.equals(AvOrderType.code2value(AvOrderType.code.ReceiveMoney.name()))){
					this.setOrderItem(OrderReceiveMoney.getInstance().initFromJson(obj.getString("orderItem")));
				}else if(this.orderType.equals(AvOrderType.code2value(AvOrderType.code.TakeMoney.name()))){
					this.setOrderItem(OrderTakeMoney.getInstance().initFromJson(obj.getString("orderItem")));
				}else if(this.orderType.equals(AvOrderType.code2value(AvOrderType.code.AccountCharge.name()))){
					
				}else if(this.orderType.equals(AvOrderType.code2value(AvOrderType.code.TransAcc.name()))){
					
				}else if(this.orderType.equals(AvOrderType.code2value(AvOrderType.code.RepayCredit.name()))){
					
				}else if(this.orderType.equals(AvOrderType.code2value(AvOrderType.code.PhoneCharge.name()))){
					this.setOrderItem(OrderPhoneCharge.getInstance().initFromJson(obj.getString("orderItem")));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public String getOrderFlowid() {
		return orderFlowid;
	}
	public void setOrderFlowid(String orderFlowid) {
		this.orderFlowid = orderFlowid;
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayAccName() {
		return payAccName;
	}
	public void setPayAccName(String payAccName) {
		this.payAccName = payAccName;
	}
	public String getPayAccNo() {
		return payAccNo;
	}
	public void setPayAccNo(String payAccNo) {
		this.payAccNo = payAccNo;
	}
	public String getPayBankName() {
		return payBankName;
	}
	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}
	public String getOrderSigImgstr() {
		return orderSigImgstr;
	}
	public void setOrderSigImgstr(String orderSigImgstr) {
		this.orderSigImgstr = orderSigImgstr;
	}
	public Object getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(Object orderItem) {
		this.orderItem = orderItem;
	}
}
