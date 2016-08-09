package com.rxoa.zlpay.device;

import android.content.Context;

import com.itron.android.ftf.Util;
import com.itron.android.lib.SecurityUtils;
import com.itron.protol.android.BLECommandController;
import com.itron.protol.android.CommandReturn;
import com.itron.protol.android.CommunicationListener;
import com.itron.protol.android.ProtocolType;
import com.itron.protol.android.TransactionDateTime;
import com.itron.protol.android.TransactionInfo;
import com.itron.protol.android.TransationCurrencyCode;
import com.itron.protol.android.TransationTime;
import com.itron.protol.android.TransationType;
import com.rxoa.zlpay.base.device.DeviceInfo;
import com.rxoa.zlpay.base.device.DeviceInterface;
import com.rxoa.zlpay.base.device.DeviceListener;
import com.rxoa.zlpay.base.device.EmvReadCardInfoVo;
import com.rxoa.zlpay.base.util.CodeUtil;
import com.rxoa.zlpay.base.util.StringUtil;

public class DEVCICI21BImpl implements DeviceInterface,CommunicationListener{
	private BLECommandController itcommm;
	private Context context;
	private DeviceListener deviceListener;
	private String deviceAddress;
	private String deviceKeys;
	private boolean isEntrack = true;
	private static final long WAIT_TIMEOUT = 15000; //超时时间 
	private String keyMod = "001";
	private String dVersion = "BT_ACI21B_NEW";
	
	/*******设备接口操作信息***********/
	@Override
	public String getKeyMod(){
		return keyMod;
	}
	@Override
	public void setDeviceListener(DeviceListener listener) {
		this.deviceListener = listener;
	}

	@Override
	public void disConnected() {
		itcommm.closeDevice();
	}

	@Override
	public void installKeys(String keystr, int keytype) {
		if(keytype==10){
			deviceListener.showMessage("正在初始化本地设备[TMK]...");
			if(StringUtil.isDbNull(this.deviceKeys)){this.deviceKeys = keystr;}
			new Thread(new Runnable(){
				@Override
				public void run() {
					String tmkstr = deviceKeys.substring(200,232);
					//tmkstr = "4C5FD19CD4CE8E81A360CCC2530290AF";
					CommandReturn creturn = itcommm.Get_TMK(CodeUtil.hexStr2Bytes(tmkstr));
					if((creturn!=null)&&((creturn.Return_Result==0X00))){
						deviceListener.showMessage("主密钥匙装载成功～");
						installKeys(deviceKeys,0);
					}
				}
			}).start();
		}else if(keytype==0){
			deviceListener.showMessage("正在初始化本地设备...");
			if(StringUtil.isDbNull(this.deviceKeys)){this.deviceKeys = keystr;}
			new Thread(new Runnable(){
				@Override
				public void run() {
					CommandReturn cmdret = new CommandReturn();
					String pinstr = deviceKeys.substring(240,280);
					String macstr = deviceKeys.substring(280,296)+deviceKeys.substring(312,320);
					String desstr = deviceKeys.substring(320,360);
					//pinstr = "25DDDD3D6C60634FBF60A0DEC5AD31CE1E8AC52C";
					//macstr = "25DDDD3D6C60634FBF60A0DEC5AD31CE1E8AC52C";
					//desstr = "25DDDD3D6C60634FBF60A0DEC5AD31CE1E8AC52C";
					cmdret = itcommm.Get_RenewKey(Util.HexToBin(pinstr), Util.HexToBin(macstr), Util.HexToBin(desstr));
					if((cmdret!=null)&&(cmdret.Return_Result==0X00)){
						deviceListener.showMessage("工作钥匙装载成功～");
						deviceListener.onKeyInstalled(1);
					}else{
						deviceListener.onKeyInstalled(10);
					}
				}
			}).start();
		}
	}
	@Override
	public void readDeviceInfo() {
		deviceListener.showMessage("正在读取设备信息...");
		new Thread(new Runnable(){
			@Override
			public void run() {
				CommandReturn cmd = itcommm.getTerminalTypeReI21();
				StringBuilder sb = new StringBuilder();
				if(cmd!=null){
		            sb.append("设备类型: "+cmd.deviceType+"\n");
		            if(cmd.ksn != null){
		                sb.append("ksn: "+ Util.BinToHex(cmd.ksn,0,cmd.ksn.length)+"\n");
		            }
		            if(cmd.Return_TerSerialNo != null){
		                sb.append("终端版本号: "+ new String(cmd.Return_TerSerialNo)+"\n");
		            }
		            if(cmd.btName != null){
		                sb.append("蓝牙名称: "+ new String(cmd.btName)+"\n");
		            }
		            if(cmd.btVersion != null){
		                sb.append("蓝牙版本号: "+ new String(cmd.btVersion)+"\n");
		            }
		            if(cmd.btMac != null){
		                sb.append("蓝牙MAC地址: "+ new String(cmd.btMac)+"\n");
		            }
		            System.out.println(sb.toString());
					DeviceInfo deviceInfo = new DeviceInfo();
					if(cmd.ksn != null){
						deviceInfo.setDeviceSn(Util.BinToHex(cmd.ksn,0,cmd.ksn.length));
						deviceListener.onGetDeviceInfo(deviceInfo);
					}else{
						deviceListener.showMessage("读取设备SN信息失败...");
					}
				}else{
					deviceListener.showMessage("读取设备信息失败...");
				}
			}
		}).start();
	}

