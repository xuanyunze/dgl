package com.rxoa.zlpay.acty;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.acty.PhoneChargeInputActy.RadioClickListener;
import com.rxoa.zlpay.acty.PhoneChargeInputActy.RadioOnClick;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.Base64Util;
import com.rxoa.zlpay.base.util.DataStore;
import com.rxoa.zlpay.base.util.ImageUtil;
import com.rxoa.zlpay.base.util.StringUtil;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.entity.UserAccInfo;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.util.AvatorUtil;
import com.rxoa.zlpay.vo.BindMainCardReqVo;
import com.rxoa.zlpay.vo.BindOtherCardReqVo;
import com.rxoa.zlpay.vo.UserAccInfoReqVo;
import com.rxoa.zlpay.vo.UserAccInfoRespVo;

public class BindOtherCardActy extends BaseUIActivity implements OnClickListener{
	private String path_pos = Environment.getExternalStorageDirectory().getAbsolutePath() + "/realname";
	private File posFile;
	private Uri tmpuri;

	private String[] valuetype = new String[]{"信用卡","借记卡"};
	private boolean[] valuestat=new boolean[]{true, false};  
	private RadioOnClick radioOnClick = new RadioOnClick(0);
	private ListView lvValuetype;

	private LinearLayout barCardType;
	private LinearLayout barIsFast;
	private CheckBox chxIsFast;

	private TextView tvCardType;
	private TextView tvIsFast;

