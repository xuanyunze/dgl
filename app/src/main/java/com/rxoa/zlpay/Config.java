package com.rxoa.zlpay;

import com.rxoa.zlpay.base.util.DateUtil;

import android.os.Environment;

public class Config {
	public static String VERSION = "2.0";
	public static boolean USE_LOCAL_DATA = false;
	
	public static String BASE_VERURL = "http://210.51.190.6:9921/verserver";
	
	
	//public static String BASE_URL = "http://218.106.246.198:9821/mpayserver";
	//public static String BASE_URL = "http://192.168.0.193:8080/mpayserver";
	
	//老登陆id
	//public static String BASE_URL = "http://210.51.190.6:9921/mpayserver";
	
	//新ip地址
	public static String BASE_URL = "http://123.125.127.79:9921/mpayserver";
	
	//测试用的id
	/*public static String BASE_URL = "https://www.hybunion.cn/CubeCoreConsole/merchant/";
	public static String DEAL_URL = "http://123.127.39.54:1900";*/
	//==current user info
	public static String Uid = null;
	public static String Uname = null;
	public static String PhoneSN = null;
	public static String AppUnid = null;
	public static String DeviceSN = null;
	public static String Token = null;
	public static String PublicKey = null;
	public static String PrivateKey = null;
	public static String WorkKey = null;
	public static String SignTime = null;
	public static String LastActTime = DateUtil.getNowFullDateTime();
	public static int SessionTimeOut = 15*60;
	
	public static String location_string = "";
	public static double location_lat = 0.0D;
	public static double location_lng = 0.0D;
	
	public static final boolean DATA_ENCRYPT = true;
	public static final String PRE_ISLOGIN = "isLogin";
	public static final String PRE_USERINFO = "userinfo";

	public static boolean _isLogin = false;
	public static final String PRODCUT_NAME = "";
	public static final String PRE_SETTING = "pre_setting";
	
	public static float _Density = 240;
	public static int _ScreenWidth;
	public static int _ScreenHeight;
	
	public static final int RESPONSE_SUCCESS = 1;
	public static final int RESPONSE_FAIL = 0;
	
	public static String path_auth = Environment.getExternalStorageDirectory().getAbsolutePath() + "/auth/";
	public static String path_sig = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sig/";
}
