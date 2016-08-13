package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.avator.AvArriveType;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.DateUtil;
import com.rxoa.zlpay.base.util.IDUtil;
import com.rxoa.zlpay.entity.OrderPayCredit;
import com.rxoa.zlpay.entity.OrderPhoneCharge;
import com.rxoa.zlpay.entity.OrderReceiveMoney;
import com.rxoa.zlpay.entity.OrderTakeMoney;
import com.rxoa.zlpay.entity.OrderTransAccount;
import com.rxoa.zlpay.entity.OrderWrapper;
import com.rxoa.zlpay.entity.OrderWrapper.PayType;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.util.AvatorUtil;
import com.rxoa.zlpay.vo.OrderDetailReqVo;
import com.rxoa.zlpay.vo.OrderDetailRespVo;
import com.rxoa.zlpay.vo.OrderPayReqVo;
import com.rxoa.zlpay.vo.OrderPayRespVo;

public class OrderConfirmActy extends BaseUIActivity implements OnClickListener{
	public static final String TAG = OrderConfirmActy.class.getName();
	private TextView tvOrderFlowid;
	private TextView tvOrderType;
	private TextView tvOrderValue;
	private TextView tvOrderDate;
	private TextView tvOrderStat;
	private TextView tvPayType;
	private TextView ltvOrderValue;
	
	private LinearLayout barPwd;
	private EditText edtvPwd;
	private Button btnGo;
	
