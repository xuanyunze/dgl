package com.rxoa.zlpay.acty;

import java.io.File;
import java.net.URLEncoder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.avator.AvPayType;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.device.DeviceInfo;
import com.rxoa.zlpay.base.device.DeviceInterface;
import com.rxoa.zlpay.base.device.DeviceListener;
import com.rxoa.zlpay.base.device.EmvReadCardInfoVo;
import com.rxoa.zlpay.base.device.EmvTransReqInfo;
import com.rxoa.zlpay.base.device.EmvTransRespInfo;
import com.rxoa.zlpay.base.device.SwipeCardInfo;
import com.rxoa.zlpay.base.util.Base64Util;
import com.rxoa.zlpay.base.util.CodeUtil;
import com.rxoa.zlpay.base.util.DataStore;
import com.rxoa.zlpay.base.util.ImageUtil;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.combx.DigitPasswordKeyPad;
import com.rxoa.zlpay.combx.DigitPasswordKeyPadListener;
import com.rxoa.zlpay.device.DeviceManager;
import com.rxoa.zlpay.device.DeviceManager.DeviceManagerListener;
import com.rxoa.zlpay.entity.OrderPayCredit;
import com.rxoa.zlpay.entity.OrderPhoneCharge;
import com.rxoa.zlpay.entity.OrderReceiveMoney;
import com.rxoa.zlpay.entity.OrderTransAccount;
import com.rxoa.zlpay.entity.OrderWrapper;
import com.rxoa.zlpay.entity.OrderWrapper.PayType;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.BindDeviceReqVo;
import com.rxoa.zlpay.vo.BindDeviceRespVo;
import com.rxoa.zlpay.vo.OrderPayReqVo;
import com.rxoa.zlpay.vo.OrderPayRespVo;
import com.rxoa.zlpay.vo.PaytypeRespVo;

public class ReadCardActy extends BaseUIActivity implements OnClickListener,DeviceListener,DeviceManagerListener,DigitPasswordKeyPadListener{
	private Dialog dialog = null;
	private String[] valuetype = new String[]{"耳机接口设备","蓝牙设备"};
	private boolean[] valuestat=new boolean[]{true, false};  
	private RadioOnClick radioOnClick = new RadioOnClick(0);
	private ListView lvValuetype;
	private Button btnReConnect;
	private Button btnSwipe;
	private Button btnEmv;
	
	private TextView tvInfo;
	private LinearLayout barDeviceType;
	private TextView tvDeviceType;
	private DeviceInterface device = null;
	private DeviceManager deviceMgr = null;
	
	private String usetype = null;
	private OrderWrapper order = null;
	private String orderValue = null;
	private int deviceStat = 0;
	private String pwdString = null;
	//private SwipeCardInfo scardInfo = null;
	private EmvTransReqInfo reqInfo = null;
	private RespParser parser = null;
	private String orderid;
	private String ordertype;
	private EmvReadCardInfoVo cardInfo = null;
	private WindowManager windowmanager;
	private DigitPasswordKeyPad dpk;
	private View passwdview;
	private int useType = 0;//0消费，1查询，2绑定设备；
	private int deviceType = 1;
	
	private String path_sig = Config.path_sig;
	private File sigFile;
	private String sigImageStr;
	private Bitmap imageBitmap;
	private Uri tmpuri;
	
