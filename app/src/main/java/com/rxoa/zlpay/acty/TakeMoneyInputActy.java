package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.avator.AvPayType;
import com.rxoa.zlpay.base.util.RegExUtil;
import com.rxoa.zlpay.entity.OrderPhoneCharge;
import com.rxoa.zlpay.entity.OrderTakeMoney;
import com.rxoa.zlpay.entity.OrderWrapper;

public class TakeMoneyInputActy extends BaseUIActivity implements OnClickListener{
	private CheckBox chxReAcc;
	private CheckBox chxFrAcc;
	private CheckBox chxChAcc;
	private CheckBox chxArriveToday;
	private CheckBox chxArriveTomorrow;
	
	private LinearLayout barReAcc;
	private LinearLayout barFrAcc;
	private LinearLayout barChAcc;
	private LinearLayout barArriveToday;
	private LinearLayout barArriveTomorrow;
	
	private LinearLayout infoBarReAcc;
	private LinearLayout infoBarFrAcc;
	private LinearLayout infoBarChAcc;
	private LinearLayout infoBarArriveToday;
	private LinearLayout infoBarArriveTomorrow;
	
	private TextView infoTvReAcc;
	private TextView infoTvFrAcc;
	private TextView infoTvChAcc;
	private TextView infoTvArriveToday;
	private TextView infoTvArriveTomorrow;
	
	private EditText edtvValue;
	private Button btnNext;
	
