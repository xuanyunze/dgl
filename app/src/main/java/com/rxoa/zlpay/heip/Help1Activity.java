package com.rxoa.zlpay.heip;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.R.layout;
import com.rxoa.zlpay.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.database.DataSetObserver;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Help1Activity extends BaseUIActivity implements OnClickListener {
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help1);
		initView();
	}

	
	private void initView() {
		setTitleText(R.string.title_help);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		/*tv=(TextView) findViewById(R.id.tv);
		Adapter adapter=new ArrayAdapter<T>(context, resource) ;*/
	}








	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help1, menu);
		return true;
	}


	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
	}

}
