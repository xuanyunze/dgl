package com.rxoa.zlpay.combx;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

abstract public class ComxDialog {
	private Dialog dialog = null;
	
	public Dialog genBaseDialog(Context context){
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setIcon(null);
		localBuilder.setMessage(null);
	    localBuilder.setTitle("友情提示");
	    localBuilder.setPositiveButton("立即登录", new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		doPostiveClick();
	    	}
	    });
	    localBuilder.setNegativeButton("前往实名认证", new DialogInterface.OnClickListener(){
	    	public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
	    		doNegativeClick();
	    	}
	    });
	    this.dialog = localBuilder.create();
	    this.dialog.setCancelable(false);
	    this.dialog.setCanceledOnTouchOutside(false);
	    this.dialog.show();
	    return this.dialog;
	}
	
	abstract public void doPostiveClick();
	abstract public void doNegativeClick();
}
