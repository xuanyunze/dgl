package com.rxoa.zlpay.acty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.adapter.PaytypeCardAdapter;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.avator.AvPayType;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.entity.OrderWrapper;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.PaytypeReqVo;
import com.rxoa.zlpay.vo.PaytypeRespVo;

public class PayTypeSelectActy extends BaseUIActivity implements OnClickListener{
	private LinearLayout barinfoFast;
	private TextView btnAddFast;
	
	private LinearLayout barAccount;
	private LinearLayout barFast;
	private LinearLayout barDevice;
	
	private CheckBox chxAccount;
	private CheckBox chxFast;
	private CheckBox chxDevice;
	
	private TextView tvValue;
	private Button btnNext;
	private List<String> useType = new ArrayList<String>(Arrays.asList("account","fast","device"));
	private boolean isBlanceEnabled = true;
	
	private List<BankCardEntity> fastCards;
	private String blanceValue;
    private ListView lvFastCards;
    private PaytypeCardAdapter adapter;
    
    OrderWrapper order = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_paytype_select);
		setTitleText(R.string.title_paytype);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		barinfoFast = (LinearLayout) findViewById(R.id.paytype_barinfo_fast);
		btnAddFast = (TextView) findViewById(R.id.paytype_addnew_fast);
		lvFastCards = (ListView) findViewById(R.id.paytype_listview_fast);
		
		barAccount = (LinearLayout) findViewById(R.id.paytype_bar_account);
		barFast = (LinearLayout) findViewById(R.id.paytype_bar_fast);
		barDevice = (LinearLayout) findViewById(R.id.paytype_bar_device);
		
		chxAccount = (CheckBox) findViewById(R.id.paytype_btnradio_account);
		chxFast = (CheckBox) findViewById(R.id.paytype_btnradio_fast);
		chxDevice = (CheckBox) findViewById(R.id.paytype_btnradio_device);
		tvValue = (TextView) findViewById(R.id.paytype_tv_value);
		btnNext = (Button) findViewById(R.id.paytype_btnnext);
		chxAccount.setOnClickListener(this);
		chxFast.setOnClickListener(this);
		chxDevice.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnAddFast.setOnClickListener(this);
		
		if(useType.contains("account")){
			barAccount.setVisibility(View.VISIBLE);
		}
		if(useType.contains("fast")){
			barFast.setVisibility(View.VISIBLE);
		}
		if(useType.contains("device")){
			barDevice.setVisibility(View.VISIBLE);
		}
		parseOrder();
	}
	public void parseOrder(){
		Intent intent = getIntent();
		order = (OrderWrapper) intent.getSerializableExtra("order");
		tvValue.setText(order.getOrderValue());
		System.out.println("xx=="+order.getOrderType());
		System.out.println("yy=="+AvOrderType.code2value(AvOrderType.code.ReceiveMoney.name()));
		if(order.getOrderType().equals(AvOrderType.code.ReceiveMoney.name())
				||order.getOrderType().equals(AvOrderType.code.ZeroReceiveMoney.name())){
			Intent nintent = new Intent(PayTypeSelectActy.this,ReadCardActy.class);
			nintent.putExtra("usetype", "payorder");
			order.setPayType(AvPayType.code.Device.name());
			order.setOrderValue(StringUtil.toIsoMoney(StringUtil.format(order.getOrderValue())));
			nintent.putExtra("order", order);
			startActivity(nintent);
			finish();
		}else{
			syncPaytypeInfo();
		}
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.paytype_btnradio_account){
			if(!isBlanceEnabled){
				chxAccount.setChecked(false);
			}else{
				if(chxAccount.isChecked()){
					chxFast.setChecked(false);
					barinfoFast.setVisibility(View.GONE);
					chxDevice.setChecked(false);
				}
			}
		}else if(id==R.id.paytype_btnradio_fast){
			if(chxFast.isChecked()){
				chxAccount.setChecked(false);
				chxDevice.setChecked(false);
				barinfoFast.setVisibility(View.VISIBLE);
			}else{
				barinfoFast.setVisibility(View.GONE);
			}
		}else if(id==R.id.paytype_btnradio_device){
			if(chxDevice.isChecked()){
				chxAccount.setChecked(false);
				chxFast.setChecked(false);
				barinfoFast.setVisibility(View.GONE);
			}
		}else if(id==R.id.paytype_addnew_fast){
			Intent intent = new Intent(PayTypeSelectActy.this,BindOtherCardActy.class);
			startActivity(intent);
		}else if(id==R.id.paytype_btnnext){
			doSelPayType();
		}
	}

	public boolean doSelPayType(){
		if(!(chxAccount.isChecked()||chxFast.isChecked()||chxDevice.isChecked())){
			showToast("请选择支付方式");return false;
		}
		if(chxAccount.isChecked()){
			gotoOrderConfirm("account");
			return true;
		}
		if(chxFast.isChecked()){
			if(getSelectItems()==9999){
				showToast("请选择快捷支付银行卡");return false;
			}
			gotoOrderConfirm("fast");
			return true;
		}
		if(chxDevice.isChecked()){
			gotoOrderConfirm("device");
			return true;
		}
		return false;
	}
	public void gotoOrderConfirm(String type){
		if(type.equals("account")){
			order.setPayType(AvPayType.code.Account.name());
		}else if(type.equals("fast")){
			order.setPayType(AvPayType.code.Fast.name());
			order.setFastAccName(fastCards.get(getSelectItems()).getAccName());
			order.setFastAccNo(fastCards.get(getSelectItems()).getAccNo());
		}else if(type.equals("device")){
			order.setPayType(AvPayType.code.Device.name());
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", order);
		Intent intent = new Intent(PayTypeSelectActy.this,OrderConfirmActy.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	public void syncPaytypeInfo(){
		try{
			if(!Config._isLogin){return;}
			final PaytypeReqVo reqVo = new PaytypeReqVo();
			new DefAsyncTask(PayTypeSelectActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在加载支付方式数据...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getUserAccRequest(PayTypeSelectActy.this).doQueryPaytypeInfo(reqVo);
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
							blanceValue = ((PaytypeRespVo)parser.getRespObject()).getBlanceValue();
							fastCards = ((PaytypeRespVo)parser.getRespObject()).getFastCards();
							refreshViewUI();
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("支付信息加载失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void refreshViewUI(){
		if(Float.parseFloat(blanceValue)<Float.parseFloat(tvValue.getText().toString())){
			chxAccount.setText("账户余额支付(余额不足)");
			chxAccount.setTextColor(Color.parseColor("#ff999999"));
			isBlanceEnabled = false;
		}else{
			chxAccount.setText("账户余额支付");
			chxAccount.setTextColor(Color.parseColor("#ff666666"));
			isBlanceEnabled = true;
		}
		adapter = new PaytypeCardAdapter(PayTypeSelectActy.this,fastCards);
		lvFastCards.setAdapter(adapter);
		setListViewHeightBasedOnChildren(lvFastCards);
		lvFastCards.setOnItemClickListener(new OnItemClickListener() {
			@Override  
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
			long arg3) {  
				if(arg2>=fastCards.size()){
					return;
				}
				lvFastCards.setItemChecked(arg2, true);
			}  
			});
	}
	public int getSelectItems(){
		SparseBooleanArray checkedArray = lvFastCards.getCheckedItemPositions();
		for(int i=0;i<checkedArray.size();i++){
			if(checkedArray.valueAt(i)){
				return i;
			}
		}
		return 9999;
	}
	
	
    public void setListViewHeightBasedOnChildren(ListView listView) {   
        // 获取ListView对应的Adapter   
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listView);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度   
        listView.setLayoutParams(params);
    }
}