	OrderWrapper order = null;
	private String orderid;
	private String ordertype;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_order_confirm);
		setTitleText(R.string.title_order_confirm);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		tvOrderFlowid = (TextView) findViewById(R.id.order_confirm_flowid_tv);
		tvOrderType = (TextView) findViewById(R.id.order_confirm_type_tv);
		tvOrderValue = (TextView) findViewById(R.id.order_confirm_value_tv);
		tvOrderDate = (TextView) findViewById(R.id.order_confirm_date_tv);
		tvOrderStat = (TextView) findViewById(R.id.order_confirm_stat_tv);
		tvPayType = (TextView) findViewById(R.id.order_confirm_paytype_tv);
		ltvOrderValue = (TextView) findViewById(R.id.order_confirm_value_label);
		
		barPwd = (LinearLayout) findViewById(R.id.order_confirm_bar_pwd);
		edtvPwd = (EditText) findViewById(R.id.order_confirm_edtv_pwd);
		btnGo = (Button) findViewById(R.id.order_confirm_btngo);
		
		btnGo.setOnClickListener(this);
		parseOrder();
	}
	public void parseOrder(){
		Intent intent = getIntent();
		order = (OrderWrapper) intent.getSerializableExtra("order");
		String otype = order.getOrderType();
		String paytype = order.getPayType();
		if(order.getOrderId()==null||order.getOrderId().equals("null")){
			order.setOrderId(IDUtil.genOrderFlowid());
		}
		ordertype = order.getOrderType();
		orderid = order.getOrderId();
		tvOrderFlowid.setText(order.getOrderId());
		tvOrderValue.setText(order.getOrderValue());
		tvOrderDate.setText(DateUtil.getNowFullDateTime());
		tvOrderStat.setText("等待支付");
		if(otype.equals(AvOrderType.code.ReceiveMoney.name())){
			findViewById(R.id.order_confirm_content_receivemoney).setVisibility(View.VISIBLE);
			tvOrderType.setText("收款");
			ltvOrderValue.setText("收款金额：");
			OrderReceiveMoney orderItem = (OrderReceiveMoney) order.getOrderItem();
			((TextView)findViewById(R.id.order_content_recemcc_tv)).setText(AvatorUtil.feeCode("v2t", orderItem.getPayFeeCode()));
			//((TextView)findViewById(R.id.order_content_recesettlecode_tv)).setText(AvatorUtil.settleCode("v2t", orderItem.getPaySettleCode()));
		}else if(otype.equals(AvOrderType.code.AccountCharge.name())){
			findViewById(R.id.order_confirm_content_chargeacc).setVisibility(View.VISIBLE);
			tvOrderType.setText("账户充值");
			ltvOrderValue.setText("充值金额：");
		}else if(otype.equals(AvOrderType.code.TransAcc.name())){
			findViewById(R.id.order_confirm_content_transacc).setVisibility(View.VISIBLE);
			tvOrderType.setText("卡卡转账");
			ltvOrderValue.setText("转账金额：");
			OrderTransAccount orderItem = (OrderTransAccount) order.getOrderItem();
			((TextView)findViewById(R.id.order_content_receaccname_tv)).setText(orderItem.getReceiveAccName());
			((TextView)findViewById(R.id.order_content_receaccno_tv)).setText(orderItem.getRceceiveAccNo());
			((TextView)findViewById(R.id.order_content_recebankname_tv)).setText(orderItem.getBankName());
			((TextView)findViewById(R.id.order_content_recebankdistr_tv)).setText(orderItem.getBankDistr());
			((TextView)findViewById(R.id.order_content_recebankbranch_tv)).setText(orderItem.getBankBranch());
			((TextView)findViewById(R.id.order_content_notifyphone_tv)).setText(orderItem.getNoticePhone());
		}else if(otype.equals(AvOrderType.code.RepayCredit.name())){
			findViewById(R.id.order_confirm_content_repayedit).setVisibility(View.VISIBLE);
			tvOrderType.setText("还信用卡");
			ltvOrderValue.setText("还款金额：");
			OrderPayCredit orderItem = (OrderPayCredit) order.getOrderItem();
			((TextView)findViewById(R.id.order_content_creditaccname_tv)).setText(orderItem.getAccName());
			((TextView)findViewById(R.id.order_content_creditaccno_tv)).setText(orderItem.getAccNo());
			((TextView)findViewById(R.id.order_content_creditbankname_tv)).setText(orderItem.getAccBank());
		}else if(otype.equals(AvOrderType.code.PhoneCharge.name())){
			findViewById(R.id.order_confirm_content_phonecharge).setVisibility(View.VISIBLE);
			tvOrderType.setText("手机充值");
			ltvOrderValue.setText("充值金额：");
			OrderPhoneCharge orderItem = (OrderPhoneCharge) order.getOrderItem();
			((TextView)findViewById(R.id.order_content_phoneno_tv)).setText(orderItem.getPhoneNumber());
		}else if(otype.equals(AvOrderType.code.TakeMoney.name())){
			findViewById(R.id.order_confirm_content_takemoney).setVisibility(View.VISIBLE);
			OrderTakeMoney orderItem = (OrderTakeMoney) order.getOrderItem();
			//((TextView)findViewById(R.id.order_content_taketype_tv)).setText(AvTakeType.value2text(orderItem.getTakeType()));
			setTitleText("提现确认");
			tvOrderType.setText("用户提现");
			ltvOrderValue.setText("提现金额：");
			tvOrderStat.setText("等待确认");
			btnGo.setText("提交订单");
			((LinearLayout)findViewById(R.id.order_confirm_paytype_lnr)).setVisibility(View.GONE);
		}
		if(paytype.equals(PayType.Account.name())){
			tvPayType.setText("账户余额支付");
		}else if(paytype.equals(PayType.Fast.name())){
			tvPayType.setText("快捷支付");
		}else if(paytype.equals(PayType.Device.name())){
			tvPayType.setText("刷卡支付");
			barPwd.setVisibility(View.GONE);
			openCardReader();
		}
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.order_confirm_btngo){
			payOrderNow();
		}
	}
	
	public void openCardReader(){
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", order);
		
		Intent intent = new Intent(OrderConfirmActy.this,ReadCardActy.class);
		intent.putExtra("usetype", "payorder");
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	public void payOrderNow(){
		try{
			if(!Config._isLogin){return;}
			final OrderPayReqVo reqVo = new OrderPayReqVo();
			reqVo.setOrderId(order.getOrderId());
			reqVo.setOrderType(order.getOrderType());
			reqVo.setOrderValue(order.getOrderValue());
			String otype = order.getOrderType();
			if(otype.equals(AvOrderType.code.TakeMoney.name())){
				OrderTakeMoney orderItem = (OrderTakeMoney) order.getOrderItem();
				reqVo.setTakeAccount(orderItem.getTakeAccount());
				reqVo.setTakeType(orderItem.getTakeType());
			}
			
			new DefAsyncTask(OrderConfirmActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在提交订单数据...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getOrderRequest(OrderConfirmActy.this).doPayOrder(reqVo);
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
							OrderPayRespVo respVo = (OrderPayRespVo)parser.getRespObject();
							Intent intent = new Intent(OrderConfirmActy.this,OrderSignatureActy.class);
							intent.putExtra("orderid", orderid);
							intent.putExtra("ordertype", AvOrderType.code2value(ordertype));
							
							showToast("订单提交成功");
							startActivity(intent);
							finish();
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("订单提交失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
