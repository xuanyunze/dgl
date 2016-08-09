package com.rxoa.zlpay.acty;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.DateUtil;
import com.rxoa.zlpay.base.util.PasswordUtil;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.combx.DigitPasswordKeyPad;
import com.rxoa.zlpay.combx.DigitPasswordKeyPadListener;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.BlanceTakemoneyReqVo;
import com.rxoa.zlpay.vo.BlanceTakemoneyRespVo;
import com.rxoa.zlpay.vo.OrderSignatureReqVo;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;

public class TakeMoneyActy extends BaseUIActivity implements OnClickListener,DigitPasswordKeyPadListener{
	private TextView tvValue;
	private TextView tvInfoArrivetime;
	private TextView tvInfoArrivevalue;
	private TextView tvInfoArrivebenefit;
	private TextView tvTakeleft;
	
	private CheckBox chxT0;
	private CheckBox chxT1;
	private CheckBox chxT7;
	private CheckBox chxT15;
	private CheckBox chxT30;
	
	private Button btnNext;
	private String valueString = "";
	private String takeLeft;
	private Double[] feeRate = {-0.0025,-0.0030,0.0,0.0014,0.0033,0.0075};//T0,D0,T1,T7,T15,T30
	private Double[] valueLimit = {100.00,100000.00};//MIN,MAX
	private Double benefitLimit = -2.0D;
	BlanceTakemoneyReqVo reqVo = new BlanceTakemoneyReqVo();
	
