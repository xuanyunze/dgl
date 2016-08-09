package com.rxoa.zlpay.avator;

public class AvOrderType{
	public static enum code{
		ReceiveMoney,TakeMoney,AccountCharge,TransAcc,RepayCredit,PhoneCharge,QueryBlance,
		ZeroReceiveMoney,CommonReceiveMoney,FastReceiveMoney,S0ReceiveMoney
	}
	public static String value2text(String value) {
		if(value.equals("1"))
			return "当面收款";
		if(value.equals("2"))
			return "用户提现";
		if(value.equals("3"))
			return "账户充值";
		if(value.equals("4"))
			return "卡卡转账";
		if(value.equals("5"))
			return "还信用卡";
		if(value.equals("6"))
			return "手机充值";
		if(value.equals("7"))
			return "查询余额";
		if(value.equals("8"))
			return "零手续费收款";
		if(value.equals("9"))
			return "普通收款";
		if(value.equals("10"))
			return "快速收款";
		if(value.equals("11"))
			return "快速收款";
		return "未知";
	}
	public static String text2value(String text) {
		if(text.equals("当面收款"))
			return "1";
		if(text.equals("用户提现"))
			return "2";
		if(text.equals("账户充值"))
			return "3";
		if(text.equals("卡卡转账"))
			return "4";
		if(text.equals("还信用卡"))
			return "5";
		if(text.equals("手机充值"))
			return "6";
		if(text.equals("查询余额"))
			return "7";
		if(text.equals("零手续费收款"))
			return "8";
		if(text.equals("普通收款"))
			return "9";
		if(text.equals("快速收款"))
			return "10";
		if(text.equals("快速收款"))
			return "11";
		return "0";
	}
	public static String value2code(String value) {
		if(value.equals("1"))
			return code.ReceiveMoney.name();
		if(value.equals("2"))
			return code.TakeMoney.name();
		if(value.equals("3"))
			return code.AccountCharge.name();
		if(value.equals("4"))
			return code.TransAcc.name();
		if(value.equals("5"))
			return code.RepayCredit.name();
		if(value.equals("6"))
			return code.PhoneCharge.name();
		if(value.equals("7"))
			return code.QueryBlance.name();
		if(value.equals("8"))
			return code.ZeroReceiveMoney.name();
		if(value.equals("9"))
			return code.CommonReceiveMoney.name();
		if(value.equals("10"))
			return code.FastReceiveMoney.name();
		if(value.equals("11"))
			return code.S0ReceiveMoney.name();
		return value;
	}
	public static String code2value(String vcode) {
		if(vcode.equals(code.ReceiveMoney.name()))
			return "1";
		if(vcode.equals(code.TakeMoney.name()))
			return "2";
		if(vcode.equals(code.AccountCharge.name()))
			return "3";
		if(vcode.equals(code.TransAcc.name()))
			return "4";
		if(vcode.equals(code.RepayCredit.name()))
			return "5";
		if(vcode.equals(code.PhoneCharge.name()))
			return "6";
		if(vcode.equals(code.QueryBlance.name()))
			return "7";
		if(vcode.equals(code.ZeroReceiveMoney.name()))
			return "8";
		if(vcode.equals(code.CommonReceiveMoney.name()))
			return "9";
		if(vcode.equals(code.FastReceiveMoney.name()))
			return "10";
		if(vcode.equals(code.S0ReceiveMoney.name()))
			return "11";
		return "0";
	}
	
	public static String code2text(String code) {
		return code;
	}
	public static String text2code(String text) {
		return text;
	}
}
