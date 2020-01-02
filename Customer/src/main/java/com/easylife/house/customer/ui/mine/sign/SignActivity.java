package com.easylife.house.customer.ui.mine.sign;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.SignAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.SignListBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.integral.IntegralActivity;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.GsonUtils;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * - @Description:  签到
 * - @Author:  hkj
 * - @Time:  2018/9/17 16:45
 */
public class SignActivity extends BaseActivity {


    @Bind(R.id.tv_sign_days)
    TextView tvSignDays;
    @Bind(R.id.iv_left_line)
    View ivLeftLine;
    @Bind(R.id.iv_left_line2)
    View ivLeftLine2;
    @Bind(R.id.iv_no_sign)
    ImageView ivNoSign;
    @Bind(R.id.iv_right_line)
    ImageView ivRightLine;
    @Bind(R.id.iv_right_line2)
    View ivRightLine2;
    @Bind(R.id.iv_sign)
    ImageView ivSign;
    @Bind(R.id.tv_sign_integrail)
    TextView tvSignIntegrail;
    @Bind(R.id.tv_sign_days_continue)
    TextView tvSignDaysContinue;
    @Bind(R.id.tv_date_one)
    TextView tvDateOne;
    @Bind(R.id.tv_date_two)
    TextView tvDateTwo;
    @Bind(R.id.ll_top)
    RelativeLayout llTop;
    @Bind(R.id.tv_prize)
    TextView tvPrize;
    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.progress)
    ProgressBar progress;

    private SignAdapter mAdapter;


    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_sign, null);
    }

    @Override
    protected void initView() {
        layActionBar.setBackground(ContextCompat.getDrawable(this, R.drawable.react_gradient_bg));
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        btnRightText.setTextColor(ContextCompat.getColor(this, R.color.white));
        btnRightText.setVisibility(View.VISIBLE);
        btnRightText.setText("签到规则");
        imgBack.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(SignActivity.this, R.color.white)));
        mDao.sign(0, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                signList();
                MaterialDialog.Builder builder = new MaterialDialog.Builder(SignActivity.this);
                builder.negativeText("查看").positiveText("知道了").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(SignActivity.this, IntegralActivity.class));
                    }
                }).title("提示").content(response).show();


                tvDateOne.setText(TimeUtils.millis2String((System.currentTimeMillis() - 86400000), new SimpleDateFormat("yyyy-MM-dd")));
                tvDateTwo.setText(TimeUtils.millis2String((System.currentTimeMillis()), new SimpleDateFormat("yyyy-MM-dd")));

            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                ToastUtils.showShort(code.msg);
                if (code.code.equals("201")) {
                    finish();
                } else {
                    signList();
                }

            }
        });
        mAdapter = new SignAdapter(R.layout.sign_item, null);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(mAdapter);
        signList();


    }

    private void signFail() {
        tvDateOne.setText(TimeUtils.millis2String((System.currentTimeMillis() - 86400000), new SimpleDateFormat("yyyy-MM-dd")));
        tvDateTwo.setText(TimeUtils.millis2String((System.currentTimeMillis()), new SimpleDateFormat("yyyy-MM-dd")));
        ivNoSign.setImageResource(R.mipmap.no_sign);
        ivSign.setImageResource(R.mipmap.no_sign);
        ivRightLine.setVisibility(View.INVISIBLE);
        ivRightLine2.setVisibility(View.VISIBLE);
        ivLeftLine.setVisibility(View.VISIBLE);
        ivLeftLine2.setVisibility(View.INVISIBLE);
    }

    private void signList() {
        mDao.signList(0, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                SignListBean signListBean = GsonUtils.fromJson(response, SignListBean.class);

                if (signListBean != null) {
                    if (signListBean.times >= 2) {
                        ivLeftLine.setVisibility(View.INVISIBLE);
                        ivLeftLine2.setVisibility(View.VISIBLE);
                        ivNoSign.setImageResource(R.mipmap.sign_ok);
                        ivSign.setImageResource(R.mipmap.sign_ok);
                        ivRightLine.setVisibility(View.VISIBLE);
                        ivRightLine2.setVisibility(View.INVISIBLE);
                        ivLeftLine.setVisibility(View.INVISIBLE);
                        ivLeftLine2.setVisibility(View.VISIBLE);
                    } else {
                        ivLeftLine.setVisibility(View.VISIBLE);
                        ivLeftLine2.setVisibility(View.INVISIBLE);
                        ivSign.setImageResource(R.mipmap.sign_ok);
                        ivNoSign.setImageResource(R.mipmap.no_sign);
                        ivRightLine.setVisibility(View.VISIBLE);
                        ivRightLine2.setVisibility(View.INVISIBLE);
                        ivLeftLine.setVisibility(View.VISIBLE);
                        ivLeftLine2.setVisibility(View.INVISIBLE);
                    }

                    tvSignDays.setText("连续签到" + signListBean.times + "天");
//                    tvSignIntegrail.setText("签到总积分：" + signListBean.point + "积分");

                    if (signListBean.point_details != null) {
                        mAdapter.setNewData(signListBean.point_details);
                    }

                    tvDateOne.setText(TimeUtils.millis2String((System.currentTimeMillis() - 86400000), new SimpleDateFormat("yyyy-MM-dd")));
                    tvDateTwo.setText(TimeUtils.millis2String((System.currentTimeMillis()), new SimpleDateFormat("yyyy-MM-dd")));
                }


            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                ToastUtils.showShort(code.msg);
                signFail();
            }
        });
    }


    @OnClick({R.id.btnRightText})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRightText:
                WebViewActivity.startActivity(this, "签到规则", Constants.SIGN_RULE_URL);
                break;
        }
    }


    @Override
    protected void setActionBarDetail() {

    }
}
