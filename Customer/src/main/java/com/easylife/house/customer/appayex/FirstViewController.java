package com.easylife.house.customer.appayex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.allinpay.appayassistex.APPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstViewController extends Activity {
    private String hostAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void startPayTest(View view) {
        JSONObject payData = PaaCreator.randomPaa();
        // APPayAssistEx.startPay(this, payData.toString(),
        // APPayAssistEx.MODE_DEBUG);
        APPayAssistEx.startPay(this, payData.toString(),
                APPayAssistEx.MODE_DEBUG, hostAddress);
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
