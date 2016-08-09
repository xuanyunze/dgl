package com.rxoa.zlpay.base.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.base.util.Base64Util;
import com.rxoa.zlpay.base.util.DES3Util;
import com.rxoa.zlpay.base.util.HttpUtil;
import com.rxoa.zlpay.base.util.LogUtil;
import com.rxoa.zlpay.base.util.RSAUtil;
import com.rxoa.zlpay.base.util.StringUtil;

import android.annotation.SuppressLint;

public class DefHttpClient {
	private static final String TAG = "HttpConnection";
	private static final int CONNECTION_TIMEOUT = 165 * 1000;
	private static final int READ_TIMEOUT = 165 * 1000;
	public static HttpURLConnection conn = null;
	
	private static HttpURLConnection getHttpURLConnection(Method m,String urlStr) throws DefHttpException {
		if (conn != null) {
			close();
		}
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			setConnection(m, conn);
			return conn;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			close();
			throw new DefHttpException(DefHttpException.URL_ERROR);
		} catch (ProtocolException e) {
			e.printStackTrace();
			close();
			throw new DefHttpException(DefHttpException.PROTOCL_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			close();
			throw new DefHttpException(DefHttpException.ERROR);
		}
	}

	private static void setConnection(Method method, HttpURLConnection conn)
			throws ProtocolException {
		conn.setConnectTimeout(CONNECTION_TIMEOUT);
		//conn.setRequestMethod(method.name());
		String boundary = "*****";
		/* 允许Input、Output，不使用Cache */
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		/* 设定传输的method=POST */
		conn.setConnectTimeout(CONNECTION_TIMEOUT);
		conn.setReadTimeout(READ_TIMEOUT);
		/* setRequestProperty */
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		//conn.setChunkedStreamingMode(10*1024);
				//"application/x-www-form-urlencoded"+boundary);
	}

	private static String readInputStream(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		byte[] buff = new byte[4096];
		int count = 0;
		while (count > -1) {
			count = is.read(buff);
			if (count > 0) {
				String str = new String(buff, 0, count, "UTF8");
				sb.append(str);
			} else {
				break;
			}
		}
		String result = sb.toString();
		LogUtil.log(TAG, "RESPONSE:"+result);
		return result;
	}

	private static void close() {
		if (null != conn) {
			LogUtil.log(TAG, "关闭连接!URL:" + conn.getURL().toString());
			conn.disconnect();
			conn = null;
		}
	}

	public static String doRequest(String MethodName, String urlStr,Map<String,String> params,boolean isAddParam)
			throws DefHttpException {
		return doRequest(MethodName,urlStr,params,isAddParam,Config.DATA_ENCRYPT,true);
	}
	
	@SuppressLint("DefaultLocale")
	public static String doRequest(String MethodName, String urlStr,
			Map<String, String> params,boolean isAddParam,boolean isEncrypt,boolean isSig) throws DefHttpException {
		if(isAddParam){params = addClientInfo(params);}
		HttpURLConnection conn = null;
		InputStream inStream = null;
		OutputStreamWriter oStream = null;
		String paramStr = dealParams(params);
		if(isEncrypt&&isSig){
			paramStr = encryptParam(paramStr);
		}else if(isSig){
			paramStr = signParam(paramStr);
		}else{
			paramStr = clearParam(paramStr);
		}
		if(MethodName.equalsIgnoreCase("GET")){
			urlStr+="&"+paramStr;
			LogUtil.log(TAG,"DOGET:URL:"+urlStr);
			conn = getHttpURLConnection(Method.valueOf(MethodName.toUpperCase()), urlStr);
			try{
				conn.setDoOutput(false);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(MethodName.equalsIgnoreCase("POST")){
			LogUtil.log(TAG,"DOPOST:URL:"+urlStr+"###"+paramStr);
			conn = getHttpURLConnection(Method.valueOf(MethodName.toUpperCase()), urlStr);
			try {
				oStream = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				oStream.write(paramStr,0,paramStr.getBytes().length);
				//oStream.write(paramStr);
				oStream.flush();
			}catch (IOException e) {
				e.printStackTrace();
				throw new DefHttpException(DefHttpException.CONNECT_INTERRUPT);
			} finally {
				try{
					if(null!=oStream){
						oStream.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		try{
			LogUtil.log(TAG,"HTTP-STAT:"+conn.getResponseCode());
			inStream = conn.getInputStream();
			String inputString = readInputStream(inStream);
			if (conn.getResponseCode() != DefHttpException.OK) {
				throw new DefHttpException(DefHttpException.ERROR);
			}
			if(StringUtil.isEmpty(inputString)){
				throw new DefHttpException(DefHttpException.CONNECT_INTERRUPT);
			}
			return inputString;
		}catch (IOException e) {
			e.printStackTrace();
			throw new DefHttpException(DefHttpException.CONNECT_INTERRUPT);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (null != inStream){
					inStream.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String dealParams(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		if (!params.isEmpty()) {
			Set<String> keys = params.keySet();
			int index = 0;
			for (String key : keys) {
				if (index == 0) {
					sb.append(key + "=" + params.get(key));
				} else {
					sb.append("&" + key + "=" + params.get(key));
				}
				index++;
			}
		}
		return sb.toString();
	}
	
	public enum Method {
		GET,
		POST
	}
	
	//======添加公共参数====
	private static Map<String, String> addClientInfo(Map<String, String> params) {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put("seUid", Config.Uid);
		params.put("sePhoneSn", Config.PhoneSN);
		params.put("seAppUuid", Config.AppUnid);
		params.put("seClientIp", "4");
		params.put("seToken", Config.Token);
		params.put("seFlowid", "");
		params.put("seDeviceSn", Config.DeviceSN);
		params.put("seLocationlat", Config.location_lat+"");
		params.put("seLocationlng", Config.location_lng+"");
		params.put("seLocationString", Config.location_string);
		return params;
	}
	private static String encryptParam(String param){
		String resString = param;
		try{
			String data = Base64Util.encrypt(DES3Util.encrypt(Base64Util.decrypt(Config.WorkKey), param.getBytes()));
			String sig = RSAUtil.sign(data.getBytes(), Config.PrivateKey);
			resString = "qdata="+URLEncoder.encode(HttpUtil.encodeBreak(data),"utf-8")+"&qsig="+URLEncoder.encode(HttpUtil.encodeBreak(sig),"utf-8")+"&qtoken="+URLEncoder.encode(HttpUtil.encodeBreak(Config.Token),"utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return resString;
	}
	private static String signParam(String param){
		String resString = param;
		try{
			String data = Base64Util.encrypt(param.getBytes());
			String sig = RSAUtil.sign(data.getBytes(), Config.PrivateKey);
			resString = "qdata="+URLEncoder.encode(HttpUtil.encodeBreak(data),"utf-8")+"&qsig="+URLEncoder.encode(HttpUtil.encodeBreak(sig),"utf-8")+"&qtoken="+URLEncoder.encode(HttpUtil.encodeBreak(Config.Token),"utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return resString;
	}
	private static String clearParam(String param){
		String resString = param;
		try{
			resString = "qdata="+URLEncoder.encode(HttpUtil.encodeBreak(Base64Util.encrypt(param.getBytes())),"utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		return resString;
	}
}

