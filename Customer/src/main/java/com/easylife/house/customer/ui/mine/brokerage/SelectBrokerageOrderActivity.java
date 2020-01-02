package com.easylife.house.customer.ui.mine.brokerage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easylife.house.customer.R;
import com.easylife.house.customer.adapter.SelectOrderAdapter;
import com.easylife.house.customer.base.BaseActivity;
import com.easylife.house.customer.bean.DevListBean;
import com.easylife.house.customer.bean.SelectBrokerOrderBean;
import com.easylife.house.customer.bean.SelectBrokerOrderRequest;
import com.easylife.house.customer.http.bean.NetBaseStatus;
import com.easylife.house.customer.http.impl.RequestManagerImpl;
import com.easylife.house.customer.util.GsonUtils;
import com.easylife.house.customer.util.MoneyUtils;
import com.easylife.house.customer.view.SpaceItemDecoration;
import com.easylife.house.customer.view.popwindow.MultiChooseListPopWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectBrokerageOrderActivity extends BaseActivity {

    @Bind(R.id.tvArea)
    TextView tvArea;
    @Bind(R.id.llDefaultSort)
    LinearLayout llDefaultSort;
    @Bind(R.id.tvHousePrice)
    TextView tvHousePrice;
    @Bind(R.id.ivHousePrice)
    ImageView ivHousePrice;
    @Bind(R.id.llHousePrice)
    LinearLayout llHousePrice;
    @Bind(R.id.tvAcreage)
    TextView tvAcreage;
    @Bind(R.id.ivAcreage)
    ImageView ivAcreage;
    @Bind(R.id.llAcreage)
    LinearLayout llAcreage;
    @Bind(R.id.llSortContent)
    LinearLayout llSortContent;
    @Bind(R.id.rcvSelectBrokerage)
    RecyclerView rcvSelectBrokerage;
    @Bind(R.id.cb)
    CheckBox cbSelectAll;
    @Bind(R.id.tvPriceConfirmOrder)
    TextView tvPriceConfirmOrder;
    @Bind(R.id.tvOrderConfirmOrder)
    TextView tvOrderConfirmOrder;
    @Bind(R.id.relConfirm)
    RelativeLayout relConfirm;
    @Bind(R.id.clContent)
    ConstraintLayout clContent;
    private SelectOrderAdapter selectOrderAdapter;
    private String orderBy = ""; //升序 1，降序 2，默认 1
    private boolean isFrist = true;
    private ArrayList<String> devIdList;
    private DevListBean devListBean;
    private MultiChooseListPopWindow multiChooseListPopWindow;
    private SelectBrokerOrderRequest selectBrokerOrderRequest;
    private double totalAmout;
    /**
     * 上个页面传进来的订单
     */
    private ArrayList<SelectBrokerOrderBean> preSelectedList;

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_select_brokerage_order, null);
    }

    public static void startActivity(Activity activity, ArrayList<SelectBrokerOrderBean> selectedList, int requestCode) {
        Intent intent = new Intent(activity, SelectBrokerageOrderActivity.class);
        intent.putExtra("selectedList", selectedList);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void initView() {
        preSelectedList = (ArrayList<SelectBrokerOrderBean>) getIntent().getSerializableExtra("selectedList");
        selectOrderAdapter = new SelectOrderAdapter(R.layout.select_order_item, null);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_pub, clContent, false);
        ((TextView) emptyView.findViewById(R.id.tvEmpty)).setText("暂无结佣订单");
        selectOrderAdapter.setEmptyView(emptyView);

        rcvSelectBrokerage.setLayoutManager(new LinearLayoutManager(this));
        rcvSelectBrokerage.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(16)));
        rcvSelectBrokerage.setAdapter(selectOrderAdapter);
        selectOrderAdapter.setOnCheckedChangedListener(new SelectOrderAdapter.OnCheckedChangedListener() {
            @Override
            public void onCheckedChanged(SelectBrokerOrderBean item) {
                List<SelectBrokerOrderBean> data = selectOrderAdapter.getData();
                updateTotalMoney(data);
                checkSelectAll(data);
                tvOrderConfirmOrder.setText("确定(" + selectOrderAdapter.getSelectSize() + ")");

            }
        });
        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全选
                selectOrderAdapter.selectAll(cbSelectAll.isChecked());
                updateTotalMoney(selectOrderAdapter.getData());
                tvOrderConfirmOrder.setText("确定(" + selectOrderAdapter.getSelectSize() + ")");
            }
        });
        /*cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //全选
                selectOrderAdapter.selectAll(isChecked);
                updateTotalMoney(selectOrderAdapter.getData());
                tvOrderConfirmOrder.setText("确定(" + selectOrderAdapter.getSelectSize() + ")");
            }
        });*/

        requestData();

