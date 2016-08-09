package com.rxoa.zlpay.vo;

public class UserLogoutRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	public static UserLogoutRespVo getInstance(){
		return new UserLogoutRespVo();
	}
	public UserLogoutRespVo initFromJson(String json){
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
}
