package com.easylife.house.customer.ui.houses.housesdetail.housesmore;

import android.view.View;
import android.widget.TextView;

import com.easylife.house.customer.R;
import com.easylife.house.customer.bean.HousesDetailBaseBean;
import com.easylife.house.customer.mvp.MVPBaseActivity;
import com.easylife.house.customer.util.CustomerUtils;

import butterknife.Bind;

/**
 * 楼盘显示更多
 */
public class HousesMoreActivity extends MVPBaseActivity<HousesMoreContract.View, HousesMorePresenter> implements HousesMoreContract.View {

    @Bind(R.id.tv_dvp_value)
    TextView tvDvpValue;
    @Bind(R.id.tv_open_value)
    TextView tvOpenValue;
    @Bind(R.id.tv_complete_value)
    TextView tvCompleteValue;
    @Bind(R.id.tv_agree_value)
    TextView tvAgreeValue;
    @Bind(R.id.tv_type_value)
    TextView tvTypeValue;
    @Bind(R.id.tv_build_value)
    TextView tvBuildValue;
    @Bind(R.id.tv_year_value)
    TextView tvYearValue;
    @Bind(R.id.tv_standard_value)
    TextView tvStandardValue;
    @Bind(R.id.tv_rate_value)
    TextView tvRateValue;
    @Bind(R.id.tv_green_value)
    TextView tvGreenValue;
    @Bind(R.id.tv_car_value)
    TextView tvCarValue;
    @Bind(R.id.tv_money_value)
    TextView tvMoneyValue;
    @Bind(R.id.tv_company_value)
    TextView tvCompanyValue;
    @Bind(R.id.tv_address_value)
    TextView tvAddressValue;
    @Bind(R.id.tv_project_address)
    TextView tvProjectAddress;
    @Bind(R.id.tv_project_address_value)
    TextView tvProjectAddressValue;


    @Override
    public void showTip(String msg) {

    }

    @Override
    protected View setContentLayoutView() {
        return View.inflate(this, R.layout.activity_houses_more, null);
    }

    @Override
    protected void initView() {
        hideNoNetView();
        try {
            HousesDetailBaseBean baseBean = (HousesDetailBaseBean) getIntent().getSerializableExtra("MORE_INFO");
            if (baseBean != null) {
//                tvAddressValue.setText(baseBean.addressDistrict +" "+baseBean.addressTown +" "+baseBean.addressDetail);//售楼处地址
//                tvProjectAddressValue.setText(baseBean.saleAddressDistrict+" "+baseBean.saleAddressTown+" "+baseBean.saleAddressDetail);//项目地址
//
//                tvAgreeValue.setText(baseBean.salesLicence);//预售许可证
//                tvBuildValue.setText(baseBean.buildType);//建筑类型
//                tvCarValue.setText(baseBean.parkRatio);//车位配比
//                tvCompanyValue.setText(baseBean.propertyCompany);//物业公司
//                tvCompleteValue.setText(baseBean.liveRange + " " + CustomerUtils.dateTransSdf(baseBean.liveTime));//预计交房
//                tvOpenValue.setText(baseBean.openingRange + " " + CustomerUtils.dateTransSdf(baseBean.openTime));//最新开盘
//                tvDvpValue.setText(baseBean.companyName);//开发商
//                tvGreenValue.setText(baseBean.greenRatio+"%");//绿化率
//                tvMoneyValue.setText(baseBean.propertyFee+"元/㎡/月");//物业费
//                tvRateValue.setText(baseBean.plotRatio);//容积率
//                tvStandardValue.setText(baseBean.decorateLevel);//装修标准
//                tvTypeValue.setText(baseBean.propertyType);//物业类型
//                tvYearValue.setText(baseBean.expires);//产权年限

                //售楼处地址
                CustomerUtils.setEmptyTv(tvAddressValue, baseBean.addressDistrict + " " + baseBean.addressTown + " " + baseBean.addressDetail, "", "暂无数据");
                //项目地址
                CustomerUtils.setEmptyTv(tvProjectAddressValue, baseBean.saleAddressDistrict + " " + baseBean.saleAddressTown + " " + baseBean.saleAddressDetail, "", "暂无数据");
                CustomerUtils.setEmptyTv(tvAgreeValue, baseBean.salesLicence, "", "暂无数据");//预售许可证
                CustomerUtils.setEmptyTv(tvBuildValue, baseBean.buildType, "", "暂无数据");//建筑类型
                CustomerUtils.setEmptyTv(tvCarValue, baseBean.parkRatio, "", "暂无数据");//车位配比
                CustomerUtils.setEmptyTv(tvCompanyValue, baseBean.propertyCompany, "", "暂无数据");//物业公司
                if (baseBean.liveTime == 0) {
                    tvCompleteValue.setText("暂无数据");
                } else {
                    tvCompleteValue.setText(baseBean.liveRange + " " + CustomerUtils.dateTransSdf(baseBean.liveTime));//预计交房
                }

                if (baseBean.openTime == 0) {
                    tvOpenValue.setText("暂无数据");
                } else {
                    tvOpenValue.setText(baseBean.openingRange + " " + CustomerUtils.dateTransSdf(baseBean.openTime));//最新开盘
                }
                CustomerUtils.setEmptyTv(tvDvpValue, baseBean.companyName, "", "暂无数据");//开发商
                CustomerUtils.setEmptyTv(tvGreenValue, baseBean.greenRatio, "%", "暂无数据");//绿化率
                CustomerUtils.setEmptyTv(tvMoneyValue, baseBean.propertyFee, "元/㎡·月", "暂无数据");//物业费
                CustomerUtils.setEmptyTv(tvRateValue, baseBean.plotRatio, "", "暂无数据");//容积率
                CustomerUtils.setEmptyTv(tvStandardValue, baseBean.decorateLevel, "", "暂无数据");//装修标准
                CustomerUtils.setEmptyTv(tvTypeValue, baseBean.propertyType, "", "暂无数据");//物业类型
                CustomerUtils.setEmptyTv(tvYearValue, baseBean.expires, "", "暂无数据");//产权年限
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void setActionBarDetail() {
        imgBack.setImageResource(R.mipmap.back_black);
        actionBar.setBackgroundColor(getResources().getColor(R.color.gray_f8));
        tvTitle.setText("更多信息");
        tvTitle.setTextColor(getResources().getColor(R.color._686769));
    }

    @Override
    protected void tryRequestData() {

    }
}