	@Override
	public void initDevice(Context context, final String address, boolean isEntrack) {
		this.context = context;
		this.deviceAddress = address;
		this.isEntrack = isEntrack;
		itcommm = BLECommandController.GetInstance(context,this);
		new Thread(new Runnable(){
			@Override
			public void run() {
				int i = itcommm.openDevice(address);
				if(i==0){
					try{
						Thread.sleep(500);
					}catch(Exception e){
						e.printStackTrace();
					}
					deviceListener.onConnected();
				}else{
					deviceListener.showMessage("设备连接失败...");
				}
			}
		}).start();
	}

	@Override
	public void encryptPwd(final EmvReadCardInfoVo cinfo, String pwd) {
		String userpin = pwd;
		System.out.println("pwd=="+pwd);
		userpin = "0"+ userpin.length() +userpin;// 前补加上长度
		if(userpin.length() < 16){//补齐八的倍数 后补F
			int bw = 16 - userpin.length();
			for (int i = 0; i < bw; i++) {
				userpin = userpin + "F";
			}
		}
		byte[] pin = Util.HexToBin(userpin);
		System.out.println("cardpan=="+cinfo.getCardPan());
		byte[] pan = Util.HexToBin(cinfo.getCardPan());
		//pin与PAN异或
		for (int i = 0; i < pan.length; i++) {
			pin[i] = (byte) (pin[i] ^ pan[i]);
		}
		String ksndes = (cinfo.getDeviceSn()+cinfo.getDeviceSn()).substring(0, 16);
		// 客户端生成随机数 这里先固定
		byte[] random= "12345678".getBytes();
		SecurityUtils securityUtils = new SecurityUtils();
		byte[] wk = securityUtils.subKey(Util.HexToBin(ksndes+ksndes), random);
		byte[] pinenc = securityUtils.encryptoCBCKey2(pin, wk);
		final byte[] ran = random;
		final byte[] pinx = pinenc;
		new Thread(new Runnable(){
			@Override
			public void run() {
				CommandReturn cmdret = new CommandReturn();
				cmdret = itcommm.transPin(ran, pinx);
				if((cmdret!=null)&&((cmdret.Return_Result==0X00))){
					cinfo.setPinData(Util.BinToHex(cmdret.transPin, 0, cmdret.transPin.length));
					System.out.println("KEY_PINDATA:"+cinfo.getPinData());
					deviceListener.onFinishComputepwd(cinfo);return;
				}else{
					deviceListener.showMessage(Util.toHex(cmdret.Return_Result)+"密码计算失败");
				}
			}
		}).start();
		
	}

