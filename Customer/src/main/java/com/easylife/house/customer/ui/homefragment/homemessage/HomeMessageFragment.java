package com.easylife.house.customer.ui.homefragment.homemessage;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.MsgDevSub;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.event.MessageEvent;
import com.easylife.house.customer.mvp.MVPBaseFragment;
import com.easylife.house.customer.ui.message.messagehouses.MessageHousesActivity;
import com.easylife.house.customer.ui.message.topmessage.TopMessageActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.LogOut;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.easylife.house.customer.event.MessageEvent.UPDATE_MESSAGE_HEADLINE;
import static com.easylife.house.customer.event.MessageEvent.UPDATE_MESSAGE_HOUSE;

/**
 * 未使用
 */
public class HomeMessageFragment extends MVPBaseFragment<HomeMessageContract.View, HomeMessagePresenter> implements HomeMessageContract.View {
    @Bind(R.id.imgDevSub)
    ImageView imgDevSub;
    @Bind(R.id.tvMessageHouse)
    TextView tvMessageHouse;
    @Bind(R.id.layDevSub)
    LinearLayout layDevSub;
    @Bind(R.id.imgHeadLine)
    ImageView imgHeadLine;
    @Bind(R.id.imgPointTipDev)
    ImageView imgPointTipDev;
    @Bind(R.id.imgPointTip)
    ImageView imgPointTip;
    @Bind(R.id.tvMessageInfo)
    TextView tvMessageInfo;
    @Bind(R.id.layHeadLine)
    LinearLayout layHeadLine;

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(getActivity(), msg);
    }

    @Override
    public int getLayout() {
        return R.layout.home_fragment_message;
    }

    @Override
    public void initViews() {
        if (mDao.isLogin()) {
            mPresenter.getDataDevSub();
        } else {
            layDevSub.setVisibility(View.GONE);
        }
        mPresenter.getDataHeadLine();
        showTip();
    }

    public static HomeMessageFragment newInstance() {
        Bundle args = new Bundle();
        HomeMessageFragment fragment = new HomeMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.layDevSub, R.id.layHeadLine})
    public void onClick(View view) {
        switch (view.getId()) {
            //楼盘订阅
            case R.id.layDevSub:
                imgPointTipDev.setVisibility(View.GONE);
                mDao.localDao.savePushTipStatusHouse(true);
                startActivity(new Intent(getActivity(), MessageHousesActivity.class));
                break;
            case R.id.layHeadLine:
                imgPointTip.setVisibility(View.GONE);
                mDao.localDao.savePushTipStatusHeadline(true);
                startActivity(new Intent(getActivity(), TopMessageActivity.class));
                break;
        }
    }

    @Override
    public void initDataHeadLine(List<MsgHeadLine> data) {
        if (data == null || data.size() == 0)
            return;
        tvMessageInfo.setText(data.get(0).title);
        showTipHeadline();
    }

    @Override
    public void initDataDevSub(List<MsgDevSub> data) {
        if (data == null || data.size() == 0) {
            layDevSub.setVisibility(View.GONE);
            return;
        }
        layDevSub.setVisibility(View.VISIBLE);
        tvMessageHouse.setText(data.get(0).title);
        showTipHouse();
    }

    @Override
    public void showTip() {
        showTipHouse();
        showTipHeadline();
    }

    public void showTipHouse() {
        if (mDao.isLogin()) {
            if (mDao.localDao.showPushTipHouse()) {
                imgPointTipDev.setVisibility(View.VISIBLE);
            } else {
                imgPointTipDev.setVisibility(View.GONE);
            }
        }
    }

    public void showTipHeadline() {
        if (mDao.localDao.showPushTipHeadline()) {
            imgPointTip.setVisibility(View.VISIBLE);
        } else {
            imgPointTip.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN)//不管在哪个线程发布的事件，订阅者都在UI线程
    public void onMessageEventMainThread(MessageEvent event) {//接受登录页面发送的事件，如果没有数据加载数据
        LogOut.d(" event.MsgType : ", event.MsgType);
        if (event.MsgType == UPDATE_MESSAGE_HOUSE) {
            // 楼盘订阅
            mDao.localDao.savePushTipStatusHouse(false);
            mPresenter.getDataDevSub();
        } else if (event.MsgType == UPDATE_MESSAGE_HEADLINE) {
            // 头条资讯
            mDao.localDao.savePushTipStatusHeadline(false);
            mPresenter.getDataHeadLine();
        }
    }
}
