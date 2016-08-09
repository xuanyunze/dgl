package com.rxoa.zlpay.acty;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvArriveStat;
import com.rxoa.zlpay.avator.AvArriveType;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.OrderDetailReqVo;
import com.rxoa.zlpay.vo.OrderDetailRespVo;
import com.rxoa.zlpay.vo.OrderTakemoneyDetailReqVo;
import com.rxoa.zlpay.vo.OrderTakemoneyDetailRespVo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OrderTakemoneyDetailActy extends BaseUIActivity implements OnClickListener{
	private String orderid = null;
	private String ordertype = null;
	private String orderstat = null;
	private TextView tvOrderid;
	private TextView tvOrdertype;
	private TextView tvOrdertime;
	private TextView tvOrderstat;
	private TextView tvTakevalue;
	private TextView tvArrivetype;
	private TextView tvArrivedate;
	private TextView tvArrivevalue;
	private TextView tvBenefitvalue;
	private TextView tvArriveaccno;
	private TextView tvArriveaccname;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		orderid = intent.getStringExtra("orderid");
		ordertype = intent.getStringExtra("ordertype");
		initView();
	}
	private void initView(){
		setContentView(R.layout.acty_order_takemoney_detail);
		setTitleText(R.string.title_order_detail);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
		tvOrderid = (TextView) findViewById(R.id.order_takemoney_detail_flowid_tv);
		tvOrdertype = (TextView) findViewById(R.id.order_takemoney_detail_type_tv);
		tvOrdertime = (TextView) findViewById(R.id.order_takemoney_detail_date_tv);
		tvOrderstat = (TextView) findViewById(R.id.order_takemoney_detail_stat_tv);
		tvTakevalue = (TextView) findViewById(R.id.order_takemoney_takevalue);
		tvArrivetype = (TextView) findViewById(R.id.order_takemoney_arrivetype);
		tvArrivedate = (TextView) findViewById(R.id.order_takemoney_arrivedate);
		tvArrivevalue = (TextView) findViewById(R.id.order_takemoney_arrivevalue);
		tvBenefitvalue = (TextView) findViewById(R.id.order_takemoney_benefitvalue);
		tvArriveaccno = (TextView) findViewById(R.id.order_takemoney_arriveaccno);
		tvArriveaccname = (TextView) findViewById(R.id.order_takemoney_arriveaccname);
		
		getOrderDetail();
	}
	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
		
	}
	public void getOrderDetail(){
		try{
			if(!Config._isLogin){return;}
			final OrderTakemoneyDetailReqVo reqVo = new OrderTakemoneyDetailReqVo();
			reqVo.setOrderFlowid(orderid);
			reqVo.setOrderType(ordertype);

			new DefAsyncTask(OrderTakemoneyDetailActy.this){
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
						parser = ReqWrapper.getOrderRequest(OrderTakemoneyDetailActy.this).doQueryOrderTakemoneyDetail(reqVo);
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
							OrderTakemoneyDetailRespVo respVo = (OrderTakemoneyDetailRespVo)parser.getRespObject();
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
	public void refreshViewUI(OrderTakemoneyDetailRespVo respVo){
		tvOrderid.setText(respVo.getOrderId());
		tvOrdertype.setText(AvOrderType.value2text(respVo.getOrderType()));
		tvOrdertime.setText(respVo.getOrderTime());
		tvOrderstat.setText(AvArriveStat.value2text(respVo.getOrderStat()));
		
		tvTakevalue.setText(respVo.getTakeValue());
		
		tvArrivetype.setText(AvArriveType.value2text(respVo.getArriveType()));
		tvArrivedate.setText(respVo.getArriveDate());
		tvArrivevalue.setText(respVo.getArriveValue());
		tvBenefitvalue.setText(respVo.getBenefitValue());
		tvArriveaccno.setText(StringUtil.hideAccno(respVo.getArriveAccno()));
		tvArriveaccname.setText(StringUtil.hideName(respVo.getArriveAccname()));
	}
}