	@Override
	public void genMac(String hexData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startSwipeCard(final String value) {
		deviceListener.showMessage("请刷卡或插入IC卡~");
		new Thread(new Runnable(){
			@Override
			public void run() {
				CommandReturn cmdret = new CommandReturn();

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
//					TransationNum num = new TransationNum();
//					num.setNum("00000001");
		        // 设置交易类型  00 消费
		        TransationType type = new TransationType();
		        type.setType("00");

		        info.setDateTime(dateTime);
		        info.setCurrencyCode(currencyCode);
//					info.setNum(num);
		        info.setTime(time);
		        info.setType(type);
//					bit0＝0/1 表示随机有爱刷产生/由双方产生。
//				    Bit1＝0/1 表示终端需要不上送/上送mac。
//			    	Bit2＝0/1 表示终端上送的卡号不屏蔽/屏蔽
//				    Bit3＝0/1  不上送/上送各磁道的原始长度

//				    Bit4=0/1磁道数据加密/不加密
//				    Bit5=0/1上送/不上送PAN码
//				    Bit6=0/1 55域不加密/加密
//			    	Bit7 =0/1 不上送/上送各磁道密文的长度

		        //bit7----bit0
		        String byte0 = "10001011";//"10000011";//钱拓 83010000 ;//"10101111";//"10100000";//"10100010";//"10001110"; 1

		        int flag0 = Util.binaryStr2Byte(byte0);
		        //itcommm.huifu = true;
		        
		        cmdret = itcommm.statEmvSwiperI21((byte)0,
		                new byte[]{(byte)flag0,0x03,0x00,0x00},
		                "123".getBytes(), value,
		                null, 30, info,ProtocolType.PROTOCOL_36);
		        if((cmdret!=null)&&((cmdret.Return_Result==0X00))){
					StringBuffer buf = new StringBuffer();
					if(cmdret.Return_CardNo!=null)
						buf.append("Return_CardNo:"+Util.BinToHex(cmdret.Return_CardNo,0,cmdret.Return_CardNo.length)+"\n");
					
					if(cmdret.Return_PSAMMAC!=null)
						buf.append("PSMMAC:"+Util.BinToHex(cmdret.Return_PSAMMAC,0,cmdret.Return_PSAMMAC.length)+"\n");
					if(cmdret.Return_PSAMRandom!=null)
						buf.append("Return_PSAMRandom:"+Util.BinToHex(cmdret.Return_PSAMRandom,0,cmdret.Return_PSAMRandom.length)+"\n");
					
					if(cmdret.Return_PSAMNo!=null)
						buf.append("ksn:"+Util.BinToHex(cmdret.Return_PSAMNo,0,cmdret.Return_PSAMNo.length)+"\n");
		            if(cmdret.trackLengths !=null && cmdret.trackLengths.length == 3){
		                buf.append("track1Length1:"+cmdret.trackLengths[0]+"\n");
		                buf.append("track1Length2:"+cmdret.trackLengths[1]+"\n");
		                buf.append("track1Length3:"+cmdret.trackLengths[2]+"\n");
		            }
		            if(cmdret.Return_PAN !=null){
		            	buf.append("Return_PAN:"+Util.BinToHex(cmdret.Return_PAN,0,cmdret.Return_PAN.length)+"\n");
		            }            
					if(cmdret.Return_PSAMTrack!=null){
						buf.append("Return_PSAMTrack:"+Util.BinToHex(cmdret.Return_PSAMTrack,0, cmdret.Return_PSAMTrack.length)+"\n");	
					}
					if(cmdret.Return_TerSerialNo !=null)
						buf.append("TerSerialNo:"+Util.BinToHex(cmdret.Return_TerSerialNo,0, cmdret.Return_TerSerialNo.length)+"\n");
					buf.append("CardType:"+cmdret.CardType+"\n");
					if(cmdret.CVM!=null)
						buf.append("CVM:"+Util.BinToHex(cmdret.CVM,0,+cmdret.CVM.length)+"\n");
					if(cmdret.CardSerial !=null)
						buf.append("CardSerial:"+Util.BinToHex(cmdret.CardSerial,0,+cmdret.CardSerial.length)+"\n");
					if(cmdret.cardexpiryDate !=null)
						buf.append("cardexpiryDate:"+Util.BinToHex(cmdret.cardexpiryDate,0,cmdret.cardexpiryDate.length)+"\n");
					if(cmdret.emvDataInfo !=null)
						buf.append("emvDataInfo:"+Util.BinToHex(cmdret.emvDataInfo,0, cmdret.emvDataInfo.length)+"\n");
					System.out.println(buf.toString());
					
					String hexTrack = Util.BinToHex(cmdret.Return_PSAMTrack,0, cmdret.Return_PSAMTrack.length);
					
					EmvReadCardInfoVo cinfo = new EmvReadCardInfoVo();
					cinfo.setCardNo(CodeUtil.hexStr2Str(Util.BinToHex(cmdret.Return_CardNo,0,cmdret.Return_CardNo.length)));
					cinfo.setCardType(cmdret.CardType+"");
					cinfo.setDeviceSn(Util.BinToHex(cmdret.Return_PSAMNo,0,cmdret.Return_PSAMNo.length));
					cinfo.setEntrack(isEntrack);
					
					if(cmdret.emvDataInfo!=null){
						String hex55 = Util.BinToHex(cmdret.emvDataInfo,0, cmdret.emvDataInfo.length);
						cinfo.setTrack2(hexTrack);
						cinfo.setTrack55(hex55);
						cinfo.setCardSequence(Util.BinToHex(cmdret.CardSerial,0,+cmdret.CardSerial.length));
					}else{
						if(cmdret.trackLengths[1]!=0){
							cinfo.setTrack2(hexTrack.substring(0,cmdret.trackLengths[1]*2));
						}
						if(cmdret.trackLengths[2]!=0){
							cinfo.setTrack3(hexTrack.substring(cmdret.trackLengths[1]*2));
						}
					}
					if(cmdret.Return_PAN !=null){
						cinfo.setCardPan(Util.BinToHex(cmdret.Return_PAN,0,cmdret.Return_PAN.length));
					}
					deviceListener.onFinishSwipeCard(cinfo);
		        }else{
		        	deviceListener.showMessage("刷卡/读卡发生错误~");
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onICWaitingOper() {
		deviceListener.showMessage("IC卡已插入，请勿拔卡~");
		
	}

	@Override
	public void onShowMessage(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*********设备监听信息****************/
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWaitingOper() {
		
	}

	@Override
	public void onWaitingPin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWaitingcard() {
		deviceListener.showMessage("");
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
