package com.rxoa.zlpay.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.newland.me.ConnUtils;
import com.newland.me.DeviceManager;
import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.DeviceInfo;
import com.newland.mtype.DeviceOutofLineException;
import com.newland.mtype.DeviceRTException;
import com.newland.mtype.ModuleType;
import com.newland.mtype.common.ExCode;
import com.newland.mtype.common.MESeriesConst.TrackEncryptAlgorithm;
import com.newland.mtype.event.AbstractProcessDeviceEvent;
import com.newland.mtype.event.DeviceEvent;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.log.DeviceLogger;
import com.newland.mtype.log.DeviceLoggerFactory;
import com.newland.mtype.module.common.cardreader.CardReader;
import com.newland.mtype.module.common.cardreader.OpenCardReaderEvent;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.emv.EmvTransController;
import com.newland.mtype.module.common.emv.OnlinePinConfig;
import com.newland.mtype.module.common.emv.QPBOCModule;
import com.newland.mtype.module.common.lcd.LCD;
import com.newland.mtype.module.common.pin.AccountInputType;
import com.newland.mtype.module.common.pin.PinInput;
import com.newland.mtype.module.common.pin.PinInputEvent;
import com.newland.mtype.module.common.pin.PinManageType;
import com.newland.mtype.module.common.pin.WorkingKey;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.printer.PrinterStatus;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.module.common.swiper.SwipResultType;
import com.newland.mtype.module.common.swiper.Swiper;
import com.newland.mtype.module.common.swiper.SwiperReadModel;
import com.newland.mtype.tlv.TLVPackage;
import com.newland.mtype.util.ISOUtils;
import com.newland.mtypex.bluetooth.BlueToothV100ConnParams;

/**
 * 具体实现类
 * 
 * 
 */
public class DEVCME30Controller{	
	private DeviceLogger logger = DeviceLoggerFactory.getLogger(DEVCME30Controller.class);
	private DeviceManager deviceManager;
	private static final String DRIVER_NAME = "com.newland.me.ME3xDriver";

	private DEVCME30Controller(Context context, String macAddress, DeviceEventListener<ConnectionCloseEvent> listener) {
		deviceManager = ConnUtils.getDeviceManager();
		BlueToothV100ConnParams params = new BlueToothV100ConnParams(macAddress);
		deviceManager.init(context, DRIVER_NAME, params, listener);
	}

	/**
	 * 构造一个蓝牙控制器
	 * 
	 * @param context
	 *            容器上下文
	 * @param listener
	 *            蓝牙监听器
	 * @return
	 */
	public static DEVCME30Controller getInstance(Context context, String macAddress, DeviceEventListener<ConnectionCloseEvent> listener) {
		return new DEVCME30Controller(context, macAddress, listener);
	}

	public void connect() throws Exception {
		deviceManager.connect();
	}

	public void disConnect() {
		deviceManager.disconnect();
	}

	private void isConnected() {
		synchronized (DRIVER_NAME) {
			if (null == deviceManager || deviceManager.getDevice() == null) {
				throw new DeviceOutofLineException("device not connect!");
			}
		}
	}

	/**
	 * 事件线程阻塞控制监听器.
	 * 
	 * @author lance
	 * 
	 * @param <T>
	 */
	private class EventHolder<T extends DeviceEvent> implements DeviceEventListener<T> {

		private T event;

		private final Object syncObj = new Object();

		private boolean isClosed = false;

		public void onEvent(T event, Handler handler) {
			this.event = event;
			synchronized (syncObj) {
				isClosed = true;
				syncObj.notify();
			}
		}

		public Handler getUIHandler() {
			return null;
		}

		void startWait() throws InterruptedException {
			synchronized (syncObj) {
				if (!isClosed)
					syncObj.wait();
			}
		}

	}

	private <T extends AbstractProcessDeviceEvent> T preEvent(T event, int defaultExCode) {
		if (!event.isSuccess()) {
			if (event.isUserCanceled()) {
				return null;
			}
			if (event.getException() != null) {
				if (event.getException() instanceof RuntimeException) {// 运行时异常直接抛出.
					throw (RuntimeException) event.getException();
				}
				throw new DeviceRTException(DEVCME30Define.GET_TRACKTEXT_FAILED, "open card reader meet error!", event.getException());
			}
			throw new DeviceRTException(ExCode.UNKNOWN, "unknown exception!defaultExCode:" + defaultExCode);
		}
		return event;
	}

