package com.rxoa.zlpay.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.rxoa.zlpay.base.async.DefAsyncTask;
import com.rxoa.zlpay.net.ReqWrapper;
import com.rxoa.zlpay.net.RespParser;
import com.rxoa.zlpay.vo.SysUpdateReqVo;
import com.rxoa.zlpay.vo.SysUpdateRespVo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class VersionMgr {
	private Context context = null;
	private String curVersion = null;
	private String newVersion = null;
	private String downloadUrl = null;
	private String newDescription;
	private String TAG = "version";
	private final static int DOWN_ERROR = 2;
	
	public static VersionMgr getInstance(Context context){
		VersionMgr vManager = new VersionMgr();
		vManager.context = context;
		return vManager;
	}
	public void checkVersion(){
		try{
			this.curVersion = getVersionName();
			getUpdataInfo();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private String getVersionName() throws Exception{  
		//获取packagemanager的实例    
		PackageManager packageManager = context.getPackageManager();  
		//getPackageName()是你当前类的包名，0代表是获取版本信息   
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);  
		return packInfo.versionName;
	}
	 
	public void getUpdataInfo() throws Exception{  
		final SysUpdateReqVo reqVo = new SysUpdateReqVo();
		new DefAsyncTask(context){
			RespParser parser = null;
			@Override
			public void onPrepare() {
				// TODO Auto-generated method stub
				setMessage("正在检查更新...");
				super.onPrepare();
			}
			@Override
			public void doInBack() {
				// TODO Auto-generated method stub
				try{
					parser = ReqWrapper.getSysRequest(context).doCheckUpdate(reqVo);
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
						newVersion = ((SysUpdateRespVo)parser.getRespObject()).getVersionName();
						downloadUrl = ((SysUpdateRespVo)parser.getRespObject()).getDownloadUrl();
						newDescription = ((SysUpdateRespVo)parser.getRespObject()).getDescription();
						if(!newVersion.equals(curVersion)){
							showUpdataDialog();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if(parser.getRespCode()==1){
					Toast.makeText(context, "加载更新信息失败",Toast.LENGTH_LONG).show();
				}
			}
		}.excute();
	}
	
	public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{  
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的   
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
			URL url = new URL(path);  
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();  
			conn.setConnectTimeout(5000);  
			//获取到文件的大小    
			pd.setMax(conn.getContentLength());  
			InputStream is = conn.getInputStream();  
			File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");  
			FileOutputStream fos = new FileOutputStream(file);  
			BufferedInputStream bis = new BufferedInputStream(is);  
			byte[] buffer = new byte[1024];  
			int len ;  
			int total=0;  
			while((len =bis.read(buffer))!=-1){  
				fos.write(buffer, 0, len);  
				total+= len;  
				//获取当前下载量   
				pd.setProgress(total);  
			}  
			fos.close();  
			bis.close();  
			is.close();  
			return file;  
		}else{  
			return null;  
		}  
	}
	
	Handler handler = new Handler(){     
	    @Override 
	    public void handleMessage(Message msg) { 
	        // TODO Auto-generated method stub  
	        super.handleMessage(msg); 
	        switch (msg.what) {  
	            case DOWN_ERROR: 
	                //下载apk失败  
	                Toast.makeText(context, "下载新版本失败!", Toast.LENGTH_LONG).show(); 
	            break;   
	            } 
	    } 
	};
	/*
	 * 
	 * 弹出对话框通知用户更新程序 
	 * 
	 * 弹出对话框的步骤：
	 *  1.创建alertDialog的builder.  
	 *  2.要给builder设置属性, 对话框的内容,样式,按钮
	 *  3.通过builder 创建一个对话框
	 *  4.对话框show()出来  
	 */ 
	protected void showUpdataDialog() { 
	    Builder builer = new Builder(context) ;
	    builer.setTitle("版本升级"); 
	    builer.setMessage(newDescription); 
	    //当点确定按钮时从服务器上下载 新的apk 然后安装   
	    builer.setPositiveButton("确定", new OnClickListener() { 
	    public void onClick(DialogInterface dialog, int which) { 
	            Log.i(TAG,"下载apk,更新"); 
	            downLoadApk(); 
	        }    
	    }); 
	    //当点取消按钮时进行登录  
	    builer.setNegativeButton("取消", new OnClickListener() { 
	        public void onClick(DialogInterface dialog, int which) { 
	        	
	        } 
	    }); 
	    AlertDialog dialog = builer.create(); 
	    dialog.show(); 
	}
	/*
	 * 从服务器中下载APK
	 */ 
	protected void downLoadApk() { 
	    final ProgressDialog pd;    //进度条对话框  
	    pd = new  ProgressDialog(context); 
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
	    pd.setMessage("正在下载更新..."); 
	    pd.show(); 
	    new Thread(){ 
	        @Override 
	        public void run() { 
	            try { 
	                File file = getFileFromServer(downloadUrl, pd); 
	                sleep(3000); 
	                installApk(file); 
	                pd.dismiss(); //结束掉进度条对话框
	            } catch (Exception e) { 
	                Message msg = new Message(); 
	                msg.what = DOWN_ERROR; 
	                handler.sendMessage(msg); 
	                e.printStackTrace(); 
	            } 
	        }}.start();
	}
	//安装apk   
	protected void installApk(File file) { 
	    Intent intent = new Intent(); 
	    //执行动作  
	    intent.setAction(Intent.ACTION_VIEW); 
	    //执行的数据类型  
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");//编者按：此处Android应为android，否则造成安装不了   
	    context.startActivity(intent);
	}
}
