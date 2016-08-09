package com.rxoa.zlpay.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rxoa.zlpay.BaseUIFragment;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.acty.AboutUsActy;
import com.rxoa.zlpay.acty.AccountSecurityActy;
import com.rxoa.zlpay.acty.HelpActy;
import com.rxoa.zlpay.acty.MainHomeActy;
import com.rxoa.zlpay.acty.UserLoginActy;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.heip.BanbenActivity;
import com.rxoa.zlpay.heip.GuideActivity;
import com.rxoa.zlpay.heip.NewsActivity;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.security.DataManager;
import com.rxoa.zlpay.vo.UserLogoutReqVo;

public class MoreInfoFmt extends BaseUIFragment implements OnClickListener{
	private MainHomeActy mActivity = null;
	
	private LinearLayout barAccSafe;
	private LinearLayout barMsg;
	private LinearLayout barLogout;
	private LinearLayout barHelp;
	private LinearLayout barAbout;
	private LinearLayout barBanben;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		View resView = inflater.inflate(R.layout.fmt_home_more, container, false);
		mActivity = (MainHomeActy) getActivity();
		return initView(resView);
	}
	
    public View initView(View view){
    	try{
    		setTitleText(view,R.string.title_more);
    		barAccSafe = (LinearLayout) view.findViewById(R.id.more_bar_accsafe);
    		barMsg = (LinearLayout) view.findViewById(R.id.more_bar_notify);
    		barLogout = (LinearLayout) view.findViewById(R.id.more_bar_logout);
    		barHelp = (LinearLayout) view.findViewById(R.id.more_bar_helpme);
    		barAbout = (LinearLayout) view.findViewById(R.id.more_bar_aboutme);
    		barBanben=(LinearLayout) view.findViewById(R.id.more_bar_banben);
    		
    		barAccSafe.setOnClickListener(this);
    		barMsg.setOnClickListener(this);
    		barLogout.setOnClickListener(this);
    		barHelp.setOnClickListener(this);
    		barAbout.setOnClickListener(this);
    		barBanben.setOnClickListener(this);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return view;
    }

	@Override
	public void onClick(View view) {
		int id = view.getId();
		
		if(id==R.id.more_bar_notify){
			Intent intent = new Intent(getActivity(),NewsActivity.class);
			startActivity(intent);
		}else 
		if(id==R.id.more_bar_accsafe){
			Intent intent = new Intent(getActivity(),GuideActivity.class);
			startActivity(intent);
			
		}else if(id==R.id.more_bar_helpme){
			Intent intent = new Intent(getActivity(),HelpActy.class);
			startActivity(intent);
		}else if(id==R.id.more_bar_aboutme){
			Intent intent = new Intent(getActivity(),AboutUsActy.class);
			startActivity(intent);
		}else if(id==R.id.more_bar_logout){
			
		}else if (id==R.id.more_bar_banben) {
			Intent intent =new Intent(getActivity(), BanbenActivity.class);
			startActivity(intent);
		}
		

		
		}


	public void doLogout(){
		try{	
			final UserLogoutReqVo reqVo = new UserLogoutReqVo();
			new DefAsyncTask(getActivity()){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在提交请求，请稍等");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					parser = ReqWrapper.getUserRequest(getActivity()).doUserLogout(reqVo);
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						showToast("已退出系统");
					}else if(parser.getRespCode()==1){
						showToast("网络出错了");
					}else{
						showToast("您的操作太频繁了");
					}
					DataManager.doLogoutSet();
					Intent intent = new Intent(getActivity(),UserLoginActy.class);
					startActivity(intent);
				}
				
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	protected void showConfirmDialog() {
		Builder builder = new Builder(getActivity());
		builder.setMessage("您要退出系统吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				doLogout();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}); 
		builder.create().show();
	}
}
