package com.easylife.house.customer.ui.homefragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.MessageListAdapter;
import com.easylife.house.customer.base.BaseFragment;
import com.easylife.house.customer.bean.PushMsgBean;
import com.easylife.house.customer.bean.ResultMessage;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.message.MessageListActivity;
import com.easylife.house.customer.ui.message.messagehouses.MessageHousesActivity;
import com.easylife.house.customer.ui.message.topmessage.TopMessageActivity;
import com.easylife.house.customer.ui.pub.loginbyverifycode.LoginByVerifyCodeActivity;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;


/**
 * 新消息tab
 */
public class MessageFragment extends BaseFragment {

    @Bind(R.id.rcvMessageList)
    RecyclerView rcvMessageList;
    @Bind(R.id.swpie)
    SwipeRefreshLayout swpie;
    private MessageListAdapter messageListAdapter;

    public static MessageFragment newInstance() {
        MessageFragment instance = new MessageFragment();
        return instance;
    }


    private void fakeData() {
        mDao.getMessageOutList(0, new RequestManagerImpl() {

            @Override
            public void onSuccess(String response, int requestType) {
                if (swpie != null)
                    swpie.setRefreshing(false);
                ResultMessage messageOut = new Gson().fromJson(response, ResultMessage.class);
                if (messageOut == null || messageOut.pushMessageOuterList == null)
                    return;
                messageListAdapter.setNewData(messageOut.pushMessageOuterList);
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                swpie.setRefreshing(false);
            }
        });
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void initViews() {
        EventBus.getDefault().register(this);

        messageListAdapter = new MessageListAdapter();
        rcvMessageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvMessageList.setAdapter(messageListAdapter);
        fakeData();

        messageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View vie, int position) {
                if (!mDao.isLogin()) {
                    getActivity().startActivityForResult(new Intent(getActivity(), LoginByVerifyCodeActivity.class), 100);
                    return;
                }
                PushMsgBean newsBean = messageListAdapter.getItem(position);
                Intent resultIntent = new Intent();
                switch (newsBean.msgType) {
                    case 0:
                        // 头条资讯
                        resultIntent.setClass(getActivity(), TopMessageActivity.class);
                        startActivity(resultIntent);
                        break;
                    case 1:
                        // 楼盘订阅
                        resultIntent.setClass(getActivity(), MessageHousesActivity.class);
                        startActivity(resultIntent);
                        break;
                    case 7:
                        //跟进消息
                        resultIntent.setClass(getActivity(), MessageListActivity.class);
                        resultIntent.putExtra("msgType", newsBean.msgType);
                        startActivity(resultIntent);
                        break;
                }
            }
        });
        swpie.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fakeData();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {
        if (event.MsgType == MessageEvent.PUSH_MSG || event.MsgType == MessageEvent.LOGIN_STATE_CHANGE || event.MsgType == MessageEvent.PUSH_MSG_REFRESH) {
            //收到推送，刷新列表
            fakeData();
        }
    }
}
