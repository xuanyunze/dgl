package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.entity.OrderReceiveMoney;
import com.rxoa.zlpay.entity.OrderWrapper;
import com.rxoa.zlpay.security.DataValidator;

public class MoneyInputActy extends BaseUIActivity implements OnClickListener{
	private String useType = null;
	private Button btnNext;
	private EditText edtvMoney;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		useType = getIntent().getStringExtra("usetype");
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_money_input);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		edtvMoney = (EditText) findViewById(R.id.money_input_money_edtv);
		btnNext = (Button) findViewById(R.id.btn_moneyinput_next);
		btnNext.setOnClickListener(this);
		if(useType.equals("receivemoney")){
			setTitleText(R.string.title_moneyinput_receivemoney);
		}else if(useType.equals("acccharge")){
			setTitleText(R.string.title_moneyinpu_acccharge);
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.btn_moneyinput_next){
			if(DataValidator.isNumbericWithToast(this, edtvMoney.getText().toString().trim(),R.string.toast_moneyinput_validmoney)){
				wrapOrder();
			}
		}
	}
	public void wrapOrder(){
		Intent intent = new Intent(MoneyInputActy.this,OrderConfirmActy.class);
		OrderReceiveMoney order = new OrderReceiveMoney();
		order.setOrderValue(edtvMoney.getText().toString().trim());
		
		OrderWrapper wrapper = OrderWrapper.getInstance();
		if(useType.equals("receivemoney")){
			wrapper.setOrderType(AvOrderType.code.ReceiveMoney.name());
		}else if(useType.equals("acccharge")){
			wrapper.setOrderType(AvOrderType.code.AccountCharge.name());
		}
		wrapper.setPayType(OrderWrapper.PayType.Device.name());
		wrapper.setOrderValue(edtvMoney.getText().toString().trim());
		wrapper.wrap(order);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", wrapper);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
}
