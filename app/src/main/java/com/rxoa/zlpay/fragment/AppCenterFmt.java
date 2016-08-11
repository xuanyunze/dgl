package com.rxoa.zlpay.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIFragment;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.MainApplication;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.acty.AboutUsActy;
import com.rxoa.zlpay.acty.BindMainCardActy;
import com.rxoa.zlpay.acty.BindOtherCardActy;
import com.rxoa.zlpay.acty.BlanceManageActy;
import com.rxoa.zlpay.acty.MoneyInputCalActy;
import com.rxoa.zlpay.acty.RealNameAuthActy;
import com.rxoa.zlpay.acty.ReceiveMoneyConfirmActy;
import com.rxoa.zlpay.acty.TransAccInputActy;
import com.rxoa.zlpay.acty.FeedBackActy;
import com.rxoa.zlpay.acty.MainHomeActy;
import com.rxoa.zlpay.acty.MoneyInputActy;
import com.rxoa.zlpay.acty.PhoneChargeInputActy;
import com.rxoa.zlpay.acty.ReadCardActy;
import com.rxoa.zlpay.acty.RepayCreditInputActy;
import com.rxoa.zlpay.acty.TakeMoneyInputActy;
import com.rxoa.zlpay.acty.UserActivateActy;
import com.rxoa.zlpay.acty.UserLoginActy;
import com.rxoa.zlpay.acty.WelcomeActy;
import com.rxoa.zlpay.adapter.GridViewAdapter;
import com.rxoa.zlpay.base.BaseViewFlipper;
import com.rxoa.zlpay.base.BaseViewFlipper.OnFlipListener;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.HttpUtil;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.combx.LocationParser;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.security.AuthChecker;
import com.rxoa.zlpay.util.VersionMgr;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;

public class AppCenterFmt extends BaseUIFragment implements OnClickListener{
	public static final String TAG = AppCenterFmt.class.getName();
	private MainHomeActy mActivity = null;
	private BaseViewFlipper bannerFlipper;
	private ImageView[] bannerImageViews;
	private RadioGroup bannerRadioGroup;
	private int[] imgIds = {R.drawable.guagao,
			R.drawable.guagao,
			R.drawable.guagao};
	private int[] pointIds = {R.id.banner_radioButton_flipperPoint0,
			R.id.banner_radioButton_flipperPoint1,
			R.id.banner_radioButton_flipperPoint2};
	private int flipperCount = pointIds.length;
	
