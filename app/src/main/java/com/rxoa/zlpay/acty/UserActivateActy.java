package com.rxoa.zlpay.acty;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.acty.ReadCardActy.RadioOnClick;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.MD5Util;
import com.rxoa.zlpay.base.util.PasswordUtil;
import com.rxoa.zlpay.device.DeviceManager;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.util.AvatorUtil;
import com.rxoa.zlpay.vo.BindDeviceReqVo;
import com.rxoa.zlpay.vo.UserActivateReqVo;

public class UserActivateActy extends BaseUIActivity implements OnClickListener{
	//private String[] valuetype = new String[]{"使用激活码","使用设备SN","连接刷卡设备"};
	//private boolean[] valuestat=new boolean[]{true, false,false};  
	private String[] valuetype = new String[]{"使用激活码","使用设备SN"};
	private boolean[] valuestat=new boolean[]{true, false};  
	private RadioOnClick radioOnClick = new RadioOnClick(0);
	private ListView lvValuetype;
	
	private LinearLayout barActivateType;
	private TextView tvActivateType;
	private Button btnGo;
	
	private LinearLayout barCode;
	private LinearLayout barSn;
	private EditText edtvCode;
	private EditText edtvSn;
	private EditText edtvPwd;
	private EditText edtvRePwd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_useractivate);
		setTitleText(R.string.title_activate);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		barActivateType = (LinearLayout) findViewById(R.id.useractivate_bar_type);
		tvActivateType = (TextView) findViewById(R.id.useractivate_tv_type);
		barCode = (LinearLayout) findViewById(R.id.useractivate_bar_code);
		barSn = (LinearLayout) findViewById(R.id.useractivate_bar_sn);
		edtvCode = (EditText) findViewById(R.id.useractivate_tv_code);
		edtvSn = (EditText) findViewById(R.id.useractivate_tv_sn);
		edtvPwd = (EditText) findViewById(R.id.useractivate_tv_paypwd);
		edtvRePwd = (EditText) findViewById(R.id.useractivate_tv_repaypwd);
		btnGo = (Button) findViewById(R.id.useractivate_btngo);
		
		barActivateType.setOnClickListener(new RadioClickListener());
		btnGo.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.useractivate_btngo){
			doUserActivate();
		}
	}
	public boolean doDataCheck(){
		if(tvActivateType.getText().toString().trim().equals("")){
			showToast("请选择激活方式");return false;
		}
		if(tvActivateType.getText().toString().equals("使用激活码")){
			if(edtvCode.getText().toString().trim().equals("")){
				showToast("请填写激活码");return false;
			}
		}else if(tvActivateType.getText().toString().equals("使用设备SN")||tvActivateType.getText().toString().equals("连接刷卡设备")){
			if(edtvSn.getText().toString().trim().equals("")){
				showToast("请填写设备SN");return false;
			}
		}
		
		if(edtvPwd.getText().toString().trim().equals("")){
			showToast("请填写支付密码");return false;
		}else if(!edtvPwd.getText().toString().equals(edtvRePwd.getText().toString())){
			showToast("两次支付支付密码不一致");return false;
		}
		return true;
	}
	public void doUserActivate(){
		try{
			if(!doDataCheck()){return;}
			final UserActivateReqVo reqVo = new UserActivateReqVo();
			String mtype = tvActivateType.getText().toString();
			if(mtype.equals("使用激活码")){
				mtype = "0";
			}else{
				mtype = "1";
			}
			reqVo.setActiType(mtype);
			reqVo.setActiSn(AvatorUtil.dealNull(edtvSn.getText().toString()));
			reqVo.setActiCode(AvatorUtil.dealNull(edtvCode.getText().toString()));
			reqVo.setPayPwd(PasswordUtil.webEncrypt(Config.Uid, edtvPwd.getText().toString().trim()));
			
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
					parser = ReqWrapper.getUserAccRequest(UserActivateActy.this).doUserActivate(reqVo);
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						showToast("账号已成功激活");
						finish();
					}else if(parser.getRespCode()==1){
						showToast("无效的激活码");
					}else if(parser.getRespCode()==2){
							showToast("请不要重复激活账号");
					}else{
						showToast("您的操作太频繁了");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void showSelectRadio(){
		AlertDialog ad = new AlertDialog.Builder(UserActivateActy.this).setTitle("选择设备类型")
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
			tvActivateType.setText(valuetype[index]);
			if(valuetype[index].equals("使用激活码")){
				barSn.setVisibility(View.GONE);
				barCode.setVisibility(View.VISIBLE);
			}else if(valuetype[index].equals("连接刷卡设备")){
				barCode.setVisibility(View.GONE);
				barSn.setVisibility(View.VISIBLE);
				Intent intent = new Intent(UserActivateActy.this,ReadCardActy.class);
				startActivity(intent);
			}else if(valuetype[index].equals("使用设备SN")){
				barCode.setVisibility(View.GONE);
				barSn.setVisibility(View.VISIBLE);
			}
			dialog.dismiss();
		}
	}
}
