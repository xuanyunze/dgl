package com.rxoa.zlpay.acty;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvOrderStat;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.avator.AvPayType;
import com.rxoa.zlpay.avator.AvTakeAccType;
import com.rxoa.zlpay.avator.AvArriveType;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.ImageUtil;
import com.rxoa.zlpay.entity.OrderPhoneCharge;
import com.rxoa.zlpay.entity.OrderReceiveMoney;
import com.rxoa.zlpay.entity.OrderTakeMoney;
import com.rxoa.zlpay.entity.OrderWrapper;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.util.AvatorUtil;
import com.rxoa.zlpay.vo.OrderDetailReqVo;
import com.rxoa.zlpay.vo.OrderDetailRespVo;

public class OrderDetailActy extends BaseUIActivity implements OnClickListener{
	private ViewHolder viewHolder = new ViewHolder();
	private String orderid = null;
	private String ordertype = null;
	private String orderstat = null;
	private File sigFile;
	private OrderDetailRespVo respVo = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		orderid = intent.getStringExtra("orderid");
		ordertype = intent.getStringExtra("ordertype");
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_order_detail);
		setTitleText(R.string.title_order_detail);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		viewHolder.barPayType = (LinearLayout) findViewById(R.id.order_detail_paytype_bar);
		viewHolder.barPayAccount = (LinearLayout) findViewById(R.id.order_detail_payaccno_bar);
		viewHolder.tvOrderId = (TextView) findViewById(R.id.order_detail_flowid_tv);
		viewHolder.tvOrderType = (TextView) findViewById(R.id.order_detail_type_tv);
		viewHolder.tvOrderDate = (TextView) findViewById(R.id.order_detail_date_tv);
		viewHolder.tvOrderValue = (TextView) findViewById(R.id.order_detail_value_tv);
		viewHolder.tvOrderStat = (TextView) findViewById(R.id.order_detail_stat_tv);
		viewHolder.tvPayType = (TextView) findViewById(R.id.order_detail_paytype_tv);
		viewHolder.tvPayAccNo = (TextView) findViewById(R.id.order_detail_payaccno_tv);
	
		viewHolder.tvReceAccName = (TextView) findViewById(R.id.order_content_receaccname_tv);
		viewHolder.tvReceAccNo = (TextView) findViewById(R.id.order_content_receaccno_tv);
		viewHolder.tvReceBankName = (TextView) findViewById(R.id.order_content_recebankname_tv);
		viewHolder.tvReceBankDistr = (TextView) findViewById(R.id.order_content_recebankdistr_tv);
		viewHolder.tvReceBankBranch = (TextView) findViewById(R.id.order_content_recebankbranch_tv);
		viewHolder.tvNotifyPhone = (TextView) findViewById(R.id.order_content_notifyphone_tv);
		viewHolder.tvChargePhone = (TextView) findViewById(R.id.order_content_phoneno_tv);
		//viewHolder.tvTakeAccount = (TextView) findViewById(R.id.order_content_takeacc_tv);
		//viewHolder.tvTakeType = (TextView) findViewById(R.id.order_content_taketype_tv);

		viewHolder.tvTipValue = (TextView) findViewById(R.id.order_detail_value_label);
		viewHolder.ivSignature = (ImageView) findViewById(R.id.order_detail_sigimg);
		viewHolder.btnNext = (Button) findViewById(R.id.order_detail_btn_next);
		getOrderDetail();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.order_detail_btn_next){
			if(orderstat.equals(AvOrderStat.code2value(AvOrderStat.code.UNPAY.name()))){
				Intent intent = new Intent(OrderDetailActy.this,PayTypeSelectActy.class);
				wrapOrder(intent,respVo);
			}else if(orderstat.equals(AvOrderStat.code2value(AvOrderStat.code.UNSIG.name()))){
				Intent intent = new Intent(OrderDetailActy.this,OrderSignatureActy.class);
				intent.putExtra("orderid", respVo.getOrderFlowid());
				intent.putExtra("ordertype", respVo.getOrderType());
				startActivity(intent);
				finish();
			}
		}
	}
	public void getOrderDetail(){
		try{
			if(!Config._isLogin){return;}
			final OrderDetailReqVo reqVo = new OrderDetailReqVo();
			reqVo.setOrderFlowid(orderid);
			reqVo.setOrderType(ordertype);

			new DefAsyncTask(OrderDetailActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在加载订单数据...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getOrderRequest(OrderDetailActy.this).doQueryOrderDetail(reqVo);
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
							respVo = (OrderDetailRespVo)parser.getRespObject();
							orderid = respVo.getOrderFlowid();
							ordertype = respVo.getOrderType();
							orderstat = respVo.getOrderStat();
							refreshViewUI(respVo);
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("订单加载失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void refreshViewUI(OrderDetailRespVo respVo){
		try{
			if(respVo.getOrderSigImgstr()!=null&&!respVo.getOrderSigImgstr().equals("null")){
				ImageUtil.GenerateImage(respVo.getOrderSigImgstr(), Config.path_sig+respVo.getOrderFlowid()+"_sig.jpg");
				this.sigFile = new File(Config.path_sig,respVo.getOrderFlowid()+"_sig.jpg");
				Bitmap bitmpgSig = ImageUtil.optimizeBitmap(ImageUtil.readStream(this.sigFile), 400, 380);
				viewHolder.ivSignature.setImageBitmap(bitmpgSig);
			}
			viewHolder.tvOrderId.setText(respVo.getOrderFlowid());
			viewHolder.tvOrderType.setText(AvOrderType.value2text(respVo.getOrderType()));
			viewHolder.tvOrderDate.setText(respVo.getOrderDate());
			viewHolder.tvOrderValue.setText(respVo.getOrderValue());
			viewHolder.tvOrderStat.setText(AvOrderStat.value2text(respVo.getOrderStat()));
			viewHolder.tvPayType.setText(AvPayType.value2text(respVo.getPayType()));
			viewHolder.tvPayAccNo.setText(AvatorUtil.dealNull(respVo.getPayAccNo()));
			if(ordertype.equals("1")){
				viewHolder.tvTipValue.setText("收款金额：");
			}else if(ordertype.equals("2")){
				viewHolder.tvTipValue.setText("提现金额：");
				findViewById(R.id.order_detail_content_takemoney).setVisibility(View.VISIBLE);
				viewHolder.tvTakeAccount.setText(AvTakeAccType.value2text(((OrderTakeMoney)respVo.getOrderItem()).getTakeAccount()));
				viewHolder.tvTakeType.setText(AvArriveType.value2text(((OrderTakeMoney)respVo.getOrderItem()).getTakeType()));
				viewHolder.barPayType.setVisibility(View.GONE);
				viewHolder.barPayAccount.setVisibility(View.GONE);
			}else if(ordertype.equals("6")){
				viewHolder.tvTipValue.setText("充值金额：");
			}
			String ostat = respVo.getOrderStat();
			if(ostat.equals(AvOrderStat.code2value(AvOrderStat.code.UNPAY.name()))){
				viewHolder.btnNext.setText("前往支付");
				viewHolder.btnNext.setOnClickListener(this);
				viewHolder.btnNext.setVisibility(View.VISIBLE);
			}else if(ostat.equals(AvOrderStat.code2value(AvOrderStat.code.UNSIG.name()))){
				viewHolder.btnNext.setText("现在签名");
				viewHolder.btnNext.setOnClickListener(this);
				viewHolder.btnNext.setVisibility(View.VISIBLE);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static class ViewHolder{
		LinearLayout barPayType;
		LinearLayout barPayAccount;
		
		TextView tvOrderId;
		TextView tvOrderType;
		TextView tvOrderValue;
		TextView tvOrderDate;
		TextView tvOrderStat;
		TextView tvPayType;
		TextView tvPayAccNo;
		
		TextView tvReceAccName;
		TextView tvReceAccNo;
		TextView tvReceBankName;
		TextView tvReceBankDistr;
		TextView tvReceBankBranch;
		TextView tvNotifyPhone;
		TextView tvChargePhone;
		TextView tvTakeAccount;
		TextView tvTakeType;
		
		TextView tvTipValue;
		ImageView ivSignature;
		Button btnNext;
	}
	public void wrapOrder(Intent intent,OrderDetailRespVo respVo){
		OrderWrapper wrapper = OrderWrapper.getInstance();
		wrapper.setOrderId(respVo.getOrderFlowid());
		wrapper.setOrderType(AvOrderType.value2code(respVo.getOrderType()));
		wrapper.setOrderValue(respVo.getOrderValue());
		if(ordertype.equals(AvOrderType.code2value(AvOrderType.code.ReceiveMoney.name()))){
			wrapper.setOrderItem((OrderReceiveMoney)respVo.getOrderItem());
		}else if(ordertype.equals(AvOrderType.code2value(AvOrderType.code.ZeroReceiveMoney.name()))){
			wrapper.setOrderItem((OrderReceiveMoney)respVo.getOrderItem());
		}else if(ordertype.equals(AvOrderType.code2value(AvOrderType.code.TakeMoney.name()))){
			
		}else if(ordertype.equals(AvOrderType.code2value(AvOrderType.code.PhoneCharge.name()))){
			wrapper.setOrderItem((OrderPhoneCharge)respVo.getOrderItem());
		}
		System.out.println("zzz=="+((OrderReceiveMoney)respVo.getOrderItem()).getPayFeeCode());
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", wrapper);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	public void btnNextGo(){
		
	}
}
