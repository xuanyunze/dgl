package com.rxoa.zlpay.device;

public class DEVCME30Define {
	public static final int DRIVER_NOT_FOUND = 1000;
	public static final int INIT_DRIVER_FAIED = 1001;
	public static final int INIT_DEVICE_PARAMS_FAILED = 1002;
	public static final int GET_TRACKTEXT_FAILED = 1003;
	public static final int GET_PININPUT_FAILED = 1004;
	public static final int GET_KEYBOARD_VALUE_FAILED = 1005;
	public static final int GET_PLAIN_ACCOUNT_NO_FAILED = 1006;
	public static final int MK_INDEX_NOFOUND_ERROR = 1007;
	public static final int UPDATE_FIRMWARE_FAILED = 1008;
	
	public static final int DEFAULT_MK_INDEX = 1;
	public static final int DEFAULT_PIN_WK_INDEX = 2;
	public static final int DEFAULT_MAC_WK_INDEX = 3;
	public static final int DEFAULT_TRACK_WK_INDEX = 4;
	public static final int DEFAULT_MUTUALAUTH_WK_INDEX = 5;
	
	public static final int MRCH_NO = 0xFF9F11;
	/**
	 * 终端号<tt>tag</tt>
	 */
	public static final int TRMNL_NO = 0xFF9F12;
	/**
	 * 商户名<tt>tag</tt>
	 */
	public static final int MRCH_NAME = 0xFF9F13;
	/**
	 * PSAM号<tt>tag</tt>
	 */
	public static final int PSAM_NO = 0xFF9F49;
	
	public enum WKType {
		/**
		 * PIN加密工作密钥
		 */
		PINKEY,
		/**
		 * 磁道加密工作密钥
		 */
		TRACKKEY,
		/**
		 * MAC加密工作密钥
		 */
		MACKEY,
		/**
		 * 数据加密工作密钥
		 */
		DATAENCRYPTKEY
	}
}
