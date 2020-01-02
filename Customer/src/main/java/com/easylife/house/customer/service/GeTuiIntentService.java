package com.easylife.house.customer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.easylife.house.customer.App;
import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.PushMsgBean;
import com.easylife.house.customer.dao.LocalDao;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.util.ChannelUtils;
import com.easylife.house.customer.util.LogOut;
import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import static com.easylife.house.customer.dao.LocalDao.FOLDER_CACHE_PUB;
import static com.easylife.house.customer.dao.LocalDao.KEY_UN_READ_MSG_COUNT;
import static com.easylife.house.customer.event.MessageEvent.FORCE_LOGIN_OUT;
import static com.easylife.house.customer.event.MessageEvent.PUSH_MSG;

/**
 * Created by zgm on 2017/3/13.
 * 消息推送回调透传
 */

public class GeTuiIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        // 接收 cid
        LogOut.d("onReceiveClientId", "onReceiveClientId -> " + "clientid = " + clientid);
        App.CLIENT_ID = clientid;
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        // 透传消息的处理方式
        byte[] payload = gtTransmitMessage.getPayload();
        if (payload == null)
            return;

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, gtTransmitMessage.getTaskId(),
                gtTransmitMessage.getMessageId(), 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));

        String data = new String(payload);
        LogOut.d("clientiddata", data);

        int id = new Random().nextInt(1000);
        Intent resultIntent = new Intent(context, PushNotificationReceiver.class);
        resultIntent.putExtra("id", id);
        resultIntent.putExtra("data", data);
        PushMsgBean newsBean = null;
        try {
            newsBean = new Gson().fromJson(data, PushMsgBean.class);
        } catch (Exception e) {
            LogOut.d("消息解析失败", data + "\n" + e.toString());
            newsBean = new PushMsgBean();
            newsBean.setTitle("系统通知");
            newsBean.setText(data);
        }
        LogOut.d("消息", newsBean == null ? "空" : newsBean.toString());
        if (newsBean.getMsgType() == -1) {
            EventBus.getDefault().post(new MessageEvent(FORCE_LOGIN_OUT));
            return;
        }

        LocalDao localDao = new LocalDao(this);
        int unReadMsgCount = localDao.getInt(FOLDER_CACHE_PUB, KEY_UN_READ_MSG_COUNT, 0);
        unReadMsgCount++;
        localDao.saveInt(FOLDER_CACHE_PUB, KEY_UN_READ_MSG_COUNT, unReadMsgCount);
        EventBus.getDefault().post(new MessageEvent(PUSH_MSG));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id + "", ChannelUtils.getChannelName(context), NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setChannelId(id + "")
                    .setContentTitle(newsBean.getTitle())
                    .setContentText(newsBean.getText())
                    .setContentIntent(pendingIntent).setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(newsBean.getTitle())
                    .setContentText(newsBean.getText())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent).setDefaults(Notification.DEFAULT_ALL)
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(id, notification);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        // cid 离线上线通知
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        // 各种事件处理回执
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
        // 通知到达，只有个推通道下发的通知会回调此方法
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
        // 通知点击，只有个推通道下发的通知会回调此方法
    }
}
