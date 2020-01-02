package com.easylife.house.customer.ui.message;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.PushMessageListAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.PushMsgBean;
import com.easylife.house.customer.bean.ResultMessage;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.mine.RecommendDetailActivity;
import com.easylife.house.customer.ui.mine.brokerage.BrokerageDetailActivity;
import com.easylife.house.customer.ui.mine.order.orderdetail.OrderDetailActivity;
import com.google.gson.Gson;

import butterknife.Bind;

/**
 * 描述：跟进消息列表
 */

public class MessageListActivity extends BaseActivity {

    @Bind(R.id.rcv_message)
    RecyclerView rcvMessage;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private PushMessageListAdapter pushMessageListAdapter;

    public static void startActivity(Activity activity, int msgType, int requestCode) {
        Intent it = new Intent(activity, MessageListActivity.class);
        it.putExtra("msgType", msgType);
        activity.startActivityForResult(it, requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_push_message_list, null);
    }

    @Override
    protected void initView() {
        msgType = getIntent().getIntExtra("msgType", 0);

        pushMessageListAdapter = new PushMessageListAdapter();
        rcvMessage.setLayoutManager(new LinearLayoutManager(this));
        rcvMessage.setAdapter(pushMessageListAdapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = Constants.PAGE_START;
                fakeData();
            }
        });
        pushMessageListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PushMsgBean message = pushMessageListAdapter.getItem(position);
                if (message.msgType == 7) {
                    switch (message.subType) {
                        case 1:
                            // 退款
                            OrderDetailActivity.startActivity(activity, message.detailId + "", 0);
                            break;
                        case 9://结佣详情
                            BrokerageDetailActivity.startActivity(activity, message.detailId, 0, 0);
                            break;
                        default:
                            RecommendDetailActivity.startActivity(activity, message.detailId + "", 0);
                            break;
                    }
                }
            }
        });
        pushMessageListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                fakeData();
            }
        }, rcvMessage);
        fakeData();
    }

    private int pageNum = Constants.PAGE_START;
    /**
     * 消息类型，7为跟进推送类型
     */
    private int msgType = 0;


    private void fakeData() {
        mDao.getMessageList(0, msgType, pageNum, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
                ResultMessage messageOut = new Gson().fromJson(response, ResultMessage.class);
                if (messageOut == null || messageOut.pushMessageList == null)
                    return;
                if (pageNum == Constants.PAGE_START) {
                    pushMessageListAdapter.setNewData(messageOut.pushMessageList);
                } else {
                    pushMessageListAdapter.addData(messageOut.pushMessageList);
                }
                if (messageOut.pushMessageList.size() == 20) {
                    pageNum++;
                    pushMessageListAdapter.setEnableLoadMore(true);
                } else {
                    pushMessageListAdapter.setEnableLoadMore(false);
                }
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                if (swipeRefresh != null)
                    swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void setActionBarDetail() {

    }
}
