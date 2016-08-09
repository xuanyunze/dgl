package com.rxoa.zlpay.combx;

import com.rxoa.zlpay.Config;
import com.rxoa.zlpay.base.util.HttpUtil;
import com.rxoa.zlpay.base.util.StringUtil;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.os.Bundle;

public class LocationParser {
	private static LocationParser instance;
	private double latitude=0.0;  
	private double longitude =0.0;
	LocationManager locationManager = null;
	LocationListener locationListener = null;
	
	public static LocationParser getInstance(Context context){
		if(instance==null){
			instance = new LocationParser();
			instance.init(context);
		}
		return instance;
	}
	public void init(Context context){
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {  
            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
            @Override  
            public void onStatusChanged(String provider, int status, Bundle extras) {  
                  
            }  
              
            // Provider被enable时触发此函数，比如GPS被打开  
            @Override  
            public void onProviderEnabled(String provider) {  
               
            }  
              
            // Provider被disable时触发此函数，比如GPS被关闭   
            @Override  
            public void onProviderDisabled(String provider) {  
                  
            }  
              
            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发   
            @Override  
            public void onLocationChanged(Location location) {  
                if (location != null) {     
                    Log.e("Map", "Location changed : Lat: "    
                    + location.getLatitude() + " Lng: "    
                    + location.getLongitude()); 
                    latitude = location.getLatitude(); //经度     
                    longitude = location.getLongitude(); //纬度
                    if(StringUtil.isDbNull(Config.location_string)){
                        Config.location_lat = latitude;
                        Config.location_lng = longitude;
                        resetName();
                    }else if(Config.location_lat!=latitude||Config.location_lng!=longitude){
                        Config.location_lat = latitude;
                        Config.location_lng = longitude;
                        resetName();
                    }
                }  
            }  
        };  
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);     
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);     
        if(location != null){     
            latitude = location.getLatitude(); //经度     
            longitude = location.getLongitude(); //纬度  
            if(StringUtil.isDbNull(Config.location_string)){
                Config.location_lat = latitude;
                Config.location_lng = longitude;
                resetName();
            }else if(Config.location_lat!=latitude||Config.location_lng!=longitude){
                Config.location_lat = latitude;
                Config.location_lng = longitude;
                resetName();
            }
        } 
	}
	
	public void start(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				try{
			        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			        	System.out.println("GPS开启");
			            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
			            if(location != null){  
			            	System.out.println("获取到GPS地址"+latitude+","+latitude);
			                latitude = location.getLatitude();  
			                longitude = location.getLongitude();
			                Config.location_lat = latitude;
			                Config.location_lng = longitude;
			             }  
			        }
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void resetName(){
        new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println("重置城市名称");
				String name = HttpUtil.getCityName( Config.location_lat+"", Config.location_lng+"");
				if(name!=null){
					Config.location_string = name;
				}
			}
        }).start();
	}
}
