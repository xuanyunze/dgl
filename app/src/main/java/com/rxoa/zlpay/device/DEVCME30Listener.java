package com.rxoa.zlpay.device;

import com.newland.mtype.module.common.emv.EmvControllerListener;
import com.newland.mtype.module.common.swiper.SwipResult;

public interface DEVCME30Listener extends EmvControllerListener{
	public void onSwipMagneticCard(SwipResult swipRslt);
	public void onOpenCardreaderCanceled();
}