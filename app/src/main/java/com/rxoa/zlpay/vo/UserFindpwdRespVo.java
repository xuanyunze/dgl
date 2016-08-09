package com.rxoa.zlpay.vo;

public class UserFindpwdRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	public static UserFindpwdRespVo getInstance(){
		return new UserFindpwdRespVo();
	}
	
	public UserFindpwdRespVo initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
}
