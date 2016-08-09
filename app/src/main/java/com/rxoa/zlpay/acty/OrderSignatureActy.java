package com.rxoa.zlpay.acty;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.avator.AvOrderStat;
import com.rxoa.zlpay.avator.AvOrderType;
import com.rxoa.zlpay.avator.AvPayType;
import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.base.util.Base64Util;
import com.rxoa.zlpay.base.util.DataStore;
import com.rxoa.zlpay.base.util.ImageUtil;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.util.AvatorUtil;
import com.rxoa.zlpay.vo.OrderDetailReqVo;
import com.rxoa.zlpay.vo.OrderDetailRespVo;
import com.rxoa.zlpay.vo.OrderSignatureReqVo;

public class OrderSignatureActy extends BaseUIActivity implements OnClickListener{
	private String path_sig = Config.path_sig;
	private ViewHolder viewHolder = new ViewHolder();
	private String orderid = null;
	private String ordertype = null;
	
	private File sigFile;
	private String sigImageStr;
	private Bitmap imageBitmap;
	private Uri tmpuri;

	private TextView tvHand;
	private TextView tvPhoto;
	private ImageView ivSigImage;
	private Button btnReset;
	private Button btnGo;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		orderid = intent.getStringExtra("orderid");
		ordertype = intent.getStringExtra("ordertype");
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_order_signature);
		setTitleText(R.string.title_order_signature);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
		tvHand = (TextView) findViewById(R.id.order_signature_tv_hand);
		tvPhoto = (TextView) findViewById(R.id.order_signature_tv_photo);
		ivSigImage = (ImageView) findViewById(R.id.order_signature_sigimg);
		btnReset = (Button) findViewById(R.id.order_signature_btnreset);
		btnGo = (Button) findViewById(R.id.order_signature_btngo);
		
		tvHand.setOnClickListener(this);
		tvPhoto.setOnClickListener(this);
		btnReset.setOnClickListener(this);
		btnGo.setOnClickListener(this);
		
		viewHolder.tvOrderId = (TextView) findViewById(R.id.order_confirm_flowid_tv);
		viewHolder.tvOrderType = (TextView) findViewById(R.id.order_confirm_type_tv);
		viewHolder.tvOrderDate = (TextView) findViewById(R.id.order_confirm_date_tv);
		viewHolder.tvOrderValue = (TextView) findViewById(R.id.order_confirm_value_tv);
		viewHolder.tvOrderStat = (TextView) findViewById(R.id.order_confirm_stat_tv);
		viewHolder.tvPayType = (TextView) findViewById(R.id.order_confirm_paytype_tv);
		viewHolder.tvPayAccNo = (TextView) findViewById(R.id.order_detail_payaccno_tv);
	
		viewHolder.tvReceAccName = (TextView) findViewById(R.id.order_content_receaccname_tv);
		viewHolder.tvReceAccNo = (TextView) findViewById(R.id.order_content_receaccno_tv);
		viewHolder.tvReceBankName = (TextView) findViewById(R.id.order_content_recebankname_tv);
		viewHolder.tvReceBankDistr = (TextView) findViewById(R.id.order_content_recebankdistr_tv);
		viewHolder.tvReceBankBranch = (TextView) findViewById(R.id.order_content_recebankbranch_tv);
		viewHolder.tvNotifyPhone = (TextView) findViewById(R.id.order_content_notifyphone_tv);
		viewHolder.tvChargePhone = (TextView) findViewById(R.id.order_content_phoneno_tv);
		viewHolder.tvTipValue = (TextView) findViewById(R.id.order_detail_value_label);
		
		getOrderDetail();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}else if(id==R.id.order_signature_tv_hand){
			openSignature();
		}else if(id==R.id.order_signature_tv_photo){
			openCamera("photo");
		}else if(id==R.id.order_signature_btnreset){
			resetSignature();
		}else if(id==R.id.order_signature_btngo){
			updateSignature();
		}
	}
	public void resetSignature(){
		try{
			ivSigImage.setVisibility(View.GONE);
			tvHand.setVisibility(View.VISIBLE);
			tvPhoto.setVisibility(View.VISIBLE);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void openSignature(){
		try{
			Intent localIntent = new Intent(OrderSignatureActy.this,MakeSignatureActy.class);
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
				showToast("");
			} 
		}catch(Exception e){
			e.printStackTrace();
		} 
	}
	public void openCamera(String type){
		try{
			Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
			if (Environment.getExternalStorageState().equals("mounted")){
		        File localFile1 = null,localFile2 = null;
				if(type.equals("photo")){
					localFile1 = new File(this.path_sig);
			        if (!localFile1.exists())
			        	localFile1.mkdirs();
			        localFile2 = new File(localFile1, "sig.jpg");
			        this.sigFile = localFile2;
			        this.tmpuri = Uri.fromFile(this.sigFile);
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
			        	ivSigImage.setImageBitmap(localBitmap2);
						ivSigImage.setVisibility(View.VISIBLE);
						tvHand.setVisibility(View.GONE);
						tvPhoto.setVisibility(View.GONE);
			        	ImageUtil.saveBitmap(localBitmap2, sigFile.getAbsolutePath(), 100);
			        	DataStore.getMap().put("sigFile", this.sigFile);
			        	DataStore.getMap().put("sigImage", localBitmap2);
			        	this.sigImageStr = new String(Base64Util.encrypt(ImageUtil.readStream(this.sigFile)));
			        }
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateSignature(){
		try{
			final OrderSignatureReqVo reqVo = new OrderSignatureReqVo();
			reqVo.setImgStr(sigImageStr);
			reqVo.setOrderId(orderid);
			reqVo.setOrderType(ordertype);
			new DefAsyncTask(OrderSignatureActy.this){
				RespParser parser = null;
				@Override
				public void onPrepare() {
					// TODO Auto-generated method stub
					setMessage("正在提交订单签名...");
					super.onPrepare();
				}
				@Override
				public void doInBack() {
					// TODO Auto-generated method stub
					try{
						parser = ReqWrapper.getOrderRequest(OrderSignatureActy.this).doOrderSignature(reqVo);
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
							showToast("签名提交成功");
							finish();
						}catch(Exception e){
							e.printStackTrace();
						}
					}else if(parser.getRespCode()==1){
						showToast("订单签名提交失败");
					}
				}
			}.excute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void getOrderDetail(){
		try{
			if(!Config._isLogin){return;}
			final OrderDetailReqVo reqVo = new OrderDetailReqVo();
			reqVo.setOrderFlowid(orderid);
			reqVo.setOrderType(ordertype);

			new DefAsyncTask(OrderSignatureActy.this){
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
						parser = ReqWrapper.getOrderRequest(OrderSignatureActy.this).doQueryOrderDetail(reqVo);
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
							OrderDetailRespVo respVo = (OrderDetailRespVo)parser.getRespObject();
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
	private void refreshViewUI(OrderDetailRespVo respVo){
		try{
			viewHolder.tvOrderId.setText(respVo.getOrderFlowid());
			viewHolder.tvOrderType.setText(AvOrderType.value2text(respVo.getOrderType()));
			viewHolder.tvOrderDate.setText(respVo.getOrderDate());
			viewHolder.tvOrderValue.setText(respVo.getOrderValue());
			viewHolder.tvOrderStat.setText(AvOrderStat.value2text(respVo.getOrderStat()));
			viewHolder.tvPayType.setText(AvPayType.value2text(respVo.getPayType()));
			viewHolder.tvPayAccNo.setText(AvatorUtil.dealNull(respVo.getPayAccNo()));
			String otype = AvOrderType.code2value(ordertype);
			if(ordertype.equals(otype)){
				viewHolder.tvTipValue.setText("收款金额：");
			}else if(ordertype.equals(otype)){
				viewHolder.tvTipValue.setText("充值金额：");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static class ViewHolder{
		TextView tvOrderId;
		TextView tvOrderType;
		TextView tvOrderValue;
		TextView tvOrderDate;
		TextView tvOrderStat;
		TextView tvPayType;
		TextView tvPayAccNo;
		
		TextView tvReceAccName;
		TextView tvReceAccNo;
		TextView tvReceBankName;
		TextView tvReceBankDistr;
		TextView tvReceBankBranch;
		TextView tvNotifyPhone;
		TextView tvChargePhone;
		
		TextView tvTipValue;
	}
}