//        fakeData();
    }

    /**
     * 检查是否全选
     */
    private void checkSelectAll(List<SelectBrokerOrderBean> data) {
        boolean flag = true;
        for (SelectBrokerOrderBean bean :
                data) {
            if (bean.getConfirmState() == 2) {
                if (!bean.isSelect) {
                    flag = false;
                    break;
                }
            }

        }

        boolean isAllUnConfirm = true;

        for (SelectBrokerOrderBean bean ://验证所有订单都是未确认的情况
                data) {
            if (bean.getConfirmState() == 2) {
                isAllUnConfirm = false;
            }

        }

        if (isAllUnConfirm) {
            cbSelectAll.setChecked(false);
        } else {
            cbSelectAll.setChecked(flag);
        }


    }

    private void updateTotalMoney(List<SelectBrokerOrderBean> data) {
        totalAmout = 0;
        if (data != null) {
            for (SelectBrokerOrderBean bean : data) {
                if (bean.isSelect) {
                    String comShouldAmount = bean.getShouldAmount();
                    if (!TextUtils.isEmpty(comShouldAmount)) {
                        BigDecimal shouldAmountB = new BigDecimal(comShouldAmount);
                        BigDecimal totalAmoutB = new BigDecimal(Double.toString(totalAmout));
                        totalAmout = shouldAmountB.add(totalAmoutB).doubleValue();
                    }

                }
            }

        }
        tvPriceConfirmOrder.setText("合计: ￥" + MoneyUtils.moneyFormat3(totalAmout + ""));
    }

    private void requestData() {

        selectBrokerOrderRequest = new SelectBrokerOrderRequest();
        selectBrokerOrderRequest.setUserId(mDao.getCustomer().id);
        selectBrokerOrderRequest.setOrderBy(orderBy);
        devIdList = new ArrayList<>();
        selectBrokerOrderRequest.setDevIdList(devIdList);


        chooseBrokeOrders();


        mDao.devNameList(0, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                devListBean = GsonUtils.fromJson(response, DevListBean.class);
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                com.blankj.utilcode.util.ToastUtils.showShort(code.msg);
            }
        });
    }


    private void chooseBrokeOrders() {
        selectBrokerOrderRequest.setDevIdList(devIdList);
        showLoading();
        mDao.chooseBrokeOrders(0, selectBrokerOrderRequest, new RequestManagerImpl() {
            @Override
            public void onSuccess(String response, int requestType) {
                cancleLoading();
                List<SelectBrokerOrderBean> listData = new Gson().fromJson(response, new TypeToken<List<SelectBrokerOrderBean>>() {
                }.getType());

                if (preSelectedList != null && preSelectedList.size() > 0 && listData != null && listData.size() > 0) {
                    for (SelectBrokerOrderBean bean :
                            preSelectedList) {
                        for (SelectBrokerOrderBean data :
                                listData) {
                            if (bean.getId() == data.getId()) {
                                data.isSelect = true;
                            }
                        }

                    }
                    preSelectedList.clear();
                    preSelectedList = null;
                }


                selectOrderAdapter.setNewData(listData);
                updateTotalMoney(listData);
                checkSelectAll(listData);
                tvOrderConfirmOrder.setText("确定(" + selectOrderAdapter.getSelectSize() + ")");
            }

            @Override
            public void onFail(NetBaseStatus code, int requestType) {
                cancleLoading();
                com.blankj.utilcode.util.ToastUtils.showShort(code.msg);
            }
        });
    }


    @Override
    protected void setActionBarDetail() {

    }


    @OnClick({R.id.llAcreage, R.id.tvOrderConfirmOrder, R.id.llHousePrice, R.id.llDefaultSort})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.llAcreage:
                if (devListBean != null) {
                    if (multiChooseListPopWindow == null) {
                        List<DevListBean.DataBean> data = null;
                        if (devListBean == null || devListBean.getData() == null || devListBean.getData().size() == 0) {
                            data = new ArrayList<DevListBean.DataBean>();
                        } else {
                            data = devListBean.getData();
                        }

                        DevListBean.DataBean dataBean = new DevListBean.DataBean();
                        dataBean.setDevName("全部");
                        dataBean.setDevId("0");
                        data.add(0, dataBean);
                        multiChooseListPopWindow = new MultiChooseListPopWindow(this, data);
                        multiChooseListPopWindow.setOnConfirmClickListener(new MultiChooseListPopWindow.OnConfirmClickListener() {
                            @Override
                            public void onConfirmClick(List selectedList) {
                                ivAcreage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(SelectBrokerageOrderActivity.this, R.color._ff6800)));
                                devIdList.clear();
                                List<DevListBean.DataBean> list = selectedList;
                                for (DevListBean.DataBean data : list
                                        ) {
                                    if (!"全部".equals(data.getText())) {
                                        devIdList.add(data.getDevId());
                                    }
                                }
                                cbSelectAll.setChecked(false);
                                chooseBrokeOrders();


                            }
                        });
                    }
                    showPopWindow2View(multiChooseListPopWindow, llSortContent);
                }
                break;

            case R.id.tvOrderConfirmOrder://确认

                Intent intent = new Intent();
                ArrayList<SelectBrokerOrderBean> selectDatas = new ArrayList<>();
                for (SelectBrokerOrderBean bean :
                        selectOrderAdapter.getData()) {
                    if (bean.isSelect) {
                        selectDatas.add(bean);
                    }
                }

                if (selectDatas.size() == 0) {
                    ToastUtils.showShort("请选择结佣订单");
                    return;
                }

                if (!checkDevId(selectDatas)) {
                    ToastUtils.showShort("只可提交相同楼盘订单");
                    return;
                }


                if (!checkDevId(selectDatas)) {

                    ToastUtils.showShort("只可提交相同楼盘订单");

                }

                //自动选中金额为负的订单

                String devId = selectDatas.get(0).getDevId();


                List<SelectBrokerOrderBean> data = selectOrderAdapter.getData();

                for (int i = 0; i < data.size(); i++) {
                    SelectBrokerOrderBean selectBrokerageOrderBean = data.get(i);
                    if (selectBrokerageOrderBean.getCompanyId().equals("2") && selectBrokerageOrderBean.getDevId().equals(devId) && Double.parseDouble(selectBrokerageOrderBean.getShouldAmount()) < 0) {
                        selectBrokerageOrderBean.isSelect = true;
                    }
                }
                selectOrderAdapter.notifyDataSetChanged();
                selectDatas.clear();
                for (SelectBrokerOrderBean bean :
                        selectOrderAdapter.getData()) {
                    if (bean.isSelect) {
                        selectDatas.add(bean);
                    }
                }


                updateTotalMoney(selectDatas);


                intent.putExtra("selectedList", selectDatas);
                intent.putExtra("totalAmout", totalAmout);
                setResult(RESULT_OK, intent);
                finish();

                break;

            case R.id.llHousePrice://审核时间
