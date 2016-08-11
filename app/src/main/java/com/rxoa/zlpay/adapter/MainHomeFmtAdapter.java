package com.rxoa.zlpay.adapter;

import com.rxoa.zlpay.acty.MainHomeActy;
import com.rxoa.zlpay.fragment.AccManageFmt;
import com.rxoa.zlpay.fragment.AppCenterFmt;
import com.rxoa.zlpay.fragment.MoreInfoFmt;
import com.rxoa.zlpay.fragment.OrderRecordFmt;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainHomeFmtAdapter extends FragmentPagerAdapter{
	public final static int TAB_COUNT = 4;
	public  MainHomeFmtAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int id) {
		switch (id) {
		case MainHomeActy.TAB_APP:
			AppCenterFmt appFragment = new AppCenterFmt();
			return appFragment;
		case MainHomeActy.TAB_ORDER:
			OrderRecordFmt orderFragment = new OrderRecordFmt();
			return orderFragment;
		case MainHomeActy.TAB_ACC:
			AccManageFmt accFragment = new AccManageFmt();
			return accFragment;
		case MainHomeActy.TAB_MORE:
			MoreInfoFmt moreFragment = new MoreInfoFmt();
			return moreFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}
}
