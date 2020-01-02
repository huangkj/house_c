package com.easylife.house.customer.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easylife.house.customer.bean.PushMsgBean;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.ui.message.MessageListActivity;
import com.easylife.house.customer.ui.message.messagehouses.MessageHousesActivity;
import com.easylife.house.customer.ui.message.topmessage.TopMessageActivity;
import com.easylife.house.customer.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * - 需要自定义通知栏消息展示，否则8.0+透传消息不会展示在通知栏
 */
public class PushNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //让通知消失
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra("id", -1));

        String data = intent.getStringExtra("data");

        Intent resultIntent = new Intent();
        PushMsgBean newsBean = GsonUtils.fromJson(data, PushMsgBean.class);
        switch (newsBean.getMsgType()) {
            case 0:
                // 头条资讯
                resultIntent.setClass(context, TopMessageActivity.class);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_MESSAGE_HEADLINE));
                context.startActivity(resultIntent);
                break;
            case 1:
                // 楼盘订阅
                resultIntent.setClass(context, MessageHousesActivity.class);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.UPDATE_MESSAGE_HOUSE));
                context.startActivity(resultIntent);
                break;
            case 7:
                //跟进消息
                resultIntent.setClass(context, MessageListActivity.class);
                resultIntent.putExtra("msgType", newsBean.msgType);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.PUSH_MSG_REFRESH));
                context.startActivity(resultIntent);
                break;
        }
    }
}
