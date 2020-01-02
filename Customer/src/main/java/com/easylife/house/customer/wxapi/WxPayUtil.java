package com.easylife.house.customer.wxapi;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.widget.Toast;

import com.easylife.house.customer.config.ConstantsKeys;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.factory.OkhttpManager;
import com.easylife.house.customer.http.factory.RequestFactory;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.SettingsUtil;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;

import static com.easylife.house.customer.wxapi.WXPayEntryActivity.ACTION_WX_PAY_RESULT;

public class WxPayUtil {
    private IWXAPI api;
    private Context context;
    private ProgressDialog dialog;
    private WXPayResult payResult;
    private OkhttpManager manager;

    public WxPayUtil(Context context, WXPayResult payResult) {
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, ConstantsKeys.WX_APPID);
        boolean isSucc = api.registerApp(ConstantsKeys.WX_APPID);
        dialog = new ProgressDialog(context);
        dialog.setMessage("正在请求微信支付，请稍候...");
        this.payResult = payResult;

        manager = RequestFactory.getOkManager();
    }

    /**
     * 微信支付成功或失败的通知
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == ACTION_WX_PAY_RESULT) {
                try {
                    // 微信支付结束
                    ConstantsKeys.isPayingWX = false;
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    boolean booleanExtra = intent.getBooleanExtra("success", false);
                    System.out.print("booleanExtra : " + booleanExtra);
                    ConstantsKeys.isWxPaySuccess = booleanExtra;
                    if (payResult != null) {
                        payResult.paySuccess(booleanExtra);
                    }
                } catch (Exception e) {
                }
                context.unregisterReceiver(receiver);
            }
        }
    };

    /**
     * 微信支付结果
     *
     * @author heiyue heiyue623@126.com
     * @ClassName: com.zipingfang.framework.dao.WXPayResult
     * @Description: TODO
     * @date 2015-2-15 下午3:34:45
     */
    public interface WXPayResult {
        // 成功回调
        void paySuccess(boolean success);

        void payStart();
    }

    /**
     * @param price    价格，单位元 如0.01
     * @param subject  主题，可以作为备注
     * @param orderids 支付订单的订单号 多个订单号以逗号分隔
     */
    public void pay(String price, String subject, String orderids) {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            Toast.makeText(context, "您的手机暂不支持微信支付，请先下载最新版微信",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "customer/weChat/order";


        if (TextUtils.isEmpty(price)) {
            Toast.makeText(context, "支付金额错误", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            price = ((int) (Double.parseDouble(price) * 100)) + "";
        } catch (Exception e) {
            LogOut.d("支付金额错误", e.toString());
        }
//        price = "1";

        dialog.show();
        Map<String, Object> mapData = new ArrayMap<>();
        mapData.put("desc", subject);
        mapData.put("out_trade_no", orderids);
        mapData.put("total_fee", price);
        mapData.put("spbill_create_ip", SettingsUtil.getWIFIIp(context));
        manager.post(1, url, mapData, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                dialog.dismiss();
                try {
                    LogOut.d("response:", response);
                    JSONObject json = new JSONObject(response);
                    if (null != json) {
                        String prepayId = json.getString("prepayid");
                        if (TextUtils.isEmpty(prepayId)) {
                            Toast.makeText(context, "预支付订单号生成失败，请重试", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        PayReq request = new PayReq();
                        request.appId = json.getString("appid");
                        request.partnerId = json.getString("partnerid");
                        request.prepayId = json.getString("prepayid"); // 预支付交易会话ID
                        request.packageValue = "Sign=WXPay";
                        request.nonceStr = json.getString("noncestr"); // 随机字符串，不长于32位
                        request.timeStamp = json.getString("timestamp");
                        request.sign = json.getString("sign");
//                        request.sign ="d0127b12f6d4e70fe33b0b8d27bc7dea";
                        if (payResult != null) {
                            // 开始请求微信支付
                            payResult.payStart();
                            // 标识正则进行微信支付
                            ConstantsKeys.isPayingWX = true;
                        }
                        api.sendReq(request);
                        // 注册监听
                        context.registerReceiver(receiver, new IntentFilter(
                                ACTION_WX_PAY_RESULT));
                    }
                } catch (Exception e) {
                    LogOut.d("TASK_GET_TOKEN", "异常：" + e.getMessage());
                    Toast.makeText(context, "异常：" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    if (payResult != null) {
                        // 开始请求微信支付
                        payResult.paySuccess(false);
                    }
                }
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                dialog.dismiss();
            }
        }, false);
    }

}
