package com.easylife.house.customer.ui.payment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.FilesAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BeanFile;
import com.easylife.house.customer.bean.ResultRefundRate;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.easylife.house.customer.util.LogOut;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Mars on 2017/7/18 16:37.
 * 描述：退款进度
 */

public class RefundRateActivity extends BaseActivity {
    @Bind(R.id.tvMoney)
    TextView tvMoney;
    @Bind(R.id.imgPoint11)
    ImageView imgPoint11;
    @Bind(R.id.imgLine11)
    View imgLine11;
    @Bind(R.id.tvRefundApply)
    TextView tvRefundApply;
    @Bind(R.id.tvRefundApplyTime)
    TextView tvRefundApplyTime;
    @Bind(R.id.imgRefundApply)
    ImageView imgRefundApply;
    @Bind(R.id.tvRefundApplyDesc)
    TextView tvRefundApplyDesc;
    @Bind(R.id.imgPoint12)
    ImageView imgPoint12;
    @Bind(R.id.imgLine12)
    View imgLine12;
    @Bind(R.id.tvRefundCheck)
    TextView tvRefundCheck;
    @Bind(R.id.tvRefundCheckTime)
    TextView tvRefundCheckTime;
    @Bind(R.id.imgRefundCheck)
    ImageView imgRefundCheck;
    @Bind(R.id.tvRefundCheckDesc)
    TextView tvRefundCheckDesc;
    @Bind(R.id.imgPoint13)
    ImageView imgPoint13;
    @Bind(R.id.tvRefundComplete)
    TextView tvRefundComplete;
    @Bind(R.id.tvRefundCompleteTime)
    TextView tvRefundCompleteTime;
    @Bind(R.id.imgRefundComplete)
    ImageView imgRefundComplete;
    @Bind(R.id.tvRefundCompleteDesc)
    TextView tvRefundCompleteDesc;
    @Bind(R.id.layRefund)
    LinearLayout layRefund;
    @Bind(R.id.recyclerViewFile)
    RecyclerView recyclerViewFile;
    @Bind(R.id.tvBankSubName)
    TextView tvBankSubName;
    @Bind(R.id.tvBankCardNum)
    TextView tvBankCardNum;
    @Bind(R.id.tvPayeeName)
    TextView tvPayeeName;
    @Bind(R.id.tvDescRefund)
    TextView tvDescRefund;

    /**
     * 已支付金额
     *
     * @param activity
     * @param orderCode
     * @param requestCode
     */
    public static void startActivity(Activity activity, String orderCode, int requestCode) {
        activity.startActivityForResult(new Intent(activity, RefundRateActivity.class)
                .putExtra("orderCode", orderCode), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.order_activity_refund_rate, null);
    }

    private boolean showApply = true;
    private boolean showApplyCheck = true;
    private boolean showApplyComplete = true;

    private String orderCode;
    private FilesAdapter adapter;


    @Override
    protected void initView() {
        orderCode = getIntent().getStringExtra("orderCode");

        recyclerViewFile.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new FilesAdapter();
        recyclerViewFile.setAdapter(adapter);

        mDao.refundRate(1, orderCode, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                LogOut.d("refundRate:", response);
                ResultRefundRate data = new Gson().fromJson(response, ResultRefundRate.class);
                initData(data);
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
            }
        });
    }

    private void initData(ResultRefundRate data) {
        if (data == null)
            return;

        tvMoney.setText(data.refund + "元");
        tvBankSubName.setText(data.bankAddress);
        tvBankCardNum.setText(data.bankNum);
        tvPayeeName.setText(data.userName);
        tvDescRefund.setText(data.comments);

        List<BeanFile> files = new ArrayList<>();
        if (data.img != null && data.img.size() > 0)
            for (String imgUrl : data.img) {
                BeanFile file = new BeanFile();
                file.url = imgUrl;
                files.add(file);
            }
        files.addAll(data.pdf);

//        BeanFile file = new BeanFile();
//        file.url = "http://kfcdn.lifeat.cn/android6.0到9.0新api适配.pdf";
//        file.name = "android6.0到9.0新api适配.pdf";
//        file.size = "1.517MB";
//        file.type = "1";
//        files.add(file);
        adapter.setNewData(files);

        for (ResultRefundRate.RefundRate rate : data.data) {
            if (ResultRefundRate.RefundStatus.PLACE.name().equals(rate.refundStatus)) {
                // 已提交
                tvRefundApplyTime.setText(rate.createTime);
            } else {
                imgPoint12.setImageResource(R.drawable.circle_end);
                imgRefundCheck.setVisibility(View.VISIBLE);
                tvRefundCheckTime.setText(rate.createTime);
                tvRefundCheckDesc.setVisibility(View.VISIBLE);
                tvRefundCheck.setTextColor(Color.parseColor("#666565"));

                if (ResultRefundRate.RefundStatus.ALL.name().equals(rate.refundStatus)) {
                    // 已退款
                    imgLine12.setBackgroundColor(getResources().getColor(R.color.gradient_end));
                    imgPoint13.setImageResource(R.drawable.circle_end);
                    imgRefundComplete.setVisibility(View.VISIBLE);
                    tvRefundCompleteDesc.setVisibility(View.VISIBLE);
                    tvRefundComplete.setTextColor(Color.parseColor("#666565"));

                    tvRefundCompleteTime.setText(rate.createTime);
                } else if (ResultRefundRate.RefundStatus.WAIT_SIGNATURE.name().equals(rate.refundStatus)) {
                    // 退款失败
                    tvRefundCheckDesc.setText("您的退款审核未通过，请[重新申请]或者联系客服。");
                }
            }
        }
    }

    @Override
    protected void setActionBarDetail() {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.mipmap.icon_refund_rate);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDao.isLogin()) {
//                    startActivity(new Intent(RefundRateActivity.this, ChatActivity.class).putExtra("userId", EaseConstant.IM_CLIENT_ID));
                } else {
                    startActivityForResult(new Intent(RefundRateActivity.this, LoginByVerifyCodeActivity.class), 1);
                }
            }
        });
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    @OnClick({R.id.imgRefundApply, R.id.imgRefundCheck, R.id.imgRefundComplete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgRefundApply:
                if (showApply) {
                    showApply = false;
                    imgRefundApply.setImageResource(R.mipmap.arrow_down);
                    tvRefundApplyDesc.setVisibility(View.GONE);
                } else {
                    showApply = true;
                    imgRefundApply.setImageResource(R.mipmap.arrow_up);
                    tvRefundApplyDesc.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.imgRefundCheck:
                if (showApplyCheck) {
                    showApplyCheck = false;
                    imgRefundCheck.setImageResource(R.mipmap.arrow_down);
                    tvRefundCheckDesc.setVisibility(View.GONE);
                } else {
                    showApplyCheck = true;
                    imgRefundCheck.setImageResource(R.mipmap.arrow_up);
                    tvRefundCheckDesc.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.imgRefundComplete:
                if (showApplyComplete) {
                    showApplyComplete = false;
                    imgRefundComplete.setImageResource(R.mipmap.arrow_down);
                    tvRefundCompleteDesc.setVisibility(View.GONE);
                } else {
                    showApplyComplete = true;
                    imgRefundComplete.setImageResource(R.mipmap.arrow_up);
                    tvRefundCompleteDesc.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

}
