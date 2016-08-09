package com.rxoa.zlpay.vo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rxoa.zlpay.entity.OrderItemEntity;

public class OrderlistRespVo extends BaseRespVo{
	private static final long serialVersionUID = 1L;
	
	private List<OrderItemEntity> items;
	
	public static OrderlistRespVo getInstance(){
		return new OrderlistRespVo();
	}
	
	public OrderlistRespVo initFromJson(String json){
		try{System.out.println(json);
			if(json!=null&&!json.equals("null")||json.equals("")){
				JSONObject obj = new JSONObject(json);
				JSONArray array = new JSONArray(obj.getString("items"));
				if(this.items==null){
					items = new ArrayList<OrderItemEntity>();
				}
				for(int i=0;i<array.length();i++){
					OrderItemEntity item = new OrderItemEntity();
					item.initFromJson(array.getString(i));
					items.add(item);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}

	public List<OrderItemEntity> getItems() {
		return items;
	}
	public void setItems(List<OrderItemEntity> items) {
		this.items = items;
	}
}
