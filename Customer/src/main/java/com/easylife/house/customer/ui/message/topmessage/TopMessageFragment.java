package com.easylife.house.customer.ui.message.topmessage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.TopMessageListAdapter;
import com.easylife.house.customer.base.LazyLoadFragment;
import com.easylife.house.customer.bean.MsgHeadLine;
import com.easylife.house.customer.bean.TopMessageBean;
import com.easylife.house.customer.config.Constants;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.homesearch.SearchSingleton;
import com.easylife.house.customer.ui.pub.WebViewActivity;
import com.easylife.house.customer.util.GsonUtils;

import java.util.List;

import butterknife.Bind;

import static com.easylife.house.customer.config.Constants.PAGE_SIZE;
import static com.easylife.house.customer.config.Constants.URL_WEB_BASE;


public class TopMessageFragment extends LazyLoadFragment implements BaseQuickAdapter.RequestLoadMoreListener, RequestManagerImpl, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    @Bind(R.id.rcvMessage)
    RecyclerView rcvMessage;
    @Bind(R.id.swpieMessage)
    SwipeRefreshLayout swpieMessage;


    private int pageNum = 1;

    /**
     * 1房产头条  2房市动态  3新闻资讯  4国家政策
     */
    private String type;
    private String cityId;
    private TopMessageListAdapter topMessageListAdapter;
    private List<TopMessageBean.ListBean> beanList;


    public TopMessageFragment() {
        // Required empty public constructor
    }

    public static TopMessageFragment newInstance(String type) {
        TopMessageFragment fragment = new TopMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    protected void loadData() {
        cityId = SearchSingleton.getIstance().cityId;
        Log.d("@@", "loaddata type:" + type);
        swpieMessage.post(new Runnable() {
            @Override
            public void run() {
                swpieMessage.setRefreshing(true);
                mDao.messageInfoList(0, cityId, type, pageNum, PAGE_SIZE, TopMessageFragment.this);
            }
        });

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_top_message;
    }

    @Override
    public void initViews() {
        topMessageListAdapter = new TopMessageListAdapter();
        topMessageListAdapter.setOnLoadMoreListener(this, rcvMessage);
        topMessageListAdapter.openLoadAnimation();
        rcvMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvMessage.setAdapter(topMessageListAdapter);
        topMessageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TopMessageBean.ListBean item = (TopMessageBean.ListBean) adapter.getItem(position);
                mDao.headMessageAddCount(1, item.getId(), TopMessageFragment.this);
                MsgHeadLine msgHeadLine = new MsgHeadLine();
                msgHeadLine.id = item.getId();
                msgHeadLine.text = item.getText();
                msgHeadLine.title = item.getTitle();
                WebViewActivity.startActivity(getActivity(), "资讯", URL_WEB_BASE + item.getId(), true, msgHeadLine);

            }
        });
        swpieMessage.setOnRefreshListener(this);
        loadData();
    }


    @Override
    public void onLoadMoreRequested() {
        rcvMessage.post(new Runnable() {
            @Override
            public void run() {
                if (beanList != null && beanList.size() >= Constants.PAGE_SIZE) {
                    pageNum++;
                    mDao.messageInfoList(0, cityId, type, pageNum, PAGE_SIZE, TopMessageFragment.this);
                } else {
                    topMessageListAdapter.loadMoreEnd(pageNum == 1);
                }
            }
        });
    }

    @Override
    public void onSuccess(String response, int requestType) {
        switch (requestType) {
            case 0:
                TopMessageBean topMessageBean = GsonUtils.fromJson(response, TopMessageBean.class);
                beanList = topMessageBean.getList();
                if (beanList != null && beanList.size() > 0) {
                    isLoadData = true;
                }
                if (swpieMessage != null && swpieMessage.isRefreshing()) {
                    swpieMessage.setRefreshing(false);
                }
                if (pageNum == Constants.PAGE_START) {
                    topMessageListAdapter.setNewData(beanList);
                    topMessageListAdapter.setEnableLoadMore(true);
                } else {
                    topMessageListAdapter.addData(beanList);
                    topMessageListAdapter.loadMoreComplete();
                }
                break;

            case 1:

                break;
        }


    }

    @Override
    public void onFail(NetBaseStatus code, int requestType) {

    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        mDao.messageInfoList(0, cityId, type, pageNum, PAGE_SIZE, TopMessageFragment.this);
    }
}
