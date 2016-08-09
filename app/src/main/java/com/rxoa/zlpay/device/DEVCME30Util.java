package com.rxoa.zlpay.device;

import com.newland.mtype.util.Dump;
import com.rxoa.zlpay.base.util.CodeUtil;
import com.rxoa.zlpay.base.util.HexCodeUtil;

public class DEVCME30Util {
	public static String formatTrack(String strTrack){
		String result = strTrack;
		try{
			result = strTrack.replaceAll("\\s", "");
			result = new String(HexCodeUtil.str2Bcd(result));
			result = result.replace("=", "D");
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public static String formatTrackEqv(String strTrack){
		String result = strTrack;
		try{
			result = strTrack.replaceAll("\\s", "");
			result = CodeUtil.bytetoAscStr(result.getBytes());
			result = result.replace("=", "D");
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public static String packTlv(String in){
		String out = "";
		String filter = "9f26,9f27,9f10,9f37,9f36,95,9a,9c,9f02,5f2a,82,9f1a,9f03,9f33,9f74,9f34,9f35,9f1e,84,9f09,9f41,91,71,72,df31,9f63";
		try{
			in = in.replaceAll("\\s", "");
			System.out.println("in=="+in);
			String[] fields = in.split(",");
			for(int i=0;i<fields.length;i++){
				String[] tlv = fields[i].split(":");
				String tag = tlv[0];
				if((","+filter+",").indexOf(","+tag+",")!=-1){
					String value = tlv[1];
					String length = Integer.toHexString(tlv[1].length()/2);
					if(length.length()%2!=0){
						length = "0"+length;
					}
					System.out.println("tag:"+tlv[0]);
					System.out.println("val:"+tlv[1]);
					System.out.println("len:"+length);
					out+=tlv[0]+length+value;
				}
			}
			System.out.println(out);
		}catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
}
