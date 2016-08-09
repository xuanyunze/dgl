package com.rxoa.zlpay.device;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ivt.bluetooth.ibridge.BluetoothIBridgeDevice;
import com.jhl.jhlblueconn.BlueCommangerCallback;
import com.jhl.jhlblueconn.BluetoothCommmanager;
import com.rxoa.zlpay.base.device.DeviceInfo;
import com.rxoa.zlpay.base.device.DeviceInterface;
import com.rxoa.zlpay.base.device.DeviceListener;
import com.rxoa.zlpay.base.device.EmvReadCardInfoVo;
import com.rxoa.zlpay.base.util.CodeUtil;
import com.rxoa.zlpay.base.util.StringUtil;

import android.content.Context;
import android.util.Log;

public class DEVCJHLM60Impl implements DeviceInterface,BlueCommangerCallback {
	private Context context;
	private DeviceListener deviceListener;
	private String deviceAddress;
	private String deviceKeys;
    BluetoothCommmanager  BluetoothComm = null;
	private static final long WAIT_TIMEOUT = 15000; //超时时间 
    private static final int  nAmount = 001; //默认传入金额 1分==001
    private boolean isEntrack = true;
    private String keyMod = "011";
    private String dVersion = "BT_JHLA80";
    /**********设备功能实现区*****************/
	@Override
	public String getKeyMod(){
		return keyMod;
	}
	@Override
	public void setDeviceListener(DeviceListener listener) {
		this.deviceListener = listener;
	}

	@Override
	public void initDevice(final Context context, final String address,boolean isEntrack) {
		this.context = context;
		this.deviceAddress = address;
		this.isEntrack = isEntrack;
		BluetoothComm = BluetoothCommmanager.getInstance(this, context);
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					BluetoothComm.ConnectDevice(address);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void startSwipeCard(final String value) {
		final long ivalue = Long.parseLong(value);
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					deviceListener.showMessage("请刷卡或插入IC卡...");
					BluetoothComm.MagnCard(WAIT_TIMEOUT,ivalue,0x03);//刷卡+密码
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	//启动IC卡传输
	@Override
	public void startICCardTransfer(String value) {
		
	}

	//读设备信息
	@Override
	public void readDeviceInfo() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					BluetoothComm.GetSnVersion();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onDealing(int code, String msg) {
		
	}

	@Override
	public void onDealFinished(int code, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void installKeys(String keystr, int keytype) {
		if(keytype==0){
			this.deviceKeys = keystr;
			new Thread(new Runnable(){
				@Override
				public void run() {
					byte[] key = CodeUtil.hexStr2Bytes(deviceKeys.substring(40,72));
					System.out.println("mk=="+deviceKeys.substring(40,72));
					BluetoothComm.WriteMainKey(16,key);
				}
			}).start();
		}else if(keytype==1){
			new Thread(new Runnable(){
				@Override
				public void run() {
					byte[] key = CodeUtil.hexStr2Bytes(deviceKeys.substring(240));
					BluetoothComm.WriteWorkKey(key.length,key);
					System.out.println("wk=="+deviceKeys.substring(240));
				}
			}).start();
		}
	}
	
	@Override
	public void encryptPwd(EmvReadCardInfoVo cinfo,String pwd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genMac(String hexData) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void disConnected() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				BluetoothComm.DisConnectBlueDevice();
			}
		}).start();
	}
	
	/***********设备listener实现区************/
	@Override
	public void onDeviceFound(ArrayList<BluetoothIBridgeDevice> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDevicePlugged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceState(int nState) {
		try{
			if(nState == 1){
				BluetoothCommmanager.SetEncryMode(0,1);
				deviceListener.onConnected();return;
			}else{
				deviceListener.showMessage("设备连接失败...");return;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		deviceListener.showMessage("设备连接失败...");
	}

	@Override
	public void onDeviceUnplugged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int arg0, String arg1) {
		System.out.println("设备处理出错...");
	}

	@Override
	public void onProgress(byte[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReadCardData(Map<?, ?> hashcard) {
		EmvReadCardInfoVo respVo = new EmvReadCardInfoVo();
		try{
		 	Set<?> set = hashcard.entrySet();   
	        Iterator<?> iterator = set.iterator();   
	        while(iterator.hasNext()){
	        	@SuppressWarnings("unchecked")Map.Entry<String, String> entry = (Map.Entry<String, String>)iterator.next();
	        	Log.i(entry.getKey(), entry.getKey()+"==="+entry.getValue());
	        }
	        
	        respVo.setEntrack(isEntrack);
	   	 	respVo.setDeviceSn(hashcard.get("SnData").toString());
	   	 	respVo.setCardNo(hashcard.get("PAN").toString());
	   	 	
	   	 	respVo.setCardType(hashcard.get("CardType").toString());
	   	 	if(!isEntrack){
	   	   	   	 respVo.setTrack2(StringUtil.setIfNotNull(hashcard.get("Track2")));
	   	   	   	 respVo.setTrack3(StringUtil.setIfNotNull(hashcard.get("Track3")));
	   	 	}else{
	   	 		if(!StringUtil.isDbNull(hashcard.get("Encrytrack2"))){
	   	   	   	 	respVo.setTrack2(hashcard.get("Encrytrack2").toString());
	   	 		}
	   	 		if(!StringUtil.isDbNull(hashcard.get("Encrytrack3"))){
	   	   	   	 	respVo.setTrack3(hashcard.get("Encrytrack3").toString().substring(2)+"00");
	   	 		}
	   	 	}
	   	 	if(!StringUtil.isDbNull(hashcard.get("PanSeqNo"))){
	   	 		respVo.setCardSequence(hashcard.get("PanSeqNo").toString());
	   	 	}
	   	 	if(!StringUtil.isDbNull(hashcard.get("Track55").toString())){
	   	   	 	respVo.setTrack55(hashcard.get("Track55").toString());
	   	 	}
	   	 	respVo.setPinData(hashcard.get("Pinblock").toString());
		}catch(Exception e){
			e.printStackTrace();
		}
   	 	deviceListener.onFinishSwipeCard(respVo);
	}

	@Override
	public void onReceive(byte[] data) {
		try{
			System.out.println("receive:"+CodeUtil.byte2HexStr(data));
			switch(data[0]){
			case BluetoothCommmanager.GETSNVERSION:	
				if (data[1] == 0x00){		
					if (data.length <2) return;
					String strDeviceSn ="";
					for(int i=0;i<data[2];++i){
						 strDeviceSn = strDeviceSn + String.format("%02x", data[i+3]);
					}
					DeviceInfo dInfo = new DeviceInfo();
					dInfo.setDeviceSn(CodeUtil.hexStr2Str(strDeviceSn));
					deviceListener.onGetDeviceInfo(dInfo);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onResult(int ntype, int code) {
		try{
			switch(ntype){
			case BluetoothCommmanager.MAINKEY:
				if (code == 0x00){
					installKeys(null,1);
				}else{
					deviceListener.onKeyInstalled(11);
				}
				break;
			case BluetoothCommmanager.WORKEY:
				if (code == 0x00){
					deviceListener.onKeyInstalled(1);
				}else{
					deviceListener.onKeyInstalled(12);
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onSendOK(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public DeviceInterface tryDevice(boolean isEntrack) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getDeviceVersion() {
		// TODO Auto-generated method stub
		return this.dVersion;
	}
}
