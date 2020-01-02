package com.easylife.house.customer.ui.fitment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.RecommendCaseListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BeanDesignCase;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的装修方案
 */
public class MyFitmentPlanActivity extends BaseActivity {

    @Bind(R.id.recyclerRecommend)
    RecyclerView recycler;
    @Bind(R.id.tvMyCaseTag)
    TextView tvMyCaseTag;
    @Bind(R.id.imgMyCaseImage)
    ImageView imgMyCaseImage;
    @Bind(R.id.layFitmentCaseItem)
    LinearLayout layFitmentCaseItem;
    @Bind(R.id.imgCaseImage)
    ImageView imgCaseImage;
    @Bind(R.id.tvCaseTitle)
    TextView tvCaseTitle;
    @Bind(R.id.tvCaseTag)
    TextView tvCaseTag;
    @Bind(R.id.tvCaseContent)
    TextView tvCaseContent;
    @Bind(R.id.layContent)
    LinearLayout layContent;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MyFitmentPlanActivity.class));
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.fitment_activity_my_plan, null);
    }

    private RecommendCaseListAdapter adapter;
    private String type, area, style;

    @Override
    protected void initView() {
        adapter = new RecommendCaseListAdapter();

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        adapter.addData(new BeanDesignCase());
        adapter.addData(new BeanDesignCase());
        adapter.addData(new BeanDesignCase());
    }

    @Override
    protected void setActionBarDetail() {

    }


    @OnClick({R.id.tvMyCaseTag, R.id.layFitmentCaseItem})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvMyCaseTag:
                //  选择筛选条件
                CaseFilterActivity.startActivity(activity, type, area, style, 1);
                break;
            case R.id.layFitmentCaseItem:
                // 事件待定
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK)
        switch (requestCode) {
            case 1:
                // 选择筛选条件返回
                type = data.getStringExtra("type");
                area = data.getStringExtra("area");
                style = data.getStringExtra("style");

                tvCaseTag.setText(type + "/" + area + "/" + style);
                break;
        }
    }
}