	private WindowManager windowmanager;
	private DigitPasswordKeyPad dpk;
	private View passwdview;
	private boolean isKeyboard = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_takemoney);
		setTitleText("提现");
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
		tvInfoArrivetime = (TextView) findViewById(R.id.takemoney_info_arrivetime);
		tvInfoArrivevalue = (TextView) findViewById(R.id.takemoney_info_arrivevalue);
		tvInfoArrivebenefit = (TextView) findViewById(R.id.takemoney_info_benefit);
		tvTakeleft = (TextView) findViewById(R.id.takemoney_takeleft);
		
		chxT0 = (CheckBox) findViewById(R.id.takemoney_bar_chx_t0);
		chxT1 = (CheckBox) findViewById(R.id.takemoney_bar_chx_t1);
		chxT7 = (CheckBox) findViewById(R.id.takemoney_bar_chx_t7);
		chxT15 = (CheckBox) findViewById(R.id.takemoney_bar_chx_t15);
		chxT30 = (CheckBox) findViewById(R.id.takemoney_bar_chx_t30);
		chxT0.setOnClickListener(this);
		chxT1.setOnClickListener(this);
		chxT7.setOnClickListener(this);
		chxT15.setOnClickListener(this);
		chxT30.setOnClickListener(this);
		
		tvValue = (EditText) findViewById(R.id.takemoney_value);
		btnNext = (Button) findViewById(R.id.takemoney_next);
		tvValue.setImeOptions(EditorInfo.IME_ACTION_DONE);
		tvValue.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		    @Override  
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
		        if (actionId == EditorInfo.IME_ACTION_DONE) {  
		        	InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
		        	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		        	if(StringUtil.isDbNull(tvValue.getText().toString().trim())){tvValue.setText("0.00");}
		    		valueString = StringUtil.toIsoMoney(StringUtil.format(tvValue.getText().toString().trim()));
		    		if(Double.parseDouble(StringUtil.format(takeLeft))<Double.parseDouble(StringUtil.toDotMoney(valueString))){
		    			valueString = StringUtil.format(takeLeft);
		    			showToast("您的提现余额不足");
		    		}
		    		if(Double.parseDouble(StringUtil.toDotMoney(valueString))<valueLimit[0]){
		    			showToast("最小提现金额不能小于"+valueLimit[0]);
		    			valueString = "0";
		    		}
		    		if(Double.parseDouble(StringUtil.toDotMoney(valueString))>valueLimit[1]){
		    			showToast("最大提现金额不能大于"+valueLimit[1]);
		    			valueString = "0";
		    		}
		    		tvValue.setText(StringUtil.toDotMoney(valueString));
		    		calValues(-1);
		            return true;    
		        }  
		        return false;  
		    } 
		}); 
		btnNext.setOnClickListener(this);
		Intent intent = getIntent();
		this.takeLeft = intent.getStringExtra("takeLeft");
		tvTakeleft.setText(this.takeLeft);
		tvInfoArrivetime.setText(DateUtil.getNowDateOnly());
	}
	
	@Override
	public void onClick(View vw) {
		int id = vw.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.takemoney_value){
			//showPassWdPadView();
			/*
			Intent intent = new Intent(TakeMoneyActy.this,MoneyInputCalActy.class);
			intent.putExtra("usetype", "takemoney");
			intent.putExtra("valuelimit", 0D);
			startActivityForResult(intent,1);
			*/
		}else if(id==R.id.takemoney_next){
			if(valueString.equals("")){
				showToast("请输入合法提现金额");return;
			}
			if(validCheckbox()==-1){
				showToast("请先选择到账类型");return;
			}
			checkUserStat();
		}else if(id==R.id.takemoney_bar_chx_t0){
			clickCheckbox(id);
		}else if(id==R.id.takemoney_bar_chx_t1){
			clickCheckbox(id);
		}else if(id==R.id.takemoney_bar_chx_t7){
			clickCheckbox(id);
		}else if(id==R.id.takemoney_bar_chx_t15){
			clickCheckbox(id);
		}else if(id==R.id.takemoney_bar_chx_t30){
			clickCheckbox(id);
		}
	}
	public void clickCheckbox(int id){
		chxT0.setChecked(false);
		chxT1.setChecked(false);
		chxT7.setChecked(false);
		chxT15.setChecked(false);
		chxT30.setChecked(false);
		if(id==R.id.takemoney_bar_chx_t0){
			chxT0.setChecked(true);
			calValues(0);
		}else if(id==R.id.takemoney_bar_chx_t1){
			chxT1.setChecked(true);
			calValues(1);
		}else if(id==R.id.takemoney_bar_chx_t7){
			chxT7.setChecked(true);
			calValues(7);
		}else if(id==R.id.takemoney_bar_chx_t15){
			chxT15.setChecked(true);
			calValues(15);
		}else if(id==R.id.takemoney_bar_chx_t30){
			chxT30.setChecked(true);
			calValues(30);
		}
	}
	public int validCheckbox(){
		if(chxT0.isChecked()) return 0;
		if(chxT1.isChecked()) return 1;
		if(chxT7.isChecked()) return 7;
		if(chxT15.isChecked()) return 15;
		if(chxT30.isChecked()) return 30;
		return -1;
	}
	public void calValues(int v){
		if(StringUtil.isDbNull(tvValue.getText().toString().trim())){tvValue.setText("0.00");}
		String tv = StringUtil.format(Double.parseDouble(tvValue.getText().toString().trim()));
		if(!this.valueString.equals(StringUtil.toIsoMoney(tv))){
			this.valueString = StringUtil.toIsoMoney(tv);
		}
		if(Double.parseDouble(StringUtil.format(this.takeLeft))<Double.parseDouble(StringUtil.toDotMoney(this.valueString))){
			this.valueString = StringUtil.format(this.takeLeft);
			showToast("您的提现余额不足");
		}
		if(v==-1){v=validCheckbox();}
		if(v==-1) return;
		Double rate = 0.0;
		Date ad = new Date();
		String arriveType = "";
		if(v==0){
			if(DateUtil.isT0Weekend(Calendar.getInstance())){
				rate = feeRate[1];
				arriveType = "D0";
			}else{
				rate = feeRate[0];
				arriveType = "T0";
			}
		}else if(v==1){
			rate = feeRate[2];
			int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
			int aj = 1;
			if(week==5){aj=3;}if(week==6){aj=2;}
			ad = DateUtil.adjust(ad, 3, aj);
			arriveType = "T1";
		}else if(v==7){
			rate = feeRate[3];
			ad = DateUtil.adjust(ad, 3, 7);
			arriveType = "T7";
		}else if(v==15){
			rate = feeRate[4];
			ad = DateUtil.adjust(ad, 3, 15);
			arriveType = "T15";
		}else if(v==30){
			rate = feeRate[5];
			ad = DateUtil.adjust(ad, 3, 30);
			arriveType = "T30";
		}
		tvInfoArrivetime.setText(DateUtil.getDateOnly(ad));
		System.out.println(DateUtil.getDateOnly(ad));
		Double bf = Double.parseDouble(StringUtil.toDotMoney(this.valueString))*rate;
		if(bf < 0.0D&&bf > benefitLimit){bf = benefitLimit;}
		tvInfoArrivevalue.setText((Double.parseDouble(StringUtil.toDotMoney(this.valueString))+bf)+"");
		tvInfoArrivebenefit.setText(StringUtil.format(bf));
		
		reqVo.setTakeValue(StringUtil.toDotMoney(this.valueString));
		reqVo.setArriveDate(DateUtil.getDateOnly(ad));
		reqVo.setArriveType(arriveType);
		reqVo.setArriveValue(tvInfoArrivevalue.getText().toString().trim());
		reqVo.setBenefitValue(bf+"");
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode==1&&resultCode==RESULT_OK){
			this.valueString = intent.getStringExtra("value");
			if(Double.parseDouble(StringUtil.format(this.takeLeft))<Double.parseDouble(StringUtil.toDotMoney(this.valueString))){
				this.valueString = StringUtil.format(this.takeLeft);
				showToast("您的提现余额不足");
			}
			tvValue.setText(StringUtil.toDotMoney(this.valueString));
			tvInfoArrivevalue.setText(StringUtil.toDotMoney(this.valueString));
		}
	}
	private void showPassWdPadView() {
		dpk = new DigitPasswordKeyPad(this);
		dpk.setLisenter(this);
        passwdview = dpk.setup(); 
        this.runOnUiThread(new Runnable() {  
            public void run() {  
                // 让一个视图浮动在你的应用程序之上  
                windowmanager = (WindowManager) TakeMoneyActy.this.getSystemService(Context.WINDOW_SERVICE);  
                LayoutParams layoutparams = new LayoutParams(-1, -1, LayoutParams.FIRST_SUB_WINDOW, LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.RGBA_8888);
                layoutparams.gravity = Gravity.BOTTOM;  
                
                passwdview.findViewById(R.id.transpwdpdpanel).getBackground().setAlpha(140);  
                windowmanager.addView(passwdview, layoutparams);
            }  
        });
        dpk.setKeyboadTip("输入提现密码");
	}

	@Override
	public void onInputFinished(String pwd) {
		reqVo.setTakePwd(PasswordUtil.webEncrypt(Config.Uid, pwd));
		resetButton(false);
		windowmanager.removeView(passwdview);
		doOnlineDeal();
	}
	public void resetButton(boolean value){
		if(value){
			btnNext.setClickable(true);
			btnNext.getBackground().setAlpha(255);
		}else{
			btnNext.setClickable(false);
			btnNext.getBackground().setAlpha(100);
		}
	}
	public void doOnlineDeal(){
		try{
			new DefAsyncTask(TakeMoneyActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在提交提现申请...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getBlanceRequest(TakeMoneyActy.this).doTakemoney(reqVo);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					BlanceTakemoneyRespVo respVo = (BlanceTakemoneyRespVo) parser.getRespObject();
					System.out.println("cpi=="+respVo.getRespCode());
					if(parser.getRespCode()==0){
						try{
							if(respVo.getRespCode()==0){
								showToast("提现申请提交成功");
								finish();
							}else{
								showToast(respVo.getRespMessage());
								resetButton(true);
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("提现申请提交失败");
						resetButton(true);
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private boolean checkKeyboard(){
		InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
		if(imm.hideSoftInputFromWindow(tvValue.getWindowToken(), 0)){
			return true;
		}
		return false;
	}
    public void checkUserStat(){
    	try{
    		final UserAccInfoReqVo reqVo = new UserAccInfoReqVo();
			new DefAsyncTask(TakeMoneyActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在校验用户信息");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					parser = ReqWrapper.getUserAccRequest(TakeMoneyActy.this).doQueryUserAcc(reqVo);
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						UserAccInfoRespVo respVo = (UserAccInfoRespVo) parser.getRespObject();
						String stat = respVo.getAccInfo().getUstat();
						String uicard = respVo.getAccInfo().getIdNo();
						BankCardEntity mcard = respVo.getMainCardInfo();
						if(!stat.equals("1")){
							showToast("请先激活账号");
							return;
						}
						if(uicard==null||uicard.equals("null")||uicard.equals("")){
							showToast("请先进行实名认证");
							return;
						}
						if(mcard==null||StringUtil.isDbNull(mcard.getAccNo())){
							showToast("请先绑定银行卡");
							return;
						}
						showPassWdPadView();
					}else{
						showToast("用户信息校验失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
}
