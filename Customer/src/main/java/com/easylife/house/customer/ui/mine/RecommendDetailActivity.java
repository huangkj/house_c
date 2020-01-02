package com.easylife.house.customer.ui.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.RecommendRateListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.RecommendDetail;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.IntentUtils;
import com.easylife.house.customer.util.ToastUtils;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 推荐详情
 */
@RuntimePermissions
public class RecommendDetailActivity extends BaseActivity implements RequestManagerImpl {


    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvCall)
    TextView tvCall;
    @Bind(R.id.tvDevName)
    TextView tvDevName;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    public static void startActivity(Activity activity, String id, int requestCode) {
        activity.startActivityForResult(new Intent(activity, RecommendDetailActivity.class)
                .putExtra("id", id), requestCode);
    }

    public static void startActivity(Context context, String id) {
        context.startActivity(new Intent(context, RecommendDetailActivity.class)
                .putExtra("id", id));
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_recommend_detail, null);
    }


    private RecommendRateListAdapter adapter;
    private String brokerCustomerId;
    private String assistant;

    @Override
    protected void initView() {
        brokerCustomerId = getIntent().getStringExtra("id");

        adapter = new RecommendRateListAdapter(R.layout.item_recommend_rate, null);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_pub, null);
        ((TextView) emptyView.findViewById(R.id.tvEmpty)).setText("暂无数据");
        adapter.setEmptyView(emptyView);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNetData();
            }
        });

        getNetData();
    }


    @Override
    protected void setActionBarDetail() {
    }

    private void getNetData() {
        mDao.getRecommendDetail(1, brokerCustomerId, this);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        refresh.setRefreshing(false);
        switch (requestType) {
            case 1:
                RecommendDetail detail = new Gson().fromJson(response, RecommendDetail.class);

                if (detail == null)
                    return;
                tvName.setText(detail.nameAndPhone);
                tvDevName.setText(detail.devName);
                assistant = detail.assistant;
                tvCall.setVisibility(TextUtils.isEmpty(detail.assistant) ? View.GONE : View.VISIBLE);

                adapter.setNewData(detail.customerFollow);
                break;
            case 2:
                ToastUtils.showShort(activity, "成功");
                getNetData();
                break;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        refresh.setRefreshing(false);
        switch (requestType) {
            case 2:
                ToastUtils.showShort(activity, code.msg);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getNetData();
        }
    }

    @NeedsPermission((Manifest.permission.CALL_PHONE))
    public void call(String assistant) {
        IntentUtils.call(assistant, activity);
    }

    /**
     * 6.0权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RecommendDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick({R.id.btnRecommendRate, R.id.tvCall, R.id.btnReport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRecommendRate:
                //   推荐进度  ,暂无点击事件
                break;
            case R.id.tvCall:
                //   拨打项目助理电话
                RecommendDetailActivityPermissionsDispatcher.callWithCheck(RecommendDetailActivity.this, assistant);
                break;
            case R.id.btnReport:
                //    再报备
                if (mDao.isLogin()) {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(mDao.getCustomer().username, mDao.getCustomer().phone));
                } else {
                    WebViewActivity.startActivity(this, "推荐有礼",
                            Constants.getCTOBUrl(null, null));
                }
                break;
        }
    }
}
