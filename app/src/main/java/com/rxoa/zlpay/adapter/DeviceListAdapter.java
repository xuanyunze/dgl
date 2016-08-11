package com.rxoa.zlpay.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rxoa.zlpay.MainApplication;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.BaseAdapter;
import com.rxoa.zlpay.entity.DeviceEntity;

public class DeviceListAdapter extends BaseAdapter{
	public static final String TAG = DeviceListAdapter.class.getName();
	
	private List<DeviceEntity> datas = null;
	private LayoutInflater inflater = null;
	private Context context;

	public DeviceListAdapter(Context context, List<DeviceEntity> datas) {
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}
	public void setDatas(List<DeviceEntity> datas) {
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
				arg1 = inflater.inflate(R.layout.accmanage_device_item, null);
				mViewHolder.tvDeviceSn = (TextView)arg1.findViewById(R.id.accmanage_device_item_sn);
				mViewHolder.tvDeviceType = (TextView)arg1.findViewById(R.id.accmanage_device_item_type);
				mViewHolder.tvDeviceStat = (TextView)arg1.findViewById(R.id.accmanage_device_item_stat);
				arg1.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) arg1.getTag();
			}
			DeviceEntity item = (DeviceEntity) getItem(arg0);
			Log.e(TAG, "DeviceEntity-sn:" + item.getDeviceSn());
			MainApplication instance = MainApplication.getInstance();
			instance.setDeviceEntity(item.getDeviceSn());
			Intent intent = new Intent("devicesn" + item.getDeviceSn());

			mViewHolder.tvDeviceSn.setText(item.getDeviceSn());
			mViewHolder.tvDeviceType.setText(typeToText(item.getDeviceType()));
			mViewHolder.tvDeviceStat.setText(statToText(item.getDeviceStat()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return arg1;
	}
	public String typeToText(String type){
		if(type.equals("0")){
			return "耳机接口设备";
		}else if(type.equals("1")){
			return "蓝牙设备";
		}
		return "未知";
	}
	public String statToText(String stat){
		if(stat.equals("0")){
			return "未分配";
		}else if(stat.equals("1")){
			return "正常";
		}
		return "未知";
	}
	static class ViewHolder {
		TextView tvDeviceSn;
		TextView tvDeviceType;
		TextView tvDeviceStat;
	}
}
