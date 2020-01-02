package com.easylife.house.customer.ui.payment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.allinpay.appayassistex.APPayAssistEx;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.AuthResult;
import com.easylife.house.customer.bean.OrderParameter;
import com.easylife.house.customer.bean.PayResult;
import com.easylife.house.customer.bean.ResultAlipaySign;
import com.easylife.house.customer.bean.ResultCheckOrder;
import com.easylife.house.customer.bean.ResultMakeOrder;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.payment.favorable.FavorablePayResultActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.DoubleFomat;
import com.easylife.house.customer.util.LogOut;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.wxapi.WxPayUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Mars on 2017/4/10 20:19.
 * 描述：选择支付方式，并下单
 */
public class PayTypeActivity extends BaseActivity implements RequestManagerImpl {
    @Bind(R.id.tvOrderName)
    TextView tvOrderName;
    @Bind(R.id.tvOrderDesc)
    TextView tvOrderDesc;
    @Bind(R.id.tvMoney)
    TextView tvMoney;
    @Bind(R.id.btnZhifubao)
    RadioButton btnZhifubao;
    @Bind(R.id.btnWechat)
    RadioButton btnWechat;
    @Bind(R.id.btnAppay)
    RadioButton btnAppay;
    @Bind(R.id.btnPay)
    ButtonTouch btnPay;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.payment_activity_paytype, null);
    }

    public static void startActivity(Activity activity, String orderCode, String localSerial, String orderName, String orderMark, OrderParameter parameter, int requestCode) {
        activity.startActivityForResult(new Intent(activity, PayTypeActivity.class)
                        .putExtra("orderCode", orderCode)
                        .putExtra("localSerial", localSerial)
                        .putExtra("orderName", orderName)
                        .putExtra("orderMark", orderMark)
                        .putExtra("parameter", parameter)
                , requestCode);
    }

    private OrderParameter parameter;
    private String orderName;
    private String orderMark;
    private String orderCode;
    private String localSerial;
    /**
     * 0-支付宝
     * 1-微信
     * 2-通联
     */
    private int payType;
    private ResultCheckOrder resultCheckOrder;

    @Override
    protected void initView() {
        orderCode = getIntent().getStringExtra("orderCode");
        localSerial = getIntent().getStringExtra("localSerial");
        orderName = getIntent().getStringExtra("orderName");
        orderMark = getIntent().getStringExtra("orderMark");
        parameter = (OrderParameter) getIntent().getSerializableExtra("parameter");

        tvOrderName.setText(orderName);
        tvOrderDesc.setText(orderMark);

        if (parameter != null && parameter.orderLog != null) {
            String price = DoubleFomat.format2(parameter.orderLog.payOnThisTime);
            tvMoney.setText("￥" + price);
            btnPay.setText("确认支付" + price + "元");
        }
    }

    @Override
    protected void setActionBarDetail() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.mipmap.icon_pay_type_question);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.startActivity(activity, "", Constants.URL_ORDER_DESC);
            }
        });
    }

    @OnClick({R.id.btnZhifubao, R.id.btnWechat, R.id.btnPay, R.id.btnAppay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnZhifubao:
                payType = 0;
                btnWechat.setChecked(false);
                btnAppay.setChecked(false);
                break;
            case R.id.btnWechat:
                payType = 1;
                btnZhifubao.setChecked(false);
                btnAppay.setChecked(false);
                break;
            case R.id.btnAppay:
                payType = 2;
                btnWechat.setChecked(false);
                btnZhifubao.setChecked(false);
                break;
            case R.id.btnPay:
                if (TextUtils.isEmpty(orderCode)) {
                    mDao.makeOrder(1, parameter, this);
                } else {
                    pay();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (APPayAssistEx.REQUESTCODE == requestCode) {
            if (null != data) {
                String payRes = null;
                String payAmount = null;
                String payTime = null;
                String payOrderId = null;
                try {
                    JSONObject resultJson = new JSONObject(data.getExtras()
                            .getString("result"));
                    payRes = resultJson.getString(APPayAssistEx.KEY_PAY_RES);
                    payAmount = resultJson.getString("payAmount");
                    payTime = resultJson.getString("payTime");
                    payOrderId = resultJson.getString("payOrderId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS)) {
                    mDao.checkOrderState(2, orderCode, PayTypeActivity.this);
                } else {
                    showPayFail();
                }
                Log.d("payResult", "payRes: " + payRes + "  payAmount: "
                        + payAmount + "  payTime: " + payTime
                        + "  payOrderId: " + payOrderId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    private String versionName = "1.0";
    private PayTask alipay;

    private void pay() {
        switch (payType) {
            case 1:
                // 微信支付
                new WxPayUtil(activity, new WxPayUtil.WXPayResult() {
                    @Override
                    public void paySuccess(boolean success) {
                        if (success) {
                            mDao.checkOrderState(2, orderCode, PayTypeActivity.this);
                        } else {
                            // 支付失败
                            showPayFail();
                        }
                    }

                    @Override
                    public void payStart() {
                    }
                }).pay(parameter.orderLog.payOnThisTime, orderName, localSerial);
                break;
            case 0:
                // 支付宝支付
                if (alipay == null) {
                    alipay = new PayTask(activity);
                    versionName = alipay.getVersion();
                }
                mDao.alipay(3, localSerial, orderName, parameter.orderLog.payOnThisTime, versionName, this);
                break;
            case 2:
                // 通联支付
                mDao.payData(1, orderName, parameter.orderLog.payOnThisTime, localSerial, new RequestManagerImpl() {
                    @Override
                    public void onSuccess(String response, int requestType) {
                        LogOut.d("ParamPay: ", response);
                        APPayAssistEx.startPay(activity, response,
                                APPayAssistEx.MODE_PRODUCT, "com.easylife.house.customer.fileprovider");
                    }

                    @Override
                    public void onFail(NetBaseStatus code, int requestType) {

                    }
                });
                break;
        }
    }

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    LogOut.d("payResult:", payResult.toString());
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        mDao.checkOrderState(2, orderCode, PayTypeActivity.this);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showPayFail();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(activity,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(activity,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 1:
                // 下单成功，跳转第三方支付
                ResultMakeOrder resultMakeOrder = new Gson().fromJson(response, ResultMakeOrder.class);
                if (resultMakeOrder == null) {
                    CustomerUtils.showTip(activity, "下单失败，请重试");
                    return;
                }
                orderCode = resultMakeOrder.orderCode;
                if (TextUtils.isEmpty(orderCode)) {
                    CustomerUtils.showTip(activity, "订单号返回失败，请重试");
                    return;
                }
                if (parameter != null && parameter.orderLog != null) {
                    pay();
                } else {
                    LogOut.d(" 下单成功，跳转第三方支付", "订单信息错误");
                }
                break;
            case 2:
                // 支付结果确认
                resultCheckOrder = new Gson().fromJson(response, ResultCheckOrder.class);
                if (resultCheckOrder == null) {
                    showPayFail();
                    return;
                }
                if (!"SUCCESS".equals(resultCheckOrder.isSuccess)) {
                    showPayFail();
                    return;
                }
                setResult(RESULT_OK);
                FavorablePayResultActivity.startActivity(activity, orderCode, resultCheckOrder, 1);
                break;
            case 3:

                final ResultAlipaySign resultAlipaySign = new Gson().fromJson(response, ResultAlipaySign.class);
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {

                        Map<String, String> result = alipay.payV2(resultAlipaySign.Result, true);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
                break;
        }
    }


    private PubTipDialog dialog;

    private void showPayFail() {
        if (dialog == null) {
            dialog = new PubTipDialog(this, null);
        }
        dialog.showPayTip(R.layout.pub_toast_pay_fail);
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
    }
}
