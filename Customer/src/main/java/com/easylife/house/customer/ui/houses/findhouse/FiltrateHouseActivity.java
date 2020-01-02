package com.easylife.house.customer.ui.houses.findhouse;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.FiltrateHouseAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.FiltrateHouseBean;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.ui.houses.housedetailv5.HouseDetailActivity;
import com.easylife.house.customer.util.GsonUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class FiltrateHouseActivity extends BaseActivity {


    @Bind(R.id.tvBudget)
    TextView tvBudget;
    @Bind(R.id.tvRoom)
    TextView tvRoom;
    @Bind(R.id.tvCountDesc)
    TextView tvCountDesc;
    @Bind(R.id.rcvList)
    RecyclerView rcvList;
    @Bind(R.id.tvCompareCount)
    TextView tvCompareCount;
    @Bind(R.id.relCompareCount)
    RelativeLayout relCompareCount;
    private double minBudget;
    private double maxBudget;
    private int room;
    private String cityId;
    private String budgetString;
    private String roomString;
    private FiltrateHouseAdapter filtrateHouseAdapter;
    private ArrayList<FiltrateHouseBean.ListBean> compareList = new ArrayList<>();

    public static void startActivity(Activity activity, double minBudget, double maxBudget,
                                     int room, String cityId, String budgetString, String roomString, int requestCode) {
        Intent intent = new Intent(activity, FiltrateHouseActivity.class);
        intent.putExtra("minBudget", minBudget);
        intent.putExtra("maxBudget", maxBudget);
        intent.putExtra("room", room);
        intent.putExtra("cityId", cityId);
        intent.putExtra("budgetString", budgetString);
        intent.putExtra("roomString", roomString);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_filtrate_house, null);
    }

    @Override
    protected void initView() {
        minBudget = getIntent().getDoubleExtra("minBudget", 0);
        maxBudget = getIntent().getDoubleExtra("maxBudget", 0);
        room = getIntent().getIntExtra("room", 0);
        cityId = getIntent().getStringExtra("cityId");
        budgetString = getIntent().getStringExtra("budgetString");
        roomString = getIntent().getStringExtra("roomString");

        tvBudget.setText(budgetString);
        tvRoom.setText(roomString);
        filtrateHouseAdapter = new FiltrateHouseAdapter(null);
        View emptyView = getLayoutInflater().inflate(R.layout.filtrate_house_empty, null, false);
        emptyView.findViewById(R.id.btnBackSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        filtrateHouseAdapter.setEmptyView(emptyView);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(filtrateHouseAdapter);

        filtrateHouseAdapter.setOnAdapterListener(new FiltrateHouseAdapter.OnAdapterListener() {
            @Override
            public void onCompareCountChange(ArrayList<FiltrateHouseBean.ListBean> compareList) {
                FiltrateHouseActivity.this.compareList = compareList;
                tvCompareCount.setText(compareList.size() + "");
            }

            @Override
            public void onItemClick(FiltrateHouseBean.ListBean item) {
                HouseDetailActivity.startActivity(FiltrateHouseActivity.this, item.getDevId(), false, 0);
            }
        });

        mDao.lookHouseList(0, minBudget, maxBudget, room, cityId, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                FiltrateHouseBean filtrateHouseBean = GsonUtils.fromJson(response, FiltrateHouseBean.class);
                filtrateHouseAdapter.setNewData(filtrateHouseBean.getList());
                if (filtrateHouseBean.getList() != null && filtrateHouseBean.getList().size() > 0) {
                    tvCountDesc.setVisibility(View.VISIBLE);
                    tvCountDesc.setText("共" + filtrateHouseBean.getList().size() + "个楼盘符合您的意向购房需求");
                    relCompareCount.setVisibility(View.VISIBLE);
                } else {
                    tvCountDesc.setVisibility(View.GONE);
                    relCompareCount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                ToastUtils.showShort(code.msg);
            }
        });
    }


    @OnClick({R.id.relCompareCount})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.relCompareCount:
                if (compareList.size() < 2) {
                    ToastUtils.showShort("请至少选择2个对比楼盘");
                    return;
                }
                ArrayList<String> compareDevIds = new ArrayList<>();

                for (FiltrateHouseBean.ListBean bean : compareList
                ) {
                    compareDevIds.add(bean.getDevId());
                }
                CompareHouseActivity.startActivity(this, compareDevIds, 0);

                break;
        }
    }


    @Override
    protected void setActionBarDetail() {

    }

}