	private boolean[] canUseStat = {true,true,false,false,false,true};
	private String[] blanceValue = {"3000.00","100.00","8.00"};
	private String[] chxHtml = {"","",""};
	private String takeAccount;
	private String takeType;
	private String takeValue;
	private String leftValue;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initView();
	}
	private void initView(){
		setContentView(R.layout.acty_takemoney_select);
		setTitleText(R.string.title_takemoney);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		chxReAcc = (CheckBox) findViewById(R.id.takemoney_select_chx_reacc);
		chxFrAcc = (CheckBox) findViewById(R.id.takemoney_select_chx_fracc);
		chxChAcc = (CheckBox) findViewById(R.id.takemoney_select_chx_chacc);
		chxArriveToday = (CheckBox) findViewById(R.id.takemoney_select_chx_t0);
		chxArriveTomorrow = (CheckBox) findViewById(R.id.takemoney_select_chx_t1);
		
		barReAcc = (LinearLayout) findViewById(R.id.takemoney_select_bar_reacc);
		barFrAcc = (LinearLayout) findViewById(R.id.takemoney_select_bar_fracc);
		barChAcc = (LinearLayout) findViewById(R.id.takemoney_select_bar_chacc);
		barArriveToday = (LinearLayout) findViewById(R.id.takemoney_select_bar_t0);
		barArriveTomorrow = (LinearLayout) findViewById(R.id.takemoney_select_bar_t1);
		
		infoBarReAcc = (LinearLayout) findViewById(R.id.takemoney_select_infobar_reacc);
		infoBarFrAcc = (LinearLayout) findViewById(R.id.takemoney_select_infobar_fracc);
		infoBarChAcc = (LinearLayout) findViewById(R.id.takemoney_select_infobar_chacc);
		infoBarArriveToday = (LinearLayout) findViewById(R.id.takemoney_select_infobar_t0);
		infoBarArriveTomorrow = (LinearLayout) findViewById(R.id.takemoney_select_infobar_t1);
		
		infoTvReAcc = (TextView) findViewById(R.id.takemoney_select_infotv_reacc);
		infoTvFrAcc = (TextView) findViewById(R.id.takemoney_select_infotv_fracc);
		infoTvChAcc = (TextView) findViewById(R.id.takemoney_select_infotv_chacc);
		infoTvArriveToday = (TextView) findViewById(R.id.takemoney_select_infotv_t0);
		infoTvArriveTomorrow = (TextView) findViewById(R.id.takemoney_select_infotv_t1);
		
		edtvValue = (EditText) findViewById(R.id.takemoney_select_tv_value);
		btnNext = (Button) findViewById(R.id.takemoney_select_btnnext);
		
		chxReAcc.setOnClickListener(this);
		chxFrAcc.setOnClickListener(this);
		chxChAcc.setOnClickListener(this);
		chxArriveToday.setOnClickListener(this);
		chxArriveTomorrow.setOnClickListener(this);
		
		btnNext.setOnClickListener(this);
		setButtonStat();
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.takemoney_select_chx_reacc){
			resetAllChx(chxReAcc);
			if(canUseStat[0]==false){
				chxArriveToday.setEnabled(false);
				chxArriveToday.setTextColor(Color.parseColor("#ffcccccc"));
			}else if(canUseStat[1]==false){
				chxArriveTomorrow.setEnabled(false);
				chxArriveTomorrow.setTextColor(Color.parseColor("#ffcccccc"));
			}
		}else if(id==R.id.takemoney_select_chx_fracc){
			resetAllChx(chxFrAcc);
			if(canUseStat[2]==false){
				chxArriveToday.setEnabled(false);
				chxArriveToday.setTextColor(Color.parseColor("#ffcccccc"));
			}else if(canUseStat[3]==false){
				chxArriveTomorrow.setEnabled(false);
				chxArriveTomorrow.setTextColor(Color.parseColor("#ffcccccc"));
			}
		}else if(id==R.id.takemoney_select_chx_chacc){
			resetAllChx(chxChAcc);
			if(canUseStat[4]==false){
				chxArriveToday.setEnabled(false);
				chxArriveToday.setTextColor(Color.parseColor("#ffcccccc"));
			}else if(canUseStat[5]==false){
				chxArriveTomorrow.setEnabled(false);
				chxArriveTomorrow.setTextColor(Color.parseColor("#ffcccccc"));
			}
		}else if(id==R.id.takemoney_select_chx_t0){
			resetAllChx(chxArriveToday);
		}else if(id==R.id.takemoney_select_chx_t1){
			resetAllChx(chxArriveTomorrow);
		}else if(id==R.id.takemoney_select_btnnext){
			wrapOrder();
		}
	}
	public void setButtonStat(){
		if((canUseStat[0]==false&&canUseStat[1]==false)||Float.parseFloat(blanceValue[0])<=0){
			chxReAcc.setEnabled(false);
			chxReAcc.setTextColor(Color.parseColor("#ffcccccc"));
		}
		if((canUseStat[2]==false&&canUseStat[3]==false)||Float.parseFloat(blanceValue[1])<=0){
			chxFrAcc.setEnabled(false);
			chxFrAcc.setTextColor(Color.parseColor("#ffcccccc"));
		}
		if((canUseStat[4]==false&&canUseStat[5]==false)||Float.parseFloat(blanceValue[2])<=0){
			chxChAcc.setEnabled(false);
			chxChAcc.setTextColor(Color.parseColor("#ffcccccc"));
		}
	}
	public void resetAllChx(CheckBox chx){
		if(chx==chxReAcc){
			chxArriveToday.setEnabled(true);
			chxArriveTomorrow.setEnabled(true);
			chxArriveToday.setTextColor(Color.parseColor("#ff666666"));
			chxArriveTomorrow.setTextColor(Color.parseColor("#ff666666"));
			
			chxFrAcc.setChecked(false);
			chxChAcc.setChecked(false);
			chxArriveToday.setChecked(false);
			chxArriveTomorrow.setChecked(false);
		}else if(chx==chxFrAcc){
			chxArriveToday.setEnabled(true);
			chxArriveTomorrow.setEnabled(true);
			chxArriveToday.setTextColor(Color.parseColor("#ff666666"));
			chxArriveTomorrow.setTextColor(Color.parseColor("#ff666666"));
			
			chxReAcc.setChecked(false);
			chxChAcc.setChecked(false);
			chxArriveToday.setChecked(false);
			chxArriveTomorrow.setChecked(false);
		}else if(chx==chxChAcc){
			chxArriveToday.setEnabled(true);
			chxArriveTomorrow.setEnabled(true);
			chxArriveToday.setTextColor(Color.parseColor("#ff666666"));
			chxArriveTomorrow.setTextColor(Color.parseColor("#ff666666"));
			
			chxReAcc.setChecked(false);
			chxFrAcc.setChecked(false);
			chxArriveToday.setChecked(false);
			chxArriveTomorrow.setChecked(false);
		}else if(chx==chxArriveToday){
			chxArriveTomorrow.setChecked(false);
		}else if(chx==chxArriveTomorrow){
			chxArriveToday.setChecked(false);
		}
	}
	public boolean checkData(){
		if(chxReAcc.isChecked()!=true&&chxFrAcc.isChecked()!=true&&chxChAcc.isChecked()!=true){
			showToast("请选择提现账户");return false;
		}
		if(chxArriveToday.isChecked()!=true&&chxArriveTomorrow.isChecked()!=true){
			showToast("请选择到账类型");return false;
		}
		if(chxReAcc.isChecked()){takeAccount="1";leftValue=blanceValue[0];}
		if(chxFrAcc.isChecked()){takeAccount="2";leftValue=blanceValue[1];}
		if(chxChAcc.isChecked()){takeAccount="3";leftValue=blanceValue[2];}
		if(!RegExUtil.isMoneyValue(edtvValue.getText().toString().trim())||Float.parseFloat(leftValue)<Float.parseFloat(edtvValue.getText().toString().trim())){
			showToast("请输入合法提现金额");return false;
		}else{
			takeValue = edtvValue.getText().toString().trim();
		}
		if(chxArriveToday.isChecked()){takeType="1";}
		if(chxArriveTomorrow.isChecked()){takeType="2";}
		return true;
	}
	public void wrapOrder(){
		if(!checkData()){return;}
		Intent intent = new Intent(TakeMoneyInputActy.this,OrderConfirmActy.class);
		OrderTakeMoney order = new OrderTakeMoney();
		order.setTakeAccount(takeAccount);
		order.setTakeType(takeType);
		order.setTakeValue(takeValue);
		
		OrderWrapper wrapper = OrderWrapper.getInstance();
		wrapper.setOrderType(AvOrderType.code.TakeMoney.name());
		wrapper.setPayType(AvPayType.code.Account.name());
		wrapper.setOrderValue(takeValue);
		wrapper.wrap(order);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", wrapper);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
}
