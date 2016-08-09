package com.rxoa.zlpay.vo;

public class UserResetpwdRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;

	public static UserResetpwdRespVo getInstance(){
		return new UserResetpwdRespVo();
	}
	
	public UserResetpwdRespVo initFromJson(String json){
		try{
			if(json!=null&&!json.equals("null")||json.equals("")){
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
}
