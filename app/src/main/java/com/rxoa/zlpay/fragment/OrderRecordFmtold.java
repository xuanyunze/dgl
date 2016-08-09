package com.rxoa.zlpay.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.rxoa.zlpay.BaseUIFragment;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.acty.MainHomeActy;
import com.rxoa.zlpay.acty.OrderConfirmActy;
import com.rxoa.zlpay.acty.OrderDetailActy;
import com.rxoa.zlpay.acty.OrderSignatureActy;
import com.rxoa.zlpay.acty.OrderTakemoneyDetailActy;

import com.rxoa.zlpay.adapter.OrderListAdapter;
import com.rxoa.zlpay.avator.AvOrderStat;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.entity.OrderItemEntity;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.OrderlistReqVo;
import com.rxoa.zlpay.vo.OrderlistRespVo;

public class OrderRecordFmtold extends BaseUIFragment implements OnClickListener{
	private MainHomeActy mActivity = null;
    private ListView listView;
    private OrderListAdapter adapter;
    private List<OrderItemEntity> orders;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		View resView = inflater.inflate(R.layout.fmt_home_order, container, false);
		mActivity = (MainHomeActy) getActivity();
		return initView(resView);
	}
	
    public View initView(View view){
    	try{
    		setTitleText(view,R.string.title_order);
    		listView = (ListView) view.findViewById(R.id.order_list);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return view;
    }

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==1){
			
		}
	}
	@Override 
	public void setUserVisibleHint(boolean isVisibleToUser){
		super.setUserVisibleHint(isVisibleToUser); 
	    if (isVisibleToUser) { 
	    	syncOrderList();
	    } else { 
	    	//相当于Fragment的onPause 
	    } 
	 }
	public void syncOrderList(){
		try{
			if(!Config._isLogin){return;}
			final OrderlistReqVo reqVo = new OrderlistReqVo();
			mActivity = (MainHomeActy) this.getActivity();
			new DefAsyncTask(this.getActivity()){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在加载订单数据...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getOrderRequest(getActivity()).doQueryOrderlist(reqVo);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						try{
							orders = ((OrderlistRespVo)parser.getRespObject()).getItems();
							refreshViewUI();
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("订单加载失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//======异步获取订单列表信息后显示列表信息=====
	private void refreshViewUI(){
		adapter = new OrderListAdapter(getActivity(),orders);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override  
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
			long arg3) {  
				if(arg2>=orders.size()){
					return;
				}	
				Intent intent = null;
				if(Integer.parseInt(orders.get(arg2).getOrderType())==2){
					intent = new Intent(getActivity(),OrderTakemoneyDetailActy.class);
				}else{
					intent = new Intent(getActivity(),OrderDetailActy.class);
				}
				
				intent.putExtra("orderid", orders.get(arg2).getOrderFlowid());
				intent.putExtra("ordertype", orders.get(arg2).getOrderType());
				getActivity().startActivity(intent);
			}  
		});
	}
}
