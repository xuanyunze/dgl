package com.rxoa.zlpay.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvArriveStat;
import com.rxoa.zlpay.avator.AvFeeType;
import com.rxoa.zlpay.avator.AvOrderStat;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.base.BaseAdapter;
import com.rxoa.zlpay.entity.OrderItemEntity;

public class OrderListAdapter extends BaseAdapter{
	
	private List<OrderItemEntity> datas = null;
	private LayoutInflater inflater = null;
	private Context context;

	public OrderListAdapter(Context context, List<OrderItemEntity> datas) {
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}
	public void setDatas(List<OrderItemEntity> datas) {
		this.datas = datas;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		try{
			// TODO Auto-generated method stub
			ViewHolder mViewHolder = null;
			if (null == arg1) {
				mViewHolder = new ViewHolder();
				arg1 = inflater.inflate(R.layout.order_list_item, null);
				mViewHolder.ivOrderIcon = (ImageView) arg1.findViewById(R.id.order_item_icon);
				mViewHolder.tvOrderFlowid = (TextView) arg1.findViewById(R.id.order_item_flowid);
				mViewHolder.tvOrderDate = (TextView) arg1.findViewById(R.id.order_item_date);
				mViewHolder.tvOrderType = (TextView) arg1.findViewById(R.id.order_item_type);
				mViewHolder.tvOrderValue = (TextView) arg1.findViewById(R.id.order_item_value);
				mViewHolder.tvOrderStat = (TextView) arg1.findViewById(R.id.order_item_stat);
				arg1.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) arg1.getTag();
			}
			OrderItemEntity item = (OrderItemEntity) getItem(arg0);
			mViewHolder.tvOrderDate.setText(item.getOrderDate());
			mViewHolder.tvOrderFlowid.setText(item.getOrderFlowid());
			mViewHolder.tvOrderValue.setText(item.getOrderValue());
			if(item.getOrderType().equals("1")||item.getOrderType().equals("8")){
				mViewHolder.tvOrderStat.setText(AvOrderStat.value2text(item.getOrderStat()));
				mViewHolder.tvOrderType.setText(AvOrderType.value2text(item.getOrderType())+"["+AvFeeType.value2text(item.getOrderSubtype())+"]");
			}else if(item.getOrderType().equals("2")){
				mViewHolder.tvOrderStat.setText(AvArriveStat.value2text(item.getOrderStat()));
				mViewHolder.tvOrderType.setText(AvOrderType.value2text(item.getOrderType())+"["+item.getOrderSubtype()+"]");
			}

			mViewHolder.ivOrderIcon.setImageResource(toImgSrc(item.getOrderType()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return arg1;
	}
	static class ViewHolder {
		ImageView ivOrderIcon;
		TextView tvOrderType;
		TextView tvOrderFlowid;
		TextView tvOrderDate;
		TextView tvOrderValue;
		TextView tvOrderStat;
	}

	public int toImgSrc(String type){
		int res = R.drawable.orderlist_icon_receivemoney;
		if(type.equals("0")){
			res = R.drawable.orderlist_icon_receivemoney;
		}else if(type.equals("1")){
			res = R.drawable.orderlist_icon_acccharge;
		}else if(type.equals("2")){
			res = R.drawable.orderlist_icon_takemoney;
		}else if(type.equals("3")){
			res = R.drawable.orderlist_icon_transacc;
		}else if(type.equals("4")){
			res = R.drawable.orderlist_icon_repaycredit;
		}else if(type.equals("5")){
			res = R.drawable.orderlist_icon_phonecharge;
		}
		return res;
	}
}
