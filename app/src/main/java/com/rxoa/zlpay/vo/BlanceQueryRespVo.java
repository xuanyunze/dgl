package com.rxoa.zlpay.vo;

import org.json.JSONObject;


public class BlanceQueryRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;

	private String uid;
	private String uname;
	private Double blanceValue;
	private Double blanceReValue;
	private Double blanceZreValue;
	private Double blanceTakeValue;
	
	public static BlanceQueryRespVo getInstance(){
		return new BlanceQueryRespVo();
	}
	
	public BlanceQueryRespVo initFromJson(String json){
		try{System.out.println("json=="+json);
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				this.setUid(obj.getString("uid"));
				this.setUname(obj.getString("uname"));
				this.setBlanceValue(obj.getDouble("blanceValue"));
				this.setBlanceReValue(obj.getDouble("blanceReValue"));
				this.setBlanceZreValue(obj.getDouble("blanceZreValue"));
				this.setBlanceTakeValue(obj.getDouble("blanceTakeValue"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public Double getBlanceValue() {
		return blanceValue;
	}

	public void setBlanceValue(Double blanceValue) {
		this.blanceValue = blanceValue;
	}

	public Double getBlanceReValue() {
		return blanceReValue;
	}

	public void setBlanceReValue(Double blanceReValue) {
		this.blanceReValue = blanceReValue;
	}

	public Double getBlanceZreValue() {
		return blanceZreValue;
	}

	public void setBlanceZreValue(Double blanceZreValue) {
		this.blanceZreValue = blanceZreValue;
	}

	public Double getBlanceTakeValue() {
		return blanceTakeValue;
	}

	public void setBlanceTakeValue(Double blanceTakeValue) {
		this.blanceTakeValue = blanceTakeValue;
	}

	@Override
	public String toString() {
		return "BlanceQueryRespVo [blanceValue=" + blanceValue
				+ ", blanceReValue=" + blanceReValue + ", blanceZreValue="
				+ blanceZreValue + ", blanceTakeValue=" + blanceTakeValue + "]";
	}
	
	
}