	private Handler mHandler = new Handler(){ 
        public void handleMessage(Message msg) { 
            switch (msg.what) { 
            case 1: 
				if(parser.getRespCode()==0){
					try{
						Intent intent = null;
						OrderPayRespVo respVo = (OrderPayRespVo) parser.getRespObject();
						if(usetype.equals("queryblance")){
							intent = new Intent(ReadCardActy.this,ShowCardBlanceActy.class);
							String cardType = "借记账户";
							String cardValue = respVo.getCardAccblance();
							if(cardValue!=null){
								if(cardValue.substring(1,3).equals("30")){
									cardType = "信用账户";
								}
								cardValue = cardValue.substring(3);
							}
							intent.putExtra("accno", respVo.getCardAccno());
							intent.putExtra("acctype", cardType);
							intent.putExtra("blance", cardValue);
						}else{
							showToast("订单支付成功");
							intent = new Intent(ReadCardActy.this,OrderDetailActy.class);
							intent.putExtra("orderid", respVo.getOrderId());
							intent.putExtra("ordertype", AvOrderType.code2value(ordertype));
						}
						startActivity(intent);
						//device.onDealFinished(0, "交易完成！");
						finish();
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if(parser.getRespCode()==2){
					showToast("呃，请勿重复支付");
					//device.onDealFinished(0, "交易失败！");
					payFailed(ReadCardActy.this);
				}else if(parser.getRespCode()==1){
					showToast("交易失败了！");
					//device.onDealFinished(0, "交易失败！");
					payFailed(ReadCardActy.this);
				}
                break;
            case 2:
            	BindDeviceRespVo respVo = (BindDeviceRespVo) parser.getRespObject();
            	if(respVo.getRespCode()==10){
            		showMessage("设备未登记");
            	}else if(respVo.getRespCode()==11){
            		showMessage("正在初始化设备");
            		int keytype=0;if(device.getDeviceVersion().equals("BT_ACI21B_NEW")){keytype=10;}
            		device.installKeys(respVo.getResKey(), keytype);
            	}else if(respVo.getRespCode()==12){
            		showMessage("设备已被禁用");
            	}else if(respVo.getRespCode()==13){
            		showMessage("设备尚未分配");
            	}else if(respVo.getRespCode()==14){
            		showMessage("设备已被其它用户使用");
            	}else if(respVo.getRespCode()==15){
            		showMessage("设备状态正常");
            		device.startSwipeCard(orderValue);
            	}else if(respVo.getRespCode()==20){
            		showMessage("非法用户");
            	}else{
            		showMessage("设备验证失败");
            	}
            	break;
            }
        }; 
    };
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(deviceStat==0){
			deviceMgr = DeviceManager.getInstance(ReadCardActy.this).initManager();
			deviceMgr.setListener(this);
		}
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_readcard);
		setTitleText(R.string.title_readcard);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		tvInfo = (TextView) findViewById(R.id.readcard_tip);
		barDeviceType = (LinearLayout) findViewById(R.id.readcard_bar_type);
		tvDeviceType = (TextView) findViewById(R.id.readcard_tv_type);
		barDeviceType.setOnClickListener(new RadioClickListener());
		btnReConnect  = (Button) findViewById(R.id.readcard_btn_reconnect);
		btnSwipe  = (Button) findViewById(R.id.readcard_btn_swipe);
		btnEmv  = (Button) findViewById(R.id.readcard_btn_emv);
		btnReConnect.setOnClickListener(this);
		btnSwipe.setOnClickListener(this);
		btnEmv.setOnClickListener(this);
		
		Intent intent = getIntent();
		usetype = intent.getStringExtra("usetype");
		order = (OrderWrapper) intent.getSerializableExtra("order");
		if(usetype.equals("payorder")){
			order = (OrderWrapper) intent.getSerializableExtra("order");
			orderid = order.getOrderId();
			ordertype = order.getOrderType();
			Log.i("otype",ordertype);
			orderValue = order.getOrderValue();
			useType = 0;
		}else if(usetype.equals("queryblance")){
			orderValue = "0";
			useType = 1;
		}else if(usetype.equals("binddevice")){
			useType = 2;
		}
		if(deviceStat==0){
			deviceMgr.findDevice();
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.readcard_btn_reconnect){
			
		}else if(id==R.id.readcard_btn_swipe){
			if(deviceStat==1){
				//scardInfo = null;
				device.startSwipeCard(orderValue);
			}else{
				showMessage("设备未就绪...");
			}
		}else if(id==R.id.readcard_btn_emv){
			if(deviceStat==1){
				reqInfo = null;
				device.startICCardTransfer(orderValue);
			}else{
				showMessage("设备未就绪...");
			}
		}
	}
	public void checkDevice(){
		showSelectRadio();
	}
	public void showSelectRadio(){
		AlertDialog ad = new Builder(ReadCardActy.this).setTitle("选择设备类型")
				.setSingleChoiceItems(valuetype,radioOnClick.getIndex(),radioOnClick).create();  
		lvValuetype = ad.getListView();
		ad.show();
	}
	class RadioClickListener implements OnClickListener {
		@Override  
		public void onClick(View v) {  
			showSelectRadio();
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
			tvDeviceType.setText(valuetype[index]);
			if(valuetype[index].equals("耳机接口设备")){
				
			}else if(valuetype[index].equals("蓝牙设备")){
				deviceMgr.scanBluetooth();
			}
			dialog.dismiss();
		}
	}
	
