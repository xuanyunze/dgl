package com.rxoa.zlpay.net;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.rxoa.zlpay.base.net.DefHttpClient;
import com.rxoa.zlpay.util.AvatorUtil;
import com.rxoa.zlpay.vo.OrderDetailReqVo;
import com.rxoa.zlpay.vo.OrderDetailRespVo;
import com.rxoa.zlpay.vo.OrderPayReqVo;
import com.rxoa.zlpay.vo.OrderPayRespVo;
import com.rxoa.zlpay.vo.OrderSignatureReqVo;
import com.rxoa.zlpay.vo.OrderSignatureRespVo;
import com.rxoa.zlpay.vo.OrderTakemoneyDetailReqVo;
import com.rxoa.zlpay.vo.OrderTakemoneyDetailRespVo;
import com.rxoa.zlpay.vo.OrderlistReqVo;
import com.rxoa.zlpay.vo.OrderlistRespVo;

public class OrderRequest extends BaseRequest{
	public static OrderRequest getInstance(Context context){
		OrderRequest req = new OrderRequest();
		req.setContext(context);
		return req;
	}
	//****获取交易记录**********//
	public RespParser doQueryOrderlist(OrderlistReqVo reqVo){
		String reqUrl = BASE_URL+"/order/query?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "queryorderlist");
			}else{
				params.put("action", "queryorderlist");
				params.put("startIndex", reqVo.getStartIndex());
				params.put("maxCount", reqVo.getMaxCount());
				params.put("orderType", reqVo.getOrderType());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(OrderlistRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	public RespParser doQueryOrderDetail(OrderDetailReqVo reqVo){
		String reqUrl = BASE_URL+"/order/detail?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "orderdetail");
			}else{
				params.put("action", "orderdetail");
				params.put("orderFlowid", reqVo.getOrderFlowid());
				params.put("orderType", reqVo.getOrderType());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(OrderDetailRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//提现订单查询
	public RespParser doQueryOrderTakemoneyDetail(OrderTakemoneyDetailReqVo reqVo){
		String reqUrl = BASE_URL+"/order/detail/takemoney?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "orderdetailtakemoney");
			}else{
				params.put("action", "orderdetailtakemoney");
				params.put("orderFlowid", reqVo.getOrderFlowid());
				params.put("orderType", reqVo.getOrderType());
			}
			String respString = DefHttpClient.doRequest("GET", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(OrderTakemoneyDetailRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	 //交易推送
	public RespParser doPayOrder(OrderPayReqVo reqVo){
		String reqUrl = BASE_URL+"/order/pay?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "payorder");
			}else{
				params.put("action", "payorder");
				params.put("orderId", reqVo.getOrderId());
				params.put("orderDate", reqVo.getOrderDate());
				params.put("orderType", reqVo.getOrderType());
				params.put("orderValue", reqVo.getOrderValue());
				params.put("orderStat", reqVo.getOrderStat());
				
				params.put("payType", reqVo.getPayType());
				params.put("payAccName", reqVo.getPayAccName());
				params.put("payAccNo", reqVo.getPayAccNo());
				params.put("payBankName", reqVo.getPayBankName());
				params.put("payPwd", reqVo.getPayPwd());
				params.put("payFeeCode", reqVo.getPayFeeCode());
				params.put("paySettleCode", reqVo.getPaySettleCode());
				params.put("payAreaCode", reqVo.getPayAreaCode());
				params.put("payDeviceSn", reqVo.getPayDeviceSn());
				System.out.println("xxxxxxxx=="+reqVo.getPayFeeCode()+reqVo.getPayAreaCode()+reqVo.getPayFeeCode());
				params.put("receAccName", reqVo.getReceAccName());
				params.put("receAccNo", reqVo.getReceAccNo());
				params.put("receBankName", reqVo.getReceBankName());
				params.put("receBankDistr", reqVo.getReceBankDistr());
				params.put("receBankBranch", reqVo.getReceBankBranch());
				params.put("chargePhoneNo", reqVo.getChargePhoneNo());
				params.put("notifyPhoneNo", reqVo.getNotifyPhoneNo());
				params.put("takeAccount", reqVo.getTakeAccount());
				params.put("takeType", reqVo.getTakeType());
				
				params.put("isEntrack",reqVo.getIsEntrack());
				params.put("trackTwo", reqVo.getTrackTwo());
				params.put("trackThree",reqVo.getTrackThree());
				params.put("track55",reqVo.getTrack55());
				params.put("cardSequence",reqVo.getCardSequence());
				params.put("cardExternal",reqVo.getCardExternal());
				params.put("sigImageStr", reqVo.getSigImageStr());
			}
			String respString = DefHttpClient.doRequest("POST", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(OrderPayRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
	//****获取交易记录**********//
	public RespParser doOrderSignature(OrderSignatureReqVo reqVo){
		String reqUrl = BASE_URL+"/order/signature?random="+getRandom();
		RespParser parser = RespParser.getInstance();
		try{
			Map<String, String> params = new HashMap<String, String>();
			if(USE_LOCAL_DATA){
				params.put("action", "signature");
			}else{
				params.put("action", "signature");
				params.put("orderId", reqVo.getOrderId());
				params.put("orderType", reqVo.getOrderType());
				params.put("imgStr", reqVo.getImgStr());
			}
			String respString = DefHttpClient.doRequest("POST", reqUrl, params,true,true,true);
			parser.parse(context,respString, true);
			parser.setRespObject(OrderSignatureRespVo.getInstance().initFromJson(parser.getRespString()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return parser;
	}
}
