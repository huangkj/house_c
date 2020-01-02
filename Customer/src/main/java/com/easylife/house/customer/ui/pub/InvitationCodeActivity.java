package com.easylife.house.customer.ui.pub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 填写邀请码页面
 */
public class InvitationCodeActivity extends BaseActivity implements RequestManagerImpl {
    @Bind(R.id.edInvitationCode)
    EditText edInvitationCode;

    public static void startActivity(Activity activity, String name, String phone, String verifyCode, String pass, int requestCode) {
        activity.startActivityForResult(new Intent(activity, InvitationCodeActivity.class)
                        .putExtra("name", name)
                        .putExtra("phone", phone)
                        .putExtra("verifyCode", verifyCode)
                        .putExtra("pass", pass)
                , requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.pub_activity_invitation, null);
    }

    private String name;
    private String phone;
    private String verifyCode;
    private String pass;

    @Override
    protected void initView() {
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        verifyCode = getIntent().getStringExtra("verifyCode");
        pass = getIntent().getStringExtra("pass");
    }

    @Override
    protected void setActionBarDetail() {
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setText("跳过");
        btnRightText.setTextColor(getResources().getColor(R.color.gradient_end));
        btnRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   跳过填写邀请码
                mDao.register(1, name, phone, verifyCode, pass, null, InvitationCodeActivity.this);
            }
        });
    }

    @OnClick(R.id.btnSubmit)
    public void onViewClicked() {
        String inviteCode = edInvitationCode.getText().toString();
        if (TextUtils.isEmpty(inviteCode)) {
            Toast.makeText(activity, "请输入邀请码", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading();
        mDao.register(1, name, phone, verifyCode, pass, inviteCode, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        cancleLoading();
        mDao.pointRegister(phone);
        Toast.makeText(activity, "注册成功", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        cancleLoading();
        Toast.makeText(activity, code.msg, Toast.LENGTH_SHORT).show();
    }
}