	@Override
	public String showMessage(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvInfo.setText(msg);
			}
		});
		return null;
	}
	@Override
	public void onDeviceInited() {
		deviceStat = 1;
		if(useType==2){
			
		}else{
			showMessage("请点击刷卡/读卡按钮...");
		}
	}
	@Override
	public void onFinishSwipeCard(EmvReadCardInfoVo cinfo){
		this.cardInfo = cinfo;
		if(!StringUtil.isEmpty(cinfo.getPinData())){
			//device.onDealFinished(1,null);
			openSignature();
		}else{
			showMessage("请输入交易密码...");
			showPassWdPadView();
		}
	}

	@Override
	public void onFinishComputepwd(EmvReadCardInfoVo cinfo) {
		onFinishSwipeCard(cinfo);
	}

	@Override
	public DeviceListener onSelDevice(DeviceInterface device) {
		this.device = device;
		this.device.setDeviceListener(this);
		return this;
	}

	@Override
	public void showBlDevices(final Builder builder) {
		runOnUiThread(new Runnable(){

			@Override
			public void run() {
				builder.create().show();
			}
		});
	}

	@Override
	public String onOltransRequest(EmvTransReqInfo req){
		showMessage("正在请求联机交易...");
		reqInfo = req;
		//doOnlineDeal();
		return null;
	}
	@Override
	public String onOltransResponse(EmvTransRespInfo resp){
		showMessage("交易完成！");
		return null;
	}
	public void openSignOrder(){
		
	}
	public void doOnlineDeal(EmvReadCardInfoVo cinfo){
		try{
			showMessage("正在进行联机交易...");
			//device.onDealing(0, null);
			final OrderPayReqVo reqVo = new OrderPayReqVo();
			if(usetype.equals("queryblance")&&order==null){
				order = new OrderWrapper();
				order.setOrderValue("0.00");
				order.setOrderType(AvOrderType.code.QueryBlance.name());
				order.setPayType(AvPayType.code.Device.name());
				
				reqVo.setPayAreaCode("1001");
				reqVo.setPaySettleCode("1001");
				reqVo.setPayFeeCode("1001");
			}else{
				OrderReceiveMoney ino =  (OrderReceiveMoney) order.getOrderItem();
				reqVo.setPayAreaCode(ino.getPayAreaCode());
				reqVo.setPayFeeCode(ino.getPayFeeCode());
				reqVo.setPaySettleCode(ino.getPaySettleCode());
			}
			reqVo.setOrderId(order.getOrderId());
			reqVo.setOrderType(order.getOrderType());
			reqVo.setOrderValue(order.getOrderValue());
			
			if(order.getOrderType().equals(AvOrderType.code.ReceiveMoney.name())){
			}else if(order.getOrderType().equals(AvOrderType.code.AccountCharge.name())){	
			}else if(order.getOrderType().equals(AvOrderType.code.TransAcc.name())){
				OrderTransAccount orderItem = (OrderTransAccount) order.getOrderItem();
				reqVo.setReceAccName(orderItem.getReceiveAccName());
				reqVo.setReceAccNo(orderItem.getRceceiveAccNo());
				reqVo.setReceBankName(orderItem.getBankName());
				reqVo.setReceBankDistr(orderItem.getBankDistr());
				reqVo.setReceBankBranch(orderItem.getBankBranch());
			}else if(order.getOrderType().equals(AvOrderType.code.RepayCredit.name())){
				OrderPayCredit orderItem = (OrderPayCredit) order.getOrderItem();
				reqVo.setReceAccName(orderItem.getAccName());
				reqVo.setReceAccNo(orderItem.getAccNo());
				reqVo.setReceBankName(orderItem.getAccBank());
			}else if(order.getOrderType().equals(AvOrderType.code.PhoneCharge.name())){
				OrderPhoneCharge orderItem = (OrderPhoneCharge) order.getOrderItem();
				reqVo.setChargePhoneNo(orderItem.getPhoneNumber());
			}else if(order.getOrderType().equals(AvOrderType.code.QueryBlance.name())){
				
			}
			if(order.getPayType().equals(PayType.Device.name())){
				reqVo.setPayType(AvPayType.code.Device.name());
				reqVo.setPayAccNo(cinfo.getCardNo());
				reqVo.setIsEntrack(cinfo.isEntrack()==true?"1":"0");
				reqVo.setTrackTwo(cinfo.getTrack2());
				reqVo.setTrackThree(cinfo.getTrack3());
				reqVo.setTrack55(cinfo.getTrack55());
				reqVo.setCardSequence(cinfo.getCardSequence());
				reqVo.setPayPwd(cinfo.getPinData());
				reqVo.setPayDeviceSn(cinfo.getDeviceSn());
				reqVo.setSigImageStr(this.sigImageStr);
			}else if(order.getPayType().equals(PayType.Fast.name())){
				reqVo.setPayType(AvPayType.code.Fast.name());
			}else if(order.getPayType().equals(PayType.Account.name())){
				reqVo.setPayType(AvPayType.code.Account.name());
			}
			new Thread(new Runnable(){
				@Override
				public void run() {
					showMessage("正在进行联机交易...");
					parser = ReqWrapper.getOrderRequest(ReadCardActy.this).doPayOrder(reqVo);
		            Message message = new Message(); 
		            message.what = 1; 
		            mHandler.sendMessage(message);
				}
			}).start();
			
			/*
			//Looper.prepare();
			new DefAsyncTask(ReadCardActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在进行联机交易...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getOrderRequest(ReadCardActy.this).doPayOrder(reqVo);
					}catch(Exception e){
						e.printStackTrace();
					}
				}~
				
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						try{
							//blanceValue = ((PaytypeRespVo)parser.getRespObject()).getBlanceValue();
							//fastCards = ((PaytypeRespVo)parser.getRespObject()).getFastCards();
							//refreshViewUI();
							showToast("订单支付成功～");
							Intent intent = new Intent(ReadCardActy.this,OrderSignatureActy.class);
							startActivity(intent);
							finish();
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("呃，交易失败了！");
					}
					System.out.println("rrrr=="+parser.getRespCode());
				}
			}.excute();
			*/
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	public void payFailed(Context paramContext){
		String msg = "订单支付失败";
		String repay = "重新支付";
		String giveup = "放弃支付";
		if(usetype.equals("queryblance")){
			msg = "余额查询失败";
			repay = "重新查询";
			giveup = "放弃查询";
		}
		Builder localBuilder = new Builder(paramContext);
		localBuilder.setIcon(null);
		localBuilder.setMessage(msg);
	    localBuilder.setTitle("友情提示");
	    /*localBuilder.setPositiveButton(repay, new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		showMessage("请选择读卡类型");
	    		ReadCardActy.this.dialog.dismiss();
	    	}
	    });*/
	    localBuilder.setNegativeButton(giveup, new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		Intent intent = new Intent(ReadCardActy.this,MainHomeActy.class);
	    		intent.putExtra("curFrag", 1);
	    		ReadCardActy.this.startActivity(intent);
	    		ReadCardActy.this.dialog.dismiss();
	    		ReadCardActy.this.finish();
	    	}
	    });
	    this.dialog = localBuilder.create();
	    this.dialog.setCancelable(false);
	    this.dialog.setCanceledOnTouchOutside(false);
	    this.dialog.show();
	}
	@Override
	public void onInputFinished(String pwd) {
		try{
			windowmanager.removeView(passwdview);
			device.encryptPwd(cardInfo, pwd);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			passwdview = null;
		}
	}
	private void showPassWdPadView() {  
		dpk = new DigitPasswordKeyPad(this);
		dpk.setLisenter(this);
        passwdview = dpk.setup(); 
        this.runOnUiThread(new Runnable() {  
            public void run() {  
                // 让一个视图浮动在你的应用程序之上  
                windowmanager = (WindowManager) ReadCardActy.this.getSystemService(Context.WINDOW_SERVICE);  
                LayoutParams layoutparams = new LayoutParams(-1, -1, LayoutParams.FIRST_SUB_WINDOW, LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.RGBA_8888);
                layoutparams.gravity = Gravity.BOTTOM;  
                
                passwdview.findViewById(R.id.transpwdpdpanel).getBackground().setAlpha(140);
                windowmanager.addView(passwdview, layoutparams);
            }
        }); 
	}

	@Override
	public void onConnected() {
		showMessage("设备连接成功...");
		deviceStat = 1;
		device.readDeviceInfo();
		deviceType = 1;
		/*
		if(useType == 2){
			device.readDeviceInfo();
		}else{
			showMessage("设备连接成功...");
			device.startSwipeCard(orderValue);
		}
		*/
	}
	@Override
	public void onRequireInputpwd() {
		showPassWdPadView();
	}

	@Override
	public void onGetDeviceInfo(final DeviceInfo info) {
		doBindDevice(info);
		/*
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showBindInfo(info,ReadCardActy.this);
			}
		});
		*/
	}
	
	public void showBindInfo(final DeviceInfo info,Context context){
		String msg = "即将进行设备绑定\r\n设备编号:"+info.getDeviceSn()+"\r\n用户账号"+Config.Uid+"\r\n是否确认绑定？";
		String repay = "立即绑定";
		String giveup = "放弃绑定";
		Builder localBuilder = new Builder(context);
		localBuilder.setIcon(null);
		localBuilder.setMessage(msg);
	    localBuilder.setTitle("信息提示");
	    localBuilder.setPositiveButton(repay, new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		ReadCardActy.this.dialog.dismiss();
	    		doBindDevice(info);
	    	}
	    });
	    localBuilder.setNegativeButton(giveup, new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		Intent intent = new Intent(ReadCardActy.this,MainHomeActy.class);
	    		intent.putExtra("curFrag", 1);
	    		ReadCardActy.this.startActivity(intent);
	    		ReadCardActy.this.dialog.dismiss();
	    		ReadCardActy.this.finish();
	    	}
	    });
	    this.dialog = localBuilder.create();
	    this.dialog.setCancelable(false);
	    this.dialog.setCanceledOnTouchOutside(false);
	    this.dialog.show();
	}
	public void doBindDevice(DeviceInfo dInfo){
		try{
			final BindDeviceReqVo reqVo = new BindDeviceReqVo();
			reqVo.setDeviceSn(dInfo.getDeviceSn());
			reqVo.setKeyMod(device.getKeyMod());
			reqVo.setDeviceType(deviceType+"");
			new Thread(new Runnable(){
				@Override
				public void run() {
					showMessage("正在联机验证设备...");
					parser = ReqWrapper.getUserAccRequest(ReadCardActy.this).doBindDevice(reqVo);
		            Message message = new Message();
		            message.what = 2; 
		            mHandler.sendMessage(message);
				}
			}).start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onKeyInstalled(int res) {
		if(res==1){
			showMessage("["+res+"]绑定设备成功");
			device.startSwipeCard(orderValue);
			//showAlert("设备绑定成功~",ReadCardActy.this);
		}else{
			showMessage("["+res+"]本地绑定设备失败");
			//showAlert("["+res+"]本地设备绑定失败~",ReadCardActy.this);
		}
	}
	public void showAlert(String msg,Context context){
		String giveup = "确 定";
		Builder localBuilder = new Builder(context);
		localBuilder.setIcon(null);
		localBuilder.setMessage(msg);
	    localBuilder.setTitle("信息提示");
	    localBuilder.setNegativeButton(giveup, new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		Intent intent = new Intent(ReadCardActy.this,MainHomeActy.class);
	    		intent.putExtra("curFrag", 1);
	    		ReadCardActy.this.startActivity(intent);
	    		ReadCardActy.this.dialog.dismiss();
	    		ReadCardActy.this.finish();
	    	}
	    });
	    this.dialog = localBuilder.create();
	    this.dialog.setCancelable(false);
	    this.dialog.setCanceledOnTouchOutside(false);
	    this.dialog.show();
	}
	@Override
	public void onPause(){
		System.out.println("正在暂停");
		super.onPause();
	}
	@Override
	public void onDestroy(){
		System.out.println("正在销毁");
		super.onDestroy();
		try{
			if(device!=null) device.disConnected();
			if(deviceMgr!=null){
				deviceMgr.cancelDeviceSearch();
				deviceMgr.unRegisterReceiver();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			device = null;
			deviceMgr = null;
		}
		try{
			if(passwdview!=null&&passwdview.getParent()!=null){
				windowmanager.removeView(passwdview);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			passwdview = null;
			windowmanager = null;
		}
		deviceStat = 0;
	}
	@Override
	public void onStop(){
		System.out.println("正在停止");
		super.onStop();
		try{
			if(device!=null) device.disConnected();
			if(deviceMgr!=null){
				deviceMgr.cancelDeviceSearch();
				deviceMgr.unRegisterReceiver();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			device = null;
			deviceMgr = null;
		}
		deviceStat = 0;
	}
	@Override
	public void cancelDeviceSearch() {
		finish();
	}
	public void openSignature(){
		try{
			deviceStat = 1;
			Intent localIntent = new Intent(ReadCardActy.this,MakeSignatureActy.class);
			if (Environment.getExternalStorageState().equals("mounted")){
		        File localFile1 = null,localFile2 = null;
				localFile1 = new File(this.path_sig);
			    if (!localFile1.exists())
			        localFile1.mkdirs();
			    localFile2 = new File(localFile1, "sig.jpg");
			    this.sigFile = localFile2;
			    this.tmpuri = Uri.fromFile(this.sigFile);
			    localIntent.putExtra("output", this.sigFile.getAbsolutePath());
			    startActivityForResult(localIntent, 3);
			}else{
				showToast("手机存储空间已满");
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent paramIntent){
		super.onActivityResult(requestCode, resultCode, paramIntent);
		if(resultCode==-1){
			if(requestCode==2||requestCode==3){
				try{
			        this.sigFile = new File(this.path_sig, "sig.jpg");
			        this.imageBitmap = ImageUtil.optimizeBitmap(ImageUtil.readStream(this.sigFile), 500, 480);
			        if (this.imageBitmap != null){
			        	Bitmap localBitmap2 = ImageUtil.rotateBitMap(this.imageBitmap,0.0F);
			        	//ivSigImage.setImageBitmap(localBitmap2);
						//ivSigImage.setVisibility(View.VISIBLE);
						//tvHand.setVisibility(View.GONE);
						//tvPhoto.setVisibility(View.GONE);
			        	ImageUtil.saveBitmap(localBitmap2, sigFile.getAbsolutePath(), 100);
			        	DataStore.getMap().put("sigFile", this.sigFile);
			        	DataStore.getMap().put("sigImage", localBitmap2);
			        	this.sigImageStr = new String(Base64Util.encrypt(ImageUtil.readStream(this.sigFile)));
			        	doOnlineDeal(this.cardInfo);
			        }
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onFindRadioDevice(DeviceInterface device) {
		this.device = device;
		showMessage("已检测到音频读卡器");
		device.readDeviceInfo();
		deviceStat = 1;
		deviceType = 0;
	}

	@Override
	public void onFindRadioDeviceTimeout() {
		deviceMgr.scanBluetooth();
	}
}
