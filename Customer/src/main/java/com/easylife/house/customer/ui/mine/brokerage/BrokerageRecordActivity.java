package com.easylife.house.customer.ui.mine.brokerage;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.BrokerageRecordAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.BrokerageRecordBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.view.SpaceItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.Bind;

public class BrokerageRecordActivity extends BaseActivity {

    @Bind(R.id.rcvBrokerageRecord)
    RecyclerView rcvBrokerageRecord;
    @Bind(R.id.relContent)
    RelativeLayout relContent;

    private BrokerageRecordAdapter brokerageRecordAdapter;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_brokerage_record, null);
    }

    @Override
    protected void initView() {
        brokerageRecordAdapter = new BrokerageRecordAdapter(R.layout.brokerage_record_item, null);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_pub, relContent, false);
        ((TextView) emptyView.findViewById(R.id.tvEmpty)).setText("暂无结佣记录");
        brokerageRecordAdapter.setEmptyView(emptyView);
        rcvBrokerageRecord.setLayoutManager(new LinearLayoutManager(this));
        rcvBrokerageRecord.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(20)));
        rcvBrokerageRecord.setAdapter(brokerageRecordAdapter);
        brokerageRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BrokerageRecordBean brokerageRecordBean = (BrokerageRecordBean) adapter.getData().get(position);
                BrokerageDetailActivity.startActivity(BrokerageRecordActivity.this, brokerageRecordBean.getId(), brokerageRecordBean.getAuditStatus(), 0);
            }
        });


        brokeAmountRecord();
    }

    private void brokeAmountRecord() {
        showLoading();
        mDao.brokeAmountRecord(0, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                cancleLoading();
                List<BrokerageRecordBean> listData = new Gson().fromJson(response, new TypeToken<List<BrokerageRecordBean>>() {
                }.getType());

                brokerageRecordAdapter.setNewData(listData);

            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                cancleLoading();
            }
        });
    }


    @Override
    protected void setActionBarDetail() {

    }

}
