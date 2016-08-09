package com.rxoa.zlpay;

import com.rxoa.zlpay.R;
import com.rxoa.zlpay.base.BaseFragment;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class BaseUIFragment extends BaseFragment{
	@SuppressWarnings("unused")
	private void setContent(View view,int id, String content) {
		try{
			if (content != null) {
				TextView textView = (TextView) view.findViewById(id);
				String title = textView.getText().toString();
				String html = title + ":  <font color=#909090>" + content
						+ "</font>";
				textView.setText(Html.fromHtml(html));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setTitleText(View view,String text){
		((TextView)view.findViewById(R.id.titleText)).setText(text);
	}
	public void setLeftButtonText(View view,String text){
		((TextView)view.findViewById(R.id.titleLeftButton)).setVisibility(View.VISIBLE);
		((TextView)view.findViewById(R.id.titleLeftButton)).setText(text);
	}
	public void setRightButtonText(View view,String text){
		((TextView)view.findViewById(R.id.titleRightButton)).setVisibility(View.VISIBLE);
		((TextView)view.findViewById(R.id.titleRightButton)).setText(text);
	}
	public void setTitleText(View view,int id){
		((TextView)view.findViewById(R.id.titleText)).setText(id);
	}
	public void setLeftButtonText(View view,int id){
		((TextView)view.findViewById(R.id.titleLeftButton)).setVisibility(View.VISIBLE);
		((TextView)view.findViewById(R.id.titleLeftButton)).setText(id);
	}
	public void setRightButtonText(View view,int id){
		((TextView)view.findViewById(R.id.titleRightButton)).setVisibility(View.VISIBLE);
		((TextView)view.findViewById(R.id.titleRightButton)).setText(id);
	}
	public TextView getLeftButton(View view){
		return (TextView)view.findViewById(R.id.titleLeftButton);
	}
	public TextView getRightButton(View view){
		return (TextView)view.findViewById(R.id.titleRightButton);
	}
}
