package com.rxoa.zlpay.net;

import java.util.HashMap;
import java.util.Map;

import com.rxoa.zlpay.base.net.DefHttpClient;
import com.rxoa.zlpay.vo.BlanceQueryReqVo;
import com.rxoa.zlpay.vo.BlanceQueryRespVo;
import com.rxoa.zlpay.vo.BlanceTakemoneyReqVo;
import com.rxoa.zlpay.vo.BlanceTakemoneyRespVo;
import com.rxoa.zlpay.vo.OrderlistReqVo;
import com.rxoa.zlpay.vo.OrderlistRespVo;

import android.content.Context;

public class BlanceRequest extends BaseRequest{
	public static BlanceRequest getInstance(Context context){
		BlanceRequest req = new BlanceRequest();
		req.setContext(context);
		return req;
	}
	
	//***查询账户信息**********//
	public RespParser doQueryBlance(BlanceQueryReqVo reqVo){
		String reqUrl = BASE_URL+"/blance/query?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "queryblance");
			}else{
				params.put("action", "queryblance");
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(BlanceQueryRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****提现**********//
	public RespParser doTakemoney(BlanceTakemoneyReqVo reqVo){
		String reqUrl = BASE_URL+"/blance/takemoney?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "takemoney");
			}else{
				params.put("action", "takemoney");
				params.put("takeValue", reqVo.getTakeValue());
				params.put("takePwd", reqVo.getTakePwd());
				params.put("arriveType", reqVo.getArriveType());
				params.put("arriveDate", reqVo.getArriveDate());
				params.put("arriveValue", reqVo.getArriveValue());
				params.put("benefitValue", reqVo.getBenefitValue());
				System.out.println("xpwd=="+reqVo.getTakePwd());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(BlanceTakemoneyRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
}
