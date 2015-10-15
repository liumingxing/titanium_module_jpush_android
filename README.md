# titanium_module_jpush_android
* 极光推送的安卓封装（ios平台我用rpush在自己的服务器发，没做module）
* 作者刘明星 lmxbitihero@126.com

Appcelerator安卓开发比较悲催，无法使用google的gcm，只能绕道，用国内的服务，极光推送还不错。

订阅推送：
```javascript
var push = require('com.mamashai.jpush');
push.setAlias(Ti.App.Properties.getString("userid", ""), function(e){
	Ti.API.log("register to jpush code: " + e.code + ", token: " + e.device_token);
});
```

接收推送：
```javascript
var act = Titanium.Android.currentActivity;
var _intent = act.intent;
var str = _intent.getStringExtra("cn.jpush.android.EXTRA");
if (str && str.length > 0){
	push_call(_intent.getStringExtra("cn.jpush.android.ALERT"), JSON.parse(str));
}

//用户点击了推送
var bc = Ti.Android.createBroadcastReceiver({
	onReceived : function(e) {
		Ti.API.info("cn.jpush.android.PUSH_ID: " + e.intent.getStringExtra("cn.jpush.android.PUSH_ID"));
		Ti.API.info("app: " 							+ e.intent.getStringExtra("app"));
		Ti.API.info("cn.jpush.android.ALERT: " 			+ e.intent.getStringExtra("cn.jpush.android.ALERT"));
		Ti.API.info("cn.jpush.android.EXTRA: " 			+ e.intent.getStringExtra("cn.jpush.android.EXTRA"));
		Ti.API.info("cn.jpush.android.NOTIFICATION_ID: "+ e.intent.getStringExtra("cn.jpush.android.NOTIFICATION_ID"));
		Ti.API.info("cn.jpush.android.NOTIFICATION_CONTENT_TITLE: " + e.intent.getStringExtra("cn.jpush.android.NOTIFICATION_CONTENT_TITLE"));
		Ti.API.info("cn.jpush.android.MSG_ID: " 		+ e.intent.getStringExtra("cn.jpush.android.MSG_ID"));
		Ti.API.info("cn.jpush.android.TITLE: " 			+ e.intent.getStringExtra("cn.jpush.android.TITLE"));
		Ti.API.info("cn.jpush.android.MESSAGE: " 		+ e.intent.getStringExtra("cn.jpush.android.MESSAGE"));
		Ti.API.info("cn.jpush.android.CONTENT_TYPE: " 	+ e.intent.getStringExtra("cn.jpush.android.CONTENT_TYPE"));
		var str = e.intent.getStringExtra("cn.jpush.android.EXTRA"); 
		if (str && str.length > 0){
			show_alert("提示", e.intent.getStringExtra("cn.jpush.android.EXTRA"));
		}
	}
});
		 
Ti.Android.registerBroadcastReceiver(bc, ['mamashai_jpush']);
//一定要释放掉，否则容易出问题
win.addEventListener("close", function(){
	Ti.Android.unregisterBroadcastReceiver(bc);
});	
		
//收到推送，还未打开
var bc2 = Ti.Android.createBroadcastReceiver({
    onReceived : function(e) {
        var json = JSON.parse(e.intent.getStringExtra("cn.jpush.android.EXTRA"));
        //to do
    }
});
Ti.Android.registerBroadcastReceiver(bc2, ['mamashai_jpush_received']);
//一定要释放掉，否则容易出问题
win.addEventListener("close", function(){
	Ti.Android.unregisterBroadcastReceiver(bc2);
});	
```	

使用module前还需要对tiapp.xml进行配置，在android->mainifest->application下添加如下配置信息
```xml
<service android:enabled="true" android:exported="false" android:name="cn.jpush.android.service.PushService">
	<intent-filter>
        	<action android:name="cn.jpush.android.intent.REGISTER"/>
                <action android:name="cn.jpush.android.intent.REPORT"/>
                <action android:name="cn.jpush.android.intent.PushService"/>
                <action android:name="cn.jpush.android.intent.PUSH_TIME"/>
        </intent-filter>
</service>
<receiver android:enabled="true" android:name="cn.jpush.android.service.PushReceiver">
	<intent-filter android:priority="1000">
		<action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED_PROXY"/>
		<category android:name="com.mamashai.babycalendar"/>
	</intent-filter>
        <intent-filter>
		<action android:name="android.intent.action.USER_PRESENT"/>
		<action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
	</intent-filter>
	<intent-filter>
		<action android:name="android.intent.action.PACKAGE_ADDED"/>
		<action android:name="android.intent.action.PACKAGE_REMOVED"/>
                <data android:scheme="package"/>
	</intent-filter>
</receiver>
<receiver android:name="com.mamashai.jpush.MamashaiReceiver">
	<intent-filter>
		<action android:name="cn.jpush.android.intent.REGISTRATION"/>
                <action android:name="cn.jpush.android.intent.UNREGISTRATION"/>
                <action android:name="cn.jpush.android.intent.MESSAGE_RECEIVED"/>
                <!--Required  用户接收SDK消息的intent-->
                <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED"/>
                <!--Required  用户接收SDK通知栏信息的intent-->
                <action android:name="cn.jpush.android.intent.NOTIFICATION_OPENED"/>
                <!--Required  用户打开自定义通知栏的intent-->
                <action android:name="cn.jpush.android.intent.ACTION_RICHPUSH_CALLBACK"/>
                <!--Optional 用户接受Rich Push Javascript 回调函数的intent-->
                <action android:name="cn.jpush.android.intent.CONNECTION"/>
                <!-- 接收网络变化 连接/断开 since 1.6.3 -->
                <category android:name="com.mamashai.babycalendar"/>
        </intent-filter>
</receiver>
<activity
	android:configChanges="orientation|keyboardHidden"
	android:name="cn.jpush.android.ui.PushActivity" android:theme="@android:style/Theme.Translucent.NoTitleBar">
	<intent-filter>
		<action android:name="cn.jpush.android.ui.PushActivity"/>
		<category android:name="android.intent.category.DEFAULT"/>
		<category android:name="com.mamashai.babycalendar"/>
	</intent-filter>
	</activity>
<service android:enabled="true" android:exported="false" android:name="cn.jpush.android.service.DownloadService"/>
<receiver android:name="cn.jpush.android.service.AlarmReceiver"/>
<!-- Required. For publish channel feature -->
<!-- JPUSH_CHANNEL 是为了方便开发者统计APK分发渠道。-->
<!-- 例如: -->
<!-- 发到 Google Play 的APK可以设置为 google-play; -->
<!-- 发到其他市场的 APK 可以设置为 xxx-market。 -->
<!-- 目前这个渠道统计功能的报表还未开放。-->
<meta-data android:name="JPUSH_CHANNEL" android:value="c_1980"/>
<meta-data android:name="JPUSH_APPKEY" android:value="b789c8ed387ca31a1569c932"/>
```
