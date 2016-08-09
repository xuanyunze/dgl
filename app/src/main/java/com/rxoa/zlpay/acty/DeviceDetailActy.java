package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.entity.DeviceEntity;
import com.rxoa.zlpay.util.AvatorUtil;

public class DeviceDetailActy extends BaseUIActivity implements OnClickListener{
	private DeviceEntity device;
	
	private TextView tvSn;
	private TextView tvType;
	private TextView tvStat;
	private TextView tvBindTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		device = (DeviceEntity) intent.getSerializableExtra("device");
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_device_info);
		setTitleText(R.string.title_device_info);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		tvSn = (TextView) findViewById(R.id.deviceinfo_sn_tv);
		tvType = (TextView) findViewById(R.id.deviceinfo_type_tv);
		tvStat = (TextView) findViewById(R.id.deviceinfo_stat_tv);
		tvBindTime = (TextView) findViewById(R.id.deviceinfo_bindtime_tv);
		
		refreshViewUI();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
	}
	private void refreshViewUI(){
		try{
			tvSn.setText(device.getDeviceSn());
			tvType.setText(AvatorUtil.deviceTypeV2T(device.getDeviceType()));
			tvStat.setText(AvatorUtil.deviceStatV2T(device.getDeviceStat()));
			tvBindTime.setText(device.getBindDate());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
