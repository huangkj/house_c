package com.easylife.house.customer.ui.pub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.allinpay.appayassistex.APPayAssistEx;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.ParamPay;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.LogOut;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mars on 2017/7/3 11:10.
 * 描述：
 */

public class TestActivity extends BaseActivity {

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.layout_test, null);
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void setActionBarDetail() {
    }

    private String hostAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startPayTest(View view) {
        mDao.payData(1, "安卓测试订单", "1", "asdhoahsfkasnfe", new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                LogOut.d("ParamPay: ", response);
                APPayAssistEx.startPay(activity, response,
                        APPayAssistEx.MODE_PRODUCT, hostAddress);
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {

            }
        });
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
                    showAppayRes("֧支付成功！");
                } else {
                    showAppayRes("֧支付失败！");
                }
                Log.d("payResult", "payRes: " + payRes + "  payAmount: "
                        + payAmount + "  payTime: " + payTime
                        + "  payOrderId: " + payOrderId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 支付按钮点击事件
     *
     * @param res
     */
    public void showAppayRes(String res) {
        new AlertDialog.Builder(this).setMessage(res)
                .setPositiveButton("确定", null).show();
    }
}
