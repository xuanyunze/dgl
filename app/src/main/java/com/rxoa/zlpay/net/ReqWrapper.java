package com.rxoa.zlpay.net;

import android.content.Context;

public class ReqWrapper {
	public static SysRequest getSysRequest(Context context){
		return SysRequest.getInstance(context);
	}
	
	public static UserRequest getUserRequest(Context context){
		return UserRequest.getInstance(context);
	}
	
	public static OrderRequest getOrderRequest(Context context){
		return OrderRequest.getInstance(context);
	}
	
	public static UserAccRequest getUserAccRequest(Context context){
		return UserAccRequest.getInstance(context);
	}
	
	public static BlanceRequest getBlanceRequest(Context context){
		return BlanceRequest.getInstance(context);
	}
}
