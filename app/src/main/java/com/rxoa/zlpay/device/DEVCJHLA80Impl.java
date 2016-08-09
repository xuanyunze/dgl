package com.rxoa.zlpay.device;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.ivt.bluetooth.ibridge.BluetoothIBridgeDevice;
import com.jhl.jhlblueconn.BlueCommangerCallback;
import com.mf.mpos.pub.CommEnum;
import com.mf.mpos.pub.Controler;
import com.mf.mpos.pub.CommEnum.CONNECTMODE;
import com.mf.mpos.pub.result.CalPinResult;
import com.mf.mpos.pub.result.ConnectPosResult;
import com.mf.mpos.pub.result.LoadKekResult;
import com.mf.mpos.pub.result.LoadMainKeyResult;
import com.mf.mpos.pub.result.LoadWorkKeyResult;
import com.mf.mpos.pub.result.ReadPosInfoResult;
import com.mf.mpos.pub.swiper.ISwiper;
import com.mf.mpos.pub.swiper.SwiperInfo;
import com.mf.mpos.util.Misc;
import com.rxoa.zlpay.base.device.DeviceInfo;
import com.rxoa.zlpay.base.device.DeviceInterface;
import com.rxoa.zlpay.base.device.DeviceListener;
import com.rxoa.zlpay.base.device.EmvReadCardInfoVo;
import com.rxoa.zlpay.base.util.CodeUtil;

public class DEVCJHLA80Impl implements DeviceInterface,BlueCommangerCallback{

	@Override
	public void onDeviceFound(ArrayList<BluetoothIBridgeDevice> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDevicePlugged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceState(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeviceUnplugged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProgress(byte[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReadCardData(Map<?, ?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceive(byte[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResult(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
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
	public void disConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encryptPwd(EmvReadCardInfoVo arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genMac(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDeviceVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKeyMod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initDevice(Context arg0, String arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void installKeys(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDealFinished(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDealing(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readDeviceInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDeviceListener(DeviceListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startICCardTransfer(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startSwipeCard(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DeviceInterface tryDevice(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
