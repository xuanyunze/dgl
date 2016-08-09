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
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.RegExUtil;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.entity.OrderPhoneCharge;
import com.rxoa.zlpay.entity.OrderReceiveMoney;
import com.rxoa.zlpay.entity.OrderTakeMoney;
import com.rxoa.zlpay.entity.OrderWrapper;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;

public class ReceiveMoneyConfirmActy extends BaseUIActivity implements OnClickListener{
	private CheckBox chxMccMsfw;
	private CheckBox chxMccRybh;
	private CheckBox chxMccDzpf;
	private CheckBox chxArrT0;
	private CheckBox chxArrT1;
	private CheckBox chxArrT7;
	
	private LinearLayout barMsfw;
	private LinearLayout barRybh;
	private LinearLayout barDzpf;
	private LinearLayout barArrT0;
	private LinearLayout barArrT1;
	private LinearLayout barArrT7;
	
	private LinearLayout infoBarMsfw;
	private LinearLayout infoBarRybh;
	private LinearLayout infoBarDzpf;
	private LinearLayout infoBarArrT0;
	private LinearLayout infoBarArrT1;
	private LinearLayout infoBarArrT7;
	
	private TextView infoTvMsfw;
	private TextView infoTvRybh;
	private TextView infoTvDzpf;
	private TextView infoTvArrT0;
	private TextView infoTvArrT1;
	private TextView infoTvArrT7;
	private TextView vwValue;
	
	private EditText edtvValue;
	private Button btnNext;
	
