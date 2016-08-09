package com.rxoa.zlpay.acty;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.Base64Util;
import com.rxoa.zlpay.base.util.DataStore;
import com.rxoa.zlpay.base.util.ImageUtil;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.combx.WheelView;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.entity.UserAccInfo;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.BindMainCardReqVo;
import com.rxoa.zlpay.vo.GetBankReqVo;
import com.rxoa.zlpay.vo.GetBankRespVo;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;

public class BindMainCardActy extends BaseUIActivity implements OnClickListener{
	private String path_pos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mcard";
	private String path_nage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mcard";

	private File posFile;
	private File nageFile;
	
	private String posImageStr;
	private String nageImageStr;
	private Bitmap imageBitmap;
	private Uri tmpuri;
	
	private EditText edtvProvince;
	private EditText edtvCity;
	private RadioGroup rgAccCate;
	private EditText edtvAccName;
	private EditText edtvAccNo;
	private TextView tvBankName;
	private EditText edtvBankDistr;
	private TextView tvBankBranch;
	private TextView tvOldCard;
	private TextView tvXinfo;
	private TextView tvAccArea;
	
	private LinearLayout mbarPos;
	private LinearLayout mbarNage;
	
	private LinearLayout barPos;
	private LinearLayout barNage;
	
	private ImageView ivPos;
	private ImageView ivNage;
	
	private ImageView cameraPos;
	private ImageView cameraNage;
	
	private TextView tvCameraPos;
	private TextView tvCameraNage;
	
	private TextView tvPos;
	private TextView tvNage;
	
	private Button btnGo;
	private String strRealname;
	
