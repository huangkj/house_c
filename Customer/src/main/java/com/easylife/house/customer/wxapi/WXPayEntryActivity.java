package com.easylife.house.customer.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.easylife.house.customer.config.ConstantsKeys;
import com.easylife.house.customer.util.LogOut;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    public static final String ACTION_WX_PAY_RESULT = "action_wx_pay_result";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ConstantsKeys.WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogOut.d("onResp  *-* - *- * : ", resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
                    //支付成功
                    Intent intent = new Intent(ACTION_WX_PAY_RESULT);
                    intent.putExtra("success", true);
                    Toast.makeText(this, "支付成功!", Toast.LENGTH_SHORT).show();
                    sendBroadcast(intent);
                    break;
                default:
                    //支付未完成
                    intent = new Intent(ACTION_WX_PAY_RESULT);
                    intent.putExtra("success", false);
                    Toast.makeText(this, "未完成支付!", Toast.LENGTH_SHORT).show();
                    sendBroadcast(intent);
                    break;
            }
            finish();
        }
    }
}