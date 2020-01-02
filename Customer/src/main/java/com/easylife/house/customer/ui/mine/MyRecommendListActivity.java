package com.easylife.house.customer.ui.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.ItemClickListener;
import com.easylife.house.customer.adapter.RecommendListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.RecommendBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.IntentUtils;
import com.easylife.house.customer.util.ToastUtils;
import com.easylife.house.customer.view.ButtonTouch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * 我的推荐列表
 */
@RuntimePermissions
public class MyRecommendListActivity extends BaseActivity implements RequestManagerImpl {


    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.recycle)
    RecyclerView recycle;
    @Bind(R.id.btnReport)
    ButtonTouch btnReport;

    public static void startActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, MyRecommendListActivity.class), requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_recommend_list, null);
    }

    private int page = Constants.PAGE_START;
    private RecommendListAdapter adapter;

    @Override
    protected void initView() {

        adapter = new RecommendListAdapter(activity, mDao, R.layout.item_recommend, null, new ItemClickListener<String>() {
            @Override
            public void itemClick(int viewId, int actionType, String data) {
                MyRecommendListActivityPermissionsDispatcher.callWithCheck(MyRecommendListActivity.this, data);
            }
        });
        View emptyView = getLayoutInflater().inflate(R.layout.empty_mine_recommend_list, null);
        emptyView.findViewById(R.id.btnRecommend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDao.isLogin()) {
                    WebViewActivity.startActivity(activity, "推荐有礼",
                            Constants.getCTOBUrl(mDao.getCustomer().username, mDao.getCustomer().phone));
                } else {
                    WebViewActivity.startActivity(activity, "推荐有礼",
                            Constants.getCTOBUrl(null, null));
                }
            }
        });
        adapter.setEmptyView(emptyView);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNetData();
            }
        }, recycle);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = Constants.PAGE_START;
                getNetData();
            }
        });

        getNetData();
    }


    @Override
    protected void setActionBarDetail() {
    }

    private void getNetData() {
        mDao.getRecommendList(1, page, this);
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
        MyRecommendListActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onSuccess(String response, int requestType) {
        refresh.setRefreshing(false);
        adapter.loadMoreComplete();
        switch (requestType) {
            case 1:
                List<RecommendBean> data = new Gson().fromJson(response, new TypeToken<List<RecommendBean>>() {
                }.getType());
                if (page == 1) {
                    adapter.setNewData(data);
                } else {
                    adapter.addData(data);
                }
                if (data == null || data.size() < Constants.PAGE_SIZE) {
                    adapter.setEnableLoadMore(false);
                } else {
                    adapter.setEnableLoadMore(true);
                    page++;
                }
                if (adapter.getData().size() != 0) {
                    btnReport.setVisibility(View.VISIBLE);
                } else {
                    btnReport.setVisibility(View.GONE);
                }
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
        adapter.loadMoreComplete();
        switch (requestType) {
            case 1:
                if (adapter.getItemCount() != 0) {
                    btnReport.setVisibility(View.VISIBLE);
                } else {
                    btnReport.setVisibility(View.GONE);
                }
                break;
            case 2:
                ToastUtils.showShort(activity, code.msg);
                break;
        }
    }

    @OnClick(R.id.btnReport)
    public void onViewClicked() {
        //    再报备
        if (mDao.isLogin()) {
            WebViewActivity.startActivity(this, "推荐有礼",
                    Constants.getCTOBUrl(mDao.getCustomer().username, mDao.getCustomer().phone));
        } else {
            WebViewActivity.startActivity(this, "推荐有礼",
                    Constants.getCTOBUrl(null, null));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getNetData();
        }
    }

}
