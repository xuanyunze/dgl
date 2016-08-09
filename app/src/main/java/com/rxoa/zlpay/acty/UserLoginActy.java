package com.rxoa.zlpay.acty;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.async.DefAsyncTask;

import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.security.DataManager;
import com.rxoa.zlpay.util.ValidUtil;
import com.rxoa.zlpay.vo.UserLoginReqVo;
import com.rxoa.zlpay.vo.UserLoginRespVo;
import com.rxoa.zlpay.vo.UserSigninReqVo;
import com.rxoa.zlpay.vo.UserSigninRespVo;

public class UserLoginActy extends BaseUIActivity implements OnClickListener{
	private Dialog dialog = null;
	private TextView edtvPhone;
	private TextView edtvPwd;
	private TextView edtvCode;
	private Button btnGetCode;
	private Button btnLoginNow;
	private TextView tvGoFastLogin;
	
	private TextView tvRempwd;
	private TextView tvLostpwd;
	private CheckBox chxRmpwd;
	private SharedPreferences sharedPreferences;
	private String upwd = "";
	private String uid = "";
	
	public static void launch(Context context,Intent intent){
		if(intent == null){
			intent = new Intent(context,UserLoginActy.class);
		}
		context.startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_login);
		setTitleText(R.string.title_login);
		setLeftButtonText(R.string.btn_return_text);
		setRightButtonText(R.string.btn_reg_text);
		getLeftButton().setOnClickListener(this);
		getRightButton().setOnClickListener(this);
		
		this.edtvPhone = ((TextView)findViewById(R.id.login_edt_phone));
		this.edtvPwd = ((TextView)findViewById(R.id.login_edt_pwd));
		this.btnLoginNow = ((Button)findViewById(R.id.btn_login));
		this.btnLoginNow.setOnClickListener(this);
		//this.tvGoFastLogin = ((TextView)findViewById(R.id.login_btn_fastlogin));
		//this.btnGetCode.setOnClickListener(this);
		//this.tvGoFastLogin.setOnClickListener(this);
		
		this.tvRempwd = ((TextView)findViewById(R.id.login_tv_rempwd));
		this.tvLostpwd = ((TextView)findViewById(R.id.login_tv_lostpwd));
		this.chxRmpwd = (CheckBox) findViewById(R.id.login_chx_rempwd);
		this.tvRempwd.setOnClickListener(this);
		this.tvLostpwd.setOnClickListener(this);
		this.chxRmpwd.setOnClickListener(this);
		
		if(sharedPreferences.getBoolean("IS_REMPWD", false)){
			this.edtvPhone.setText(sharedPreferences.getString("REM_UID", ""));
			this.edtvPwd.setText(sharedPreferences.getString("REM_PWD", ""));
			this.chxRmpwd.setChecked(true);
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.titleRightButton){
			Intent intent = new Intent(UserLoginActy.this,UserRegProtActy.class);
			startActivity(intent);
		}else if(id==R.id.btn_getcode){
			
		}else if(id==R.id.btn_login){
			doUserLogin();
			
			
		}else if(id==R.id.login_tv_rempwd){
			if(chxRmpwd.isChecked()){chxRmpwd.setChecked(false);}else{chxRmpwd.setChecked(true);}	
		}else if(id==R.id.login_tv_lostpwd){
			Intent intent = new Intent(UserLoginActy.this,UserFindpwdActy.class);
			startActivity(intent);
		}
	}
	
	public boolean doLoginCheck(){
		if(!ValidUtil.isEmpty(
				UserLoginActy.this, edtvPhone.getText().toString(), "手机号")) return false;
		if(!ValidUtil.isEmpty(
				UserLoginActy.this, edtvPwd.getText().toString(), "密码")) return false;
		return true;
	}

	
	public void doUserLogin(){
		try{
			if(!doLoginCheck()){return;}
			
			final UserLoginReqVo reqVo = new UserLoginReqVo();
			reqVo.setUid(edtvPhone.getText().toString());
			reqVo.setUpwd(edtvPwd.getText().toString());
			uid = reqVo.getUid();
			upwd = reqVo.getUpwd();
			new DefAsyncTask(this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在努力提交...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getUserRequest(UserLoginActy.this).doUserLogin(reqVo);
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
							UserLoginRespVo respVo = (UserLoginRespVo) parser.getRespObject();
							setRmpwd();
							DataManager.doLoginSet(respVo);
							doUserSignin();
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("用户名或密码不正确");
					}
				}
				
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setRmpwd(){
		if(chxRmpwd.isChecked()){
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("IS_REMPWD", true);
			editor.putString("REM_UID", uid);
			editor.putString("REM_PWD", upwd);
			editor.commit();
		}
	}
	public void doUserSignin(){
		try{			
			final UserSigninReqVo reqVo = new UserSigninReqVo();
			
			new DefAsyncTask(this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在进行签到...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getUserRequest(UserLoginActy.this).doUserSignin(reqVo);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						UserSigninRespVo respVo = (UserSigninRespVo) parser.getRespObject();
						DataManager.doSigninSet(respVo);
					}else if(parser.getRespCode()==1){
						showToast("签到失败，请重新签到");
					}
					Intent intent = new Intent(UserLoginActy.this,MainHomeActy.class);
					startActivity(intent);
					finish();
				}
				
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void showLoginDialog(Context paramContext){
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
		localBuilder.setIcon(null);
		localBuilder.setMessage(null);
		localBuilder.setTitle("友情提示");
		localBuilder.setPositiveButton("立即登录", new DialogInterface.OnClickListener(){
		    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
		    	Intent localIntent = new Intent();
		    	localIntent.setClass(UserLoginActy.this, UserLoginActy.class);
		    	UserLoginActy.this.startActivity(localIntent);
		    	UserLoginActy.this.dialog.dismiss();
		   		UserLoginActy.this.finish();
		   	}
		});
	    localBuilder.setNegativeButton("前往实名认证", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
		    		
		    }
		});
		this.dialog = localBuilder.create();
		this.dialog.setCancelable(false);
		this.dialog.setCanceledOnTouchOutside(false);
		this.dialog.show();
	}
	
}
