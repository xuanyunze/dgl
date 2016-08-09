package com.rxoa.zlpay.acty;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.entity.UserAccInfo;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;

import com.rxoa.zlpay.util.AvatorUtil;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;

public class UserInfoDetailActy extends BaseUIActivity implements OnClickListener{
	private UserAccInfo accInfo;
	
	private TextView tvUid;
	private TextView tvRegTime;
	private TextView tvStat;
	private TextView tvRealName;
	private TextView tvIdType;
	private TextView tvIdNo;
	private TextView tvAuthTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_user_info);
		setTitleText(R.string.title_user_info);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
		tvUid = (TextView) findViewById(R.id.userinfo_uid_tv);
		tvRegTime = (TextView) findViewById(R.id.userinfo_regtime_tv);
		tvStat = (TextView) findViewById(R.id.userinfo_stat_tv);
		tvRealName = (TextView) findViewById(R.id.userinfo_realname_tv);
		tvIdType = (TextView) findViewById(R.id.userinfo_idtype_tv);
		tvIdNo = (TextView) findViewById(R.id.userinfo_idno_tv);
		tvAuthTime = (TextView) findViewById(R.id.userinfo_authtime_tv);
		
		syncUserInfo();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
	}
	
	public void syncUserInfo(){
		try{
			if(!Config._isLogin){return;}
			final UserAccInfoReqVo reqVo = new UserAccInfoReqVo();
			new DefAsyncTask(UserInfoDetailActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在加载数据...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getUserAccRequest(UserInfoDetailActy.this).doQueryUserAcc(reqVo);
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
							accInfo = ((UserAccInfoRespVo)parser.getRespObject()).getAccInfo();
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
	//======异步获取账户信息后显示列表信息=====
	private void refreshViewUI(){
		try{
			tvUid.setText(accInfo.getUid());
			tvRegTime.setText(accInfo.getRegTime());
			tvStat.setText(AvatorUtil.ustatV2T(accInfo.getUstat()));
			tvRealName.setText(accInfo.getRealName());
			tvIdType.setText(AvatorUtil.idCardTypeV2T(accInfo.getIdType()));
			tvIdNo.setText(accInfo.getIdNo());
			tvAuthTime.setText(accInfo.getAuthTime());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
