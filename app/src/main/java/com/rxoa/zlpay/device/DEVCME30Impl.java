package com.rxoa.zlpay.device;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.ModuleType;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.emv.EmvTransController;
import com.newland.mtype.module.common.emv.EmvTransInfo;
import com.newland.mtype.module.common.emv.SecondIssuanceRequest;
import com.newland.mtype.module.common.pin.AccountInputType;
import com.newland.mtype.module.common.pin.PinInputEvent;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.util.Dump;
import com.rxoa.zlpay.base.device.DeviceInfo;
import com.rxoa.zlpay.base.device.DeviceInterface;
import com.rxoa.zlpay.base.device.DeviceListener;
import com.rxoa.zlpay.base.device.EmvReadCardInfoVo;
import com.rxoa.zlpay.base.device.EmvTransReqInfo;
import com.rxoa.zlpay.base.device.EmvTransRespInfo;
import com.rxoa.zlpay.base.device.SwipeCardInfo;

public class DEVCME30Impl implements DeviceInterface,DEVCME30Listener{
	private Context context;
	private DeviceListener listener;
	private DEVCME30Controller controller;
	private String deviceAddress;
	private String keyMod;
	private String dVersion = "BT_ME30";
	
	@Override
	public String getKeyMod(){
		return keyMod;
	}
	@Override
	public void setDeviceListener(DeviceListener listener) {
		this.listener = listener;
	}
	@Override
	public void initDevice(Context context,final String deviceAddress,boolean isEntrack){
		this.context = context;
		this.deviceAddress = deviceAddress;
		initController(this.deviceAddress);
	}
	private void initController(final String address) {
		try {
			controller = DEVCME30Controller.getInstance(context, address, new DeviceEventListener<ConnectionCloseEvent>() {
				@Override
				public void onEvent(ConnectionCloseEvent event, Handler handler) {
					if (event.isSuccess()) {
						listener.showMessage("蓝牙断开成功！");
					}
					if (event.isFailed()) {
						listener.showMessage("蓝牙链接断开异常！");
					}
				}
				@Override
				public Handler getUIHandler() {
					// TODO Auto-generated method stub
					return null;
				}
			});
			listener.showMessage("初始化完成，设备已就绪...");
		} catch (Exception e) {
			Log.e("Controller", "failed to connect to:" + address, e);
			if (controller != null) {
				try {
					controller.disConnect();
				} catch (Exception ex) {
				} finally {
					controller = null;
				}
			}
		}
	}
	@Override
	public void startSwipeCard(final String value) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				connectDevice();
				try {
					BigDecimal amt = null;
					controller.clearScreen();
					SwipResult swipRslt = null;
					//刷卡
					listener.showMessage("正在等待刷卡...");
					if(value==null){
						swipRslt = controller.swipCard("查询卡内余额\n请刷卡...", 30000L, TimeUnit.MILLISECONDS);
					}else{
						//输入金额
						amt = new BigDecimal(value);
						swipRslt = controller.swipCard("消费金额为：" + amt.toString() + "\n请刷卡", 30000L, TimeUnit.MILLISECONDS);
					}
					if (swipRslt == null) {
						controller.clearScreen();
						listener.showMessage("等待超时，刷卡取消...");
						return;
					}
					byte[] secondTrack = swipRslt.getSecondTrackData();
					byte[] thirdTrack = swipRslt.getThirdTrackData();
					SwipeCardInfo cardInfo = new SwipeCardInfo();
					cardInfo.setCardNo(swipRslt.getAccount().getAcctNo());
					//cardInfo.setSecondTrack(secondTrack == null ? "null" : DEVCME30Util.formatTrack(Dump.getHexDump(secondTrack)));
					//cardInfo.setThirdTrack(thirdTrack == null ? "null" : DEVCME30Util.formatTrack(Dump.getHexDump(thirdTrack)));
					cardInfo.setSecondTrack(secondTrack == null ? "null" : DEVCME30Util.formatTrack(Dump.getHexDump(secondTrack)));
					cardInfo.setThirdTrack(thirdTrack == null ? "null" : DEVCME30Util.formatTrack(Dump.getHexDump(thirdTrack)));

					//listener.onFinishSwipeCard(cardInfo);
					System.out.println("二磁:"+cardInfo.getSecondTrack());
					System.out.println("三磁:"+cardInfo.getThirdTrack());
					//appendInteractiveInfoAndShow("getValidDate:" + swipRslt.getValidDate());
					//appendInteractiveInfoAndShow("getValidDate:" + swipRslt.getServiceCode());
					//appendInteractiveInfoAndShow("刷卡成功\n二磁道:" + (secondTrack == null ? "null" : Dump.getHexDump(secondTrack)) + "\n三磁道:" + (thirdTrack == null ? "null" : Dump.getHexDump(thirdTrack)) + "\n请输入密码：");

					// 密码输入
					PinInputEvent event = null;
					listener.showMessage("等待输入密码...");
					if(value==null){
						event = controller.startPininput(AccountInputType.USE_ACCT_HASH, swipRslt.getAccount().getAcctHashId(), 6, true, "请输入交易密码:", 60000);
					}else{
						event = controller.startPininput(AccountInputType.USE_ACCT_HASH, swipRslt.getAccount().getAcctHashId(), 6, true, "消费金额为:" + amt.toString() + "\n请输入交易密码:", 60000);
					}
					if (event == null) {
						listener.showMessage("等待超时，操作取消...");
						return;
					}
					//listener.onFinishComputepwd(Dump.getHexDump(event.getEncrypPin()).replaceAll("\\s", ""));
					//appendInteractiveInfoAndShow("密码输入完成, 密码:" + Dump.getHexDump(event.getEncrypPin()));
					//controller.showMessage("消 费 完 成");
					//listener.showMessage("操作成功完成！");
				} catch (Exception e) {
					//appendInteractiveInfoAndShow("消费处理异常：" + e.getMessage());
					listener.showMessage("消费处理异常，操作失败！");
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void connectDevice() {
		listener.showMessage("正在连接设备...");
		try {
			controller.connect();
		} catch (Exception e1) {
			e1.printStackTrace();
			listener.showMessage("设备连接失败...");
		}
		listener.showMessage("设备连接成功...");
	}
	@Override
	public void readDeviceInfo() {
		DeviceInfo deviceInfo = new DeviceInfo();
		try{
			com.newland.mtype.DeviceInfo mDevice = controller.getDeviceInfo();
			deviceInfo.setDeviceSn(mDevice.getSN());
		}catch(Exception e){
			e.printStackTrace();
		}
		//return deviceInfo;
	}
	@Override
	public void startICCardTransfer(final String value) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				connectDevice();
				try{
					connectDevice();
					listener.showMessage("设备就绪，请读卡...");
					if(value==null){
						controller.startTransfer(new ModuleType[] { ModuleType.COMMON_SWIPER, ModuleType.COMMON_ICCARD }, "\n请刷卡或者插入IC卡", new BigDecimal("1.00"), 60, TimeUnit.SECONDS,DEVCME30Impl.this);
					}else{
						controller.startTransfer(new ModuleType[] { ModuleType.COMMON_SWIPER, ModuleType.COMMON_ICCARD }, "消费金额"+value+"\n请刷卡或者插入IC卡", new BigDecimal(value), 60, TimeUnit.SECONDS,DEVCME30Impl.this);			
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	@Override
	public void onEmvFinished(boolean arg0, EmvTransInfo arg1) throws Exception {
		// TODO Auto-generated method stub
		EmvTransRespInfo respInfo = new EmvTransRespInfo();
		
		listener.onOltransResponse(respInfo);
	}
	@Override
	public void onError(EmvTransController arg0, Exception arg1) {
		// TODO Auto-generated method stub
		listener.showMessage("交易失败！");
	}
	@Override
	public void onFallback(EmvTransInfo arg0) throws Exception {
		// TODO Auto-generated method stub
		listener.showMessage("交易降级！");
	}
	@Override
	public void onRequestOnline(EmvTransController controller, EmvTransInfo context)
			throws Exception {
		// TODO Auto-generated method stub
		listener.showMessage("正在联机交易...");
		EmvTransReqInfo reqInfo = new EmvTransReqInfo();
		reqInfo.setCardNo(context.getCardNo());
		reqInfo.setCardPwd(Dump.getHexDump(context.getOnLinePin()).replaceAll("\\s", ""));
		reqInfo.setCvmRslt(new String(context.getCvmRslt()));
		reqInfo.setAppCrypt(new String(context.getAppCryptogram()));
		reqInfo.setVertifyRslt(new String(context.getTerminalVerificationResults()));
		reqInfo.setCardSequence(context.getCardSequenceNumber());
		reqInfo.setExternalStr(DEVCME30Util.packTlv(context.externalToString()));
		reqInfo.setTrackTwo(DEVCME30Util.formatTrackEqv(Dump.getHexDump(context.getTrack_2_eqv_data())));
		SecondIssuanceRequest request = new SecondIssuanceRequest();
		request.setAuthorisationResponseCode("00");
		controller.secondIssuance(request);
		listener.onOltransRequest(reqInfo);
	}
	@Override
	public void onRequestPinEntry(EmvTransController arg0, EmvTransInfo arg1)
			throws Exception {
		// TODO Auto-generated method stub
		listener.showMessage("验证错误，交易失败！");
		arg0.cancelEmv();
	}
	@Override
	public void onRequestSelectApplication(EmvTransController arg0,
			EmvTransInfo arg1) throws Exception {
		// TODO Auto-generated method stub
		listener.showMessage("验证错误，交易失败！");
		arg0.cancelEmv();
	}
	@Override
	public void onRequestTransferConfirm(EmvTransController arg0,
			EmvTransInfo arg1) throws Exception {
		// TODO Auto-generated method stub
		listener.showMessage("验证错误，交易失败！");
		arg0.cancelEmv();
	}
	@Override
	public void onSwipMagneticCard(SwipResult swipRslt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onOpenCardreaderCanceled() {
		// TODO Auto-generated method stub
		listener.showMessage("用户撤销刷卡操作！");
	}
	@Override
	public void onDealing(int code, String msg) {
		// TODO Auto-generated method stub
		controller.showMessage("正在交易,请勿取消...");
	}
	@Override
	public void onDealFinished(int code, String msg) {
		// TODO Auto-generated method stub
		controller.showMessage("交易完成！");
	}
	@Override
	public void installKeys(String keystr, int keytype) {
		// TODO Auto-generated method stub
		
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
