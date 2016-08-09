package com.rxoa.zlpay.vo;

public class UserActivateRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	public static UserActivateRespVo getInstance(){
		return new UserActivateRespVo();
	}
	public UserActivateRespVo initFromJson(String json){
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
}
