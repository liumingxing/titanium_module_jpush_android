package com.mamashai.jpush;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import cn.jpush.android.api.JPushInterface;
import android.app.ActivityManager;
import android.content.ComponentName;

import cn.jpush.android.service.PushReceiver;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MamashaiReceiver extends PushReceiver {
	private static final String TAG = "MamashaiJPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        String running_activity_name = getRunningActivityName(context);
        super.onReceive(context, intent);
        
        Log.d("~~~~~~~~~~~", running_activity_name);
        if (running_activity_name.equals("org.appcelerator.titanium.TiActivity")){
        	if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
	        	Intent intent2 = new Intent();
	            intent2.setAction("mamashai_jpush_received");
	        	intent2.putExtras(intent.getExtras());
	            
	            context.sendBroadcast(intent2);
	        }
        	
        	if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()) ||
	        		JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
	        	Intent intent2 = new Intent();
	            intent2.setAction("mamashai_jpush");
	        	intent2.putExtras(intent.getExtras());
	            
	            context.sendBroadcast(intent2);
	        }
        }
        else{
        	if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
	        	PackageManager pm = context.getPackageManager();
    			Intent intent2 = pm.getLaunchIntentForPackage(context.getPackageName());
    			intent2.putExtras(intent.getExtras());
    			context.startActivity(intent2);
    			
    			Log.d("~~~~~~~~~~~", "send intent args");
	        	Intent intent3 = new Intent();
	            intent3.setAction("mamashai_jpush");
	        	intent3.putExtras(intent.getExtras());
	            
	            context.sendBroadcast(intent3);

	        	return;
	        }
        }
	}
	
	private String getRunningActivityName(Context context){          
        ActivityManager activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();  
        return runningActivity;                 
	}
}
