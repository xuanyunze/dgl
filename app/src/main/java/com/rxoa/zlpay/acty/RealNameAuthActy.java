package com.rxoa.zlpay.acty;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.Base64Util;
import com.rxoa.zlpay.base.util.DataStore;
import com.rxoa.zlpay.base.util.ImageUtil;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.entity.UserAccInfo;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;
import com.rxoa.zlpay.vo.UserRealNameReqVo;

public class RealNameAuthActy extends BaseUIActivity implements OnClickListener{
	private String path_pos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/realname";
	private String path_nage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/realname";
	private String path_self = Environment.getExternalStorageDirectory().getAbsolutePath() + "/realname";
	private File posFile;
	private File nageFile;
	private File selfFile;
	private String posImageStr;
	private String nageImageStr;
	private String selfImageStr;
	private Bitmap imageBitmap;
	private Uri tmpuri;
	
	private EditText edtvName;
	private TextView tvIdType;
	private EditText edtvIdNo;
	
	private LinearLayout mbarPos;
	private LinearLayout mbarNage;
	private LinearLayout mbarSelf;
	
	private LinearLayout barPos;
	private LinearLayout barNage;
	private LinearLayout barSelf;
	
	private ImageView ivPos;
	private ImageView ivNage;
	private ImageView ivSelf;
	
	private ImageView cameraPos;
	private ImageView cameraNage;
	private ImageView cameraSelf;
	private TextView tvCameraPos;
	private TextView tvCameraNage;
	private TextView tvCameraSelf;
	
	private TextView tvPos;
	private TextView tvNage;
	private TextView tvSelf;
	
