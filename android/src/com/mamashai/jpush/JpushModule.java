/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package com.mamashai.jpush;

import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.titanium.TiApplication;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.api.*;

@Kroll.module(name="Jpush", id="com.mamashai.jpush")
public class JpushModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "JpushModule";
	private static final boolean DBG = TiConfig.LOGD;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;

	public JpushModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		Log.d(LCAT, "inside onAppCreate");
		JPushInterface.setDebugMode(true);
        JPushInterface.init(app); //init jpush
	}
	
	@Kroll.method
    public void stopPush(){ //stop push
        JPushInterface.stopPush(TiApplication.getInstance());
    }
    
    @Kroll.method
    public void resumePush(){ //resume push
        JPushInterface.resumePush(TiApplication.getInstance());
    }
    
    
    @Kroll.method
    public void setAliasAndTags(String alias, Object[] tags,final KrollFunction callback){
        Set set=new HashSet();
        for(Object n : tags){ 					
           set.add(n.toString());
        }
        
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(TiApplication.getInstance());
        //builder.statusBarDrawable = R.drawable.jpush_notification_icon;
        //builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        //builder.notificationDefaults = Notification.DEFAULT_SOUND ｜ Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;  // 设置为铃声与震动都要
        
        JPushInterface.setPushNotificationBuilder(1, builder);
        
        JPushInterface.setAliasAndTags(TiApplication.getInstance(),alias,set,new TagAliasCallback(){
            @Override
            public void gotResult(int arg0, String arg1,Set<String> arg2) {
                Log.d("JPush", "Jpush setAliasAndTags status: " + arg0);
                if(callback != null){
                    KrollDict map = new KrollDict(); 
                    map.put("code", arg0);   
                    String device_token = JPushInterface.getRegistrationID(TiApplication.getInstance());
                    map.put("device_token", device_token);
                    callback.callAsync(getKrollObject(),map);
                    
                    Log.d("JPush", "Jpush setTags status: " + arg0 + " device_token: " + device_token);
                }
            }
        });
    }
    
    @Kroll.method
    public void setAlias(String alias,final KrollFunction callback){
        JPushInterface.setAlias(TiApplication.getInstance(),alias,new TagAliasCallback(){
            @Override
            public void gotResult(int arg0, String arg1,Set<String> arg2) {
                Log.d("JPush", "Jpush setAlias status: " + arg0);
                if(callback != null){
                    KrollDict map = new KrollDict();
                    map.put("code", arg0);
                    String device_token = JPushInterface.getRegistrationID(TiApplication.getInstance());
                    map.put("device_token", device_token);
                    callback.callAsync(getKrollObject(),map);
                    
                    Log.d("JPush", "Jpush setTags status: " + arg0 + " device_token: " + device_token);
                }
            }
        });
    }
    
    @Kroll.method
    public void setTags(Object[] tags,final KrollFunction callback){
        Set set=new HashSet();
        for(Object n : tags){
           set.add(n.toString());
        }
        JPushInterface.setTags(TiApplication.getInstance(),set,new TagAliasCallback(){
            @Override
            public void gotResult(int arg0, String arg1,Set<String> arg2) {
                if(callback != null){
                    KrollDict map = new KrollDict();
                    map.put("code", arg0);       
                    String device_token = JPushInterface.getRegistrationID(TiApplication.getInstance());
                    map.put("device_token", device_token);
                    callback.callAsync(getKrollObject(),map);
                    
                    Log.d("JPush", "Jpush setTags status: " + arg0 + " device_token: " + device_token);
                }
            }
        });
    }
    

	// Methods
	@Kroll.method
	public String example()
	{
		Log.d(LCAT, "example called");
		return "hello world";
	}

	// Properties
	@Kroll.getProperty
	public String getExampleProp()
	{
		Log.d(LCAT, "get example property");
		return "hello world";
	}


	@Kroll.setProperty
	public void setExampleProp(String value) {
		Log.d(LCAT, "set example property: " + value);
	}

}
