package com.rxoa.zlpay.acty;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.util.ImageUtil;
import com.rxoa.zlpay.listener.DialogListener;
import com.rxoa.zlpay.view.MyDialog;

public class MakeSignatureActy extends BaseUIActivity implements OnClickListener,OnTouchListener{
	private Bitmap mSignBitmap;
	private ImageView ivSign;
	private String output;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		output = intent.getStringExtra("output");
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_makesignature);
		setTitleText(R.string.title_makesignature);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		this.ivSign = ((ImageView)findViewById(R.id.makesignature_iv_sig));
	    new MyDialog(this, new DialogListener(){
	    	public void refreshActivity(Object paramAnonymousObject){
	    		MakeSignatureActy.this.mSignBitmap = ((Bitmap)paramAnonymousObject);
	            MakeSignatureActy.this.ivSign.setVisibility(0);
	            ImageUtil.saveBitmap(mSignBitmap, output, 100);
	            
                Intent intent = new Intent();  
                intent.putExtra("name","admin");  
                intent.putExtra("password","password");
                setResult(RESULT_OK,intent);
                finish(); 
	        }
	    }).show();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}
