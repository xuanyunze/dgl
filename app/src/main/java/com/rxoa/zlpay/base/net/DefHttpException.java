package com.rxoa.zlpay.base.net;

public class DefHttpException extends Exception{
	private static final long serialVersionUID = 1L;
		/**
		 * 成功
		 */
		public static final int OK=200;
		/**
		 * 找不到服务器
		 */
		public static final int ERROR=400;
		/**连接超时
		 */
		public static final int TIME_OUT=506;
		/**
		 *URL 解析出错
		 */
		public static final int URL_ERROR=700;
		/**
		 *服务器错
		 */
		public static final int SERVER_ERROR=500;
		/**
		 *连接中断
		 */
		public static final int CONNECT_INTERRUPT =701;
		/**
		 *不支持协议
		 */
		public static final int PROTOCL_ERROR=702;
		
		private int status;
		private String message;
		
		public DefHttpException() {
			// TODO Auto-generated constructor stub
		}
		public DefHttpException(int status) {
			// TODO Auto-generated constructor stub
			this.status = status;
		}
		public DefHttpException(String message) {
			// TODO Auto-generated constructor stub
			this.message = message;
		}
		public DefHttpException(int status,String msg) {
			// TODO Auto-generated constructor stub
			this(status);
			this.message = msg;
		}
		
		public String getMessage() {
			return getMessage(this.status);
		}
		public int getStatus() {
			return status;
		}
		
		private String getMessage(int status){
			switch (status) {
			case OK:
				this.message = "请求成功";
				break;
			case ERROR:
				this.message = "连接出错";
				break;
			case TIME_OUT:
				this.message ="网络连接超时,请重试！";
			case URL_ERROR:
				this.message ="URL解析错误!";
			case SERVER_ERROR:
				this.message ="服务器错误";
			case CONNECT_INTERRUPT:
				this.message ="网络连接中断,请检查网络！";
				break;
			default:
				break;
			}
			return this.message;
		}
		
}
