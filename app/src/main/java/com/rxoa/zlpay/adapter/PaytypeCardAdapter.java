package com.rxoa.zlpay.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.BaseAdapter;
import com.rxoa.zlpay.entity.BankCardEntity;

public class PaytypeCardAdapter extends BaseAdapter{
	
	private List<BankCardEntity> datas = null;
	private LayoutInflater inflater = null;
	private Context context;

	public PaytypeCardAdapter(Context context, List<BankCardEntity> datas){
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}
	public void setDatas(List<BankCardEntity> datas) {
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
			ViewHolder mViewHolder = null;
			if (null == arg1) {
				mViewHolder = new ViewHolder();
				arg1 = inflater.inflate(R.layout.paytype_bankcard_item, null);
				mViewHolder.tvCardAccName = (TextView)arg1.findViewById(R.id.paytype_item_accname);
				mViewHolder.tvCardAccNo = (TextView)arg1.findViewById(R.id.paytype_item_accno);
				arg1.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) arg1.getTag();
			}
			BankCardEntity item = (BankCardEntity) getItem(arg0);
			mViewHolder.tvCardAccName.setText(item.getAccName()+"("+item.getAccBank()+")");
			mViewHolder.tvCardAccNo.setText(item.getAccNo());
		}catch(Exception e){
			e.printStackTrace();
		}
		return arg1;
	}
	static class ViewHolder {
		TextView tvCardAccName;
		TextView tvCardAccNo;
	}
}
