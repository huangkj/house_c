package com.easylife.house.customer.ui.mine.accountsafe;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.view.ButtonTouch;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 忘记密码 验证身份证
 */
public class TradeForgetPswActivity extends BaseActivity {

    @Bind(R.id.etRealName)
    EditText etRealName;
    @Bind(R.id.etIdCardNumber)
    EditText etIdCardNumber;
    @Bind(R.id.btnVerify)
    ButtonTouch btnVerify;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_trade_forget_psw, null);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void setActionBarDetail() {

    }

    public static void startActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, TradeForgetPswActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @OnClick({R.id.btnVerify})
    public void onViewClick(View v) {
        String realName = etRealName.getText().toString().trim();
        String idCardNum = etIdCardNumber.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btnVerify:
                if (TextUtils.isEmpty(realName)) {
                    ToastUtils.showShort("请输入真实姓名");
                    return;
                }
                if (TextUtils.isEmpty(idCardNum)) {
                    ToastUtils.showShort("请输入身份证号");
                    return;
                }

                if (!RegexUtils.isIDCard18(idCardNum)) {
                    ToastUtils.showShort("身份证格式不正确");
                    return;
                }


                showLoading();

                mDao.checkIdCard(0, realName, idCardNum, new RequestManagerImpl() {
                    @Override
                    public void onSuccess(String response, int requestType) {
                        cancleLoading();
                        SetTraderPasswordActivity.startActivity(TradeForgetPswActivity.this, 0);
//                        setResult(RESULT_OK);
//                        finish();
                    }

                    @Override
                    public void onFail(NetBaseStatus code, int requestType) {
                        cancleLoading();
                        ToastUtils.showShort(code.msg);
                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