	private CheckBox isFast;
	private EditText edtvAccName;
	private EditText edtvAccNo;
	private EditText edtvBankName;
	private EditText edtvanquao;
	private EditText edtvBankDistr;
	private EditText edtvBankBranch;
	private EditText edtcardValidDate;
	private EditText edtcardCvv;
	private TextView tvOldCard ;
	private Button btnGo;
	
	
	private LinearLayout mbarPos;
	private LinearLayout barPos;
	private ImageView ivPos;
	private ImageView cameraPos;
	private TextView tvCameraPos;
	private TextView tvPos;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView(){
		setContentView(R.layout.acty_bind_othercard);
		setTitleText(R.string.title_bind_othercard);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		mbarPos=(LinearLayout) findViewById(R.id.bind_maincard_mbar_pos1);
		barPos=(LinearLayout) findViewById(R.id.bind_maincard_bar_pos1);
		ivPos=(ImageView) findViewById(R.id.bind_maincard_img_pos1);
		cameraPos=(ImageView) findViewById(R.id.bind_maincard_img_pos1);
		tvCameraPos=(TextView) findViewById(R.id.bind_maincard_camera_txt_pos1);
		tvPos=(TextView) findViewById(R.id.bind_maincard_tv_pos1);
		tvOldCard=(TextView) findViewById(R.id.bind_othercard_old);
		
		edtvAccName = (EditText) findViewById(R.id.bind_othercard_accname);
		edtvAccNo = (EditText) findViewById(R.id.bind_othercard_accno);
		edtvBankName = (EditText) findViewById(R.id.bind_othercard_bankname);
		edtvBankDistr = (EditText) findViewById(R.id.bind_othercard_bankdistr);
		edtvBankBranch = (EditText) findViewById(R.id.bind_othercard_bankbranch);
		edtcardValidDate=(EditText) findViewById(R.id.bind_othercard_cardValidDate);
		edtcardCvv=(EditText) findViewById(R.id.bind_othercard_cardCvv);
		/*tvOldCard=(EditText) findViewById(R.id.bind_othercard_old);*/
		

		barCardType = (LinearLayout) findViewById(R.id.bind_othercard_bar_cardtype);
		tvCardType = (TextView) findViewById(R.id.bind_othercard_tv_cardtype);
		barCardType.setOnClickListener(new RadioClickListener());
		barIsFast = (LinearLayout) findViewById(R.id.bind_othercard_bar_isfast);
		tvIsFast = (TextView) findViewById(R.id.bind_othercard_tv_isfast);
		chxIsFast = (CheckBox) findViewById(R.id.bind_othercard_ivbtn_isfast);
		btnGo = (Button) findViewById(R.id.bind_othercard_btngo);
		barIsFast.setOnClickListener(this);
		chxIsFast.setOnClickListener(this);
		btnGo.setOnClickListener(this);
		
		tvPos.setOnClickListener(this);
		tvCameraPos.setOnClickListener(this);
		cameraPos.setOnClickListener(this);
		ivPos.setOnClickListener(this);

		edtvAccName.setEnabled(false);
		getAccoutInfo();
		switchImageCard("pos");
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.bind_othercard_ivbtn_isfast){
			if(chxIsFast.isChecked()){
				tvIsFast.setText("开通");
			}else{
				tvIsFast.setText("不开通");
			}

		}else if(id==R.id.bind_maincard_tv_pos1){
			switchImageCard("pos");
		}
		else if(id==R.id.bind_maincard_camera_icon_pos1||id==R.id.bind_maincard_camera_txt_pos1){
			openCamera("pos");}
		else if(id==R.id.bind_maincard_img_pos1){
			openCamera("pos");
		}
		else if(id==R.id.bind_othercard_btngo){
			doBindOtherCard();
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
					localFile2 = new File(localFile1, "realname_pos.jpg");
					this.posFile = localFile2;
					this.tmpuri = Uri.fromFile(this.posFile);
					localIntent.putExtra("output", this.tmpuri);
					startActivityForResult(localIntent, 2);
				}
			}else{
				showToast("");
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
	}
	public boolean doDataCheck(){
		if(edtvAccName.getText().toString().trim().equals("")){
			showToast("请填写账户名称");return false;
		}
		if(edtvAccNo.getText().toString().trim().equals("")){
			showToast("请填写银行卡号");return false;
		}
		/*if(edtvBankName.getText().toString().trim().equals("")){
			showToast("请填写开户行");return false;
		}

		if(edtvBankBranch.getText().toString().trim().equals("")){
			showToast("请填写开户支行");return false;
		}
		if(tvCardType.getText().toString().trim().equals("")){
			showToast("请选择银行卡类型");return false;
		}*/

		if(edtcardValidDate.getText().toString().trim().equals("")){
			showToast("请填写有效期");return false;
		}

		/*if(edtcardCvv.getText().toString().trim().equals("")){
			showToast("请填写安全码");return false;
		}*/
		if(posImageStr==null||posImageStr.equals("")){
			showToast("请拍摄证件正面照");return false;
		}
		return true;
	}
	public void switchImageCard(String type){
		/*ivPos.setVisibility(View.VISIBLE);

		tvPos.setTextColor(Color.parseColor("#ff97db4f"));*/

	}
	public void doBindOtherCard(){
		try{
			if(!doDataCheck()){return;}
			final BindOtherCardReqVo reqVo = new BindOtherCardReqVo();
			reqVo.setAccName(edtvAccName.getText().toString());
			reqVo.setAccNo(edtvAccNo.getText().toString());
			reqVo.setBankName(edtvBankName.getText().toString());
			reqVo.setBankDistr(edtvBankDistr.getText().toString());
			reqVo.setCardValidDate(edtcardValidDate.getText().toString());
			reqVo.setCardCvv(edtcardCvv.getText().toString());
			reqVo.setPosImgStr(posImageStr);
			reqVo.setCardType(AvatorUtil.bankCardTypeT2V(tvCardType.getText().toString()));
			reqVo.setIsFast(AvatorUtil.isFastPayT2V(tvIsFast.getText().toString()));

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
					parser = ReqWrapper.getUserAccRequest(BindOtherCardActy.this).doBindOtherCard(reqVo);
				}
				@Override
				public void onPosExcute() {
					// TODO Auto-generated method stub
					super.onPosExcute();
					if(parser.getRespCode()==0){
						showToast("提交成功，请耐心等待审核");
						finish();
					}else if(parser.getRespCode()==1){
						showToast("提交出错了");
					}else{
						showToast("您的操作太频繁了");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	class RadioClickListener implements OnClickListener {  
		@Override  
		public void onClick(View v) {  
			AlertDialog ad = new AlertDialog.Builder(BindOtherCardActy.this).setTitle("选择银行卡类型")
					.setSingleChoiceItems(valuetype,radioOnClick.getIndex(),radioOnClick).create();  
			lvValuetype = ad.getListView();  
			ad.show();
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
			tvCardType.setText(valuetype[index]);
			dialog.dismiss();
		}
	}

	public void getAccoutInfo(){
		try{
			if(!Config._isLogin){return;}
			final UserAccInfoReqVo reqVo = new UserAccInfoReqVo();
			new DefAsyncTask(BindOtherCardActy.this){
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
						parser = ReqWrapper.getUserAccRequest(BindOtherCardActy.this).doQueryUserAccInfo(reqVo);
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
							List<BankCardEntity> otherCardInfo = ((UserAccInfoRespVo)parser.getRespObject()).getOtherCardInfo();
							if(StringUtil.isDbNull(accInfo.getRealName())||StringUtil.isDbNull(accInfo.getIdNo())){
								showToast("请先进行实名认证");finish();
							}else{
								edtvAccName.setText(accInfo.getRealName());
							}
							if(otherCardInfo!=null&&otherCardInfo.size()>0){
								tvOldCard.setText(Html.fromHtml("已绑定"+(otherCardInfo.size())+"张信用卡"));
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

			}
		}
	}
	private String posImageStr;
	private Bitmap imageBitmap;
}
