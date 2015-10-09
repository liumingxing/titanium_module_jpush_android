# titanium_module_jpush_android
* 极光推送的安卓封装（ios平台我用rpush在自己的服务器发，没做module）
* 作者刘明星 lmxbitihero@126.com

Appcelerator安卓开发比较悲催，无法使用google的gcm，只能绕道，用国内的服务，极光推送还不错。

订阅推送：
```ruby
var push = require('com.mamashai.jpush');
push.setAlias(Ti.App.Properties.getString("userid", ""), function(e){
	Ti.API.log("register to jpush code: " + e.code + ", token: " + e.device_token);
});
```

接收推送：
```ruby
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
