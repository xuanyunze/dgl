package com.rxoa.zlpay.acty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.rxoa.zlpay.BaseUIActivity;
import com.rxoa.zlpay.R;
import com.rxoa.zlpay.entity.BankCardEntity;
import com.rxoa.zlpay.util.AvatorUtil;

public class BankCardDetailActy extends BaseUIActivity implements OnClickListener{
	private BankCardEntity cardInfo;
	
	private TextView tvAccName;
	private TextView tvAccNo;
	private TextView tvBankName;
	private TextView tvBankDistr;
	private TextView tvBankBranch;
	private TextView tvCardType;
	private TextView tvIsFast;
	private TextView tvIsMainCard;
	private TextView tvBindTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		cardInfo = (BankCardEntity) intent.getSerializableExtra("card");
		initView();
	}
	
	private void initView(){
		setContentView(R.layout.acty_bankcard_info);
		setTitleText(R.string.title_card_info);
		setLeftButtonText(R.string.btn_return_text);
		getLeftButton().setOnClickListener(this);
		
		tvAccName = (TextView) findViewById(R.id.cardinfo_accname_tv);
		tvAccNo = (TextView) findViewById(R.id.cardinfo_accno_tv);
		tvBankName = (TextView) findViewById(R.id.cardinfo_bankname_tv);
		tvBankDistr = (TextView) findViewById(R.id.cardinfo_bankdistr_tv);
		tvBankBranch = (TextView) findViewById(R.id.cardinfo_bankbranch_tv);
		tvCardType = (TextView) findViewById(R.id.cardinfo_cardtype_tv);
		tvIsFast = (TextView) findViewById(R.id.cardinfo_isfastpay_tv);
		tvIsMainCard = (TextView) findViewById(R.id.cardinfo_ismaincard_tv);
		tvBindTime = (TextView) findViewById(R.id.cardinfo_bindtime_tv);
		
		refreshViewUI();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.titleLeftButton){
			goBackBtnClick();
		}
	}
	private void refreshViewUI(){
		try{
			tvAccName.setText(cardInfo.getAccName());
			tvAccNo.setText(cardInfo.getAccNo());
			tvBankName.setText(cardInfo.getAccBank());
			tvBankDistr.setText(AvatorUtil.dealNull(cardInfo.getAccDistr()));
			tvBankBranch.setText(AvatorUtil.dealNull(cardInfo.getAccBankBranch()));
			tvCardType.setText(AvatorUtil.bankCardTypeV2T(cardInfo.getCardType()));
			tvIsFast.setText(AvatorUtil.isFastPayV2T(cardInfo.getIsFast()));
			tvIsMainCard.setText(AvatorUtil.isMainCardV2T(cardInfo.getIsMainCard()));
			tvBindTime.setText(cardInfo.getBindTime());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
