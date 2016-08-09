package com.rxoa.zlpay.vo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rxoa.zlpay.base.util.JsonUtil;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.entity.DeviceEntity;
import com.rxoa.zlpay.entity.UserAccInfo;

public class UserAccInfoRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	private UserAccInfo accInfo;
	private BankCardEntity mainCardInfo;
	private List<BankCardEntity> otherCardInfo;
	private List<DeviceEntity> deviceInfo;
	
	public UserAccInfoRespVo initFromJson(String json){
		try{
			if(!JsonUtil.isJsonNull(json)){
				JSONObject obj = new JSONObject(json);
				accInfo  = new UserAccInfo();
				mainCardInfo = new BankCardEntity();
				otherCardInfo = new ArrayList<BankCardEntity>();
				deviceInfo = new ArrayList<DeviceEntity>();
				
				if(!JsonUtil.isJsonNull(obj.getString("accInfo"))){
					accInfo.initFromJson(obj.getString("accInfo"));
				}
				
				if(!JsonUtil.isJsonNull(obj.getString("mainCardInfo"))){
					mainCardInfo.initFromJson(obj.getString("mainCardInfo"));
				}
				if(!JsonUtil.isJsonNull(obj.getString("otherCardInfo"))){
					JSONArray cards = new JSONArray(obj.getString("otherCardInfo"));
					for(int i=0;i<cards.length();i++){
						BankCardEntity card = new BankCardEntity();
						card.initFromJson(cards.getString(i));
						otherCardInfo.add(card);
					}
				}
				if(!JsonUtil.isJsonNull(obj.getString("deviceInfo"))){
					JSONArray devices = new JSONArray(obj.getString("deviceInfo"));
					for(int j=0;j<devices.length();j++){
						DeviceEntity device = new DeviceEntity();
						device.initFromJson(devices.getString(j));
						deviceInfo.add(device);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	public static UserAccInfoRespVo getInstance(){
		return new UserAccInfoRespVo();
	}
	public UserAccInfo getAccInfo() {
		return accInfo;
	}
	public void setAccInfo(UserAccInfo accInfo) {
		this.accInfo = accInfo;
	}
	public BankCardEntity getMainCardInfo() {
		return mainCardInfo;
	}
	public void setMainCardInfo(BankCardEntity mainCardInfo) {
		this.mainCardInfo = mainCardInfo;
	}
	public List<BankCardEntity> getOtherCardInfo() {
		return otherCardInfo;
	}
	public void setOtherCardInfo(List<BankCardEntity> otherCardInfo) {
		this.otherCardInfo = otherCardInfo;
	}
	public List<DeviceEntity> getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(List<DeviceEntity> deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
}