	public PinInputEvent startPininput(AccountInputType acctInputType, String acctHash, int inputMaxLen, boolean isEnterEnabled, String msg, long timeout) throws InterruptedException {
		isConnected();
		PinInput pinInput = (PinInput) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PININPUT);
		EventHolder<PinInputEvent> listener = new EventHolder<PinInputEvent>();
		WorkingKey wkey = new WorkingKey(DEVCME30Define.DEFAULT_PIN_WK_INDEX);
		pinInput.startStandardPinInput(wkey, PinManageType.MKSK, acctInputType, acctHash, inputMaxLen, new byte[] { 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F' }, isEnterEnabled, msg, (int) timeout, TimeUnit.MILLISECONDS, listener);
		try {
			listener.startWait();
		} catch (InterruptedException e) {
			pinInput.cancelPinInput();
			throw e;
		} finally {
			clearScreen();
		}
		PinInputEvent event = listener.event;
		event = preEvent(event, DEVCME30Define.GET_PININPUT_FAILED);
		if (event == null) {
			logger.info("start getChipherText,but return is none!may user canceled?");
			return null;
		}
		return event;
	}

	public void showMessage(String msg) {
		isConnected();
		LCD lcd = (LCD) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_LCD);
		if (lcd != null) {
			lcd.draw(msg);
		}
	}