    private String[] BANKS = null;
    private String[] BANK_BRANCHES = null;
    private List<String> banks;
    private List<String> branches;
    private String strBankCode;
    private WheelView wva;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String rname = intent.getStringExtra("uname");
		if(!StringUtil.isDbNull(rname)){
			strRealname = rname;
		}
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_bind_maincard);
		setTitleText(R.string.title_bind_maincard);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
		edtvProvince = (EditText) findViewById(R.id.bind_maincard_province);
		edtvCity = (EditText) findViewById(R.id.bind_maincard_city);
		rgAccCate = (RadioGroup) findViewById(R.id.bind_maincard_acccate);
		
		tvOldCard = (TextView) findViewById(R.id.bind_maincard_old);
		tvXinfo = (TextView) findViewById(R.id.bind_maincard_xinfo);
		edtvAccName = (EditText) findViewById(R.id.bind_maincard_accname);
		edtvAccNo = (EditText) findViewById(R.id.bind_maincard_accno);
		tvBankName = (TextView) findViewById(R.id.bind_maincard_bankname);
		edtvBankDistr = (EditText) findViewById(R.id.bind_maincard_bankdistr);
		tvBankBranch = (TextView) findViewById(R.id.bind_maincard_bankbranch);
		tvAccArea = (TextView) findViewById(R.id.bind_maincard_area);
		
		mbarPos = (LinearLayout) findViewById(R.id.bind_maincard_mbar_pos);
		mbarNage = (LinearLayout) findViewById(R.id.bind_maincard_mbar_nage);
		mbarPos.setVisibility(View.GONE);
		mbarNage.setVisibility(View.GONE);
		
		barPos = (LinearLayout) findViewById(R.id.bind_maincard_bar_pos);
		barNage = (LinearLayout) findViewById(R.id.bind_maincard_bar_nage);
		
		ivPos = (ImageView) findViewById(R.id.bind_maincard_img_pos);
		ivNage = (ImageView) findViewById(R.id.bind_maincard_img_nage);
		
		cameraPos = (ImageView) findViewById(R.id.bind_maincard_camera_icon_pos);
		cameraNage = (ImageView) findViewById(R.id.bind_maincard_camera_icon_nage);
		
		tvCameraPos = (TextView) findViewById(R.id.bind_maincard_camera_txt_pos);
		tvCameraNage = (TextView) findViewById(R.id.bind_maincard_camera_txt_nage);
		
		tvPos = (TextView) findViewById(R.id.bind_maincard_tv_pos);
		tvNage = (TextView) findViewById(R.id.bind_maincard_tv_nage);
		
		btnGo = (Button) findViewById(R.id.bind_maincard_btngo);
		
		tvPos.setOnClickListener(this);
		tvNage.setOnClickListener(this);
		tvCameraPos.setOnClickListener(this);
		tvCameraNage.setOnClickListener(this);
		cameraPos.setOnClickListener(this);
		cameraNage.setOnClickListener(this);
		ivPos.setOnClickListener(this);
		ivNage.setOnClickListener(this);
		btnGo.setOnClickListener(this);
		
		edtvAccName.setText(strRealname);
		edtvAccName.setEnabled(false);
		
		tvBankName.setOnClickListener(this);
		tvAccArea.setOnClickListener(this);
		tvBankBranch.setOnClickListener(this);
		getAccoutInfo();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.bind_maincard_tv_pos){
			switchImageCard("pos");
		}else if(id==R.id.bind_maincard_tv_nage){
			switchImageCard("nage");
		}else if(id==R.id.bind_maincard_camera_icon_pos||id==R.id.bind_maincard_camera_txt_pos){
			openCamera("pos");
		}else if(id==R.id.bind_maincard_camera_icon_nage||id==R.id.bind_maincard_camera_txt_nage){
			openCamera("nage");
		}else if(id==R.id.bind_maincard_btngo){
			doMainCardAuth();
		}else if(id==R.id.bind_maincard_bankname){
			getBanks();
		}else if(id==R.id.bind_maincard_area){
			if(tvBankName.getText().toString().equals("")){
				showToast("请先选择开户银行");return;
			}
			Intent intent = new Intent(this,CitiesActivity.class);
			startActivityForResult(intent,10);
		}else if(id==R.id.bind_maincard_bankbranch){
			getBankBranches();
		}
	}
	public boolean doDataCheck(){
		//if(edtvAccName.getText().toString().trim().equals("")){
		//	showToast("请填写账户名称");return false;
		//}
		if(edtvAccNo.getText().toString().trim().equals("")){
			showToast("请填写银行卡号");return false;
		}
		if(tvBankName.getText().toString().trim().equals("")){
			showToast("请选择开户行");return false;
		}
		if(tvBankBranch.getText().toString().trim().equals("")){
			showToast("请选择开户支行");return false;
		}
		if(tvAccArea.getText().toString().trim().equals("")){
			showToast("请选择所属区域");return false;
		}
		//if(edtvCity.getText().toString().trim().equals("")){
			//showToast("请填写所属城市");return false;
		//}
		//if(posImageStr==null||posImageStr.equals("")){
		//	showToast("请拍摄银行卡正面照");return false;
		//}
		//if(nageImageStr==null||nageImageStr.equals("")){
		//	showToast("请拍摄银行卡背面照");return false;
		//}
		return true;
	}
	public void doMainCardAuth(){
		try{
			if(!doDataCheck()){return;}
			final BindMainCardReqVo reqVo = new BindMainCardReqVo();
			String cate = "0";
			int rdid = rgAccCate.getCheckedRadioButtonId();
			if(rdid==R.id.bind_maincard_gong){cate="1";}
			String[] areas = tvAccArea.getText().toString().split("-");
			reqVo.setProvince(areas[0]);
			reqVo.setCity(areas[1]);
			reqVo.setAccCate(cate);
			reqVo.setAccName(edtvAccName.getText().toString());
			reqVo.setAccNo(edtvAccNo.getText().toString());
			reqVo.setBankName(tvBankName.getText().toString());
			reqVo.setBankDistr(edtvBankDistr.getText().toString());
			reqVo.setBankBranch(tvBankBranch.getText().toString());
			reqVo.setBankCode(strBankCode);
			reqVo.setPosImgStr(posImageStr);
			reqVo.setNageImgStr(nageImageStr);
			
			new DefAsyncTask(this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在提交数据，请稍等");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					parser = ReqWrapper.getUserAccRequest(BindMainCardActy.this).doBindMainCard(reqVo);
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						showToast("银行卡绑定成功");
						finish();
					}else if(parser.getRespCode()==1){
						showToast("绑定银行卡出错了");
					}else{
						showToast("您的操作太频繁了");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void switchImageCard(String type){
		ivPos.setVisibility(View.GONE);
		ivNage.setVisibility(View.GONE);
		cameraPos.setVisibility(View.GONE);
		cameraNage.setVisibility(View.GONE);
		tvCameraPos.setVisibility(View.GONE);
		tvCameraNage.setVisibility(View.GONE);
		
		tvPos.setTextColor(Color.parseColor("#ffcccccc"));
		tvNage.setTextColor(Color.parseColor("#ffcccccc"));
		if(type.equals("pos")){
			tvPos.setTextColor(Color.parseColor("#ff97db4f"));
			ivPos.setVisibility(View.VISIBLE);
			cameraPos.setVisibility(View.VISIBLE);
			tvCameraPos.setVisibility(View.VISIBLE);
		}else if(type.equals("nage")){
			tvNage.setTextColor(Color.parseColor("#ff97db4f"));
			ivNage.setVisibility(View.VISIBLE);
			cameraNage.setVisibility(View.VISIBLE);
			tvCameraNage.setVisibility(View.VISIBLE);
		}
	}
	public void openCamera(String type){
		try{
			Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
			if (Environment.getExternalStorageState().equals("mounted")){
		        File localFile1 = null,localFile2 = null;
				if(type.equals("pos")){
					localFile1 = new File(this.path_pos);
			        if (!localFile1.exists())
			        	localFile1.mkdirs();
			        localFile2 = new File(localFile1, "maincard_pos.jpg");
			        this.posFile = localFile2;
			        this.tmpuri = Uri.fromFile(this.posFile);
			        localIntent.putExtra("output", this.tmpuri);
			        startActivityForResult(localIntent, 2);
		        }else if(type.equals("nage")){
					localFile1 = new File(this.path_nage);
			        if (!localFile1.exists())
			        	localFile1.mkdirs();
			        localFile2 = new File(localFile1, "maincard_nage.jpg");
			        this.nageFile = localFile2;
			        this.tmpuri = Uri.fromFile(this.nageFile);
			        localIntent.putExtra("output", this.tmpuri);
			        startActivityForResult(localIntent, 3);
		        }
			}else{
				showToast("");
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent paramIntent){
		super.onActivityResult(requestCode, resultCode, paramIntent);
		if(resultCode==-1){
			if(requestCode==2){
				try{
			        this.posFile = new File(this.path_pos, "maincard_pos.jpg");
			        this.imageBitmap = ImageUtil.optimizeBitmap(ImageUtil.readStream(this.posFile), 500, 480);
			        if (this.imageBitmap != null){
			        	Bitmap localBitmap2 = ImageUtil.rotateBitMap(this.imageBitmap,90.0F);
			        	ivPos.setImageBitmap(localBitmap2);
			        	ImageUtil.saveBitmap(localBitmap2, posFile.getAbsolutePath(), 100);
			        	DataStore.getMap().put("posFile", this.posFile);
			        	DataStore.getMap().put("posImage", localBitmap2);
			        	this.posImageStr = new String(Base64Util.encrypt(ImageUtil.readStream(this.posFile)));
			        }
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(requestCode==3){
				try{
		            nageFile = new File(path_nage, "maincard_nage.jpg");
		            imageBitmap = ImageUtil.optimizeBitmap(ImageUtil.readStream(nageFile), 500, 480);
					Log.i("info", "reverse bitmap not null");
					Bitmap localBitmap1 = ImageUtil.rotateBitMap(this.imageBitmap,90.0F);
					ivNage.setImageBitmap(localBitmap1);
					ImageUtil.saveBitmap(localBitmap1, this.nageFile.getAbsolutePath(), 100);
					DataStore.getMap().put("nageFile", this.nageFile);
					DataStore.getMap().put("nageImage", localBitmap1);
					this.nageImageStr = new String(Base64Util.encrypt(ImageUtil.readStream(this.nageFile)));
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(requestCode==10){
				String cities = paramIntent.getStringExtra("cities");
				if(!cities.equals(tvAccArea.getText().toString().trim())){
					tvBankBranch.setText("");
				}
				tvAccArea.setText(cities);
			}
		}
	}
	public void getAccoutInfo(){
		try{
			if(!Config._isLogin){return;}
			final UserAccInfoReqVo reqVo = new UserAccInfoReqVo();
			new DefAsyncTask(BindMainCardActy.this){
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
						parser = ReqWrapper.getUserAccRequest(BindMainCardActy.this).doQueryUserAccInfo(reqVo);
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
							UserAccInfo accInfo = ((UserAccInfoRespVo)parser.getRespObject()).getAccInfo();
							BankCardEntity mainCardInfo = ((UserAccInfoRespVo)parser.getRespObject()).getMainCardInfo();
							if(StringUtil.isDbNull(accInfo.getRealName())||StringUtil.isDbNull(accInfo.getIdNo())){
								showToast("请先进行实名认证");finish();
							}else{
								edtvAccName.setText(accInfo.getRealName());
							}
							if(!(mainCardInfo==null||StringUtil.isDbNull(mainCardInfo.getAccName()))){
								tvOldCard.setText(Html.fromHtml("已绑定："+mainCardInfo.getAccBank()+"<br>　　　　"+StringUtil.hideAccno(mainCardInfo.getAccNo())+"</div>"));
								tvXinfo.setText("如需更换，请填写以下内容");
								btnGo.setText("确认更换");
							}
							//otherCardItems = ((UserAccInfoRespVo)parser.getRespObject()).getOtherCardInfo();
							//deviceItems = ((UserAccInfoRespVo)parser.getRespObject()).getDeviceInfo();
							//refreshViewUI();
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
	public void showBranchlist(){
		/*
        View outerView = LayoutInflater.from(BindMainCardActy.this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);
        wv.setItems(Arrays.asList(BANK_BRANCHES));
        wv.setSeletion(3);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
            	tvBankBranch.setText(item.trim());
            	strBankCode = branches.get(selectedIndex);
                Log.i("x", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item+strBankCode);
            }
        });
        */
		
		ListView lv = new ListView(BindMainCardActy.this);
		lv.setBackgroundColor(Color.parseColor("#FFEEEEEE"));
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,BANK_BRANCHES));
		final AlertDialog dialog = new Builder(BindMainCardActy.this)
    		.setTitle("请选择开户支行")
			.setView(lv)
			.show();
		lv.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	tvBankBranch.setText(BANK_BRANCHES[arg2]);
            	strBankCode = branches.get(arg2).split("\\|")[1];
            	dialog.dismiss();
            }
        });
	}
	public void showBanklist(){
		ListView lv = new ListView(BindMainCardActy.this);
		lv.setBackgroundColor(Color.parseColor("#FFEEEEEE"));
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,BANKS));
		final AlertDialog dialog = new Builder(BindMainCardActy.this)
    		.setTitle("请选择开户银行行")
			.setView(lv)
			.show();
		lv.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	if(!BANKS[arg2].trim().equals(tvBankName.getText().toString().trim())){
            		tvAccArea.setText("");tvBankBranch.setText("");
            	}
            	tvBankName.setText(BANKS[arg2]);
            	dialog.dismiss();
            }
        });
	}
	/*
	public void showBranchlist(){
        View outerView = LayoutInflater.from(BindMainCardActy.this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);
        wv.setItems(Arrays.asList(BANK_BRANCHES));
        wv.setSeletion(3);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
            	tvBankBranch.setText(item.trim());
            	strBankCode = branches.get(selectedIndex);
                Log.i("x", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item+strBankCode);
            }
        });
        new AlertDialog.Builder(BindMainCardActy.this)
                .setTitle("请选择开户支行～")
                .setView(outerView)
                .setPositiveButton("确认选择", null)
                .show();
	}
	*/
	/*
	public void showBanklist(){
        View outerView = LayoutInflater.from(BindMainCardActy.this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);
        wv.setItems(Arrays.asList(BANKS));
        wv.setSeletion(3);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
            	if(!item.trim().equals(tvBankName.getText().toString().trim())){
            		tvAccArea.setText("");tvBankBranch.setText("");
            	}
            	tvBankName.setText(item.trim());
            }
        });
        new AlertDialog.Builder(BindMainCardActy.this)
                .setTitle("请选择开户银行～")
                .setView(outerView)
                .setPositiveButton("确认选择", null)
                .show();
	}
	*/
	public void getBanks(){
		try{
			if(BANKS!=null){showBanklist();return;}
			final GetBankReqVo reqVo = new GetBankReqVo();
			reqVo.setReqtype("MCARD_BANKS");
			new DefAsyncTask(BindMainCardActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在加载银行数据...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getUserAccRequest(BindMainCardActy.this).doQueryBanksInfo(reqVo);
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
							GetBankRespVo bankInfo = (GetBankRespVo) parser.getRespObject();
							banks = bankInfo.getBankInfo();
							BANKS = new String[banks.size()];
							banks.toArray(BANKS);
							showBanklist();
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("银行信息加载失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void getBankBranches(){
		try{
			if(tvAccArea.getText().toString().equals("")){
				showToast("请先选择开户地区");return;
			}
			String [] xarea = tvAccArea.getText().toString().split("-");
			final GetBankReqVo reqVo = new GetBankReqVo();
			reqVo.setReqtype("MCARD_BRANCHES");
			reqVo.setProvince(xarea[0]);
			reqVo.setCity(xarea[1]);
			reqVo.setArea(xarea[2]);
			reqVo.setBankName(tvBankName.getText().toString().trim());
			new DefAsyncTask(BindMainCardActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在加载银行数据...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getUserAccRequest(BindMainCardActy.this).doQueryBanksInfo(reqVo);
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
							GetBankRespVo bankInfo = (GetBankRespVo) parser.getRespObject();
							branches = bankInfo.getBankInfo();
							BANK_BRANCHES = new String[branches.size()];
							for(int i=0;i<BANK_BRANCHES.length;i++){
								BANK_BRANCHES[i] = branches.get(i).split("\\|")[0];
							}
							showBranchlist();
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("支行信息加载失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
