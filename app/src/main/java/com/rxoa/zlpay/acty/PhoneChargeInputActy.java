package com.rxoa.zlpay.acty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.entity.OrderPhoneCharge;
import com.rxoa.zlpay.entity.OrderWrapper;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;

import com.rxoa.zlpay.util.ValidUtil;
import com.rxoa.zlpay.vo.UserRegReqVo;
import com.rxoa.zlpay.vo.UserRegRespVo;

public class PhoneChargeInputActy extends BaseUIActivity implements OnClickListener{

	private EditText edtvPhone;
	private TextView tvValue;
	private ImageView btnSelPhone;
	private LinearLayout barSelValue;
	private Button btnNext;
	
	private String[] valuetype = new String[]{"30.00","50.00", "100.00"};
	private boolean[] valuestat=new boolean[]{true, false, false};  
	private RadioOnClick radioOnClick = new RadioOnClick(0);
	private ListView lvValuetype;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initView();
	}
	private void initView(){
		setContentView(R.layout.acty_phonecharge_input);
		setTitleText(R.string.title_phonecharge);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		edtvPhone = (EditText) findViewById(R.id.phonecharge_input_edtv_phonecode);
		tvValue = (TextView) findViewById(R.id.phonecharge_input_tv_value);
		btnSelPhone = (ImageView) findViewById(R.id.phonecharge_input_ivbtn_phonecode);
		barSelValue = (LinearLayout) findViewById(R.id.phonecharge_input_bar_value);
		btnNext = (Button) findViewById(R.id.phonecharge_input_btnnext);
		btnSelPhone.setOnClickListener(this);
		barSelValue.setOnClickListener(new RadioClickListener());
		btnNext.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.phonecharge_input_btnnext){
			if(doCheckData()){
				wrapOrder();
			}
		}else if(id==R.id.phonecharge_input_ivbtn_phonecode){
			startActivityForResult(new Intent(
	        Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI), 0);
		}
	}
	public void wrapOrder(){
		Intent intent = new Intent(PhoneChargeInputActy.this,PayTypeSelectActy.class);
		OrderPhoneCharge order = new OrderPhoneCharge();
		order.setChargeValue(tvValue.getText().toString().trim());
		order.setPhoneNumber(edtvPhone.getText().toString().trim());
		
		OrderWrapper wrapper = OrderWrapper.getInstance();
		wrapper.setOrderType(AvOrderType.code.PhoneCharge.name());
		wrapper.setOrderValue(tvValue.getText().toString().trim());
		wrapper.wrap(order);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", wrapper);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	public boolean doCheckData(){
		try{
			if(edtvPhone.getText().toString().equals("")){
				showToast("请输入充值手机号");return false;
			}else if(tvValue.getText().toString().equals("")){
				showToast("请选择充值金额");return false;
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	class RadioClickListener implements OnClickListener {  
		@Override  
		public void onClick(View v) {  
			AlertDialog ad =new AlertDialog.Builder(PhoneChargeInputActy.this).setTitle("选择充值金额")
					.setSingleChoiceItems(valuetype,radioOnClick.getIndex(),radioOnClick).create();  
			lvValuetype=ad.getListView();  
			ad.show();
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
			tvValue.setText(valuetype[index]);
			dialog.dismiss();
		}
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
        	ContentResolver reContentResolverol = getContentResolver();
            Uri contactData = data.getData();
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
                     null, 
                     ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, 
                     null, 
                     null);
             while (phone.moveToNext()) {
            	 String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            	 edtvPhone.setText(usernumber);
             }

         }
    }
}
