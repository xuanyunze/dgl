package com.rxoa.zlpay.device;

import com.itron.android.ftf.Util;
import com.itron.cswiper4.CSwiper;
import com.itron.cswiper4.CSwiperStateChangedListener;
import com.itron.cswiper4.DecodeResult;
import com.itron.protol.android.TransactionDateTime;
import com.itron.protol.android.TransactionInfo;
import com.itron.protol.android.TransationCurrencyCode;
import com.itron.protol.android.TransationNum;
import com.itron.protol.android.TransationTime;
import com.itron.protol.android.TransationType;
import com.rxoa.zlpay.base.device.DeviceInfo;
import com.rxoa.zlpay.base.device.DeviceInterface;
import com.rxoa.zlpay.base.device.DeviceListener;
import com.rxoa.zlpay.base.device.EmvReadCardInfoVo;
import com.rxoa.zlpay.base.util.StringUtil;

import android.content.Context;

public class DEVCICR20Impl implements DeviceInterface{
	private Context context;
	private DeviceListener deviceListener;
	private boolean isEntrack = false;
	public CSwiper cSwiperController;
	private CSwiperListener cSwiperListener; 
	public int deStat = 0;
	private String dVersion = "AU_ACI21B";
	
	@Override
	public void setDeviceListener(DeviceListener listener) {
		this.deviceListener = listener;
	}

	@Override
	public void disConnected() {
		try{
			cSwiperController.stopCSwiper();
			cSwiperController.deleteCSwiper();
			cSwiperController.unregisterServiceReceiver();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cSwiperListener = null;
			cSwiperController = null;
		}
	}

