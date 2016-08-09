package com.rxoa.zlpay.acty;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.device.DeviceManager;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.util.AvatorUtil;
import com.rxoa.zlpay.vo.BindDeviceReqVo;

public class BindDeviceActy extends BaseUIActivity implements OnClickListener{
	private String[] valuetype = new String[]{"耳机接口设备","蓝牙设备"};
	private boolean[] valuestat=new boolean[]{true, false};  
	private RadioOnClick radioOnClick = new RadioOnClick(0);
	private ListView lvValuetype;
	
	private LinearLayout barType;
	private TextView tvType;
	private EditText edtvSn;
	private Button btnGo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_bind_device);
		setTitleText(R.string.title_bind_device);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		barType = (LinearLayout) findViewById(R.id.binddevice_bar_type);
		tvType = (TextView) findViewById(R.id.binddevice_tv_type);
		edtvSn = (EditText) findViewById(R.id.binddevice_edtv_sn);
		btnGo = (Button) findViewById(R.id.binddevice_btngo);
		barType.setOnClickListener(new RadioClickListener());
		btnGo.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.binddevice_btngo){
			doBindDevice();
		}
	}
	public boolean doDataCheck(){
		if(tvType.getText().toString().trim().equals("")){
			showToast("请选择设备类型");return false;
		}
		if(edtvSn.getText().toString().trim().equals("")){
			showToast("请填写设备SN");return false;
		}
		return true;
	}
	public void doBindDevice(){
		try{
			if(!doDataCheck()){return;}
			final BindDeviceReqVo reqVo = new BindDeviceReqVo();
			reqVo.setDeviceType(AvatorUtil.deviceTypeT2V(tvType.getText().toString()));
			reqVo.setDeviceSn(edtvSn.getText().toString());
			
			new DefAsyncTask(this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在提交数据，请稍等");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					parser = ReqWrapper.getUserAccRequest(BindDeviceActy.this).doBindDevice(reqVo);
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						showToast("提交成功，请耐心等待审核");
						finish();
					}else if(parser.getRespCode()==1){
						showToast("上传出错了");
					}else{
						showToast("您的操作太频繁了哦");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void showSelectRadio(){
		AlertDialog ad = new AlertDialog.Builder(BindDeviceActy.this).setTitle("选择设备类型")
				.setSingleChoiceItems(valuetype,radioOnClick.getIndex(),radioOnClick).create();  
		lvValuetype = ad.getListView();  
		ad.show();
	}
	class RadioClickListener implements OnClickListener {  
		@Override  
		public void onClick(View v) {  
			showSelectRadio();
		}
	}
	class RadioOnClick implements DialogInterface.OnClickListener{  
		private int index;  
		public RadioOnClick(int index){  
			this.index = index;  
		}  
		public void setIndex(int index){  
			this.index=index;  
		}  
		public int getIndex(){  
			return index;  
		}
		@Override
		public void onClick(DialogInterface dialog, int whichButton) {
			// TODO Auto-generated method stub
			setIndex(whichButton);
			tvType.setText(valuetype[index]);
			if(valuetype[index].equals("耳机接口设备")){
				
			}else if(valuetype[index].equals("蓝牙设备")){
				
			}
			dialog.dismiss();
		}
	}
}
