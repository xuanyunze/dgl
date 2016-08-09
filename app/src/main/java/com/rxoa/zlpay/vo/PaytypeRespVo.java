package com.rxoa.zlpay.vo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rxoa.zlpay.base.util.JsonUtil;
import com.rxoa.zlpay.base.util.RegExUtil;
import com.rxoa.zlpay.entity.BankCardEntity;

public class PaytypeRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	private String blanceValue;
	private List<BankCardEntity> fastCards;
	
	public PaytypeRespVo initFromJson(String json){
		try{
			if(!JsonUtil.isJsonNull(json)){
				JSONObject obj = new JSONObject(json);
				blanceValue = obj.getString("blanceValue");
				if(!RegExUtil.isMoneyValue(blanceValue)){
					blanceValue = "0.00";
				}
				String strCards = obj.getString("fastCards");
				if(!JsonUtil.isJsonNull(strCards)){
					fastCards = new ArrayList<BankCardEntity>();
					JSONArray array = new JSONArray(strCards);
					for(int i=0;i<array.length();i++){
						BankCardEntity card = new BankCardEntity();
						card.initFromJson(array.getString(i));
						fastCards.add(card);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public static PaytypeRespVo getInstance(){
		return new PaytypeRespVo();
	}
	public String getBlanceValue() {
		return blanceValue;
	}
	public void setBlanceValue(String blanceValue) {
		this.blanceValue = blanceValue;
	}
	public List<BankCardEntity> getFastCards() {
		return fastCards;
	}
	public void setFastCards(List<BankCardEntity> fastCards) {
		this.fastCards = fastCards;
	}
}
