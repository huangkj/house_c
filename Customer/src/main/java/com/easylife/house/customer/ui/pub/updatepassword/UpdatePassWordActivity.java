package com.easylife.house.customer.ui.pub.updatepassword;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.ui.pub.forgetpassword.ForgetPassWordActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.easylife.house.customer.view.EditTextClearAble;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 修改密码页面
 */
public class UpdatePassWordActivity extends MVPBaseActivity<UpdatePassWordContract.View, UpdatePassWordPresenter> implements UpdatePassWordContract.View {

    @Bind(R.id.edPass)
    EditTextClearAble edPass;
    @Bind(R.id.edNewPass)
    EditTextClearAble edNewPass;
    @Bind(R.id.edNewPassAgain)
    EditTextClearAble edNewPassAgain;
    @Bind(R.id.btnSubmit)
    ButtonTouch btnSubmit;
    @Bind(R.id.tvByVarifyCode)
    TextView tvByVarifyCode;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_update_pass_word, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
    }

    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void updateSuccess() {
        showTip("密码修改成功");
        finish();
    }

    @Override
    public void updateFail(String msg) {
        showTip(msg);
    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @OnClick({R.id.btnSubmit, R.id.tvByVarifyCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                mPresenter.updatePassWord(edPass.getText().toString(), edNewPass.getText().toString(), edNewPassAgain.getText().toString());
                break;
            case R.id.tvByVarifyCode:
                startActivityForResult(new Intent(activity, ForgetPassWordActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
