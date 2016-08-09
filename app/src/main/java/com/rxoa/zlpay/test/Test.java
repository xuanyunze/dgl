package com.rxoa.zlpay.test;

import com.rxoa.zlpay.base.util.Base64Util;

public class Test {
	public static void main(String[] args){
		String str = "OK,我是一个中国人！";
		String enstr = Base64Util.encrypt(str.getBytes());
		String srcstr = new String(Base64Util.decrypt(enstr));
		System.out.println("加密前："+str);
		System.out.println("加密后："+enstr);
		System.out.println(srcstr);
	}
}