	private boolean[] canUseStat = {true,true,false,false,false,true};
	private String[] blanceValue = {"3000.00","100.00","8.00"};
	private String[] chxHtml = {"","",""};
	private String mccType = "1001";
	private String arrType;
	private String receValue;
	private String valueString;
	private String usetype;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initView();
	}
	private void initView(){
		setContentView(R.layout.acty_money_confirm);
		setTitleText(R.string.title_moneyinput_receivemoney);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		String value = getIntent().getStringExtra("value");
		this.valueString = value;
		this.usetype = getIntent().getStringExtra("usetype");
		
		chxMccMsfw = (CheckBox) findViewById(R.id.tran_mcc_chx_msfw);
		chxMccRybh = (CheckBox) findViewById(R.id.tran_mcc_chx_rybh);
		chxMccDzpf = (CheckBox) findViewById(R.id.tran_mcc_chx_dzpf);
		chxArrT0 = (CheckBox) findViewById(R.id.tran_arr_chx_t0);
		chxArrT1 = (CheckBox) findViewById(R.id.tran_arr_chx_t1);
		chxArrT7 = (CheckBox) findViewById(R.id.tran_arr_chx_t7);
		
		barMsfw = (LinearLayout) findViewById(R.id.tran_mcc_bar_msfw);
		barRybh = (LinearLayout) findViewById(R.id.tran_mcc_bar_rybh);
		barDzpf = (LinearLayout) findViewById(R.id.tran_mcc_bar_dzpf);
		barArrT0 = (LinearLayout) findViewById(R.id.tran_arr_bar_t0);
		barArrT1 = (LinearLayout) findViewById(R.id.tran_arr_bar_t1);
		barArrT7 = (LinearLayout) findViewById(R.id.tran_arr_bar_t7);
		
		infoBarMsfw = (LinearLayout) findViewById(R.id.tran_mcc_infobar_msfw);
		infoBarRybh = (LinearLayout) findViewById(R.id.tran_mcc_infobar_rybh);
		infoBarDzpf = (LinearLayout) findViewById(R.id.tran_mcc_infobar_dzpf);
		infoBarArrT0 = (LinearLayout) findViewById(R.id.tran_arr_infobar_t0);
		infoBarArrT1 = (LinearLayout) findViewById(R.id.tran_arr_infobar_t1);
		infoBarArrT7 = (LinearLayout) findViewById(R.id.tran_arr_infobar_t7);
		
		infoTvMsfw = (TextView) findViewById(R.id.tran_mcc_infotv_msfw);
		infoTvRybh = (TextView) findViewById(R.id.tran_mcc_infotv_rybh);
		infoTvDzpf = (TextView) findViewById(R.id.tran_mcc_infotv_dzpf);
		infoTvArrT0 = (TextView) findViewById(R.id.tran_arr_infotv_t0);
		infoTvArrT1 = (TextView) findViewById(R.id.tran_arr_infotv_t1);
		infoTvArrT7 = (TextView) findViewById(R.id.tran_arr_infotv_t7);
		vwValue = (TextView) findViewById(R.id.tran_value);
		
		edtvValue = (EditText) findViewById(R.id.tran_tv_value);
		btnNext = (Button) findViewById(R.id.tran_btn_next);
		
		chxMccMsfw.setOnClickListener(this);
		chxMccRybh.setOnClickListener(this);
		chxMccDzpf.setOnClickListener(this);
		chxArrT0.setOnClickListener(this);
		chxArrT1.setOnClickListener(this);
		chxArrT7.setOnClickListener(this);
		
		btnNext.setOnClickListener(this);
		vwValue.setText(StringUtil.toDotMoney(this.valueString));
		if(usetype.equals("s0receivemoney")){
			wrapOrder();
		}
		if(usetype.equals("commonreceivemoney")){
			wrapOrder();
		}
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.tran_mcc_bar_msfw||id==R.id.tran_mcc_chx_msfw){
			resetMcc(chxMccMsfw);
		}else if(id==R.id.tran_mcc_bar_rybh||id==R.id.tran_mcc_chx_rybh){
			resetMcc(chxMccRybh);
		}else if(id==R.id.tran_mcc_bar_dzpf||id==R.id.tran_mcc_chx_dzpf){
			resetMcc(chxMccDzpf);
		}else if(id==R.id.tran_arr_bar_t0||id==R.id.tran_arr_chx_t0){
			resetArr(chxArrT0);
		}else if(id==R.id.tran_arr_bar_t1||id==R.id.tran_arr_chx_t1){
			resetArr(chxArrT1);
		}else if(id==R.id.tran_arr_bar_t7||id==R.id.tran_arr_chx_t7){
			resetArr(chxArrT7);
		}else if(id==R.id.tran_btn_next){
			wrapOrder();
		}
	}
	public void resetMcc(Object chx){
		chxMccMsfw.setChecked(false);
		chxMccRybh.setChecked(false);
		chxMccDzpf.setChecked(false);
		((CheckBox)chx).setChecked(true);
	}
	public void resetArr(Object chx){
		chxArrT0.setChecked(false);
		chxArrT1.setChecked(false);
		chxArrT7.setChecked(false);
		((CheckBox)chx).setChecked(true);
	}
	public boolean checkData(){
		if(chxMccMsfw.isChecked()!=true&&chxMccRybh.isChecked()!=true&&chxMccDzpf.isChecked()!=true){
			showToast("请选择收款类型");return false;
		}
		if(chxMccMsfw.isChecked()){mccType="1001";}
		if(chxMccRybh.isChecked()){mccType="1002";}
		if(chxMccDzpf.isChecked()){mccType="1003";}
		/*
		if(chxArrT0.isChecked()!=true&&chxArrT1.isChecked()!=true&&chxArrT7.isChecked()!=true){
			showToast("请选择到账类型～");return false;
		}

		if(chxArrT0.isChecked()){arrType="1001";}
		if(chxArrT1.isChecked()){arrType="1002";}
		if(chxArrT7.isChecked()){arrType="1003";}
		*/
		/*
		if(!RegExUtil.isMoneyValue(edtvValue.getText().toString().trim())){
			showToast("请输入合法提现金额～");return false;
		}else{
			receValue = edtvValue.getText().toString().trim();
		}
		*/
		return true;
	}
	public void wrapOrder(){
		if(!usetype.equals("s0receivemoney")&&!usetype.equals("commonreceivemoney")&&!checkData()){return;}
		OrderWrapper wrapper = OrderWrapper.getInstance();
		Intent intent = new Intent(ReceiveMoneyConfirmActy.this,OrderConfirmActy.class);
		OrderReceiveMoney order = new OrderReceiveMoney();
		order.setOrderValue(valueString);
		order.setPayFeeCode(mccType);
		
		if(usetype.equals("s0receivemoney")){
			order.setPayFeeCode("1001");
			wrapper.setOrderType(AvOrderType.code.S0ReceiveMoney.name());
		}else if(usetype.equals("commonreceivemoney")){
			order.setPayFeeCode("1003");
			wrapper.setOrderType(AvOrderType.code.CommonReceiveMoney.name());
		}else if(usetype.equals("fastreceivemoney")){
			wrapper.setOrderType(AvOrderType.code.FastReceiveMoney.name());
		}else if(usetype.equals("zeroreceivemoney")){
			wrapper.setOrderType(AvOrderType.code.ZeroReceiveMoney.name());
			order.setPaySettleCode("1003");
		}else{
			wrapper.setOrderType(AvOrderType.code.ReceiveMoney.name());
		}
		order.setPayAreaCode("1001");
		
		wrapper.setPayType(AvPayType.code.Device.name());
		wrapper.setOrderValue(valueString);
		wrapper.wrap(order);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", wrapper);
		intent.putExtras(bundle);
		//checkUserStat(intent);
		startActivity(intent);
		finish();
	}
    public void checkUserStat(final Intent intent){
    	try{
    		final UserAccInfoReqVo reqVo = new UserAccInfoReqVo();
			new DefAsyncTask(ReceiveMoneyConfirmActy.this){
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
					parser = ReqWrapper.getUserAccRequest(ReceiveMoneyConfirmActy.this).doQueryUserAcc(reqVo);
				}
				@Override
				public void onPosExcute() {
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
						startActivity(intent);
						finish();
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
