package com.rxoa.zlpay.net;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.rxoa.zlpay.base.net.DefHttpClient;
import com.rxoa.zlpay.vo.BindDeviceReqVo;
import com.rxoa.zlpay.vo.BindDeviceRespVo;
import com.rxoa.zlpay.vo.BindMainCardReqVo;
import com.rxoa.zlpay.vo.BindMainCardRespVo;
import com.rxoa.zlpay.vo.BindOtherCardReqVo;
import com.rxoa.zlpay.vo.BindOtherCardRespVo;
import com.rxoa.zlpay.vo.GetBankReqVo;
import com.rxoa.zlpay.vo.GetBankRespVo;
import com.rxoa.zlpay.vo.PaytypeReqVo;
import com.rxoa.zlpay.vo.PaytypeRespVo;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;
import com.rxoa.zlpay.vo.UserActivateReqVo;
import com.rxoa.zlpay.vo.UserActivateRespVo;
import com.rxoa.zlpay.vo.UserLogoutReqVo;
import com.rxoa.zlpay.vo.UserRealNameReqVo;
import com.rxoa.zlpay.vo.UserRealNameRespVo;

public class UserAccRequest extends BaseRequest{
	
	public static UserAccRequest getInstance(Context context){
		UserAccRequest req = new UserAccRequest();
		req.setContext(context);
		return req;
	}
	
	//****获银行字典信息-所有******//
	public RespParser doQueryBanksInfo(GetBankReqVo reqVo){
		String reqUrl = BASE_URL+"/dict/banks?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "querybanks");
				params.put("userid", "");
			}else{
				params.put("action", "querybanks");
				params.put("reqtype", reqVo.getReqtype());
				params.put("province", reqVo.getProvince());
				params.put("city", reqVo.getCity());
				params.put("area", reqVo.getArea());
				params.put("bankName", reqVo.getBankName());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(GetBankRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****获取用户账户信息-所有******//
	public RespParser doQueryUserAccInfo(UserAccInfoReqVo reqVo){
		String reqUrl = BASE_URL+"/account/query?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "queryaccount");
				params.put("userid", "");
			}else{
				params.put("action", "queryaccount");
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(UserAccInfoRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****获取用户账户信息******//
	public RespParser doQueryUserAcc(UserAccInfoReqVo reqVo){
		String reqUrl = BASE_URL+"/account/query/accinfo?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "queryaccinfo");
				params.put("userid", "18600765527");
			}else{
				params.put("action", "queryaccinfo");
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(UserAccInfoRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****获取用户账户信息******//
	public RespParser doQueryPaytypeInfo(PaytypeReqVo reqVo){
		String reqUrl = BASE_URL+"/account/query/paytype?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "realnameauth");
				params.put("userid", "18600765527");
			}else{
				params.put("action", "querypaytype");
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(PaytypeRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****用户实名认证******//
	public RespParser doRealNameAuth(UserRealNameReqVo reqVo){
		String reqUrl = BASE_URL+"/account/realnameauth?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "realnameauth");
			}else{
				params.put("action", "realnameauth");
				params.put("realName", reqVo.getRealName());
				params.put("idType", reqVo.getIdType());
				params.put("idNo", reqVo.getIdNo());
				params.put("posImgStr", reqVo.getPosImgStr());
				params.put("nageImgStr", reqVo.getNageImgStr());
				params.put("selfImgStr", reqVo.getSelfImgStr());
			}
			String respString = DefHttpClient.doRequest("POST", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(UserRealNameRespVo.getInstance().initFromJson(parser.getRespString()));			
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****绑定主银行卡******//
	public RespParser doBindMainCard(BindMainCardReqVo reqVo){
		String reqUrl = BASE_URL+"/account/bindmaincard?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "bindmaincard");
			}else{
				params.put("action", "bindmaincard");
				params.put("province",reqVo.getProvince());
				params.put("city", reqVo.getCity());
				params.put("accCate", reqVo.getAccCate());
				params.put("accName", reqVo.getAccName());
				params.put("accNo", reqVo.getAccNo());
				params.put("bankName", reqVo.getBankName());
				params.put("bankDistr", reqVo.getBankDistr());
				params.put("bankBranch", reqVo.getBankBranch());
				params.put("bankCode", reqVo.getBankCode());
				params.put("posImgStr", reqVo.getPosImgStr());
				params.put("nageImgStr", reqVo.getNageImgStr());
			}
			String respString = DefHttpClient.doRequest("POST", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(BindMainCardRespVo.getInstance().initFromJson(parser.getRespString()));			
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****绑定其它银行卡******//
	public RespParser doBindOtherCard(BindOtherCardReqVo reqVo){
		String reqUrl = BASE_URL+"/account/bindothercard?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "bindmaincard");
			}else{
				/*params.put("action", "bindothercard");
				params.put("accName", reqVo.getAccName());
				params.put("accNo", reqVo.getAccNo());
				params.put("bankName", reqVo.getBankName());
				params.put("bankDistr", reqVo.getBankDistr());
				params.put("bankBranch", reqVo.getBankBranch());
				params.put("cardType", reqVo.getCardType());
				params.put("isFast", reqVo.getIsFast());*/
				params.put("action", "bindothercard");
				params.put("accName", reqVo.getAccName());
				params.put("accNo", reqVo.getAccNo());
				params.put("bankName", reqVo.getBankName());
				params.put("bankDistr", reqVo.getBankDistr());
				params.put("bankBranch", reqVo.getBankBranch());
				params.put("cardType", reqVo.getCardType());
				params.put("isFast", reqVo.getIsFast());
				params.put("cardValidDate", reqVo.getCardValidDate());
				params.put("cardCvv", reqVo.getCardCvv());
				params.put("posImgStr", reqVo.getPosImgStr());

			}
			String respString = DefHttpClient.doRequest("POST", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(BindOtherCardRespVo.getInstance().initFromJson(parser.getRespString()));			
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****绑定设备******//
	public RespParser doBindDevice(BindDeviceReqVo reqVo){
		String reqUrl = BASE_URL+"/account/binddevice?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "binddevice");
			}else{
				params.put("action", "binddevice");
				params.put("deviceType", reqVo.getDeviceType());
				params.put("deviceSn", reqVo.getDeviceSn());
				params.put("keyMod", reqVo.getKeyMod());
			}
			String respString = DefHttpClient.doRequest("POST", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(BindDeviceRespVo.getInstance().initFromJson(parser.getRespString()));			
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****激活用户******//
	public RespParser doUserActivate(UserActivateReqVo reqVo){
		String reqUrl = BASE_URL+"/account/activate?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "bindmaincard");
			}else{
				params.put("action", "useractivate");
				params.put("actiType", reqVo.getActiType());
				params.put("actiCode", reqVo.getActiCode());
				params.put("actiSn", reqVo.getActiSn());
				params.put("payPwd", reqVo.getPayPwd());
			}
			String respString = DefHttpClient.doRequest("POST", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(UserActivateRespVo.getInstance().initFromJson(parser.getRespString()));			
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}

	public RespParser doQueryUserAccInfo(UserLogoutReqVo reqVo) {
		// TODO Auto-generated method stub
		return null;
	}
}
