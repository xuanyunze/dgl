package com.rxoa.zlpay.device;

import java.util.ArrayList;
import java.util.List;

import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.MainApplication;
import com.rxoa.zlpay.acty.MainHomeActy;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.device.DeviceInterface;
import com.rxoa.zlpay.base.device.DeviceListener;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.fragment.AppCenterFmt;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.nfc.Tag;
import android.util.Log;

import javax.security.auth.login.LoginException;

public class DeviceManager {
	MainHomeActy mActivity;
	private String deviceEntity;
	private String userDeviceSn;
	public static final String TAG = DeviceManager.class.getName();
	public enum DeviceType{AudioDevice,BluetoothDevice}
	private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private List<BlDeviceInfo> blDevices = new ArrayList<BlDeviceInfo>();
	private DeviceManagerListener devMgrlistener;
	private Context context;
	private DeviceInterface device;
	private boolean isEntrack = true;
	private boolean isRunning = true;
	public void setListener(DeviceManagerListener listener){
		this.devMgrlistener = listener;
	}
	public static DeviceManager getInstance(Context context){
		DeviceManager mgr = new DeviceManager();
		mgr.context = context;

		return mgr;
	}
	public DeviceManager initManager(){
		IntentFilter audio_filter = new IntentFilter();
		audio_filter.addAction("android.intent.action.HEADSET_PLUG");
		context.registerReceiver(audioReceiver,audio_filter);
		IntentFilter bluetooth_filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(discoveryReciever, bluetooth_filter);
		return this;
	}
	
	public void findDevice(){
		try{
			AudioManager localAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);  
			boolean isAudioDevice = localAudioManager.isWiredHeadsetOn();
			if(isAudioDevice){
				devMgrlistener.showMessage("正在检测音频读卡设备");
				findRadioDevice();
			}else{
				scanBluetooth();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public DeviceInterface findRadioDevice(){
		try{
			//DEVCICR20Impl dev_ac20 = new DEVCICR20Impl();
			//dev_ac20.setDeviceListener(devMgrlistener.onSelDevice(dev_ac20));
			//dev_ac20.initDevice(context, null, false);
			DEVCMFA60Impl dev_mfa60 = new DEVCMFA60Impl();
			dev_mfa60.setDeviceListener(devMgrlistener.onSelDevice(dev_mfa60));
			dev_mfa60.initDevice(context, null, true);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public void scanBluetooth(){
		if (bluetoothAdapter.isEnabled()) {
			if (blDevices != null) {
				blDevices.clear();
			}
			devMgrlistener.showMessage("正在搜索蓝牙设备...");
			bluetoothAdapter.startDiscovery();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
					} finally {
						//listener.showMessage("结束搜索蓝牙设备！");
						bluetoothAdapter.cancelDiscovery();
					}

					showBlDevice();
				}
			}).start();
		} else {
			devMgrlistener.showMessage("手机蓝牙功能未开启！");
		}
	}
	public void cancelDeviceSearch(){
		this.isRunning = false;
		bluetoothAdapter.cancelDiscovery();
	}
	private void showBlDevice() {
		if(!isRunning) return;
		int i = 0;
		final String[] bluetoothName = new String[blDevices.size()];
		for (BlDeviceInfo deviceInfo : blDevices) {
			bluetoothName[i++] = deviceInfo.name;
			Log.e(TAG, "deviceinfo:" + deviceInfo.name);
			Log.e(TAG, "deviceEntity:" + deviceEntity);
			if (deviceInfo.name.equals(deviceEntity)&&bluetoothName!=null) {
				devMgrlistener.showMessage("正在连接设备...");
				device.setDeviceListener(devMgrlistener.onSelDevice(device));
				device.initDevice(context, blDevices.get(i).address,isEntrack);
			}
			break;
		}
		//System.out.println("showdevice called..");
		//if(blDevices.size()==0){
		//	scanBluetooth();return ;
		//}
		final Builder builder = new Builder(context);
		builder.setCancelable(false);
		builder.setTitle("请选择读卡设备");
		if(blDevices.size()==0){
			builder.setMessage("请插入或打开蓝牙设备");
		}else{
			builder.setSingleChoiceItems(bluetoothName, 0, new AlertDialog.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					String selDeviceName = bluetoothName[which];
					dialog.dismiss();

					if(selDeviceName.substring(0,3).equals("JHL")){
						device = new DEVCJHLM60Impl();					
					}else if(selDeviceName.substring(0,2).equals("MF")){
						device = new DEVCMF60Impl();
					}else if(selDeviceName.substring(0,2).equals("MP")){
						device = new DEVCMFKM60Impl();
					}else if(selDeviceName.substring(0,2).equals("AC")){
						device = new DEVCICI21BImpl();
					}
					if(device!=null){
						devMgrlistener.showMessage("正在连接设备...");
						device.setDeviceListener(devMgrlistener.onSelDevice(device));
						device.initDevice(context, blDevices.get(which).address,isEntrack);
					}else{
						devMgrlistener.showMessage("设备型号未支持...");
					}
				}
			});
		}
		builder.setPositiveButton("继续搜索", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				findDevice();
			}
		});
		builder.setNegativeButton("放弃搜索", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				devMgrlistener.cancelDeviceSearch();
			}
		});
		devMgrlistener.showBlDevices(builder);
	}
	private final BroadcastReceiver discoveryReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
//			userDeviceSn = intent.getStringExtra("devicesn");
			deviceEntity = MainApplication.getInstance().getDeviceEntity();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {

				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (ifAddressExist(device.getAddress())) {
					return;
				}

				BlDeviceInfo tempDevice = new BlDeviceInfo(device.getName() == null ? device.getAddress() : device.getName(), device.getAddress());
				if(isInFilter(tempDevice.name)){
					blDevices.add(tempDevice);
				}
			}

		}
	};
	private boolean ifAddressExist(String addr) {
		for (BlDeviceInfo devcie : blDevices) {
			if (addr.equals(devcie.address))
				return true;
		}
		return false;
	}
	private final BroadcastReceiver audioReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
				int headsetState = intent.getExtras().getInt("state");
				int microphoneState = intent.getExtras().getInt("microphone");

				if(headsetState == 0) {
					
				}else if(headsetState == 1){
					if(microphoneState == 1) {
						
					}else{
						
					}
				}
			}
		}
	};
	private class BlDeviceInfo {
		public String name = "";
		public String address = "";

		BlDeviceInfo(String name, String address) {
			this.name = name;
			this.address = address;
		}
	}
	public void unRegisterReceiver(){
		try{
			context.unregisterReceiver(discoveryReciever);
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			context.unregisterReceiver(audioReceiver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public interface DeviceManagerListener{
		public String showMessage(String msg);
		public DeviceListener onSelDevice(DeviceInterface device);
		public void showBlDevices(Builder builder);
		public void onDeviceInited();
		public void cancelDeviceSearch();
	}
	public boolean isInFilter(String bluetoothName){
		try{
			if(!StringUtil.isDbNull(bluetoothName)){
				if(bluetoothName.substring(0,2).equals("MF")
						||bluetoothName.substring(0,2).equals("MP")
						||bluetoothName.substring(0,3).equals("JHL")
						||bluetoothName.substring(0,2).equals("AC")){
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}



}

