package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.entity.OrderTransAccount;
import com.rxoa.zlpay.entity.OrderWrapper;

public class TransAccInputActy extends BaseUIActivity implements OnClickListener{

	private TextView edtvAccName;
	private TextView edtvAccNo;
	private TextView edtvReAccNo;
	private TextView edtvBankName;
	private TextView edtvBankDistr;
	private TextView edtvBankBranch;
	private TextView edtvValue;
	private TextView edtvPhone;
	
	private Button btnNext;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initView();
	}
	private void initView(){
		setContentView(R.layout.acty_transacc_input);
		setTitleText(R.string.title_transacc);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		edtvAccName = (TextView) findViewById(R.id.transacc_input_edtv_tarname);
		edtvAccNo = (TextView) findViewById(R.id.transacc_input_edtv_tarno);
		edtvReAccNo = (TextView) findViewById(R.id.transacc_input_edtv_retarno);
		edtvBankName = (TextView) findViewById(R.id.transacc_input_edtv_bankname);
		edtvBankDistr = (TextView) findViewById(R.id.transacc_input_edtv_bankdistr);
		edtvBankBranch = (TextView) findViewById(R.id.transacc_input_edtv_bankbranch);
		edtvValue = (TextView) findViewById(R.id.transacc_input_edtv_value);
		edtvPhone = (TextView) findViewById(R.id.transacc_input_edtv_smsphone);
		btnNext = (Button) findViewById(R.id.transacc_input_btnnext);
		btnNext.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.transacc_input_btnnext){
			if(doDataCheck()){
				wrapOrder();
			}
		}	
	}
	public void wrapOrder(){
		OrderTransAccount order = new OrderTransAccount();
		order.setReceiveAccName(edtvAccName.getText().toString().trim());
		order.setRceceiveAccNo(edtvAccNo.getText().toString().trim());
		order.setBankName(edtvBankName.getText().toString().trim());
		order.setBankDistr(edtvBankDistr.getText().toString().trim());
		order.setBankBranch(edtvBankBranch.getText().toString().trim());
		order.setTransValue(edtvValue.getText().toString().trim());
		order.setNoticePhone(edtvPhone.getText().toString().trim());
		
		OrderWrapper wrapper = new OrderWrapper();
		wrapper.setOrderType(AvOrderType.code.TransAcc.name());
		wrapper.setOrderValue(edtvValue.getText().toString().trim());
		wrapper.setOrderItem(order);
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", wrapper);
		
		Intent intent = new Intent(TransAccInputActy.this,PayTypeSelectActy.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	public boolean doDataCheck(){
		try{
			if(edtvAccName.getText().toString().equals("")){
				showToast("收款户不能为空");return false;
			}else if(edtvAccNo.getText().toString().equals("")){
				showToast("收款账号不能为空");return false;
			}else if(!edtvAccNo.getText().toString().equals(edtvReAccNo.getText().toString())){
				showToast("两次账号输入不一致");return false;
			}else if(edtvBankName.getText().toString().equals("")){
				showToast("开户银行不能为空");return false;
			}else if(edtvValue.getText().toString().equals("")){
				showToast("请输入合法转账金额");return false;
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
