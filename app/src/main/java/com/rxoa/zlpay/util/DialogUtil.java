package com.rxoa.zlpay.util;

import android.R.layout;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import com.rxoa.zlpay.MainApplication;


public class DialogUtil{
	public static void showDialog(final Context paramContext, final MainApplication paramContextApplication){
		final String simpleName = paramContext.getClass().getSimpleName();
		Builder localBuilder = new Builder(paramContext);
		localBuilder.setTitle(null);
		localBuilder.setMessage(paramContext.getResources().getString(2131165230));
		localBuilder.setPositiveButton(paramContext.getResources().getString(2131165232), new OnClickListener(){
			public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
				paramAnonymousDialogInterface.dismiss();
				if ("AppCenterActy".equals(simpleName)){
					//AppCenterActy localAppCenterActy = (AppCenterActy)paramContext;
					//paramAnonymousDialogInterface.dismiss();
					//localAppCenterActy.finish();
				}
        while (true)
        {
          if (paramContextApplication != null)
          {
        	  
          }
          if ("AccountManagerActy".equals(simpleName))
          {
            //AccountManagerActy localAccountManagerActy = (AccountManagerActy)paramContext;
            //paramAnonymousDialogInterface.dismiss();
            //localAccountManagerActy.finish();
          }
          else if ("ConsumeActy".equals(simpleName))
          {
            //ConsumeActy localConsumeActy = (ConsumeActy)paramContext;
            //paramAnonymousDialogInterface.dismiss();
            //localConsumeActy.finish();
          }
          else if ("MoreInfoActy".equals(simpleName))
          {
            //MoreInfoActy localMoreInfoActy = (MoreInfoActy)paramContext;
            //paramAnonymousDialogInterface.dismiss();
            //localMoreInfoActy.finish();
          }
          else
          {
            //((PeanutsHomeActy)DataStore.getMap().get("homeActivity")).finish();
          }
        }
      }
    });
    localBuilder.setNegativeButton(paramContext.getResources().getString(2131165237), new OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface.dismiss();
      }
    });
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.setCanceledOnTouchOutside(false);
    localAlertDialog.show();
  }

	public static ProgressDialog showProDialog(Context paramContext){
		ProgressDialog localProgressDialog = new ProgressDialog(paramContext);
		localProgressDialog.setMessage(paramContext.getResources().getString(2131165220));
		localProgressDialog.setCanceledOnTouchOutside(false);
		localProgressDialog.setCancelable(false);
		return localProgressDialog;
	}

	public static void showToast(Context paramContext, String paramString){
		//View localView = LayoutInflater.from(paramContext).inflate(R.id., null);
		//((TextView)localView.findViewById(2131362077)).setText(paramString);
		//Toast localToast = new Toast(paramContext);
		//localToast.setView(localView);
		//localToast.setDuration(1);
		//localToast.show();
		Toast.makeText(paramContext, paramString,
			     Toast.LENGTH_SHORT).show();
	}

	public static ProgressDialog showVersionProDialog(Context paramContext){
		ProgressDialog localProgressDialog = new ProgressDialog(paramContext);
		localProgressDialog.setMessage("版本检测中，请稍后...");
		localProgressDialog.setCanceledOnTouchOutside(false);
		localProgressDialog.setCancelable(false);
		return localProgressDialog;
	}
}