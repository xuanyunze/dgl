package com.rxoa.zlpay.net;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.base.net.DefHttpClient;
import com.rxoa.zlpay.base.util.PasswordUtil;

import com.rxoa.zlpay.vo.UserLoginReqVo;
import com.rxoa.zlpay.vo.UserLoginRespVo;

import com.rxoa.zlpay.vo.UserFindpwdReqVo;
import com.rxoa.zlpay.vo.UserFindpwdRespVo;
import com.rxoa.zlpay.vo.UserLogoutReqVo;
import com.rxoa.zlpay.vo.UserLogoutRespVo;
import com.rxoa.zlpay.vo.UserRegReqVo;
import com.rxoa.zlpay.vo.UserRegRespVo;
import com.rxoa.zlpay.vo.UserResetpwdReqVo;
import com.rxoa.zlpay.vo.UserSigninReqVo;
import com.rxoa.zlpay.vo.UserSigninRespVo;

public class UserRequest extends BaseRequest{
	
	public static UserRequest getInstance(Context context){
		UserRequest req = new UserRequest();
		req.setContext(context);
		return req;
	}

	//****用户注册**********//
	public RespParser doUserRegister(UserRegReqVo reqVo){
		String reqUrl = BASE_URL+"/auth/register?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "register");
				params.put("phone", "18600765527");
				params.put("pwd", "password");
				params.put("pwdstr", "passwrod123");
				params.put("scopestr", "水产渔业");
				params.put("addressstr", "北京市");
			}else{
				params.put("action", "register");
				params.put("reqid", reqVo.getReqid());
				params.put("uphone", reqVo.getUphone());
				params.put("upwd", PasswordUtil.webEncrypt(reqVo.getUphone(),reqVo.getUpwd()));
				params.put("ucode", reqVo.getUcode());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,false,false);
			parser.parse(context,respString, false);
			parser.setRespObject(UserRegRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****找回密码**********//
	public RespParser doFindUserpwd(UserFindpwdReqVo reqVo){
		String reqUrl = BASE_URL+"/auth/findpwd?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				
			}else{
				params.put("action", "findpwd");
				params.put("userId", reqVo.getUserId());
				params.put("reqId", reqVo.getReqId());
				params.put("userPwd", PasswordUtil.webEncrypt(reqVo.getUserId(),reqVo.getUserPwd()));
				params.put("vrCode", reqVo.getVrCode());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,false,false);
			parser.parse(context,respString, false);
			parser.setRespObject(UserFindpwdRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****重置密码**********//
	public RespParser doResetUserpwd(UserResetpwdReqVo reqVo){
		String reqUrl = BASE_URL+"/auth/resetpwd?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				
			}else{
				params.put("action", "resetpwd");
				params.put("hisPwd", PasswordUtil.webEncrypt(Config.Uid,reqVo.getHisPwd()));
				params.put("newPwd", PasswordUtil.webEncrypt(Config.Uid,reqVo.getNewPwd()));
				params.put("pwdType",reqVo.getPwdType());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(UserFindpwdRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****用户登录**********//
	public RespParser doUserLogin(UserLoginReqVo reqVo){
		String reqUrl = BASE_URL+"/auth/login?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "userlogin");
				params.put("userid", "18600765527");
				params.put("userpwd", "password");
				params.put("appuuid", "passwrod");
			}else{
				params.put("action", "login");
				params.put("uid", reqVo.getUid());
				params.put("upwd", PasswordUtil.webEncrypt(reqVo.getUid(),reqVo.getUpwd()));
				params.put("ucode", reqVo.getUcode());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,false,false);
			parser.parse(context,respString, false);
			parser.setRespObject(UserLoginRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****用户注销*********//
	public RespParser doUserLogout(UserLogoutReqVo reqVo){
		String reqUrl = BASE_URL+"/auth/logout?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "userlogout");
				params.put("userid", "18600765527");
			}else{
				params.put("action", "userlogout");
				params.put("userid", Config.Uid);
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(UserLogoutRespVo.getInstance().initFromJson(parser.getRespString()));			
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****用户签到*********//
	public RespParser doUserSignin(UserSigninReqVo reqVo){
		String reqUrl = BASE_URL+"/auth/signin?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "userlogout");
				params.put("userid", "18600765527");
			}else{
				params.put("action", "usersignin");
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,false,true);
			parser.parse(context,respString, false);
			parser.setRespObject(UserSigninRespVo.getInstance().initFromJson(parser.getRespString()));			
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
}
