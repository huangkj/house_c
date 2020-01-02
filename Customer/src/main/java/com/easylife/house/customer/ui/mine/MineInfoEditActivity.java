package com.easylife.house.customer.ui.mine;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.ResultEditUserInfo;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.ValidatorUtils;
import com.google.gson.Gson;

import butterknife.Bind;

/**
 * 编辑修改用户单一信息页面
 */
public class MineInfoEditActivity extends BaseActivity implements RequestManagerImpl {

    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.edit)
    EditText edit;

    public static void startActivity(Activity activity, int type, String defaultText, int requestCode) {
        activity.startActivityForResult(new Intent(activity, MineInfoEditActivity.class)
                        .putExtra("type", type)
                        .putExtra("defaultText", defaultText)
                , requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.user_activity_info_edit, null);
    }

    private int type;
    private String defaultText;

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type", 0);
        defaultText = getIntent().getStringExtra("defaultText");

        InputFilter[] filters12 = {new InputFilter.LengthFilter(12)};
        InputFilter[] filters16 = {new InputFilter.LengthFilter(16)};
        switch (type) {
            case 1:
                tvTitle.setText("邮箱");
                tvDesc.setText("邮箱");
                edit.setHint("请输入邮箱");
                break;
            case 2:
                tvTitle.setText("姓名");
                tvDesc.setText("姓名");
                edit.setHint("请输入姓名");
                edit.setFilters(filters12);
                break;
            default:
                tvTitle.setText("我的用户名");
                tvDesc.setText("我的用户名");
                edit.setHint("请输入用户名");
                edit.setFilters(filters16);
                break;
        }

    }

    @Override
    protected void setActionBarDetail() {
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setText("保存");
        btnRightText.setTextColor(getResources().getColor(R.color.gradient_end));
        btnRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   保存
                defaultText = edit.getText().toString();
                switch (type) {
                    case 1:
                        if (TextUtils.isEmpty(defaultText)) {
                            ToastUtils.showShort("请输入邮箱");
                            return;
                        }

                        if (!ValidatorUtils.isEmail(defaultText)) {
                            ToastUtils.showShort("请输入正确格式");
                            return;
                        }
                        mDao.editUserEmail(1, defaultText, MineInfoEditActivity.this);
                        break;
                    case 2:
                        if (TextUtils.isEmpty(defaultText)) {
                            ToastUtils.showShort("请输入姓名");
                            return;
                        }
                        // 12个字符限制，不可有特殊符号，必须是汉字或字母组合，若输入其它格式则
                        //提示“请输入正确格式”
                        if (!ValidatorUtils.isUsername(defaultText)) {
                            ToastUtils.showShort("请输入正确格式");
                            return;
                        }
                        mDao.editUserName(1, defaultText, MineInfoEditActivity.this);
                        break;
                    default:
                        if (TextUtils.isEmpty(defaultText)) {
                            ToastUtils.showShort("请输入用户名");
                            return;
                        }
                        //16个字符限制，不可出现特殊符号，若出现特殊符号则
                        //提示“存在特殊符号，请重新输入”
                        if (!ValidatorUtils.isSpecial(defaultText)) {
                            ToastUtils.showShort("存在特殊符号，请重新输入");
                            return;
                        }
                        mDao.editUserNick(1, defaultText, MineInfoEditActivity.this);
                        break;
                }
            }
        });
    }

    @Override
    public void onSuccess(String response, int requestType) {
        ResultEditUserInfo resultEditUserInfo = new Gson().fromJson(response, ResultEditUserInfo.class);

        if (!TextUtils.isEmpty(resultEditUserInfo.msg) && resultEditUserInfo.msg.contains("恭喜你")) {
            new AlertDialog.Builder(activity)
                    .setMessage(resultEditUserInfo.msg)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("立即前往", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WebViewActivity.startActivity(MineInfoEditActivity.this, "游戏活动", Constants.ACTIVITY_GAME_URL + mDao.localDao.getCustomer().id);
                        }
                    }).show();
        } else {
            com.easylife.house.customer.util.ToastUtils.showShort(activity, TextUtils.isEmpty(resultEditUserInfo.msg) ? "修改成功" : resultEditUserInfo.msg);
        }

        setResult(RESULT_OK, new Intent()
                .putExtra("type", type)
                .putExtra("text", defaultText));
        finish();
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }
}
