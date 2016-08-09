package com.rxoa.zlpay.net;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.base.net.DefHttpClient;
import com.rxoa.zlpay.vo.SmsCodeReqVo;
import com.rxoa.zlpay.vo.SmsCodeRespVo;
import com.rxoa.zlpay.vo.SysUpdateReqVo;
import com.rxoa.zlpay.vo.SysUpdateRespVo;

public class SysRequest extends BaseRequest{
	public static SysRequest getInstance(Context context){
		SysRequest req = new SysRequest();
		req.setContext(context);
		return req;
	}
	
	/**检查更新**/
	public RespParser doCheckUpdate(SysUpdateReqVo reqVo){
		String reqUrl = Config.BASE_VERURL+"/app/version?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "checkversion");
			}else{
				params.put("action", "checkversion");
				params.put("appCate", "android");
				params.put("appCode", "main");
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,false,false);
			parser.parse(context,respString, false);
			parser.setRespObject(SysUpdateRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	public RespParser doGetSmsCode(SmsCodeReqVo reqVo){
		String reqUrl = BASE_URL+"/auth/vercode?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "checkversion");
			}else{
				params.put("action", "getvercode");
				params.put("reqType", reqVo.getReqType());
				params.put("phoneNo", reqVo.getPhoneNo());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,false,false);
			parser.parse(context,respString, false);
			parser.setRespObject(SmsCodeRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
}
