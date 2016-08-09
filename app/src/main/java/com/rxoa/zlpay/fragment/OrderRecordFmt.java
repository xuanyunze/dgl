package com.rxoa.zlpay.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

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
import com.rxoa.zlpay.combx.xlistview.XListView;
import com.rxoa.zlpay.combx.xlistview.XListView.IXListViewListener;
import com.rxoa.zlpay.entity.OrderItemEntity;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.OrderlistReqVo;
import com.rxoa.zlpay.vo.OrderlistRespVo;

public class OrderRecordFmt extends BaseUIFragment implements OnClickListener,IXListViewListener{
	private MainHomeActy mActivity = null;
	private XListView listview;
    private OrderListAdapter adapter;
    private List<OrderItemEntity> orders;
    private int actionType = 0;
    private int pageCount = 20;
    private int page = 1;
    private int startIndex = 0;
	private TextView tvReceive;
	private TextView tvTake;
	private int oType = 9;
	private int conMax = 200;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle bundle) {
		View resView = inflater.inflate(R.layout.fmt_home_order, container, false);
		mActivity = (MainHomeActy) getActivity();
		return initView(resView);
	}
	
    public View initView(View view){
    	try{
    		setTitleText(view,R.string.title_order);
    		listview = (XListView) view.findViewById(R.id.order_list);
    		listview.setXListViewListener(this);
    		listview.setPullRefreshEnable(true);
    		listview.setPullLoadEnable(true);
    		tvReceive = (TextView)view.findViewById(R.id.olist_cate_receive);
    		tvTake = (TextView)view.findViewById(R.id.olist_cate_take);
    		tvReceive.setOnClickListener(this);
    		tvTake.setOnClickListener(this);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return view;
    }

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.olist_cate_receive){
			tvReceive.setBackgroundColor(Color.parseColor("#ff3a9ee6"));
			tvReceive.setTextColor(Color.parseColor("#eefefefe"));
			tvTake.setBackgroundColor(Color.parseColor("#00000000"));
			tvTake.setTextColor(Color.parseColor("#cc999999"));
			if(oType!=1){
				oType = 9;
				loadOrderList();
			}
		}else if(id==R.id.olist_cate_take){
			tvTake.setBackgroundColor(Color.parseColor("#ff3a9ee6"));
			tvTake.setTextColor(Color.parseColor("#eefefefe"));
			tvReceive.setBackgroundColor(Color.parseColor("#00000000"));
			tvReceive.setTextColor(Color.parseColor("#cc999999"));
			if(oType!=2){
				oType = 11;
				loadOrderList();
			}
		}
	}
	@Override 
	public void setUserVisibleHint(boolean isVisibleToUser){
		super.setUserVisibleHint(isVisibleToUser); 
	    if (isVisibleToUser) { 
	    	loadOrderList();
	    } else { 
	    	//相当于Fragment的onPause 
	    } 
	 }
	public void loadOrderList(){
		try{
			if(!Config._isLogin){return;}
			if(actionType==0){startIndex = 0;}else{startIndex+=pageCount;}
			final OrderlistReqVo reqVo = new OrderlistReqVo();
			reqVo.setStartIndex(startIndex+"");
			reqVo.setMaxCount(pageCount+"");
			reqVo.setOrderType(oType+"");
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
							List<OrderItemEntity> morders = ((OrderlistRespVo)parser.getRespObject()).getItems();
							if(actionType==0){
								orders = morders;
								if(morders==null||morders.size()==0){
									showToast("没有订单数据");
								}
							}else{
								if(morders==null||morders.size()==0){
									showToast("没有更多订单数据了");
								}else if(orders.size()<conMax){
									orders.addAll(morders);
								}else{
									orders = morders;
								}
							}
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
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override  
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
			long arg3) {
				if(arg2>orders.size()){
					return;
				}
				arg2 = arg2-1;
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
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
		if(actionType==1){
			listview.setSelection(listview.getBottom());
		}
	}

	@Override
	public void onRefresh() {
		actionType=0;
		loadOrderList();
	}

	@Override
	public void onLoadMore() {
		actionType=1;
		loadOrderList();
	}
	
}
