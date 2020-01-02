package com.easylife.house.customer.ui.mine.minebuyhouse;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.City;
import com.easylife.house.customer.bean.ItemSelect;
import com.easylife.house.customer.config.ItemSelectManager;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;
import com.easylife.house.customer.util.PubTipDialog;
import com.easylife.house.customer.view.ButtonTouch;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我要买房
 */
public class MineBuyHouseActivity extends MVPBaseActivity<MineBuyHouseContract.View, MineBuyHousePresenter> implements MineBuyHouseContract.View {


    @Bind(R.id.tvCity)
    TextView tvCity;
    @Bind(R.id.tvBudget)
    TextView tvBudget;
    @Bind(R.id.tvLoan)
    TextView tvLoan;
    @Bind(R.id.tvStructure)
    TextView tvStructure;
    @Bind(R.id.edIntentDev)
    EditText edIntentDev;
    @Bind(R.id.btnSubmit)
    ButtonTouch btnSubmit;

    @Override
    protected View setContentLayoutView() {
        return getLayoutInflater().inflate(R.layout.mine_activity_buy_house, null);
    }

    private String defaultCity;
    private String defaultBudget;
    private String defaultLoan;
    private String defaultStructure;
    private String defaultCityId;
    private String defaultBudgetId;
    private String defaultLoanId;
    private String defaultStructureId;
    private String remark;

    private List<City> citys;

    @Override
    protected void initView() {
        hideNoNetView();
        citys = new ArrayList<>();
        mPresenter.getCityList();
    }

    @Override
    protected void setActionBarDetail() {
    }

    @Override
    protected void tryRequestData() {

    }

    @Override
    public void showTip(String msg) {
        CustomerUtils.showTip(activity, msg);
    }

    @Override
    public void submitSucc() {
        dao.pointWantBuyHouse(defaultCity, defaultBudget, defaultLoan, defaultStructure, remark);
        showTip("提交成功");
        finish();
    }

    @Override
    public void initCity(List<City> data) {
        citys.addAll(data);
    }


    @OnClick({R.id.tvCity, R.id.tvBudget, R.id.tvLoan, R.id.tvStructure, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCity:
                new PubTipDialog(activity).showDialogList("请选择购房城市", defaultCity, citys, new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        defaultCityId = date.getId();
                        defaultCity = date.getText();

                        tvCity.setText(defaultCity);
                    }
                });
                break;
            case R.id.tvBudget:
                new PubTipDialog(activity).showDialogList("请选择购房预算", defaultBudget, ItemSelectManager.getSelectItems(ItemSelectManager.TYPE_SELECT_INFO_BUDGET), new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        defaultBudgetId = date.getId();
                        defaultBudget = date.getText();

                        tvBudget.setText(defaultBudget);
                    }
                });
                break;
            case R.id.tvLoan:
                new PubTipDialog(activity).showDialogList("是否考虑贷款", defaultLoan, ItemSelectManager.getSelectItems(ItemSelectManager.TYPE_SELECT_INFO_LOAN), new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        defaultLoanId = date.getId();
                        defaultLoan = date.getText();

                        tvLoan.setText(defaultLoan);
                    }
                });
                break;
            case R.id.tvStructure:
                new PubTipDialog(activity).showDialogList("请选择居室需求", defaultStructure, ItemSelectManager.getSelectItems(ItemSelectManager.TYPE_SELECT_INFO_HOUSE_TYPE), new PubTipDialog.ItemSelectListener() {
                    @Override
                    public void click(int i, ItemSelect date) {
                        defaultStructureId = date.getId();
                        defaultStructure = date.getText();

                        tvStructure.setText(defaultStructure);
                    }
                });
                break;
            case R.id.btnSubmit:
                if (TextUtils.isEmpty(defaultCityId)) {
                    CustomerUtils.showTip(this, "请选择购房城市");
                    return;
                }

                if (TextUtils.isEmpty(defaultBudgetId)) {
                    CustomerUtils.showTip(this, "请选择购房预算");
                    return;
                }

//                if (TextUtils.isEmpty(defaultLoanId)) {
//                    CustomerUtils.showTip(this,"请选择是否贷款");
//                    return;
//                }
//                if (TextUtils.isEmpty(defaultStructureId)) {
//                    CustomerUtils.showTip(this,"请选择居室需求");
//                    return;
//                }
                remark = edIntentDev.getText().toString().trim();
//                if (TextUtils.isEmpty(remark)) {
//                    CustomerUtils.showTip(this,"请填写意向楼盘");
//                    return;
//                }


                mPresenter.submit(defaultCityId, defaultBudgetId, defaultLoanId, defaultStructureId, edIntentDev.getText().toString());
                break;
        }
    }
}
