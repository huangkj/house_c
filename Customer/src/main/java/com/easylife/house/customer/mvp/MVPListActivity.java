package com.easylife.house.customer.mvp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.dao.ServerDao;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by Mars on 2017/4/13 17:42.
 * 描述：
 */

public abstract class MVPListActivity<T> extends MVPBaseActivity implements RequestManagerImpl {
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    protected int page = Constants.PAGE_START;
    protected ServerDao mDao;

    private BaseQuickAdapter<T, BaseViewHolder> adapter;

    @Override
    protected View setContentLayoutView() {
        return null;
    }

    @Override
    protected void initView() {
        mDao = new ServerDao(activity);
        adapter = new BaseQuickAdapter<T, BaseViewHolder>(getItemLayout(), null) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, T t) {
                initItemData(baseViewHolder, t);
            }
        };
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = Constants.PAGE_START;
                getNetDate();
            }
        });
    }

    protected abstract int getItemLayout();

    protected abstract void initItemData(BaseViewHolder baseViewHolder, T t);

    @Override
    public void onSuccess(String response, int requestType) {
        cancelLoading();
        List<T> data = new Gson().fromJson(response, new TypeToken<List<T>>() {
        }.getType());
        if (page == Constants.PAGE_START) {
            swipeLayout.setRefreshing(false);
            adapter.setNewData(data);
        } else {
            adapter.loadMoreComplete();
            adapter.addData(data);
        }
        if (data != null && data.size() == Constants.PAGE_SIZE) {
            page++;
        }
    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {
        cancelLoading();
    }

    @Override
    protected void setActionBarDetail() {
    }

    protected abstract void getNetDate();

    @Override
    public void showTip(String msg) {
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(activity.getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(activity.getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }
}
