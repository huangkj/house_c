package com.easylife.house.customer.ui.c2b;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.Record;

import butterknife.Bind;

/**
 * Created by Mars on 2017/10/18 15:42.
 * 描述：客户详情
 */

public class RecordDetailActivity extends BaseActivity {

    @Bind(R.id.imgSex)
    ImageView imgSex;
    @Bind(R.id.tvCustomerName)
    TextView tvCustomerName;
    @Bind(R.id.tvDate)
    TextView tvDate;
    @Bind(R.id.tvPhone)
    TextView tvPhone;
    @Bind(R.id.tvDevName)
    TextView tvDevName;
    @Bind(R.id.tvRate)
    TextView tvRate;
    @Bind(R.id.tvDateArrived)
    TextView tvDateArrived;
    @Bind(R.id.layContent)
    LinearLayout layContent;
    @Bind(R.id.imgPoint11)
    ImageView imgPoint11;
    @Bind(R.id.imgLine11)
    View imgLine11;
    @Bind(R.id.tvRecommend)
    TextView tvRecommend;
    @Bind(R.id.tvRecommendDate)
    TextView tvRecommendDate;
    @Bind(R.id.imgPoint21)
    ImageView imgPoint21;
    @Bind(R.id.imgLine21)
    View imgLine21;
    @Bind(R.id.tvArrived)
    TextView tvArrived;
    @Bind(R.id.tvArrivedDate)
    TextView tvArrivedDate;
    @Bind(R.id.tvArrivedAddress)
    TextView tvArrivedAddress;
    @Bind(R.id.imgPoint31)
    ImageView imgPoint31;
    @Bind(R.id.imgLine31)
    View imgLine31;
    @Bind(R.id.tvPaid)
    TextView tvPaid;
    @Bind(R.id.tvPaidNo)
    TextView tvPaidNo;
    @Bind(R.id.tvPaidDate)
    TextView tvPaidDate;
    @Bind(R.id.imgPoint41)
    ImageView imgPoint41;
    @Bind(R.id.tvSigned)
    TextView tvSigned;
    @Bind(R.id.tvSignedDate)
    TextView tvSignedDate;

    public static void startActivity(Activity activity, String id, int requestCode) {
        Intent intent = new Intent(activity, RecordDetailActivity.class);
        intent.putExtra("id", id);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_record_detail, null);
    }

    private Record record;
    private String id;

    @Override
    protected void initView() {

        id = getIntent().getStringExtra("id");

        getNetData();
    }

    private void getNetData() {
        // TODO  接口请求
        initRecord();
    }

    private void initRecord() {
        if (record == null)
            return;
        if (!TextUtils.isEmpty(record.recommendDate)) {
            tvRecommendDate.setText(record.recommendDate);
            imgPoint11.setImageResource(R.drawable.circle_end);
            imgLine11.setBackgroundColor(getResources().getColor(R.color.gradient_end));
        }
        if (!TextUtils.isEmpty(record.arrivedData)) {
            tvArrivedDate.setText(record.arrivedData);
            tvArrivedAddress.setText(record.arrivedLocation);
            imgPoint21.setImageResource(R.drawable.circle_end);
            imgLine21.setBackgroundColor(getResources().getColor(R.color.gradient_end));
        }
        if (!TextUtils.isEmpty(record.paidDate)) {
            tvPaidDate.setText(record.paidDate);
            tvPaidNo.setText(record.paidHouseNo);
            imgPoint31.setImageResource(R.drawable.circle_end);
            imgLine31.setBackgroundColor(getResources().getColor(R.color.gradient_end));
        }
        if (!TextUtils.isEmpty(record.signedDate)) {
            tvSignedDate.setText(record.signedDate);
            imgPoint41.setImageResource(R.drawable.circle_end);
        }
    }

    @Override
    protected void setActionBarDetail() {

    }

}
