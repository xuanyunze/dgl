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
import com.rxoa.zlpay.entity.OrderPayCredit;
import com.rxoa.zlpay.entity.OrderWrapper;

public class RepayCreditInputActy extends BaseUIActivity implements OnClickListener{

	private TextView edtvAccName;
	private TextView edtvAccNo;
	private TextView edtvReAccNo;
	private TextView edtvBankName;
	private TextView edtvValue;
	private Button btnNext;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initView();
	}
	private void initView(){
		setContentView(R.layout.acty_repaycredit_input);
		setTitleText(R.string.title_repaycredit);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		edtvAccName = (TextView) findViewById(R.id.repaycredit_input_edtv_accname);
		edtvAccNo = (TextView) findViewById(R.id.repaycredit_input_edtv_accno);
		edtvReAccNo = (TextView) findViewById(R.id.repaycredit_input_edtv_reaccno);
		edtvBankName = (TextView) findViewById(R.id.repaycredit_input_edtv_bankname);
		edtvValue = (TextView) findViewById(R.id.repaycredit_input_edtv_value);
		btnNext = (Button) findViewById(R.id.repaycredit_input_btnnext);
		btnNext.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.repaycredit_input_btnnext){
			if(doCheckData()){
				wrapOrder();
			}
		}	
	}
	public void wrapOrder(){
		OrderPayCredit order = new OrderPayCredit();
		order.setAccName(edtvAccName.getText().toString().trim());
		order.setAccNo(edtvAccNo.getText().toString().trim());
		order.setAccBank(edtvBankName.getText().toString().trim());
		order.setAccValue(edtvValue.getText().toString().trim());
		
		OrderWrapper wrapper = OrderWrapper.getInstance();
		wrapper.setOrderType(AvOrderType.code.RepayCredit.name());
		wrapper.setOrderValue(edtvValue.getText().toString().trim());
		wrapper.setOrderItem(order);
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", wrapper);
		
		Intent intent = new Intent(RepayCreditInputActy.this,PayTypeSelectActy.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	public boolean doCheckData(){
		try{
			if(edtvAccName.getText().toString().equals("")){
				showToast("开户名称不能为空");return false;
			}else if(edtvAccNo.getText().toString().equals("")){
				showToast("信用卡号不能为空");return false;
			}else if(!edtvAccNo.getText().toString().equals(edtvReAccNo.getText().toString())){
				showToast("两次卡号输入不一致");return false;
			}else if(edtvBankName.getText().toString().equals("")){
				showToast("发卡银行不能为空哦");return false;
			}else if(edtvValue.getText().toString().equals("")){
				showToast("请输入正确还款金额");return false;
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