	@Override
	public void installKeys(String keystr, int keytype) {
		try{
			deviceListener.onKeyInstalled(1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void readDeviceInfo() {System.out.println("读取设备信息。。。");
		try{
			new Thread(new Runnable(){
				@Override
				public void run() {
					try{
						DeviceInfo deviceInfo = new DeviceInfo();
						String ksn = cSwiperController.getKSN();
						System.out.println("ksn=="+ksn);
						deviceInfo.setDeviceSn(ksn);
						deviceListener.onGetDeviceInfo(deviceInfo);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void initDevice(Context context, String address, boolean isEntrack) {
		this.context = context;
		try{
			System.out.println("开始初始化了～");
			cSwiperListener = new CSwiperListener(this);
			cSwiperController = CSwiper.GetInstance(context, cSwiperListener);
			System.out.println("初始化完成了～");
			new Thread(new Runnable(){
				@Override
				public void run() {
					try{
						Thread.sleep(1000);
					}catch(Exception e){
						e.printStackTrace();
					}
					findDevice();
				}
			}).start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void findDevice(){
		try{
			deviceListener.onFindRadioDevice(this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void encryptPwd(EmvReadCardInfoVo cinfo, String pwd) {
		try{
			cinfo.setPinData(pwd);
			deviceListener.onFinishComputepwd(cinfo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void genMac(String hexData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startSwipeCard(final String value) {
		System.out.println("开始刷卡喽～");
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					deviceListener.showMessage("请刷卡或插入IC卡~");
					TransactionInfo info = new TransactionInfo();
					// 设置交易日期 格式: YYMMDD
					TransactionDateTime dateTime = new TransactionDateTime();
					dateTime.setDateTime("140930");
					// 设置交易时间   hhssmm
					TransationTime time = new TransationTime();
					time.setTime("105130");
					// 设置货币代码
					TransationCurrencyCode currencyCode = new TransationCurrencyCode();
					
					currencyCode.setCode("0156");
					// 设置交易流水号
					TransationNum num = new TransationNum();
					num.setNum("00000001");
					// 设置交易类型  00 消费 
					TransationType type = new TransationType();
					type.setType("00");
					
					info.setDateTime(dateTime);
					info.setCurrencyCode(currencyCode);
					info.setNum(num);
					info.setTime(time);
					info.setType(type);
//					字节1:
//					bit0＝0/1 表示随机有爱刷产生/由双方产生。
//		            由双方产生时，爱刷只产生4字节。如果手机侧不足4字节，则爱刷会前补FF到4字节
//		          Bit1＝0/1 表示终端需要不上送/上送mac。
//		          Bit2＝0/1 表示终端上送的卡号不屏蔽/屏蔽
//			   		Bit3＝0/1  不上送/上送各磁道的原始长度
//			   		Bit4=0/1磁道数据加密/不加密
//			   		Bit5=0/1上送/不上送PAN码
//			   		Bit6=0/1 55域不加密/加密
//		    		Bit7 =0/1 不上送/上送各磁道密文的长度
//					字节2：
//					Bit0 = 0/1 表示这个数据域参与mac计算/只有数据域内的磁道数据和随机数参与
//		   		Bit1 = 0/1 使用全部ic卡/银联标准55域 
//		   		Bit2-bit7  保留
//					字节3：
//		    		bit0 = 0/1 刷卡的卡号不加密/加密（当字节1的bit2=0时，该标志有用）
//		   		 bit1 = 0/1卡号加密时不压缩/压缩（当字节3的bit0=1且字节1的bit2=0时，该标志有用）
//		    		bit2-bit7  保留
//		    		bit7----bit0

//					String byte0 = "11000111";//"10001110";
					String byte0 = "10010011";
			        String byte1 = "00111011";
			        String byte2 = "00000010";
					int flag0 = Util.binaryStr2Byte(byte0);
					int flag1 = Util.binaryStr2Byte(byte1);
					int flag2 = Util.binaryStr2Byte(byte2);

					cSwiperController.statEmvSwiper((byte)0,
							new byte[]{(byte)flag0,(byte) flag1,(byte) flag2,0x00}, 
							"123".getBytes(), value,
							null, 50, info);
					System.out.println("刷卡命令发送完成～");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	@Override
	public DeviceInterface tryDevice(boolean isEntrack) {
		return this;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getKeyMod() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//===========设备监听=========
	class CSwiperListener implements CSwiperStateChangedListener {
		private DeviceInterface device = null;
		public CSwiperListener(DeviceInterface de){
			this.device = de;
		}
		@Override
		public void EmvOperationWaitiing() {
			try{
				deviceListener.showMessage("正在读卡，请勿拔卡~");
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onCardSwipeDetected() {
			try{
				deviceListener.showMessage("正在读卡，请勿拔卡~");
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onDecodeCompleted(String formatID, String ksn,
				String encTracks, int track1Length, int track2Length,
				int track3Length, String randomNumber, String maskedPAN,
				String pan,
				String expiryDate, String cardHolderName,String mac
				,int cardType,byte[] cardSeriNo,byte[] ic55Data) {
			try{
				System.out.println("读到信息喽～");
				String cardSeriNoStr = "";
				String ic55DataStr = "";
				if(cardSeriNo != null){
					cardSeriNoStr = Util.BinToHex(cardSeriNo, 0, cardSeriNo.length);
				}
				
				if(ic55Data != null){
					ic55DataStr = Util.BinToHex(ic55Data, 0, ic55Data.length);
				}
				StringBuffer strb = new StringBuffer();
				strb.append("formatID:" + formatID + "\n");
				strb.append("ksn:" + ksn + "\n");
				strb.append("track1Length :" + track1Length + "\n");
				strb.append("track2Length:" + track2Length + "\n");
				strb.append("track3Length:" + track3Length + "\n");
				strb.append("encTracks:" + encTracks + "\n");
				strb.append("randomNumber: " + randomNumber + "\n");
				strb.append("maskedPAN :" + maskedPAN + "\n");
				strb.append("pan :" + pan + "\n");
				strb.append("expiryDate:" + expiryDate + "\n");
				strb.append("cardHolderName : " + cardHolderName + "\n");
				strb.append("mac: "+ mac+ "\n");
				strb.append("cardType: "+ cardType+ "\n");
				strb.append("cardSeriNo: "+ cardSeriNoStr+ "\n");
				strb.append("ic55Data: "+ ic55DataStr);	
				System.out.println(strb.toString());
				
				EmvReadCardInfoVo cinfo = new EmvReadCardInfoVo();
				cinfo.setCardNo(maskedPAN);
				cinfo.setDeviceSn(ksn);
				cinfo.setEntrack(isEntrack);
				String track2;String track3;
				track2 = encTracks.substring(0,encTracks.indexOf("FFF"));
				track3 = encTracks.substring(encTracks.indexOf("FFF")).replace("F", "");
				if(!StringUtil.isDbNull(track2)) cinfo.setTrack2(track2);
				if(!StringUtil.isDbNull(track3)) cinfo.setTrack3(track3);
				if(!StringUtil.isDbNull(ic55Data)) cinfo.setTrack55(Util.BinToHex(ic55Data, 0, ic55Data.length));
				if(!StringUtil.isDbNull(cardType)) cinfo.setCardType(cardType+"");
				if(!StringUtil.isDbNull(cardSeriNoStr)) cinfo.setCardSequence(cardSeriNoStr);
				
				deviceListener.onFinishSwipeCard(cinfo);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onDecodeError(DecodeResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDecodingStart() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDevicePlugged() {
			System.out.println("检测到插入啦哈哈");
		}

		@Override
		public void onDeviceUnplugged() {
			try{
				System.out.println("检测到拔出啦哈哈");
				deviceListener.onFindRadioDeviceTimeout();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onError(int arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onICResponse(int arg0, byte[] arg1, byte[] arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onInterrupted() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNoDeviceDetected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTimeout() {
			try{
				deviceListener.showMessage("用户操作超时~");
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onWaitingForCardSwipe() {
			try{
				deviceListener.showMessage("请刷卡或插入IC卡~");
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onWaitingForDevice() {
			// TODO Auto-generated method stub
			
		}
	}
	@Override
	public String getDeviceVersion() {
		// TODO Auto-generated method stub
		return this.dVersion;
	}
}
