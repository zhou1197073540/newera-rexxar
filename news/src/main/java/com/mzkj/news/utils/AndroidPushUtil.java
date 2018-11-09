package com.mzkj.news.utils;

import com.mzkj.news.bean.MsgPushBean;
import com.mzkj.news.bean.MsgPushNews;
import com.mzkj.news.bean.MsgPushStock;
import com.mzkj.news.push.AndroidNotification;
import com.mzkj.news.push.PushClient;
import com.mzkj.news.push.android.*;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AndroidPushUtil {
	private final static String appkey = "5b7244bca40fa35639000010";
	private final static String appMasterSecret = "3jd22b9jlkpwqmcmqjtalxqaab0msuth";
	private String timestamp = null;
	private static PushClient client = new PushClient();

    public AndroidPushUtil() {}
//	public AndroidPushUtil(String key, String secret) {
//		try {
//			this.appkey = key;
//			appMasterSecret = secret;
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//	}

    public static void main(String[] args) throws Exception {
        AndroidPushUtil t=new AndroidPushUtil();
        MsgPushBean bean=new MsgPushBean("新消息","你好");
//        MsgPushNews stock=new MsgPushNews();
//        t.sendAndroidByToken(bean,stock);

//        t.sendAndroidAllDevice(bean);
        //指定跳转页面
//        MsgPushStock stock=new MsgPushStock("sz000001","平安银行","000001");
//        MsgPushNews stock=new MsgPushNews("你好","平安银行","1900");
//        t.sendAndroidBroadcastActivity(stock);
    }


    public void sendAndroidAllDevice(MsgPushBean msg) throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast(appkey,appMasterSecret);
		broadcast.setTicker( "有新消息通知!");
        broadcast.setTitle( msg.getTitle());
        broadcast.setText( msg.getContent());
		broadcast.goAppAfterOpen();
		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		broadcast.setProductionMode();
		// Set customized fields
//		broadcast.setExtraField("test", "helloworld");
		client.send(broadcast);
	}

    /**
     *股票到家提醒，根据用户app token来推送
     */
    public static boolean pushAndroidNews(MsgPushNews map,String app_token) throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(appkey,appMasterSecret);
        unicast.setDeviceToken(app_token);
        unicast.setTicker( "有新消息通知!");
        unicast.setTitle(map.getTitle());
        unicast.setText("测试。。。");
        unicast.goAppAfterOpen();
        unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        unicast.goActivityAfterOpen(MsgPushStock.package_name);
        unicast.setProductionMode();
        Set set = map.keySet();
        for(Object o:set){
            String key=String.valueOf(o);
            unicast.setExtraField(key,map.get(o));
        }
        return client.send(unicast);
    }

    /**
     *股票到家提醒，根据用户app token来推送
     */
	public static boolean sendAndroidByToken(MsgPushStock map,String app_uid,float warnPrice) throws Exception {
		AndroidUnicast unicast = new AndroidUnicast(appkey,appMasterSecret);
		unicast.setDeviceToken(app_uid);
        unicast.setTicker( "有新消息通知!");
        unicast.setTitle("到价提醒:"+warnPrice+","+map.get("symbolName")+"("+map.get("symbol")+")");
        unicast.setText( "你关注的股票到价了，请及时查看！");
		unicast.goAppAfterOpen();
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
        unicast.goActivityAfterOpen(MsgPushStock.package_name);
		unicast.setProductionMode();
        Set set = map.keySet();
        for(Object o:set){
            String key=String.valueOf(o);
            unicast.setExtraField(key,map.get(o));
        }
		return client.send(unicast);
	}

}