	private Button btnGo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_realname);
		setTitleText(R.string.title_realname);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		edtvName = (EditText) findViewById(R.id.realname_edtv_name);
		edtvIdNo = (EditText) findViewById(R.id.realname_edtv_idno);
		//去除空格方法
		edtvIdNo.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.toString().contains(" ")) {
					String[] str=s.toString().split(" ");
					String str1 = "";
					for (int i = 0; i < str.length; i++) {
						str1 += str[i];
					}
					edtvIdNo.setText(str1);
					edtvIdNo.setSelection(start);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//去除空格方法
		edtvName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.toString().contains(" ")) {
					String[] str=s.toString().split(" ");
					String str1 = "";
					for (int i = 0; i < str.length; i++) {
						str1 += str[i];
					}
					edtvName.setText(str1);
					edtvName.setSelection(start);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mbarPos = (LinearLayout) findViewById(R.id.realname_mbar_pos);
		mbarNage = (LinearLayout) findViewById(R.id.realname_mbar_nage);
		mbarSelf = (LinearLayout) findViewById(R.id.realname_mbar_self);
		
		barPos = (LinearLayout) findViewById(R.id.realname_bar_pos);
		barNage = (LinearLayout) findViewById(R.id.realname_bar_nage);
		barSelf = (LinearLayout) findViewById(R.id.realname_bar_self);
		
		ivPos = (ImageView) findViewById(R.id.realname_img_pos);
		ivNage = (ImageView) findViewById(R.id.realname_img_nage);
		ivSelf = (ImageView) findViewById(R.id.realname_img_self);
		
		cameraPos = (ImageView) findViewById(R.id.realname_camera_icon_pos);
		cameraNage = (ImageView) findViewById(R.id.realname_camera_icon_nage);
		cameraSelf = (ImageView) findViewById(R.id.realname_camera_icon_self);
		
		tvCameraPos = (TextView) findViewById(R.id.realname_camera_txt_pos);
		tvCameraNage = (TextView) findViewById(R.id.realname_camera_txt_nage);
		tvCameraSelf = (TextView) findViewById(R.id.realname_camera_txt_self);
		
		tvPos = (TextView) findViewById(R.id.realname_tv_pos);
		tvNage = (TextView) findViewById(R.id.realname_tv_nage);
		tvSelf = (TextView) findViewById(R.id.realname_tv_self);
		btnGo = (Button) findViewById(R.id.realname_btngo);
		
		tvPos.setOnClickListener(this);
		tvNage.setOnClickListener(this);
		tvSelf.setOnClickListener(this);
		tvCameraPos.setOnClickListener(this);
		tvCameraNage.setOnClickListener(this);
		tvCameraSelf.setOnClickListener(this);
		cameraPos.setOnClickListener(this);
		cameraNage.setOnClickListener(this);
		cameraSelf.setOnClickListener(this);
		ivPos.setOnClickListener(this);
		ivNage.setOnClickListener(this);
		ivSelf.setOnClickListener(this);
		btnGo.setOnClickListener(this);
		switchImageCard("pos");
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.realname_tv_pos){
			switchImageCard("pos");
		}else if(id==R.id.realname_tv_nage){
			switchImageCard("nage");
		}else if(id==R.id.realname_tv_self){
			switchImageCard("self");
		}else if(id==R.id.realname_camera_icon_pos||id==R.id.realname_camera_txt_pos){
			openCamera("pos");
		}else if(id==R.id.realname_camera_icon_nage||id==R.id.realname_camera_txt_nage){
			openCamera("nage");
		}else if(id==R.id.realname_camera_icon_self||id==R.id.realname_camera_txt_self){
			openCamera("self");
		}else if(id==R.id.realname_img_pos){
			openCamera("pos");
		}else if(id==R.id.realname_img_nage){
			openCamera("nage");
		}else if(id==R.id.realname_img_self){
			openCamera("self");
		}else if(id==R.id.realname_btngo){
			doRealNameAuth();
		}
	}
	public boolean doDataCheck(){
		if(edtvName.getText().toString().trim().equals("")){
			showToast("请输入真实姓名");return false;
		}
		if(edtvIdNo.getText().toString().trim().equals("")){
			showToast("请输入证件号码");return false;
		}
		if(edtvIdNo.getText().toString().trim().length()<18){
			showToast("请输入正确证件号码");return false;
		}
		/*if(edtvIdNo.getText().toString().trim().length()<18){
			showToast("请输入证件号码");return false;
		}*/
		if(posImageStr==null||posImageStr.equals("")){
			showToast("请拍摄证件正面照");return false;
		}
		if(nageImageStr==null||nageImageStr.equals("")){
			showToast("请拍摄证件背面照");return false;
		}
		if(selfImageStr==null||selfImageStr.equals("")){
			showToast("请拍摄手持证件照");return false;
		}
		return true;
	}
	public void doRealNameAuth(){
		try{
			if(!doDataCheck()){return;}
			final UserRealNameReqVo reqVo = new UserRealNameReqVo();
			reqVo.setRealName(this.edtvName.getText().toString());
			reqVo.setIdNo(this.edtvIdNo.getText().toString());
			reqVo.setPosImgStr(posImageStr);
			reqVo.setNageImgStr(nageImageStr);
			reqVo.setSelfImgStr(selfImageStr);
			
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
					parser = ReqWrapper.getUserAccRequest(RealNameAuthActy.this).doRealNameAuth(reqVo);
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						showToast("上传成功，请耐心等待审核");
						finish();
					}else if(parser.getRespCode()==1){
						showToast("上传出错了");
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
		ivPos.setVisibility(View.VISIBLE);
		ivNage.setVisibility(View.VISIBLE);
		ivSelf.setVisibility(View.VISIBLE);
		cameraPos.setVisibility(View.VISIBLE);
		cameraNage.setVisibility(View.VISIBLE);
		cameraSelf.setVisibility(View.VISIBLE);
		tvCameraPos.setVisibility(View.VISIBLE);
		tvCameraNage.setVisibility(View.VISIBLE);
		tvCameraSelf.setVisibility(View.VISIBLE);
		tvPos.setTextColor(Color.parseColor("#ff97db4f"));
		tvNage.setTextColor(Color.parseColor("#ff97db4f"));
		tvSelf.setTextColor(Color.parseColor("#ff97db4f"));
		/*
		tvPos.setTextColor(Color.parseColor("#ffcccccc"));
		tvNage.setTextColor(Color.parseColor("#ffcccccc"));
		tvSelf.setTextColor(Color.parseColor("#ffcccccc"));
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
		}else if(type.equals("self")){
			tvSelf.setTextColor(Color.parseColor("#ff97db4f"));
			ivSelf.setVisibility(View.VISIBLE);
			cameraSelf.setVisibility(View.VISIBLE);
			tvCameraSelf.setVisibility(View.VISIBLE);
		}
		*/
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
			        localFile2 = new File(localFile1, "realname_pos.jpg");
			        this.posFile = localFile2;
			        this.tmpuri = Uri.fromFile(this.posFile);
			        localIntent.putExtra("output", this.tmpuri);
			        startActivityForResult(localIntent, 2);
		        }else if(type.equals("nage")){
					localFile1 = new File(this.path_nage);
			        if (!localFile1.exists())
			        	localFile1.mkdirs();
			        localFile2 = new File(localFile1, "realname_nage.jpg");
			        this.nageFile = localFile2;
			        this.tmpuri = Uri.fromFile(this.nageFile);
			        localIntent.putExtra("output", this.tmpuri);
			        startActivityForResult(localIntent, 3);
		        }else if(type.equals("self")){
					localFile1 = new File(this.path_self);
			        if (!localFile1.exists())
			        	localFile1.mkdirs();
			        localFile2 = new File(localFile1, "realname_self.jpg");
			        this.selfFile = localFile2;
			        this.tmpuri = Uri.fromFile(this.selfFile);
			        localIntent.putExtra("output", this.tmpuri);
			        startActivityForResult(localIntent, 4);
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
			        this.posFile = new File(this.path_pos, "realname_pos.jpg");
			        this.imageBitmap = ImageUtil.optimizeBitmap(ImageUtil.readStream(this.posFile), 500, 480);
			        if (this.imageBitmap != null){
			        	Bitmap localBitmap2 = ImageUtil.rotateBitMap(this.imageBitmap,90.0F);
			        	ivPos.setImageBitmap(localBitmap2);
			        	ImageUtil.saveBitmap(localBitmap2, posFile.getAbsolutePath(), 100);
			        	DataStore.getMap().put("posFile", this.posFile);
			        	DataStore.getMap().put("posImage", localBitmap2);
			        	this.posImageStr = new String(Base64Util.encrypt(ImageUtil.readStream(this.posFile)));
			        	Log.i("info", "posImageStr--->" + this.posImageStr);
			        }
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(requestCode==3){
				try{
		            nageFile = new File(path_nage, "realname_nage.jpg");
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
			}else if(requestCode==4){
				try{
		            selfFile = new File(path_self, "realname_self.jpg");
		            imageBitmap = ImageUtil.optimizeBitmap(ImageUtil.readStream(selfFile), 500, 480);
					Log.i("info", "reverse bitmap not null");
					Bitmap localBitmap1 = ImageUtil.rotateBitMap(this.imageBitmap,0.0F);
					ivSelf.setImageBitmap(localBitmap1);
					ImageUtil.saveBitmap(localBitmap1, this.selfFile.getAbsolutePath(), 100);
					DataStore.getMap().put("selfFile", this.selfFile);
					DataStore.getMap().put("selfImage", localBitmap1);
					this.selfImageStr = new String(Base64Util.encrypt(ImageUtil.readStream(this.selfFile)));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