	public void clearScreen() {
		isConnected();
		LCD lcd = (LCD) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_LCD);
		if (lcd != null) {
			lcd.clearScreen();
		}
	}

	public SwipResult swipCard(String msg, long timeout, TimeUnit timeUnit) throws InterruptedException {
		isConnected();
		CardReader cardReader = (CardReader) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_CARDREADER);
		if (cardReader == null) {
			throw new DeviceRTException(DEVCME30Define.GET_TRACKTEXT_FAILED, "not support read card!");
		}
		try {
			EventHolder<OpenCardReaderEvent> listener = new EventHolder<OpenCardReaderEvent>();
			cardReader.openCardReader(msg, new ModuleType[] { ModuleType.COMMON_SWIPER }, timeout, timeUnit, listener);
			try {
				listener.startWait();
			} catch (InterruptedException e) {
				cardReader.cancelCardRead();
				throw e;
			} finally {
				clearScreen();
			}
			OpenCardReaderEvent event = listener.event;
			event = preEvent(event, DEVCME30Define.GET_TRACKTEXT_FAILED);
			if (event == null) {
				return null;
			}
			ModuleType[] openedModuleTypes = event.getOpenedCardReaders();
			if (openedModuleTypes == null || openedModuleTypes.length <= 0) {
				logger.info("start cardreader,but return is none!may user canceled?");
				return null;
			}
			if (openedModuleTypes.length > 1) {
				logger.warn("should return only one type of cardread action!but is " + openedModuleTypes.length);
				throw new DeviceRTException(DEVCME30Define.GET_TRACKTEXT_FAILED, "should return only one type of cardread action!but is " + openedModuleTypes.length);
			}
			switch (openedModuleTypes[0]) {
			case COMMON_SWIPER:
				int trackKey = DEVCME30Define.DEFAULT_TRACK_WK_INDEX;
				Swiper swiper = (Swiper) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SWIPER);
				System.out.println("trackKey=="+trackKey);
				System.out.println("index=="+TrackEncryptAlgorithm.BY_UNIONPAY_MODEL);

				SwipResult swipRslt = getSwipResult(swiper, trackKey, TrackEncryptAlgorithm.BY_UNIONPAY_MODEL);
				if (swipRslt.getRsltType() == SwipResultType.SUCCESS) {
					return swipRslt;
				}
				throw new DeviceRTException(DEVCME30Define.GET_TRACKTEXT_FAILED, "swip failed:" + swipRslt.getRsltType());
			default: {
				throw new DeviceRTException(DEVCME30Define.GET_TRACKTEXT_FAILED, "not support cardreader module:" + openedModuleTypes[0]);
			}
			}
		} finally {
			cardReader.closeCardReader();
		}
	}

	private SwipResult getSwipResult(Swiper swiper, int trackKey, String encryptType) {
		isConnected();
		WorkingKey wkey = new WorkingKey(trackKey);
		System.out.println("wkey=="+wkey.getWk());
		SwipResult swipRslt = swiper.readEncryptResult(new SwiperReadModel[] { SwiperReadModel.READ_SECOND_TRACK, SwiperReadModel.READ_THIRD_TRACK }, wkey, encryptType);
		//读取明文磁道，不推荐
		//SwipResult swipRslt = swiper.readPlainResult(new SwiperReadModel[] { SwiperReadModel.READ_SECOND_TRACK, SwiperReadModel.READ_THIRD_TRACK });
		return swipRslt;
	}

	public void setParamStr(int tag, String paramValue) {
		isConnected();
		TLVPackage tlvpackage = ISOUtils.newTlvPackage();
		try {
			tlvpackage.append(tag, paramValue.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		deviceManager.getDevice().setDeviceParams(tlvpackage);

	}

	public String getParamStr(int tag) {
		isConnected();
		TLVPackage pack = deviceManager.getDevice().getDeviceParams(tag);
		if (pack != null) {
			byte[] rslt = pack.getValue(getOrginTag(tag));
			if (rslt != null) {
				try {
					return new String(rslt, "GBK");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}

	private int getOrginTag(int tag) {
		if ((tag & 0xFF0000) == 0xFF0000) {
			return tag & 0xFFFF;
		}
		return tag;
	}

	public DeviceInfo getDeviceInfo() {
		isConnected();
		return deviceManager.getDevice().getDeviceInfo();
	}

	public void reset() {
		isConnected();
		deviceManager.getDevice().reset();
	}

	public PrinterStatus getPrinterStatus() {
		isConnected();
		Printer printer = (Printer) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PRINTER);
		return printer.getStatus();
	}

	public void print(String printData, long timeout, TimeUnit timeUnit) {
		isConnected();
		Printer print = (Printer) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PRINTER);
		print.init();
		print.print(printData, timeout, timeUnit);
	}

	public void printBitMap(int position, Bitmap bitmap) {
		isConnected();
		Printer print = (Printer) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PRINTER);
		print.init();
		print.print(position, bitmap, 30, TimeUnit.SECONDS);
	}

	public void updateFirmware(String filePath) throws Exception {
		isConnected();
		InputStream is = null;
		try {
			is = new FileInputStream(new File(filePath));
			deviceManager.getDevice().updateApp(is);
		} catch (Exception ex) {
			throw new DeviceRTException(DEVCME30Define.UPDATE_FIRMWARE_FAILED, "update firmware failed:" + ex.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public EmvModule getEmvModule() {
		isConnected();
		return (EmvModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_EMV);
	}

	public void startTransfer(ModuleType[] cardReaders, String msg, BigDecimal amt, long timeout, TimeUnit timeunit, DEVCME30Listener transferListener) {
		isConnected();
		CardReader cardReader = (CardReader) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_CARDREADER);
		if (cardReader == null) {
			throw new DeviceRTException(DEVCME30Define.GET_TRACKTEXT_FAILED, "not support read card!");
		}
		try {
			EventHolder<OpenCardReaderEvent> listener = new EventHolder<OpenCardReaderEvent>();
			cardReader.openCardReader(msg, new ModuleType[] { ModuleType.COMMON_SWIPER, ModuleType.COMMON_NCCARD, ModuleType.COMMON_ICCARD }, timeout, timeunit, listener);
			try {
				listener.startWait();
			} catch (InterruptedException e) {
				cardReader.cancelCardRead();
				transferListener.onOpenCardreaderCanceled();
			} finally {
				clearScreen();
			}
			OpenCardReaderEvent event = listener.event;
			event = preEvent(event, DEVCME30Define.GET_TRACKTEXT_FAILED);
			if (event == null) {
				transferListener.onOpenCardreaderCanceled();
				return;
			}
			ModuleType[] openedModuleTypes = event.getOpenedCardReaders();
			if (openedModuleTypes == null || openedModuleTypes.length <= 0) {
				logger.info("start cardreader,but return is none!may user canceled?");
				transferListener.onOpenCardreaderCanceled();
			}
			if (openedModuleTypes.length > 1) {
				logger.warn("should return only one type of cardread action!but is " + openedModuleTypes.length);
				throw new DeviceRTException(DEVCME30Define.GET_TRACKTEXT_FAILED, "should return only one type of cardread action!but is " + openedModuleTypes.length);
			}
			switch (openedModuleTypes[0]) {
			case COMMON_SWIPER:
				int trackKey = DEVCME30Define.DEFAULT_TRACK_WK_INDEX;
				Swiper swiper = (Swiper) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SWIPER);
				SwipResult swipRslt = getSwipResult(swiper, trackKey, TrackEncryptAlgorithm.BY_UNIONPAY_MODEL);
				if (swipRslt.getRsltType() == SwipResultType.SUCCESS) {
					transferListener.onSwipMagneticCard(swipRslt);
					return;
				}
				throw new DeviceRTException(DEVCME30Define.GET_TRACKTEXT_FAILED, "swip failed:" + swipRslt.getRsltType());
			case COMMON_ICCARD:
				EmvModule module = getEMVModule();
				OnlinePinConfig config = new OnlinePinConfig();
				config.setWorkingKey(new WorkingKey(DEVCME30Define.DEFAULT_PIN_WK_INDEX));
				config.setPinManageType(PinManageType.MKSK);
				config.setPinPadding(new byte[] { 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F' });
				//config.setPinPadding(new byte[] { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0' });
				config.setDisplayContent("请输入交易密码:");
				config.setTimeout(30);
				config.setInputMaxLen(6);
				config.setEnterEnabled(true);
				module.setOnlinePinConfig(config);

				EmvTransController controller = module.getEmvTransController(transferListener);
				controller.startEmv(amt, new BigDecimal("0"), true);
				break;
			default: {
				throw new DeviceRTException(DEVCME30Define.GET_TRACKTEXT_FAILED, "not support cardreader module:" + openedModuleTypes[0]);
			}
			}
		} finally {
			cardReader.closeCardReader();
		}
	}

	private EmvModule getEMVModule() {
		return (EmvModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_EMV);
	}

	private QPBOCModule getQPBOCModule() {
		return (QPBOCModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_QPBOC);
	}

}
