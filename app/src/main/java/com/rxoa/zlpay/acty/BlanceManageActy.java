package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.BlanceQueryReqVo;
import com.rxoa.zlpay.vo.BlanceQueryRespVo;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;

public class BlanceManageActy extends BaseUIActivity implements OnClickListener{
	private TextView btnTakenow;
	private TextView btnApplynow;
	
	private TextView tvName;
	private TextView tvPhone;
	
	private TextView tvBlanceAll;
	private TextView tvBlanceRe;
	private TextView tvBlanceZero;
	private TextView tvBlanceTake;
	private TextView tvBlanceTakeleft;
	
	private TextView tvLimitAll;
	private TextView tvLimitT0;
	private TextView tvLimitTran;
	private Double takeLeft;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_blance);
		setTitleText("我的钱包");
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		tvPhone = (TextView) findViewById(R.id.blance_uid_tv);
		tvName = (TextView) findViewById(R.id.blance_realname_tv);
		
		tvBlanceAll = (TextView) findViewById(R.id.blance_acc_tv_allleft);
		tvBlanceRe = (TextView) findViewById(R.id.blance_acc_tv_re);
		tvBlanceZero = (TextView) findViewById(R.id.blance_acc_tv_zero);
		tvBlanceTake = (TextView) findViewById(R.id.blance_acc_tv_alltake);
		tvBlanceTakeleft = (TextView) findViewById(R.id.blance_acc_tv_takeleft);
		
		btnTakenow = (TextView) findViewById(R.id.blance_takenow);
		btnApplynow = (TextView) findViewById(R.id.blance_applynow);
		btnTakenow.setOnClickListener(this);
		btnApplynow.setOnClickListener(this);
		syncBlanceInfo();
	}
	public void syncBlanceInfo(){
		try{
			if(!Config._isLogin){return;}
			final BlanceQueryReqVo reqVo = new BlanceQueryReqVo();
			new DefAsyncTask(BlanceManageActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在加载账户数据...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getBlanceRequest(BlanceManageActy.this).doQueryBlance(reqVo);
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
							BlanceQueryRespVo blance = (BlanceQueryRespVo) parser.getRespObject();
							refreshUI(blance);
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("账户信息加载失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void refreshUI(BlanceQueryRespVo blance){
		if(!StringUtil.isDbNull(blance.getUname())){
			tvName.setText(blance.getUname());
		}
		tvPhone.setText(blance.getUid());
		tvBlanceAll.setText(StringUtil.format(blance.getBlanceReValue()+blance.getBlanceZreValue()+blance.getBlanceTakeValue()));
		tvBlanceRe.setText(StringUtil.format(blance.getBlanceReValue()));
		tvBlanceZero.setText(StringUtil.format(blance.getBlanceZreValue()));
		tvBlanceTake.setText(StringUtil.format(blance.getBlanceTakeValue()));
		this.takeLeft = blance.getBlanceReValue();
		tvBlanceTakeleft.setText(StringUtil.format(this.takeLeft)+"");
	}
	@Override
	public void onClick(View vw) {
		int id = vw.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.blance_takenow){
			Intent intent = new Intent(BlanceManageActy.this,TakeMoneyActy.class);
			intent.putExtra("takeLeft", StringUtil.format(this.takeLeft));
			startActivity(intent);
			finish();
		}
	}

}
