package com.rxoa.zlpay;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.rxoa.zlpay.R;
import com.rxoa.zlpay.acty.MainHomeActy;
import com.rxoa.zlpay.base.BaseActivity;

public class BaseUIActivity extends BaseActivity{
	
	
	public void setTitleText(String text){
		((TextView)findViewById(R.id.titleText)).setText(text);
	}
	public void setLeftButtonText(String text){
		((TextView)findViewById(R.id.titleLeftButton)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.titleLeftButton)).setText(text);
	}
	public void setRightButtonText(String text){
		((TextView)findViewById(R.id.titleRightButton)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.titleRightButton)).setText(text);
	}
	public void setTitleText(int id){
		((TextView)findViewById(R.id.titleText)).setText(id);
	}
	public void setLeftButtonText(int id){
		((TextView)findViewById(R.id.titleLeftButton)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.titleLeftButton)).setText(id);
	}
	public void setRightButtonText(int id){
		((TextView)findViewById(R.id.titleRightButton)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.titleRightButton)).setText(id);
	}
	public TextView getLeftButton(){
		return (TextView)findViewById(R.id.titleLeftButton);
	}
	public TextView getRightButton(){
		return (TextView)findViewById(R.id.titleRightButton);
	}
	
	//***返回首页按钮***
	protected void goHomeBtnClick(Activity acty){
		try{
			Intent intent = new Intent(acty,MainHomeActy.class);
			acty.startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//***返回上一页按钮***
	protected void goBackBtnClick(){
		try{
			this.finish();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
