package com.rxoa.zlpay.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rxoa.zlpay.BaseUIFragment;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.acty.AccountSecurityActy;
import com.rxoa.zlpay.acty.BankCardDetailActy;
import com.rxoa.zlpay.acty.BindMainCardActy;
import com.rxoa.zlpay.acty.BindOtherCardActy;
import com.rxoa.zlpay.acty.DeviceDetailActy;
import com.rxoa.zlpay.acty.MainHomeActy;
import com.rxoa.zlpay.acty.ReadCardActy;
import com.rxoa.zlpay.acty.RealNameAuthActy;
import com.rxoa.zlpay.acty.UserActivateActy;
import com.rxoa.zlpay.acty.UserInfoDetailActy;
import com.rxoa.zlpay.acty.UserLoginActy;
import com.rxoa.zlpay.adapter.CardListAdapter;
import com.rxoa.zlpay.adapter.DeviceListAdapter;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.entity.DeviceEntity;
import com.rxoa.zlpay.entity.UserAccInfo;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;

import com.rxoa.zlpay.security.DataManager;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;
import com.rxoa.zlpay.vo.UserLogoutReqVo;

public class AccManageFmt extends BaseUIFragment implements OnClickListener{
	private MainHomeActy mActivity = (MainHomeActy) this.getActivity();
	private ScrollView scrollView;
	
	private TextView tvName;
	private TextView tvUid;
	private LinearLayout barAccSafe;
	private LinearLayout barAccActive;
	private TextView tipAccActive;
	private ImageView ivBtnAccActive;
	
	private LinearLayout barRealName;
	private ImageView ivBtnRealName;
	private LinearLayout infoRealName;
	private TextView tvRealName;
	private TextView btnDetailRealName;
	private TextView btnNowRealName;
	private TextView tipRealName;
	
	private LinearLayout barMainCard;
	private ImageView ivBtnMainCard;
	private LinearLayout infoMainCard;
	private TextView tvMainCard;
	private TextView btnDetailMainCard;
	private TextView btnNowMainCard;
	private TextView tipMainCard;
	
	private LinearLayout barOtherCard;
	private ImageView ivBtnOtherCard;
	private LinearLayout infoOtherCard;
	private ListView lvOtherCard;
	private DeviceListAdapter deviceAdapter;
	private List<DeviceEntity> deviceItems;
	private TextView addNowOtherCard;
	private TextView tipOtherCard;
	
	private LinearLayout barDevice;
	private ImageView ivBtnDevice;
	private LinearLayout infoDevice;
	private ListView lvDevice;
	private CardListAdapter cardAdapter;
	private List<BankCardEntity> otherCardItems;
	private TextView addNowDevice;
	private TextView tipDevice;
	
