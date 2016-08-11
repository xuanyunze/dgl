package com.rxoa.zlpay.acty;

import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.util.DateUtil;
import com.rxoa.zlpay.base.util.StringUtil;

public class MoneyInputCalActy extends BaseUIActivity implements OnClickListener{
	private Dialog dialog = null;
	private String useType = "receivemoney";
	private String valueString = "";
	private TextView vwValue;
	private TextView vwInfo;
	private double minValue = 100D;
	private double maxValue = 100000D;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		useType = getIntent().getStringExtra("usetype");
		minValue = getIntent().getDoubleExtra("minValue", 100D);
		maxValue = getIntent().getDoubleExtra("maxValue", 100000D);
		initView();

	}
	private void initView(){
		setContentView(R.layout.acty_money_inputcal);
		setTitleText("输入金额");
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		vwInfo = (TextView) findViewById(R.id.inputcal_info);
		vwValue = (TextView) findViewById(R.id.inputcal_value);
		findViewById(R.id.inputcal_0).setOnClickListener(this);
		findViewById(R.id.inputcal_1).setOnClickListener(this);
		findViewById(R.id.inputcal_2).setOnClickListener(this);
		findViewById(R.id.inputcal_3).setOnClickListener(this);
		findViewById(R.id.inputcal_4).setOnClickListener(this);
		findViewById(R.id.inputcal_5).setOnClickListener(this);
		findViewById(R.id.inputcal_6).setOnClickListener(this);
		findViewById(R.id.inputcal_7).setOnClickListener(this);
		findViewById(R.id.inputcal_8).setOnClickListener(this);
		findViewById(R.id.inputcal_9).setOnClickListener(this);
		findViewById(R.id.inputcal_back).setOnClickListener(this);
		findViewById(R.id.inputcal_ok).setOnClickListener(this);
		
		if(useType.equals("s0receivemoney")){
			vwInfo.setText("实时到账交易时间为：6:00-22:00");
		}else if(useType.equals("commonreceivemoney")){
			vwInfo.setText("交易限额"+StringUtil.format(minValue)+" - "+StringUtil.format(maxValue)+",下一工作日到账");
		}/*else if(useType.equals("fastreceivemoney")){
			vwInfo.setText("交易限额"+StringUtil.format(minValue)+" - "+StringUtil.format(maxValue)+",立即到账");
		}else if(useType.equals("receivemoney")){
			vwInfo.setText("交易限额"+StringUtil.format(minValue)+" - "+StringUtil.format(maxValue));
		}else if(useType.equals("zeroreceivemoney")){
			vwInfo.setText("交易限额"+StringUtil.format(minValue)+" - "+StringUtil.format(maxValue)+"，7日自动到账");
		}*/
	}
	public void setValue(int type,String vchar){
		if(type==0){
			if((valueString.equals("")&&vchar.equals("0"))||valueString.length()>8) return;
			valueString += vchar;
		}else{
			if(valueString.equals("")) return;
			valueString = valueString.substring(0,valueString.length()-1);
		}
		vwValue.setText(StringUtil.toDotMoney(valueString));
	}
	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if(id==R.id.inputcal_0){
			setValue(0,"0");
		}else if(id==R.id.inputcal_1){
			setValue(0,"1");
		}else if(id==R.id.inputcal_2){
			setValue(0,"2");
		}else if(id==R.id.inputcal_3){
			setValue(0,"3");
		}else if(id==R.id.inputcal_4){
			setValue(0,"4");
		}else if(id==R.id.inputcal_5){
			setValue(0,"5");
		}else if(id==R.id.inputcal_6){
			setValue(0,"6");
		}else if(id==R.id.inputcal_7){
			setValue(0,"7");
		}else if(id==R.id.inputcal_8){
			setValue(0,"8");
		}else if(id==R.id.inputcal_9){
			setValue(0,"9");
		}else if(id==R.id.inputcal_back){
			setValue(1,null);
		}else if(id==R.id.inputcal_ok){
			if(valueString==null||valueString.equals("")||valueString.equals("0")){
				showToast("请输入有效金额");return;
			}
			
			if(Double.parseDouble(StringUtil.toDotMoney(valueString))<minValue
					||Double.parseDouble(StringUtil.toDotMoney(valueString))>maxValue){
				showToast("初始金额为"+StringUtil.format(minValue)+"-"+StringUtil.format(maxValue)+"\n"+"信用卡认证后为0-50000\n");return;
			}
			if(useType.equals("s0receivemoney")){
				confirmS0(null,MoneyInputCalActy.this);
			}else if(useType.equals("commonreceivemoney")){
				confirmS1(null,MoneyInputCalActy.this);
				/*Intent intent = new Intent(MoneyInputCalActy.this,ReceiveMoneyConfirmActy.class);
				intent.putExtra("value", valueString);
				intent.putExtra("usetype", "commonreceivemoney");
				startActivity(intent);
				finish();*/
			}else if(useType.equals("fastreceivemoney")){
				Intent intent = new Intent(MoneyInputCalActy.this,ReceiveMoneyConfirmActy.class);
				intent.putExtra("value", valueString);
				intent.putExtra("usetype", "fastreceivemoney");
				startActivity(intent);
				finish();
			}else if(useType.equals("receivemoney")){
				Intent intent = new Intent(MoneyInputCalActy.this,ReceiveMoneyConfirmActy.class);
				intent.putExtra("value", valueString);
				intent.putExtra("usetype", "receivemoney");
				startActivity(intent);
				finish();
			}else if(useType.equals("zeroreceivemoney")){
				showAlert(null,MoneyInputCalActy.this);
				//Intent intent = new Intent(MoneyInputCalActy.this,ReceiveMoneyConfirmActy.class);
				//intent.putExtra("value", valueString);
				//intent.putExtra("usetype", "zeroreceivemoney");
				//startActivity(intent);
				//finish();
			}else if(useType.equals("takemoney")){
				Intent intent = new Intent();
				intent.putExtra("value", this.valueString);
				setResult(RESULT_OK, intent);
				finish();
			}
		}else if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
	}
	public void confirmS0(String msg,Context context){
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setIcon(null);
		localBuilder.setMessage("金额【"+StringUtil.toDotMoney(valueString)+"】，立即到账");
	    localBuilder.setTitle("信息提示");
	    localBuilder.setPositiveButton("确 认", new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
				Intent intent = new Intent(MoneyInputCalActy.this,ReceiveMoneyConfirmActy.class);
				intent.putExtra("value", valueString);
				intent.putExtra("usetype", "s0receivemoney");
				startActivity(intent);
				finish();
	    	}
	    });
	    localBuilder.setNegativeButton("取 消", new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		dialog.dismiss();
	    	}
	    });
	    this.dialog = localBuilder.create();
	    this.dialog.setCancelable(false);
	    this.dialog.setCanceledOnTouchOutside(false);
	    this.dialog.show();
	}
	public void confirmS1(String msg,Context context){
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setIcon(null);
		localBuilder.setMessage("金额【"+StringUtil.toDotMoney(valueString)+"】，下一工作日到账");
	    localBuilder.setTitle("信息提示");
	    localBuilder.setPositiveButton("确 认", new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
				Intent intent = new Intent(MoneyInputCalActy.this,ReceiveMoneyConfirmActy.class);
				intent.putExtra("value", valueString);
				intent.putExtra("usetype", "commonreceivemoney");
				startActivity(intent);
				finish();
	    	}
	    });
	    localBuilder.setNegativeButton("取 消", new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		dialog.dismiss();
	    	}
	    });
	    this.dialog = localBuilder.create();
	    this.dialog.setCancelable(false);
	    this.dialog.setCanceledOnTouchOutside(false);
	    this.dialog.show();
	}
	public void showAlert(String msg,Context context){
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setIcon(null);
		localBuilder.setMessage("交易金额为"+StringUtil.toDotMoney(valueString)+"\r\n预计于"+DateUtil.getDateOnly(DateUtil.adjust(new Date(), 3, 7))+"到账");
	    localBuilder.setTitle("信息提示");
	    localBuilder.setPositiveButton("确 认", new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
				Intent intent = new Intent(MoneyInputCalActy.this,ReceiveMoneyConfirmActy.class);
				intent.putExtra("value", valueString);
				intent.putExtra("usetype", "zeroreceivemoney");
				startActivity(intent);
				finish();
	    	}
	    });
	    localBuilder.setNegativeButton("取 消", new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		dialog.dismiss();
	    	}
	    });
	    this.dialog = localBuilder.create();
	    this.dialog.setCancelable(false);
	    this.dialog.setCanceledOnTouchOutside(false);
	    this.dialog.show();
	}
}
