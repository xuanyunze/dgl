package com.rxoa.zlpay.device;

import com.mf.mpos.pub.CommEnum;
import com.mf.mpos.pub.Controler;
import com.mf.mpos.pub.CommEnum.CONNECTMODE;
import com.mf.mpos.pub.result.CalPinResult;
import com.mf.mpos.pub.result.ConnectPosResult;
import com.mf.mpos.pub.result.InputPinResult;
import com.mf.mpos.pub.result.LoadKekResult;
import com.mf.mpos.pub.result.LoadMainKeyResult;
import com.mf.mpos.pub.result.LoadWorkKeyResult;
import com.mf.mpos.pub.result.OpenCardReaderResult;
import com.mf.mpos.pub.result.ReadPosInfoResult;
import com.mf.mpos.pub.swiper.ISwiper;
import com.mf.mpos.pub.swiper.SwiperInfo;
import com.mf.mpos.util.Misc;
import com.rxoa.zlpay.base.device.DeviceInfo;
import com.rxoa.zlpay.base.device.DeviceInterface;
import com.rxoa.zlpay.base.device.DeviceListener;
import com.rxoa.zlpay.base.device.EmvReadCardInfoVo;
import com.rxoa.zlpay.base.util.CodeUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DEVCMFKM60Impl implements DeviceInterface{
	private Context context;
	private DeviceListener deviceListener;
	private String deviceAddress;
	private String deviceKeys;
	private boolean isEntrack = false;
	private String keyMod = "111";
	private String dVersion = "BT_MF60";
	
	/***********设备功能实现区**********/
	@Override
	public String getKeyMod(){
		return keyMod;
	}
	
	@Override
	public void setDeviceListener(DeviceListener listener) {
		this.deviceListener = listener;
	}

	@Override
	public void readDeviceInfo() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				ReadPosInfoResult result = Controler.ReadPosInfo();
				if(result.commResult.equals(CommEnum.COMMRET.NOERROR)){
					DeviceInfo dInfo = new DeviceInfo();
					dInfo.setDeviceSn(result.sn);
					deviceListener.onGetDeviceInfo(dInfo);
				}
			}
		}).start();
	}

	@Override
	public void installKeys(String keystr, int keytype) {		
		if(keytype==0){
			this.deviceKeys = keystr;
			final byte[] kekD1 = CodeUtil.hexStr2Bytes(this.deviceKeys.substring(0,16));
			final byte[] kekD2 = CodeUtil.hexStr2Bytes(this.deviceKeys.substring(16,32));
			final byte[] kvc   = CodeUtil.hexStr2Bytes(this.deviceKeys.substring(32,40));
			new Thread(new Runnable(){
				@Override
				public void run() {
					LoadKekResult result = Controler.LoadKek( CommEnum.KEKTYPE.DOUBLE, kekD1,kekD2,kvc);
					if(result.commResult.equals(CommEnum.COMMRET.NOERROR)){
						if(result.loadResult){
							installKeys(null,1);
						}else{
							deviceListener.onKeyInstalled(10);
						}
					}else{
						deviceListener.onKeyInstalled(10);
					}
				}
			}).start();
		}else if(keytype==1){
			final byte[] xkekD1 = CodeUtil.hexStr2Bytes(this.deviceKeys.substring(200,216));
			final byte[] xkekD2 = CodeUtil.hexStr2Bytes(this.deviceKeys.substring(216,232));
			final byte[] xkvc   = CodeUtil.hexStr2Bytes(this.deviceKeys.substring(232,240));
			new Thread(new Runnable(){
				@Override
				public void run() {
					LoadMainKeyResult result = Controler.LoadMainKey(CommEnum.MAINKEYENCRYPT.KEK,
							CommEnum.KEYINDEX.INDEX0,
							CommEnum.MAINKEYTYPE.DOUBLE,
							xkekD1,xkekD2,xkvc);
					if(result.commResult.equals(CommEnum.COMMRET.NOERROR)){
						if(result.loadResult){
							installKeys(null,2);
						}else{
							deviceListener.onKeyInstalled(11);
						}
					}else{
						deviceListener.onKeyInstalled(11);
					}
				}
			}).start();
		}else if(keytype==2){
			final byte[] workKey = CodeUtil.hexStr2Bytes(this.deviceKeys.substring(240));
			new Thread(new Runnable(){
				@Override
				public void run() {
					LoadWorkKeyResult result = Controler.LoadWorkKey(
							CommEnum.KEYINDEX.INDEX0,
							CommEnum.WORKKEYTYPE.DOUBLEMAG,
							workKey,
							workKey.length);
					if(result.commResult.equals(CommEnum.COMMRET.NOERROR) ){
						if(result.loadResult){
							deviceListener.onKeyInstalled(1);
						}else{
							deviceListener.onKeyInstalled(12);
						}
					}else{
						deviceListener.onKeyInstalled(12);
					}
				}
			}).start();
		}		
	}

	@Override
	public void initDevice(final Context context, final String address,boolean isEntrack) {
		this.context = context;
		this.deviceAddress = address;
		this.isEntrack = isEntrack;
		new Thread(new Runnable(){
			@Override
			public void run() {
				Controler.Init(context, CONNECTMODE.BLUETOOTH, 0);
				ConnectPosResult info = Controler.connectPos(address);
				if(info.bConnected){
					deviceListener.onConnected();
				}else{
					deviceListener.showMessage("设备连接失败...");
				}
			}
		}).start();
	}

	@Override
	public void startSwipeCard(final String value) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					if(!Controler.posConnected()){
						deviceListener.showMessage("终端未连接...");
					}
					
					EmvReadCardInfoVo cinfo = new EmvReadCardInfoVo();
					ReadPosInfoResult info = Controler.ReadPosInfo();
					if(info.commResult.equals(CommEnum.COMMRET.NOERROR)){
						Log.i("1",info.sn.toString());
						cinfo.setDeviceSn(info.sn);
					}
					
					deviceListener.showMessage("请刷卡或插入IC卡...");
					ISwiper isw = Controler.getISwiper();
					SwiperInfo sinfo = null;
					if(Long.parseLong(value)==0L){
						sinfo = isw.StartCSwiper(Long.parseLong(value), CommEnum.TRANSTYPE.FUNC_BALANCE,60, isEntrack);
					}else{
						sinfo = isw.StartCSwiper(Long.parseLong(value), CommEnum.TRANSTYPE.FUNC_SALE,60, isEntrack);
					}
					StringBuilder sb = new StringBuilder();
					if(sinfo.result.toDisplayName().equals("设备未连接")||sinfo.result.toDisplayName().equals("用户取消")){
						Controler.CancelComm();
						Controler.ResetPos();
						return;
					}
					sb.append( "结果:" + sinfo.result.toDisplayName() + "\n" );
					sb.append( "是否IC卡:" + (sinfo.isIcCard ? "是":"否") + "\n" );
					sb.append( "是否FallBack:" + (sinfo.isFallBack ? "是":"否") + "\n" );
					sb.append( "主账号:" + sinfo.sPan + "\n"  );
					sb.append( "卡有效期:" + sinfo.sExpData  + "\n" );
					sb.append( "服务代码:" + sinfo.serviceCode + "\n"   );
					sb.append( "二磁道长度:" + sinfo.track2Len + "\n"  );
					sb.append( "磁道二信息:" + sinfo.sTrack2  + "\n" );
					sb.append( "三磁道长度:" + sinfo.track3Len + "\n"  );
					sb.append( "磁道三信息:" + sinfo.sTrack3 + "\n"  );
					sb.append( "数据随机数:" + Misc.hex2asc(sinfo.randomdata)  + "\n" );
					sb.append( "卡片序列号:" + sinfo.pansn + "\n"  );
					sb.append( "IC卡数据:" + Misc.hex2asc(sinfo.tlvData )  + "\n"   );
					Log.i("tag", sb.toString());
					
					cinfo.setEntrack(isEntrack);
					cinfo.setCardNo(sinfo.sPan);
					cinfo.setCardType(sinfo.isIcCard?"1":"0");
					cinfo.setTrack2(sinfo.sTrack2);
					cinfo.setTrack3(sinfo.sTrack3);
					cinfo.setCardPan(sinfo.sPan);
					if(sinfo.pansn!=null){
						cinfo.setCardSequence(sinfo.pansn);
					}
					if(sinfo.tlvData!=null){
						cinfo.setTrack55(CodeUtil.byte2HexStr(sinfo.tlvData));
					}
					//deviceListener.onFinishSwipeCard(cinfo);
					encryptPwd(cinfo,null);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}).start();
	}

	@Override
	public void startICCardTransfer(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDealing(int code, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDealFinished(int code, String msg) {
		try{
			Controler.ResetPos();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void encryptPwd(final EmvReadCardInfoVo cinfo,final String pwd) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				InputPinResult result = Controler.InputPin((byte)8, (byte)60, cinfo.getCardPan());
				if(result.commResult == (CommEnum.COMMRET.NOERROR)){
					if(result.keyType.equals( CommEnum.POSKEYTYPE.OK ) ){
						cinfo.setPinData(CodeUtil.byte2HexStr(result.pinBlock));
						new Thread(new Runnable(){
							@Override
							public void run() {
								Controler.EndEmv();
							}}).start();
						deviceListener.onFinishComputepwd(cinfo);return;
					}
				}	
				deviceListener.showMessage("密码输入操作有误...");
			}
		}).start();
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
				Controler.CancelComm();
				Controler.ResetPos();
				Controler.disconnectPos();
			}	
		}).start();
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
