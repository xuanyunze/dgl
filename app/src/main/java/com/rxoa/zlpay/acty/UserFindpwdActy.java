package com.rxoa.zlpay.acty;

import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.tool.ImageCode;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;

import com.rxoa.zlpay.util.ValidUtil;
import com.rxoa.zlpay.vo.SmsCodeReqVo;
import com.rxoa.zlpay.vo.SmsCodeRespVo;
import com.rxoa.zlpay.vo.UserFindpwdReqVo;
import com.rxoa.zlpay.vo.UserRegReqVo;
import com.rxoa.zlpay.vo.UserRegRespVo;

public class UserFindpwdActy extends BaseUIActivity implements OnClickListener{
	private AlertDialog dialog = null;
	private TextView edtvPhone;
	private TextView edtvPwd;
	private TextView edtvRePwd;
	private TextView edtvCode;
	private Button btnGetCode;
	private Button btnRegNow;
	private TimeCount time;
	private String vCode;
	private String reqId;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		time = new TimeCount(60000, 1000);
		initView();
		
	}
	private void initView(){
		setContentView(R.layout.acty_findpwd);
		setTitleText(R.string.title_findpwd);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);

		this.edtvPhone = ((TextView)findViewById(R.id.findpwd_edt_phone));
		this.edtvPwd = ((TextView)findViewById(R.id.findpwd_edt_pwd));
		this.edtvRePwd = ((TextView)findViewById(R.id.findpwd_edt_repwd));
		this.edtvCode = ((TextView)findViewById(R.id.findpwd_edt_code));
		this.btnGetCode = ((Button)findViewById(R.id.findpwd_btn_getcode));
		this.btnRegNow = ((Button)findViewById(R.id.btn_findpwd));

		this.btnGetCode.setOnClickListener(this);
		this.btnRegNow.setOnClickListener(this);
		
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		public void onFinish() {//计时完毕时触发
			btnGetCode.setText("重新验证");
			btnGetCode.setClickable(true);
		}
		public void onTick(long millisUntilFinished){//计时过程显示
			btnGetCode.setClickable(false);
			btnGetCode.setText(millisUntilFinished /1000+"秒");
		}
		}
	

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.btn_findpwd){
			doFindUserpwd();
		}else if(id==R.id.findpwd_btn_getcode){
			time.start();
			doGetVerCode();
			if (edtvPhone.getText().toString().length()<11) {
				Log.i("FF", "meizou");
			}
			
					
				
			//if(doPreCheck()){
				//showVcode(UserFindpwdActy.this);
			//}
		}
	}
	public boolean doPreCheck(){
		try{
			if(!ValidUtil.isEmpty(
					UserFindpwdActy.this, edtvPhone.getText().toString(), "手机号"))
			{Log.i("ss", "sdfsdf");
				return false;}
			if(!ValidUtil.isEmpty(
					UserFindpwdActy.this, edtvPwd.getText().toString(), "密码")) return false;
			if(!ValidUtil.isEmpty(
					UserFindpwdActy.this, edtvRePwd.getText().toString(), "确认密码")) return false;
			if(!ValidUtil.isEqual(
					UserFindpwdActy.this, edtvPwd.getText().toString(), edtvRePwd.getText().toString(),"密码","确认密码")) return false;
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public boolean doRegCheck(){
		if(!ValidUtil.isEmpty(
				UserFindpwdActy.this, edtvPhone.getText().toString(), "手机号")) return false;
		if(!ValidUtil.isEmpty(
				UserFindpwdActy.this, edtvPwd.getText().toString(), "密码")) return false;
		if(!ValidUtil.isEmpty(
				UserFindpwdActy.this, edtvRePwd.getText().toString(), "确认密码")) return false;
		if(!ValidUtil.isEmpty(
				UserFindpwdActy.this, edtvCode.getText().toString(), "验证码")) return false;
		if(!ValidUtil.isEqual(
				UserFindpwdActy.this, edtvPwd.getText().toString(), edtvRePwd.getText().toString(),"密码","确认密码")) return false;
		return true;
	}
	
	public void doFindUserpwd(){
		try{
			if(!doRegCheck()){return;}
			
			final UserFindpwdReqVo reqVo = new UserFindpwdReqVo();
			reqVo.setUserId(edtvPhone.getText().toString());
			reqVo.setUserPwd(edtvPwd.getText().toString());
			reqVo.setVrCode(edtvCode.getText().toString());
			reqVo.setReqId(reqId);
			new DefAsyncTask(this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在努力提交请求...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getUserRequest(UserFindpwdActy.this).doFindUserpwd(reqVo);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						showToast("密码重置成功，请登录");
						startActivity(new Intent(UserFindpwdActy.this,UserLoginActy.class));
					}else if(parser.getRespCode()==1){
						showToast("无效的用户");
					}else if(parser.getRespCode()==2){
						showToast("无效验证码");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void doGetVerCode(){
		try{
			if(!ValidUtil.isEmpty(
					UserFindpwdActy.this, edtvPhone.getText().toString(), "手机号")) return;
			
			final SmsCodeReqVo reqVo = new SmsCodeReqVo();
			reqVo.setReqType("1");
			reqVo.setPhoneNo(edtvPhone.getText().toString().trim());
			
			new DefAsyncTask(this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在获取验证码...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getSysRequest(UserFindpwdActy.this).doGetSmsCode(reqVo);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						SmsCodeRespVo respVo = (SmsCodeRespVo) parser.getRespObject();
						if(respVo.getRespCode()==0){
							reqId = respVo.getReqId();
							showToast("验证码已发送");
						}else{
							showToast("获取验证码失败");
						}
					}else if(parser.getRespCode()==1){
						showToast("获取太频繁了");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void showRegDialog(Context paramContext){
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
		localBuilder.setIcon(null);
		localBuilder.setMessage("用户注册成功");
	    localBuilder.setTitle("友情提示");
	    localBuilder.setPositiveButton("立即登录", new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		Intent localIntent = new Intent();
	    		localIntent.setClass(UserFindpwdActy.this, UserLoginActy.class);
	    		UserFindpwdActy.this.startActivity(localIntent);
	    		UserFindpwdActy.this.dialog.dismiss();
	    		UserFindpwdActy.this.finish();
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
	private Handler hanlder = new Handler(){
		
	};
	
	public void showVcode(Context context){
		LayoutInflater factory = LayoutInflater.from(UserFindpwdActy.this);//提示框  
        final View view = factory.inflate(R.layout.layout_vcode, null);//这里必须是final的  
        final EditText edit=(EditText)view.findViewById(R.id.layout_vcode_et);//获得输入
        final ImageView iv = (ImageView) view.findViewById(R.id.layout_vcode_img);
    	ImageCode imageCode = ImageCode.getInstance();
    	iv.setImageBitmap(imageCode.createBitmap());
    	this.vCode = imageCode.getCode();
		String msg = "请输入验证码";
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(null);
		builder.setTitle("输入验证码");
		builder.setView(view);
		builder.setPositiveButton("立即获取",null);
		builder.setNegativeButton("放弃",null);
		builder.setNeutralButton("更换一张", null);
		
		this.dialog = builder.create();
	    this.dialog.setCancelable(false);
	    this.dialog.setCanceledOnTouchOutside(false);
	    this.dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(edit.getText().toString().trim().equals(vCode)){
					doGetVerCode();
					dialog.dismiss();
				}else{
					showToast("验证码输入错误");
				}
			}
		});
		dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
		    	ImageCode imageCode = ImageCode.getInstance();
		    	iv.setImageBitmap(imageCode.createBitmap());
		    	vCode = imageCode.getCode();
			}
		});
		
	}
}
