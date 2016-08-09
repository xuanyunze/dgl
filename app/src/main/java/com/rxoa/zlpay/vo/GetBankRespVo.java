package com.rxoa.zlpay.vo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetBankRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	private List<String> bankInfo;
	
	public static GetBankRespVo getInstance(){
		return new GetBankRespVo();
	}
	
	public GetBankRespVo initFromJson(String json){
		try{System.out.println(json);
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				JSONArray array = new JSONArray(obj.getString("bankInfo"));
				if(bankInfo==null){bankInfo = new ArrayList<String>();}
				for(int i=0;i<array.length();i++){
					bankInfo.add(array.getString(i));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}

	public List<String> getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(List<String> bankInfo) {
		this.bankInfo = bankInfo;
	}
}