//                if (isFrist) {//第一次
//                    orderBy = "2";
//                    ivHousePrice.setImageResource(R.mipmap.iv_order_down);
//                    isFrist = false;
//                } else
                if (orderBy.equals("1")) {
                    ivHousePrice.setImageResource(R.mipmap.iv_order_down);
                    orderBy = "2";
                } else if (orderBy.equals("2")) {
                    orderBy = "1";
                    ivHousePrice.setImageResource(R.mipmap.iv_order_up);
                } else {
                    orderBy = "2";
                    ivHousePrice.setImageResource(R.mipmap.iv_order_down);
                }
                selectBrokerOrderRequest.setOrderBy(orderBy);
                chooseBrokeOrders();
                break;

            case R.id.llDefaultSort://默认排序
                orderBy = "";
                devIdList.clear();
                ivHousePrice.setImageResource(R.mipmap.iv_order_default);
                ivAcreage.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(SelectBrokerageOrderActivity.this, R.color._c4c4c4)));
                selectBrokerOrderRequest.setOrderBy(orderBy);
                chooseBrokeOrders();
                break;
        }
    }

    /**
     * 检查订单是否属于同一个楼盘
     *
     * @return
     */
    private boolean checkDevId(List<SelectBrokerOrderBean> selectDatas) {
        boolean flag = true;
        if (selectDatas == null && selectDatas.size() == 0) {
            return false;
        }


        String originalDevId = selectDatas.get(0).getDevId();

        for (int i = 0; i < selectDatas.size(); i++) {
            SelectBrokerOrderBean selectBrokerageOrderBean = selectDatas.get(i);
            String devId = selectBrokerageOrderBean.getDevId();
            if (!originalDevId.equals(devId)) {
                return false;
            }
        }


        return flag;
    }

    public void showPopWindow2View(PopupWindow pop, View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            pop.setHeight(height);
            pop.showAsDropDown(anchor);
        } else {
            pop.showAsDropDown(anchor);
        }

    }


}
