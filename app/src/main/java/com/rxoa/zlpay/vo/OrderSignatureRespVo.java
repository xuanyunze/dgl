package com.rxoa.zlpay.vo;

import com.rxoa.zlpay.base.util.JsonUtil;

public class OrderSignatureRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	public static OrderSignatureRespVo getInstance(){
		return new OrderSignatureRespVo();
	}
	
	public OrderSignatureRespVo initFromJson(String json){
		try{
			if(!JsonUtil.isJsonNull(json)){
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
}