    private String appTexts[] = null;
    private int appImages[] = null;
	private TextView tvLocation;
	private boolean isUpdateCheck = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		View resView = inflater.inflate(R.layout.fmt_home_app, container, false);
		mActivity = (MainHomeActy) getActivity();
		return initView(resView);
	}
	
    public View initView(View view){
		MainApplication instance = MainApplication.getInstance();
		try{
    		bannerFlipper = (BaseViewFlipper) view.findViewById(R.id.banner_viewFlipper);
    		bannerRadioGroup = (RadioGroup) view.findViewById(R.id.banner_radioGroup);
    		bannerFlipper.setOnFlipListener(bannerFlipListener);
    		addAdFlipperImageViews();   		
    		tvLocation =  (TextView) view.findViewById(R.id.home_app_location);
            appImages=new int[]{
            		R.drawable.home_app_acccharge, 
            		R.drawable.home_app_receivemoney,
            		//R.drawable.home_app_querybalance,
            		
            		R.drawable.home_app_takemoney,
            		R.drawable.home_app_acctrans,
            		R.drawable.home_app_repaycredit,
            		
            		//R.drawable.home_app_phonecharge,
            		//R.drawable.home_app_feedback,
            		
            		//R.drawable.home_app_feedback,
            		R.drawable.home_app_aboutus
            	};
            //appTexts = new String[]{
            //		"我要收款", "帐户充值", "余额查询", 
            //		"我要提现", "卡卡转账", "还信用卡",
            //        "手机充值", "意见反馈","关于我们"};
            appTexts = new String[]{
            		"快速收款", "普通收款",
                    "实名认证","信用卡认证","余额查询","更多"};
    	    GridView gridview = (GridView) view.findViewById(R.id.brainheroall);
    	        
    	    	ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
    	        for (int i = 0; i < appTexts.length; i++) {
    	            HashMap<String, Object> map = new HashMap<String, Object>();
    	            map.put("itemImage", appImages[i]);
    	            map.put("itemText", appTexts[i]);
    	            lstImageItem.add(map);
    	        }
    	        
    	        SimpleAdapter saImageItems = new SimpleAdapter(this.getActivity(),
    	                lstImageItem,
    	                R.layout.home_app_item,// 显示布局
    	                new String[] { "itemImage", "itemText" }, 
    	                new int[] { R.id.ItemImage, R.id.ItemText });
    	        
    	        
    	        GridViewAdapter gAdapter = new GridViewAdapter(this.getActivity(),lstImageItem);
    	        //gridview.setAdapter(saImageItems);
    	        gridview.setAdapter(gAdapter);
    	        gridview.setOnItemClickListener(new ItemClickListener());
    	        if(!isUpdateCheck){
    	        	VersionMgr.getInstance(getActivity()).checkVersion();isUpdateCheck = true;
    	        }
    			LocationParser.getInstance(getActivity());
    			runLocation();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return view;
    }
	public void resetName(){
        new Thread(new Runnable(){
			@Override
			public void run() {
				String name = HttpUtil.getCityName(Config.location_lat+"", Config.location_lng+"");
				if(name!=null){
					Config.location_string = name;
				}
			}
        	
        }).start();
	}
    public void runLocation(){
    	new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try{
						Thread.sleep(5*1000);
					}catch(Exception e){
						e.printStackTrace();
					}
					try{
						//resetName();
						refreshLocation();
					}catch(Exception e){
						e.printStackTrace();
					}
					try{
						Thread.sleep(50*1000);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
    	}).start();
    }
    public void refreshLocation(){
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(!StringUtil.isDbNull(Config.location_string)&&Config.location_string.indexOf("|")!=-1){
					String[] names = Config.location_string.split("\\|");
					if(!StringUtil.isDbNull(names[2])){
						tvLocation.setText("您的位置："+names[2]);
					}
				}
			}
		});
    }
    public void checkUserStat(final Intent intent,final String type){
    	try{
    		final UserAccInfoReqVo reqVo = new UserAccInfoReqVo();
			new DefAsyncTask(mActivity){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在校验用户信息");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					parser = ReqWrapper.getUserAccRequest(mActivity).doQueryUserAcc(reqVo);
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						UserAccInfoRespVo respVo = (UserAccInfoRespVo) parser.getRespObject();
						String stat = respVo.getAccInfo().getUstat();
						String uicard = respVo.getAccInfo().getIdNo();
						BankCardEntity mcard = respVo.getMainCardInfo();
						/*List<BankCardEntity> olist = respVo.getOtherCardInfo();*/
						List<BankCardEntity> olist= respVo.getOtherCardInfo();
						if(type==null){
							if(!stat.equals("1")){
								showToast("请先激活账号");
								return;
							}
							if(StringUtil.isDbNull(uicard)||StringUtil.isDbNull(respVo.getAccInfo().getRealName())||respVo.getAccInfo().getRealName().equals("IS_CHECKING")){
								showToast("请先完成实名认证");
								return;
							}
							if(mcard==null||mcard.getAccName()==null){
								showToast("请先去绑定银行卡");
								return;
							}
							startActivity(intent);
						}
						else if(type.equals("realname")){
							if((uicard==null||uicard.equals("null")||uicard.equals(""))&&StringUtil.isDbNull(respVo.getAccInfo().getRealName())){
								startActivity(intent);
							}else if(respVo.getAccInfo().getRealName().equals("IS_CHECKING")){
								showToast("您的认证信息正在审核中");
							}else{
								showToast("您已经完成实名认证");
							}
						}
						else if(type.equals("maincard")||type.equals("othercard")){
							if(StringUtil.isDbNull(uicard)||StringUtil.isDbNull(respVo.getAccInfo().getRealName())||respVo.getAccInfo().getRealName().equals("IS_CHECKING")){
								showToast("请先完成实名认证");
								return;
							}else{
								startActivity(intent);
							}
						}else if(type.equals("queryblance")){
							startActivity(intent);
						}else if(type.equals("payorder")){
							if(StringUtil.isDbNull(uicard)||StringUtil.isDbNull(respVo.getAccInfo().getRealName())||respVo.getAccInfo().getRealName().equals("IS_CHECKING")){
								showToast("请先完成实名认证");
								return;
							}
							if(mcard==null||mcard.getAccName()==null){
								showToast("请先去绑定银行卡");
			        			Intent xintent = new Intent(getActivity(),BindMainCardActy.class);
			        			startActivity(xintent);
			        			return;
							}else if (olist==null||olist.size()==0) {
								intent.putExtra("maxValue", 20000D);
							}
							/*if (olist==null||olist.size()==0) {
								intent.putExtra("maxValue", 20000D);
							}*/
							startActivity(intent);
						}else {
							if(type.equals("payorde")){
								if(StringUtil.isDbNull(uicard)||StringUtil.isDbNull(respVo.getAccInfo().getRealName())||respVo.getAccInfo().getRealName().equals("IS_CHECKING")){
									showToast("请先完成实名认证");
									return;
								}
								if(mcard==null||mcard.getAccName()==null){
									showToast("请先去绑定银行卡");
				        			Intent xintent = new Intent(getActivity(),BindMainCardActy.class);
				        			startActivity(xintent);
				        			return;
								}
								/*if (olist==null||olist.size()==0) {
									intent.putExtra("maxValue", 20000D);
								}*/
								startActivity(intent);
							}
						}
					}else{
						showToast("用户信息校验失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    @Override
    public void onClick(View v) {
    	try{
    		int id  = v.getId();
    		if(id==R.id.titleRightButton){
    			
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    private ImageView makeAdFlipperImageView() {
    	ImageView i = new ImageView(this.getActivity());
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ScaleType.FIT_CENTER);
        i.setLayoutParams(new BaseViewFlipper.LayoutParams(
        		BaseViewFlipper.LayoutParams.MATCH_PARENT,
                BaseViewFlipper.LayoutParams.MATCH_PARENT));
        return i;
     }
    private void addAdFlipperImageViews() {
    	bannerImageViews = new ImageView[flipperCount];
        for (int i = 0; i < flipperCount; i++) {
        	ImageView imageView = makeAdFlipperImageView();
            imageView.setImageResource(imgIds[i]);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setOnClickListener(bannerImageViewListener);
            bannerImageViews[i] = imageView;
            bannerFlipper.addView(imageView);
        }
     }
    private OnFlipListener bannerFlipListener = new OnFlipListener() {
    	
    	@Override
    	public void onShowPrevious(BaseViewFlipper flipper) {
    		bannerRadioGroup.check(pointIds[flipper.getDisplayedChild()]);
    	}

		@Override
		public void onShowNext(BaseViewFlipper flipper) {
			bannerRadioGroup.check(pointIds[flipper.getDisplayedChild()]);
		}
	};
	private OnClickListener bannerImageViewListener = new OnClickListener() {        
		@Override
		public void onClick(View v) {
			//Toast.makeText(HomeActivity.this, "广告 " + adViewFlipper.getDisplayedChild(), Toast.LENGTH_SHORT).show();
		}
	};
	
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position, long rowid) {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
			//获取数据源的属性值
			String itemText=(String)item.get("itemText");
			Object object=item.get("itemImage");
			if(position!=8&&!AuthChecker.checkLoginWithToast(mActivity)){
				Intent intent = new Intent(getActivity(),UserLoginActy.class);
				startActivity(intent);
				return;
			}
			Intent intent = null;
        	switch(position){
       		case 0:
       			Date dt= new Date();
       			SimpleDateFormat sdf =new SimpleDateFormat("HH");
       			String time=sdf.format(dt);
       			int i=Integer.parseInt(time);
       			if (i>5&&i<23) {
       				intent = new Intent(getActivity(),MoneyInputCalActy.class);
    				intent.putExtra("usetype", "s0receivemoney");
    				intent.putExtra("minValue", 0D);
    				intent.putExtra("maxValue", 50000D);
    				//intent.putExtra("min", "信用卡认证后为0.00-50000.00");
    				checkUserStat(intent,"payorder");
       			} else {
       				showToast("非实时交易时间，请使用普通收款。");
       			}
    			/*intent = new Intent(getActivity(),MoneyInputCalActy.class);
				intent.putExtra("usetype", "s0receivemoney");
				intent.putExtra("minValue", 0D);
				intent.putExtra("maxValue", 20000D);
				checkUserStat(intent,"payorder");*/
				//startActivity(intent);
    			break;
    		case 1:
    			intent = new Intent(getActivity(),MoneyInputCalActy.class);
				intent.putExtra("usetype", "commonreceivemoney");
				intent.putExtra("minValue", 0D);
				intent.putExtra("maxValue", 50000D);
				checkUserStat(intent,"payorde");
				//startActivity(intent);
    			break;
        		/*
        		case 0:
        			intent = new Intent(getActivity(),MoneyInputCalActy.class);
    				intent.putExtra("usetype", "receivemoney");
    				intent.putExtra("minValue", 0D);
    				intent.putExtra("maxValue", 100000D);
    				//checkUserStat(intent);
    				startActivity(intent);
        			break;
        		case 1:
        			intent = new Intent(getActivity(),MoneyInputCalActy.class);
    				intent.putExtra("usetype", "zeroreceivemoney");
    				intent.putExtra("minValue", 10000D);
    				intent.putExtra("maxValue", 30000D);
    				//checkUserStat(intent);
    				startActivity(intent);
        			break;
        		*/
        		/*case 2:
        			showToast("敬请期待...");
        			break;*/
        		case 2:
        			intent = new Intent(getActivity(),RealNameAuthActy.class);
    				checkUserStat(intent,"realname");
        			break;
        		case 3:
        			intent = new Intent(getActivity(),BindOtherCardActy.class);
        			checkUserStat(intent,"othercard");
        			break;
        		case 4:
        			intent = new Intent(getActivity(),ReadCardActy.class);
    				intent.putExtra("usetype", "queryblance");
    				checkUserStat(intent,"queryblance");
        			break;
        		case 5:
        			showToast("未达到贷款标准");
        			break;
        		case 7:
        			showToast("未达到贷款标准");
        			break;
        		case 8:
        			showToast("未达到贷款标准");
        			break;
        		default:
        			break;
        	}
    	}
	}

}
