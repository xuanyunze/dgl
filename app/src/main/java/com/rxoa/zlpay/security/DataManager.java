package com.rxoa.zlpay.security;

import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.base.util.Base64Util;
import com.rxoa.zlpay.base.util.RSAUtil;
import com.rxoa.zlpay.vo.UserLoginRespVo;
import com.rxoa.zlpay.vo.UserSigninRespVo;

public class DataManager {
	
	public static boolean doLoginSet(UserLoginRespVo respVo){
		try{
			Config.Uid = respVo.getUid();
			Config.Token = respVo.getToken();
			Config.PublicKey = respVo.getPublicKey();
			Config.PrivateKey = respVo.getPrivateKey();
			Config._isLogin = true;
			Config.LastActTime = respVo.getLoginTime();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public static boolean doSigninSet(UserSigninRespVo respVo){
		try{
			Config.WorkKey = Base64Util.encrypt(RSAUtil.decryptByPrivateKey(Base64Util.decrypt(respVo.getWorkKey()),Config.PrivateKey));
			Config.SignTime = respVo.getSignTime();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public static boolean doLogoutSet(){
		try{
			Config.Uid = null;
			Config._isLogin = false;
			Config.Token = null;
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public static byte[] getWorkKey(){
		try{
			Base64Util.decrypt(Config.WorkKey);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new byte[24];
	}
}