	private LinearLayout barLogout;
	private UserAccInfo accInfo;
	private BankCardEntity mainCardInfo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		View resView = inflater.inflate(R.layout.fmt_home_acc, container, false);
		mActivity = (MainHomeActy) getActivity();
		return initView(resView);
	}
	
    public View initView(View view){
    	try{
    		setTitleText(view,R.string.title_acc);
    		scrollView = (ScrollView) view.findViewById(R.id.accmanage_scrollview);
    		
    		tvName = (TextView) view.findViewById(R.id.accmanage_realname_tv);
    		tvUid = (TextView) view.findViewById(R.id.accmanage_uid_tv);
    		barAccSafe = (LinearLayout) view.findViewById(R.id.accmanage_bar_accsafe);
    		
    		barAccActive = (LinearLayout) view.findViewById(R.id.accmanage_bar_accactive);
    		tipAccActive = (TextView) view.findViewById(R.id.accmanage_tip_accactive);
    		ivBtnAccActive = (ImageView) view.findViewById(R.id.accmanage_ivbtn_accactive);
    		
    		barRealName = (LinearLayout) view.findViewById(R.id.accmanager_bar_realname);
    		ivBtnRealName = (ImageView) view.findViewById(R.id.accmanage_ivbtn_realname);
    		infoRealName = (LinearLayout) view.findViewById(R.id.accmanage_info_realname);
    		tvRealName = (TextView) view.findViewById(R.id.accmanage_info_realname_tv);
    		btnDetailRealName = (TextView) view.findViewById(R.id.accmanage_realname_btndetail);
    		btnNowRealName = (TextView) view.findViewById(R.id.accmanage_btnadd_realname);
    		tipRealName = (TextView) view.findViewById(R.id.accmanage_tip_realname);
    		
    		barMainCard = (LinearLayout) view.findViewById(R.id.accmanage_bar_maincard);
    		ivBtnMainCard = (ImageView) view.findViewById(R.id.accmanage_ivbtn_maincard);
    		infoMainCard = (LinearLayout) view.findViewById(R.id.accmanage_info_maincard);
    		tvMainCard = (TextView) view.findViewById(R.id.accmanage_info_maincard_tv);
    		btnDetailMainCard = (TextView) view.findViewById(R.id.accmanage_maincard_btndetail);
    		btnNowMainCard = (TextView) view.findViewById(R.id.accmanage_btnadd_maincard);
    		tipMainCard = (TextView) view.findViewById(R.id.accmanage_tip_maincard);
    		
    		barOtherCard = (LinearLayout) view.findViewById(R.id.accmanage_bar_othercard);
    		ivBtnOtherCard = (ImageView) view.findViewById(R.id.accmanage_ivbtn_othercard);
    		infoOtherCard = (LinearLayout) view.findViewById(R.id.accmanage_info_othercard);
    		lvOtherCard = (ListView) view.findViewById(R.id.accmanage_list_othercard);
    		addNowOtherCard = (TextView) view.findViewById(R.id.accmanage_btnadd_othercard);
    		tipOtherCard = (TextView) view.findViewById(R.id.accmanage_tip_othercard);
    		
    		barDevice = (LinearLayout) view.findViewById(R.id.accmanage_bar_device);
    		ivBtnDevice = (ImageView) view.findViewById(R.id.accmanage_ivbtn_device);
    		infoDevice = (LinearLayout) view.findViewById(R.id.accmanage_info_device);
    		lvDevice = (ListView) view.findViewById(R.id.accmanage_list_device);
    		addNowDevice = (TextView) view.findViewById(R.id.accmanage_btnadd_device);
    		tipDevice = (TextView) view.findViewById(R.id.accmanage_tip_device);
    		barLogout = (LinearLayout) view.findViewById(R.id.more_bar_logout);
    		addListener();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return view;
    }
	@Override 
	public void setUserVisibleHint(boolean isVisibleToUser){
		super.setUserVisibleHint(isVisibleToUser); 
	    if (isVisibleToUser) { 
	    	syncAccoutInfo();
	    } else { 
	    	//相当于Fragment的onPause 
	    } 
	 }
    public void addListener(){
    	barAccSafe.setOnClickListener(this);
    	barAccActive.setOnClickListener(this);
    	
    	barRealName.setOnClickListener(this);
    	ivBtnRealName.setOnClickListener(this);
    	btnDetailRealName.setOnClickListener(this);
    	btnNowRealName.setOnClickListener(this);
    	
    	barMainCard.setOnClickListener(this);
    	ivBtnMainCard.setOnClickListener(this);
    	btnDetailMainCard.setOnClickListener(this);
    	btnNowMainCard.setOnClickListener(this);
    	
    	barOtherCard.setOnClickListener(this);
    	ivBtnOtherCard.setOnClickListener(this);
    	addNowOtherCard.setOnClickListener(this);
    	
    	barDevice.setOnClickListener(this);
    	ivBtnDevice.setOnClickListener(this);
    	addNowDevice.setOnClickListener(this);
    	barLogout.setOnClickListener(this);
    }
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.accmanager_bar_realname||id==R.id.accmanage_ivbtn_realname){
			showRealName();
		}else if(id==R.id.accmanage_bar_maincard||id==R.id.accmanage_ivbtn_maincard){
			showMainCard();
		}else if(id==R.id.accmanage_bar_othercard||id==R.id.accmanage_ivbtn_othercard){
			showOtherCard();
		}else if(id==R.id.accmanage_bar_device||id==R.id.accmanage_ivbtn_device){
			showDevice();
		}else if(id==R.id.accmanage_bar_accsafe||id==R.id.accmanage_ivbtn_accsafe){
			Intent intent = new Intent(getActivity(),AccountSecurityActy.class);
			startActivity(intent);
		}else if(id==R.id.accmanage_bar_accactive||id==R.id.accmanage_ivbtn_accactive){
			Intent intent = new Intent(getActivity(),UserActivateActy.class);
			startActivity(intent);
		}
		else if(id==R.id.accmanage_realname_btndetail){
			Intent intent = new Intent(getActivity(),UserInfoDetailActy.class);
			startActivity(intent);
		}else if(id==R.id.accmanage_btnadd_realname){
			Intent intent = new Intent(getActivity(),RealNameAuthActy.class);
			startActivity(intent);
		}else if(id==R.id.accmanage_maincard_btndetail){
			Bundle bundle = new Bundle();
			bundle.putSerializable("card", mainCardInfo);
			Intent intent = new Intent(getActivity(),BankCardDetailActy.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}else if(id==R.id.accmanage_btnadd_maincard){
			Intent intent = new Intent(getActivity(),BindMainCardActy.class);
			intent.putExtra("uname", tvName.getText().toString().trim());
			startActivity(intent);
		}else if(id==R.id.accmanage_btnadd_othercard){
			Intent intent = new Intent(getActivity(),BindOtherCardActy.class);
			startActivity(intent);
		}else if(id==R.id.accmanage_btnadd_device){
			Intent intent = new Intent(getActivity(),ReadCardActy.class);
			Bundle bundle = new Bundle();
			bundle.putString("usetype", "binddevice");
			intent.putExtras(bundle);
			startActivity(intent);
		}else if (id==R.id.more_bar_logout){
			showConfirmDialog();
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
					getActivity().finish();
					
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
	public void showRealName(){
		if(infoRealName.getVisibility()==View.VISIBLE){
			infoRealName.setVisibility(View.GONE);
			ivBtnRealName.setImageResource(R.drawable.accmanage_arrow_down);
		}else{
			infoRealName.setVisibility(View.VISIBLE);
			ivBtnRealName.setImageResource(R.drawable.accmanage_arrow_up);
		}
	}
	public void showMainCard(){
		if(infoMainCard.getVisibility()==View.VISIBLE){
			infoMainCard.setVisibility(View.GONE);
			ivBtnMainCard.setImageResource(R.drawable.accmanage_arrow_down);
		}else{
			infoMainCard.setVisibility(View.VISIBLE);
			ivBtnMainCard.setImageResource(R.drawable.accmanage_arrow_up);
		}
	}
	public void showOtherCard(){
		if(infoOtherCard.getVisibility()==View.VISIBLE){
			infoOtherCard.setVisibility(View.GONE);
			ivBtnOtherCard.setImageResource(R.drawable.accmanage_arrow_down);
		}else{
			infoOtherCard.setVisibility(View.VISIBLE);
			ivBtnOtherCard.setImageResource(R.drawable.accmanage_arrow_up);
		}
	}
	public void showDevice(){
		if(infoDevice.getVisibility()==View.VISIBLE){
			infoDevice.setVisibility(View.GONE);
			ivBtnDevice.setImageResource(R.drawable.accmanage_arrow_down);
		}else{
			infoDevice.setVisibility(View.VISIBLE);
			ivBtnDevice.setImageResource(R.drawable.accmanage_arrow_up);
		}
	}
	
	public void syncAccoutInfo(){
		try{
			if(!Config._isLogin){return;}
			final UserAccInfoReqVo reqVo = new UserAccInfoReqVo();
			mActivity = (MainHomeActy) this.getActivity();
			new DefAsyncTask(this.getActivity()){
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
						parser = ReqWrapper.getUserAccRequest(getActivity()).doQueryUserAccInfo(reqVo);
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
							mainCardInfo = ((UserAccInfoRespVo)parser.getRespObject()).getMainCardInfo();
							otherCardItems = ((UserAccInfoRespVo)parser.getRespObject()).getOtherCardInfo();
							deviceItems = ((UserAccInfoRespVo)parser.getRespObject()).getDeviceInfo();
							refreshViewUI();
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
	
	//======异步获取账户信息后显示列表信息=====
	private void refreshViewUI(){
		if(accInfo.getRealName()==null||accInfo.getRealName().equals("")||accInfo.getRealName().equals("null")){
			tvName.setText("未认证");
		}else if(accInfo.getRealName().equals("IS_CHECKING")){
			tvName.setText("正在审核");
		}else{
			tvName.setText(accInfo.getRealName());
		}
		tvUid.setText(accInfo.getUid());
		if(accInfo.getUstat()==null||accInfo.getUstat().equals("null")||accInfo.getUstat().equals("0")){
			tipAccActive.setText("账户激活(未激活)");
		}else{
			tipAccActive.setText("账户激活(已激活)");
			barAccActive.setOnClickListener(null);
			ivBtnAccActive.setVisibility(View.GONE);
		}
		if(accInfo.getRealName()==null||accInfo.getRealName().equals("null")||accInfo.getRealName().equals("")){
			tipRealName.setText("实名认证(未认证)");
			tvRealName.setVisibility(View.GONE);
			btnDetailRealName.setVisibility(View.GONE);
			btnNowRealName.setVisibility(View.VISIBLE);
		}else if(accInfo.getRealName().equals("IS_CHECKING")){
			tipRealName.setText("实名认证(正在审核)");
			tvRealName.setVisibility(View.GONE);
			btnDetailRealName.setVisibility(View.GONE);
			btnNowRealName.setVisibility(View.GONE);
		}else{
			tipRealName.setText("实名认证(已认证)");
    		tvRealName.setText(Html.fromHtml("真实姓名："+accInfo.getRealName()+"<br>身份证号："+accInfo.getIdNo()));
    		btnNowRealName.setText("重新认证");
    		btnNowRealName.setVisibility(View.GONE);
		}
		if(mainCardInfo==null||mainCardInfo.getAccName()==null){
			tipMainCard.setText("银行卡(未绑定)");
			tvMainCard.setVisibility(View.GONE);
			btnDetailMainCard.setVisibility(View.GONE);
			btnNowMainCard.setVisibility(View.VISIBLE);
		}else{
			tipMainCard.setText("银行卡(已绑定)");
    		tvMainCard.setText(Html.fromHtml("账户名："+mainCardInfo.getAccName()+"<br>账　号："+mainCardInfo.getAccNo()+"<br>开户行："+mainCardInfo.getAccBank()));
    		btnNowMainCard.setText("修改银行卡");
			btnDetailMainCard.setVisibility(View.VISIBLE);
			btnNowMainCard.setVisibility(View.VISIBLE);
		}
		tipOtherCard.setText("信用卡认证(共"+otherCardItems.size()+"张)");
		if(otherCardItems.size()==0){
			lvOtherCard.setVisibility(View.GONE);
			addNowOtherCard.setVisibility(View.VISIBLE);
		}else{
			cardAdapter = new CardListAdapter(getActivity(),otherCardItems);
			lvOtherCard.setAdapter(cardAdapter);
			setListViewHeightBasedOnChildren(lvOtherCard);
			lvOtherCard.setOnItemClickListener(new OnItemClickListener() {
				@Override  
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
				long arg3) {  
					if(arg2>=otherCardItems.size()){
						return;
					}
					Bundle bundle = new Bundle();
					bundle.putSerializable("card", otherCardItems.get(arg2));
					Intent intent = new Intent(getActivity(),BankCardDetailActy.class);
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
				}  
			});
		}
		
		tipDevice.setText("设备信息(共"+deviceItems.size()+"台)");
		//tipDevice.setText("设备信息");
		if(deviceItems.size() == 0){
			lvDevice.setVisibility(View.GONE);
			addNowDevice.setVisibility(View.VISIBLE);
		}else{
			addNowDevice.setVisibility(View.VISIBLE);
			addNowDevice.setText("添加设备");
			deviceAdapter = new DeviceListAdapter(getActivity(),deviceItems);
			lvDevice.setAdapter(deviceAdapter);
			setListViewHeightBasedOnChildren(lvDevice);
			lvDevice.setOnItemClickListener(new OnItemClickListener() {
				@Override  
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
				long arg3) {  
					if(arg2>=deviceItems.size()){
						return;
					}
					Bundle bundle = new Bundle();
					bundle.putSerializable("device", deviceItems.get(arg2));
					Intent intent = new Intent(getActivity(),DeviceDetailActy.class);
					intent.putExtras(bundle);
					getActivity().startActivity(intent);
				}  
			});
		}
		addNowDevice.setVisibility(View.GONE);
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
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        //listView.setSelection(listView.getBottom());
    }
}
